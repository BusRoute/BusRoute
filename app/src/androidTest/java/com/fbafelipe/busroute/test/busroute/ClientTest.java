package com.fbafelipe.busroute.test.busroute;

import android.preference.PreferenceActivity;
import android.test.AndroidTestCase;
import android.util.Base64;

import com.fbafelipe.busroute.Utils;
import com.fbafelipe.busroute.busroute.ClientException;
import com.fbafelipe.busroute.busroute.ClientHeader;
import com.fbafelipe.busroute.busroute.ClientImpl;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by felipe on 5/30/15.
 */
public class ClientTest extends AndroidTestCase {
	private ClientImpl mClient;
	private HttpClient mHttpClient;
	
	@Override
	public void setUp() {
		mHttpClient = mock(HttpClient.class);
		mClient = new ClientImpl(mHttpClient);
	}
	
	public void testPostSuccess() throws Exception {
		final ClientHeader header = new ClientHeader("user", "pwd");
		JSONObject params = new JSONObject();
		params.put("testKey", "testValue");
		
		when(mHttpClient.execute(any(HttpUriRequest.class))).thenAnswer(new Answer<HttpResponse>() {
			@Override
			public HttpResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
				HttpPost request = extractRequest(invocationOnMock, header);
				String body = getBody(request);
				JSONObject bodyJson = new JSONObject(body);
				
				assertEquals("testValue", bodyJson.getString("testKey"));
				
				JSONObject responseJson = new JSONObject();
				responseJson.put("resultKey", "resultValue");
				
				StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, null);
				HttpResponse response = new BasicHttpResponse(statusLine);
				response.setEntity(new StringEntity(responseJson.toString()));
				
				return response;
			}
		});
		
		JSONObject result = mClient.post("http://www.google.com", header, params);
		assertEquals("resultValue", result.getString("resultKey"));
	}
	
	public void testPostConnectionError() throws Exception {
		ClientHeader header = new ClientHeader("user", "pwd");
		JSONObject params = new JSONObject();

		when(mHttpClient.execute(any(HttpUriRequest.class))).thenAnswer(new Answer<HttpResponse>() {
			@Override
			public HttpResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
				throw new IOException();
			}
		});
		
		try {
			mClient.post("http://www.google.com", header, params);
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}

	public void testPostServerError() throws Exception {
		ClientHeader header = new ClientHeader("user", "pwd");
		JSONObject params = new JSONObject();

		when(mHttpClient.execute(any(HttpUriRequest.class))).thenAnswer(new Answer<HttpResponse>() {
			@Override
			public HttpResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
				StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_INTERNAL_SERVER_ERROR, null);
				HttpResponse response = new BasicHttpResponse(statusLine);
				response.setEntity(new StringEntity("Internal server error"));

				return response;
			}
		});

		try {
			mClient.post("http://www.google.com", header, params);
			fail("ClientException not thrown");
		}
		catch (ClientException error) {}
	}
	
	private String getBody(HttpPost request) throws IOException {
		HttpEntity entity = request.getEntity();
		Header encodingHeader = entity.getContentEncoding();
		
		String encoding = encodingHeader != null ? encodingHeader.getValue() : "UTF-8";
		
		int length = (int) entity.getContentLength();
		assertTrue(length >= 0);

		DataInputStream input = null;
		try {
			input = new DataInputStream(entity.getContent());
			
			byte buffer[] = new byte[length];
			input.readFully(buffer);
			return new String(buffer, encoding);
		}
		finally {
			Utils.safeClose(input);
		}
	}
	
	private HttpPost extractRequest(InvocationOnMock invocationOnMock, ClientHeader header) {
		Object arg0 = invocationOnMock.getArguments()[0];
		assertNotNull(arg0);
		assertTrue(arg0 instanceof HttpPost);

		HttpPost request = (HttpPost) arg0;
		checkHeader(request, header);
		
		return request;
	}
	
	private void checkHeader(HttpPost request, ClientHeader header) {
		checkHeaderValue(request, "Content-Type", "application/json");
		
		String rawToken = header.getUsername() + ":" + header.getPassword();
		String token = "Basic " + Base64.encodeToString(rawToken.getBytes(), Base64.NO_WRAP);
		checkHeaderValue(request, "Authorization", token);
		
		Map<String, String> extraParams = header.getExtraParams();
		for (Map.Entry<String, String> param : extraParams.entrySet()) {
			checkHeaderValue(request, param.getKey(), param.getValue());
		}
	}
	
	private void checkHeaderValue(HttpPost request, String key, String value) {
		Header headers[] = request.getHeaders(key);
		assertEquals(1, headers.length);
		assertEquals(value, headers[0].getValue());
	}
}

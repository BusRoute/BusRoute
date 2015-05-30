package com.fbafelipe.busroute.busroute;

/**
 * Created by felipe on 5/30/15.
 */
public class BusRouteManager {
	private static final String USERNAME = "WKD4N7YMA1uiM8V";
	private static final String PASSWORD = "DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68";
	private static final String ENVIRONMENT_KEY = "X-AppGlu-Environment";
	private static final String ENVIRONMENT_VALUE = "staging";
	
	private Client mClient;
	private ClientHeader mHeader;
	
	public BusRouteManager() {
		mClient = new ClientImpl();
		mHeader = createClientHeader();
	}
	
	public BusRouteManager(Client client, ClientHeader header) {
		mClient = client;
		mHeader = header;
	}
	
	private ClientHeader createClientHeader() {
		ClientHeader clientHeader = new ClientHeader(USERNAME, PASSWORD);
		clientHeader.addExtraParam(ENVIRONMENT_KEY, ENVIRONMENT_VALUE);
		return clientHeader;
	}
}

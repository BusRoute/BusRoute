package com.fbafelipe.busroute.busroute;

/**
 * Created by felipe on 5/30/15.
 */
public class ClientException extends Exception {
	public ClientException(Throwable cause) {
		super(cause);
	}

	public ClientException(String error) {
		super(error);
	}

	public ClientException() {
		super("Unkown error");
	}
}

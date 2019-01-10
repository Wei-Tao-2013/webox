package com.webox.exception;

public class WBException extends Exception {

	/** For serialization. */
	private static final long serialVersionUID = -7321749619071144888L;
	
	
	public WBException() {
		super();
	}
	
	public WBException(final String message) {		
		super(message);
	}


	public WBException(final String message,Throwable cause){

		super(message,cause);
	}


}

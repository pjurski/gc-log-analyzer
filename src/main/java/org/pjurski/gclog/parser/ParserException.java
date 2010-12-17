package org.pjurski.gclog.parser;

public class ParserException extends RuntimeException {

	public ParserException() {
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
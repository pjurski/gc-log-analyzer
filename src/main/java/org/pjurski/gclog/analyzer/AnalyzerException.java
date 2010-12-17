package org.pjurski.gclog.analyzer;

public class AnalyzerException extends RuntimeException {

	public AnalyzerException() {
	}

	public AnalyzerException(String message) {
		super(message);
	}

	public AnalyzerException(Throwable cause) {
		super(cause);
	}

	public AnalyzerException(String message, Throwable cause) {
		super(message, cause);
	}
}
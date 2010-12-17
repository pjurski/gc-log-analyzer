package org.pjurski.gclog.action.stat;

import org.pjurski.gclog.parser.ILineParserListener;

class ErrorCounterListener implements ILineParserListener {

	private int errors = 0;

	public void raiseError(int startLineNumber, String line, Throwable t) {		
		++this.errors;
	}

	public int getErrors() {
		return this.errors;
	}
}
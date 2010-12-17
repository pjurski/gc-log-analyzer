package org.pjurski.gclog.parser;

import org.pjurski.gclog.parser.ILineParserListener;

class DummyLineParserListener implements ILineParserListener {

	private int errors = 0;

	public void raiseError(int startLineNumber, String line, Throwable t) {
		System.out.println("ERROR on line: " + startLineNumber + "\n" + line
				+ "\nThrowable:" + t);
		++this.errors;
	}

	public int getErrors() {
		return this.errors;
	}
}
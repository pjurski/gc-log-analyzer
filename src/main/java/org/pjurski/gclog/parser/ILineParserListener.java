package org.pjurski.gclog.parser;


public interface ILineParserListener {

	public void raiseError(int startLineNumber, String line, Throwable t);
}
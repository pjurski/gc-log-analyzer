package org.pjurski.gclog.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineParserIterator implements Iterator<Token[]> {

	private final BufferedReader reader;

	private Token[] currentTokens;

	private final ILineParserListener listener;

	private int errorLineNumber = -1;
	private String errorLine = null;
	private Throwable errorThrowable = null;

	private int currentLineNumber = 1;
	private String currentLine;

	private boolean readNext = false;

	public LineParserIterator(String s) {
		this(s, null);
	}

	public LineParserIterator(Reader reader) {
		this(reader, null);
	}

	public LineParserIterator(String s, ILineParserListener listener) {
		this(new StringReader(s), listener);
	}

	public LineParserIterator(Reader reader, ILineParserListener listener) {
		this.reader = new BufferedReader(reader);
		this.listener = listener;
	}

	public boolean hasNext() {
		if (!this.readNext) {
			this.setCurrentTokens();
			this.readNext = true;
		}
		boolean hasNext = this.currentTokens != null;
		if (!hasNext) {
			try {
				this.reader.close();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		return hasNext;
	}

	public Token[] next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}

		Token[] tokens = this.currentTokens;
		this.readNext = false;
		return tokens;
	}

	public String getCurrentLine() {
		return this.currentLine;
	}

	public int getCurrentLineNumber() {
		return this.currentLineNumber;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void raiseException() {
		if (this.errorLineNumber != -1) {
			this.raiseException(this.errorLineNumber, this.errorLine,
					this.errorThrowable);

			this.errorLineNumber = -1;
			this.errorLine = null;
			this.errorThrowable = null;
		}
	}

	private void saveException(int errorLineNumber, String errorLine,
			Throwable errorThrowable) {
		this.raiseException();

		this.errorLineNumber = errorLineNumber;
		this.errorLine = errorLine;
		this.errorThrowable = errorThrowable;
	}

	private void setCurrentTokens() {
		this.currentTokens = null;

		StringBuilder lineBuffer = new StringBuilder();
		int leftBracket = 0, rightBracket = 0;
		try {
			String line = null;
			while ((line = this.reader.readLine()) != null) {
				if (lineBuffer.length() > 0) {
					lineBuffer.append('\n');
				}
				lineBuffer.append(line);

				leftBracket += StringUtils.count(line, '[');
				if (leftBracket > 0) {
					rightBracket += StringUtils.count(line, ']');
					if (leftBracket == rightBracket) {
						if (line.trim().endsWith("]")) {
							this.currentLine = lineBuffer.toString();
							this.currentTokens = this.parse(
									this.currentLineNumber, this.currentLine);
							lineBuffer.setLength(0);
							++this.currentLineNumber;

							if (this.currentTokens != null) {
								break;
							}
						}
					}
				} else {
					if (line.equals(lineBuffer.toString())) {
						this.currentLine = lineBuffer.toString();
						// skip the line
						this.saveException(this.currentLineNumber, line,
								new ParserException("wrong line: " + line));

						lineBuffer.setLength(0);
					}
				}
				++this.currentLineNumber;
			}

			if (lineBuffer.length() > 0) {
				this.currentLine = lineBuffer.toString();
				this.currentTokens = this.parse(this.currentLineNumber,
						this.currentLine);

				lineBuffer.setLength(0);
			}

			this.raiseException();

		} catch (IOException e) {
			this.saveException(this.currentLineNumber, lineBuffer.toString(), e);
		}
	}

	private Token[] parse(int startLineNumber, String line) {
		try {
			return Parser.parse(line);
		} catch (ParseException e) {
			this.saveException(startLineNumber, line, e);
		} catch (TokenMgrError e) {
			this.saveException(startLineNumber, line, e);
		}
		return null;
	}

	private void raiseException(int startLineNumber, String line, Throwable e) {
		if (this.listener != null) {
			this.listener.raiseError(startLineNumber, line, e);
		}
	}
}
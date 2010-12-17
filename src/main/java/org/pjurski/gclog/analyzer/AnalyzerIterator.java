package org.pjurski.gclog.analyzer;

import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.pjurski.gclog.model.Event;
import org.pjurski.gclog.parser.ILineParserListener;
import org.pjurski.gclog.parser.LineParserIterator;
import org.pjurski.gclog.parser.Token;

public class AnalyzerIterator implements Iterator<Event> {

	private final LineParserIterator lineParser;
	private final Iterator<Token[]> tokensIterator;

	public AnalyzerIterator(String line) {
		this.lineParser = new LineParserIterator(line);
		this.tokensIterator = this.lineParser;
	}

	public AnalyzerIterator(String line, ILineParserListener listener) {
		this.lineParser = new LineParserIterator(line, listener);
		this.tokensIterator = this.lineParser;
	}

	public AnalyzerIterator(Reader reader) {
		this.lineParser = new LineParserIterator(reader);
		this.tokensIterator = this.lineParser;
	}

	public AnalyzerIterator(Reader reader, ILineParserListener listener) {
		this.lineParser = new LineParserIterator(reader, listener);
		this.tokensIterator = this.lineParser;
	}

	public boolean hasNext() {
		return this.tokensIterator.hasNext();
	}

	public Event next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		return this.setCurrentEvent();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private Event setCurrentEvent() {
		Token[] tokens = this.tokensIterator.next();

		AnalyzerTree tree = AnalyzerHelper.buildTree(tokens);
		Event event = AnalyzerHelper.convertTo(tree);
		return event;
	}

	public String getCurrentLine() {
		return this.lineParser.getCurrentLine();
	}
}
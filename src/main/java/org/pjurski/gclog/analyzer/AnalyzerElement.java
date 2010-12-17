package org.pjurski.gclog.analyzer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pjurski.gclog.parser.Token;

public class AnalyzerElement implements Serializable {

	private final int kind;
	private final Object value;

	private final Token token;

	private AnalyzerElement last;
	private final List<AnalyzerElement> subTokens = new ArrayList<AnalyzerElement>(
			3);

	protected AnalyzerElement() {
		this(-1, new Token(-1), null);
	}

	public AnalyzerElement(Token token) {
		this(token.kind, token, null);
	}

	public AnalyzerElement(Token token, Object value) {
		this(token.kind, token, value);
	}

	public AnalyzerElement(int kind, Token token) {
		this(kind, token, token.image);
	}

	public AnalyzerElement(int kind, Token token, Object value) {
		this.kind = kind;
		this.token = token;
		if (token == null) {
			throw new IllegalArgumentException("token cannot be null");
		}
		this.value = value;
	}

	public int getKind() {
		return this.kind;
	}

	public Object getValue() {
		return this.value;
	}

	public Token getToken() {
		return this.token;
	}

	void addSubToken(AnalyzerElement token) {
		if (token == null) {
			throw new IllegalArgumentException("token cannot be null");
		}
		this.last = token;
		this.subTokens.add(token);
	}

	AnalyzerElement getLastToken() {
		return this.last == null ? this : this.last;
	}

	public List<AnalyzerElement> getSubTokens() {
		return Collections.unmodifiableList(this.subTokens);
	}
}
package org.pjurski.gclog.analyzer;

import org.pjurski.gclog.parser.Token;

public class SingleToken {

	private final Token token;

	public SingleToken(Token token) {
		this.token = token;
	}

	public Token getToken() {
		return this.token;
	}
}

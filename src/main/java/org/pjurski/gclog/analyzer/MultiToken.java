package org.pjurski.gclog.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.pjurski.gclog.parser.Token;

public class MultiToken extends SingleToken {

	private SingleToken last;
	private List<SingleToken> tokens = new ArrayList<SingleToken>();

	public MultiToken(Token token) {
		super(token);
	}

	public void addToken(SingleToken token) {
		this.last = token;
		this.tokens.add(token);
	}

	public SingleToken getLastToken() {
		return this.last == null ? this : this.last;
	}

	public List<SingleToken> getTokens() {
		return this.tokens;
	}
}
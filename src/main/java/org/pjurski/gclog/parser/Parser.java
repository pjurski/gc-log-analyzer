package org.pjurski.gclog.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static LineParserIterator parser(Reader in) {
		return new LineParserIterator(in);
	}

	public static Token[] parse(String line) throws ParseException {
		line = line.trim();

		GcLogCC parser = new GcLogCC(line);

		Token token = parser.token;

		parser.parse();

		List<Token> tokens = new ArrayList<Token>();
		while (token != null) {
			tokens.add(token);

			token = token.next;
		}
		return tokens.toArray(new Token[tokens.size()]);
	}
}
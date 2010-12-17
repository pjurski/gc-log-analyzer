package org.pjurski.gclog.parser;

class StringUtils {

	public static int count(String s, char c) {
		if (s == null) {
			return 0;
		}
		int counter = 0;
		int len = s.length();
		for (int i = 0; i < len; i++) {
			if (c == s.charAt(i)) {
				++counter;
			}
		}

		return counter;
	}
}
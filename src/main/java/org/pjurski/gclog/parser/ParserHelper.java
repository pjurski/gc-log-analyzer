package org.pjurski.gclog.parser;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserHelper {

	private static final BigDecimal NANO_TO_SECOND = new BigDecimal(
			"1000000000");

	public static Long[] findMemoryChange(Token token) {
		if (token == null) {
			throw new IllegalArgumentException("token cannot be null");
		}
		if (token.kind != GcLogCCConstants.GC_MEMORY) {
			throw new IllegalArgumentException("token kind is wrong: "
					+ token.kind + " should be " + GcLogCCConstants.GC_MEMORY);
		}

		Long[] memory = new Long[3];
		memory[0] = parseMemory(token);

		token = token.next;
		if (token == null) {
			throw new IllegalArgumentException("2 token cannot be null");
		}

		token = token.next;
		if (token == null) {
			throw new IllegalArgumentException("3 token cannot be null");
		}
		memory[1] = parseMemory(token);

		token = token.next;
		if (token == null) {
			throw new IllegalArgumentException("2 token cannot be null");
		}

		token = token.next;
		if (token == null) {
			throw new IllegalArgumentException("3 token cannot be null");
		}
		memory[2] = parseMemory(token);

		return memory;
	}

	public static Long parseMemory(Token token) {
		if (token == null) {
			throw new IllegalArgumentException("token not exists");
		}
		if (token.kind != GcLogCCConstants.GC_MEMORY) {
			throw new IllegalArgumentException("token kind is wrong: "
					+ token.kind + " should be " + GcLogCCConstants.GC_MEMORY);
		}
		if (token.image.endsWith("K")) {
			return Long.parseLong(token.image.substring(0,
					token.image.length() - 1).trim());
		} else {
			return Long.parseLong(token.image.trim());
		}
	}

	public static BigDecimal parseTimeToNanos(Token token) {
		if (token == null) {
			throw new IllegalArgumentException("token not exists");
		}
		if (token.kind != GcLogCCConstants.GC_TIME) {
			throw new IllegalArgumentException("token kind is wrong: "
					+ token.kind + " should be " + GcLogCCConstants.GC_MEMORY);
		}
		return NANO_TO_SECOND.multiply(BigDecimal.valueOf(Double
				.parseDouble(token.image.substring(0, token.image.length()))));
	}

	public static Double convertNanosToSeconds(long nanos) {
		return convertNanosToSeconds(BigDecimal.valueOf(nanos)).doubleValue();
	}

	public static BigDecimal convertNanosToSeconds(BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("value cannot be null");
		}
		return value.divide(NANO_TO_SECOND);
	}

	public static Date parseDate(Token token) throws ParserException {
		if (token == null) {
			throw new IllegalArgumentException("token not exists");
		}
		if (token.kind != GcLogCCConstants.GC_DATE) {
			throw new IllegalArgumentException("token kind is wrong: "
					+ token.kind + " should be " + GcLogCCConstants.GC_MEMORY);
		}

		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			return format.parse(token.image);
		} catch (java.text.ParseException e) {
			throw new ParserException("cannot parser date", e);
		}
	}

	public static Token findToken(Token[] tokens, int kind) {
		if (tokens == null) {
			throw new IllegalArgumentException("Tokens not exists");
		}
		for (Token token : tokens) {
			if (token.kind == kind) {
				return token;
			}
		}
		return null;
	}

	public static Token findToken(Token startWith, Token[] tokens, int kind) {
		if (tokens == null) {
			throw new IllegalArgumentException("Tokens not exists");
		}
		if (startWith == null) {
			throw new IllegalArgumentException("StartWith token not exist");
		}
		boolean start = false;
		for (Token token : tokens) {
			if (start && token.kind == kind) {
				return token;
			}
			if (!start && token == startWith) {
				start = true;
			}
		}
		return null;
	}

	public static String toString(Token[] tokens) {
		return toString(tokens, "\n");
	}

	public static String toString(Token[] tokens, String delim) {
		if (tokens == null) {
			throw new IllegalArgumentException("Tokens not exists");
		}
		if (delim == null) {
			delim = ",";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(toString(tokens[i]));
		}
		return sb.toString();
	}

	public static String toString(Token token) {
		if (token == null) {
			throw new IllegalArgumentException("Token not exists");
		}

		return toString(token.kind, token.image);
	}

	public static String toString(int kind, Object image) {
		StringBuilder sb = new StringBuilder();

		sb.append(GcLogCCConstants.tokenImage[kind]);
		sb.append('(');
		sb.append(kind);
		sb.append(')');
		sb.append('=');
		sb.append(image);

		return sb.toString();
	}
}
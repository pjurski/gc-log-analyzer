package org.pjurski.gclog.analyzer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.pjurski.gclog.model.CollectorEvents;
import org.pjurski.gclog.model.CollectorInfo;
import org.pjurski.gclog.model.Event;
import org.pjurski.gclog.model.Stats;
import org.pjurski.gclog.model.Times;
import org.pjurski.gclog.parser.GcLogCCConstants;
import org.pjurski.gclog.parser.ParseException;
import org.pjurski.gclog.parser.Parser;
import org.pjurski.gclog.parser.ParserException;
import org.pjurski.gclog.parser.ParserHelper;
import org.pjurski.gclog.parser.Token;

class AnalyzerHelper {

	public static Event convertTo(String s) throws AnalyzerException,
			ParseException {
		return convertTo(buildTree(s));
	}

	static Event convertTo(AnalyzerElement analyzerElement) {
		if (analyzerElement == null) {
			throw new IllegalArgumentException("analyzer tree cannot be null");
		}

		Iterator<AnalyzerElement> elements = analyzerElement.getSubTokens()
				.iterator();

		Date timestamp = null;
		BigDecimal starttime = null;

		AnalyzerElement element = elements.next();
		if (element.getKind() == GcLogCCConstants.GC_DATE) {
			timestamp = (Date) element.getValue();

			element = elements.next();
			starttime = (BigDecimal) element.getValue();

		} else if (element.getKind() == GcLogCCConstants.GC_TIME) {
			starttime = (BigDecimal) element.getValue();
		}

		CollectorInfo subEvents = findCollectorEventInfo(starttime,
				analyzerElement);

		Times times = findTimesInfo(analyzerElement);

		Event event = new Event(timestamp, subEvents, times);
		return event;
	}

	private static Times findTimesInfo(AnalyzerElement element) {
		BigDecimal gcUserTime = null, gcSysTime = null, gcRealTime = null;

		Iterator<AnalyzerElement> elements = element.getSubTokens().iterator();
		while (elements.hasNext()) {
			element = elements.next();

			switch (element.getKind()) {
			case GcLogCCConstants.GC_TIMES_USER:
				gcUserTime = (BigDecimal) element.getValue();
				break;

			case GcLogCCConstants.GC_TIMES_SYS:
				gcSysTime = (BigDecimal) element.getValue();
				break;

			case GcLogCCConstants.GC_TIMES_REAL:
				gcRealTime = (BigDecimal) element.getValue();
				break;
			}
		}
		return new Times(gcUserTime, gcSysTime, gcRealTime);
	}

	private static CollectorInfo findCollectorEventInfo(BigDecimal starttime,
			AnalyzerElement element) {
		Long memoryBefore = null, memoryAfter = null, memoryTotal = null;
		BigDecimal timeTaken = null;

		boolean promotionFailed = false;

		CollectorEvents events = new CollectorEvents();

		List<AnalyzerElement> elements = element.getSubTokens();
		for (int i = 0; i < elements.size(); i++) {
			AnalyzerElement subElement = elements.get(i);

			switch (subElement.getKind()) {
			case GcLogCCConstants.GC_MEMORY:
				memoryBefore = (Long) subElement.getValue();

				++i;
				if (i >= elements.size()) {
					break;
				}
				subElement = elements.get(i);
				memoryAfter = (Long) subElement.getValue();

				++i;
				if (i >= elements.size()) {
					memoryTotal = memoryAfter;
					memoryAfter = null;
					break;
				} else if (elements.get(i).getKind() != GcLogCCConstants.GC_MEMORY) {
					--i;
					memoryTotal = memoryAfter;
					memoryAfter = null;
					break;
				}
				subElement = elements.get(i);
				memoryTotal = (Long) subElement.getValue();
				break;

			case GcLogCCConstants.GC_TIME:
				timeTaken = (BigDecimal) subElement.getValue();
				break;

			case GcLogCCConstants.GC_PROMOTION_FAILED:
				promotionFailed = true;
				break;

			case IAnalyzerConstants.GC:
			case IAnalyzerConstants.FULL_GC:
				CollectorInfo event = findCollectorEventInfo(timeTaken,
						subElement);
				events.addEvent(event);

				timeTaken = null;
				break;
			}
		}
		return new CollectorInfo(element.getKind(),
				element.getToken() == null ? IAnalyzerConstants.UNKNOWN_GC
						: element.getToken().kind, new Stats(
						starttime, memoryBefore, memoryAfter, memoryTotal,
						timeTaken), events, promotionFailed);
	}

	static AnalyzerTree buildTree(String line) throws AnalyzerException,
			ParseException {
		Token[] tokens = Parser.parse(line);
		return buildTree(tokens);
	}

	static AnalyzerTree buildTree(Token[] tokens) throws AnalyzerException {
		AnalyzerTree analyzerTree = new AnalyzerTree();

		Stack<AnalyzerElement> stack = new Stack<AnalyzerElement>();

		stack.push(analyzerTree);

		boolean leftBracketPassed = false;

		AnalyzerElement newToken = null;

		Token lastToken = null;

		for (Token token : tokens) {
			int tokenAnalyzerKind = getKind(token);

			switch (tokenAnalyzerKind) {

			case GcLogCCConstants.GC_TIME:
				int kind = token.kind;
				if (lastToken != null) {
					if (lastToken.kind == GcLogCCConstants.GC_TIMES_REAL) {
						kind = GcLogCCConstants.GC_TIMES_REAL;
					} else if (lastToken.kind == GcLogCCConstants.GC_TIMES_SYS) {
						kind = GcLogCCConstants.GC_TIMES_SYS;
					} else if (lastToken.kind == GcLogCCConstants.GC_TIMES_USER) {
						kind = GcLogCCConstants.GC_TIMES_USER;
					}
				}
				stack.peek().addSubToken(
						new AnalyzerElement(kind, token, ParserHelper
								.parseTimeToNanos(token)));
				break;

			case GcLogCCConstants.GC_DATE:
				try {
					stack.peek().addSubToken(
							new AnalyzerElement(token, ParserHelper
									.parseDate(token)));
				} catch (ParserException e) {
					throw new AnalyzerException("date cannot be parse: "
							+ token.image, e);
				}
				break;

			case GcLogCCConstants.GC_MEMORY:
				stack.peek().addSubToken(
						new AnalyzerElement(token, ParserHelper
								.parseMemory(token)));
				break;

			case GcLogCCConstants.GC_LEFT_BRACKET:
				leftBracketPassed = true;
				break;

			case GcLogCCConstants.GC_RIGHT_BRACKET:
				leftBracketPassed = false;

				stack.pop();
				break;

			case GcLogCCConstants.GC_FULL_GC:
			case GcLogCCConstants.GC_FULL_GC_SYSTEM:
			case GcLogCCConstants.GC_PERM:
			case GcLogCCConstants.GC_TENURED:

			case GcLogCCConstants.GC_1_CMS_INITIAL_MARK:
			case GcLogCCConstants.GC_1_CMS_REMARK:
			case GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN:
			case GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN_START:
			case GcLogCCConstants.GC_CMS_CONCURRENT_MARK:
			case GcLogCCConstants.GC_CMS_CONCURRENT_MARK_START:
			case GcLogCCConstants.GC_CMS_CONCURRENT_PRECLEAN:
			case GcLogCCConstants.GC_CMS_CONCURRENT_PRECLEAN_START:
			case GcLogCCConstants.GC_CMS_CONCURRENT_RESET:
			case GcLogCCConstants.GC_CMS_CONCURRENT_RESET_START:
			case GcLogCCConstants.GC_CMS_CONCURRENT_SWEEP:
			case GcLogCCConstants.GC_CMS_CONCURRENT_SWEEP_START:
				newToken = new AnalyzerElement(IAnalyzerConstants.FULL_GC,
						token);
				stack.peek().addSubToken(newToken);

				if (leftBracketPassed) {
					stack.push(newToken);
				}
				break;

			case GcLogCCConstants.GC_DESIRED_SURVIVOR:
			case GcLogCCConstants.GC_PAR_NEW_DESIRED_SURVIVOR:
			case GcLogCCConstants.GC_GC:
			case GcLogCCConstants.GC_CMS:

			case GcLogCCConstants.GC_CMS_PERM:
			case GcLogCCConstants.GC_PS_YOUNG_GEN:
			case GcLogCCConstants.GC_PS_PERM_GEN:
			case GcLogCCConstants.GC_PS_OLD_GEN:
			case GcLogCCConstants.GC_PAR_NEW:
			case GcLogCCConstants.GC_DEF_NEW:
			case GcLogCCConstants.GC_YG_OCCUPANCY:
			case GcLogCCConstants.GC_RESCAN_PARALLEL:
			case GcLogCCConstants.GC_WEAK_REFS_PROCESSING:
				newToken = new AnalyzerElement(IAnalyzerConstants.GC, token);
				stack.peek().addSubToken(newToken);

				if (leftBracketPassed) {
					stack.push(newToken);
					leftBracketPassed = false;
				}
				break;

			case GcLogCCConstants.GC_PROMOTION_FAILED:
				stack.peek().addSubToken(new AnalyzerElement(token));
				break;
			}

			lastToken = token;
		}

		return analyzerTree;
	}

	private static Integer getKind(Token token) {
		return token.kind;
	}

	public static String toString(AnalyzerElement element) {
		if (element == null) {
			throw new IllegalArgumentException("Token not exists");
		}

		StringBuilder sb = new StringBuilder();

		sb.append(toString("", element));

		return sb.toString();
	}

	private static String toString(String delim, AnalyzerElement element) {
		if (element == null) {
			throw new IllegalArgumentException("Token not exists");
		}

		StringBuilder sb = new StringBuilder();
		sb.append(delim);

		Token parserToken = element.getToken();
		if (parserToken != null) {
			sb.append(ParserHelper.toString(element.getKind(),
					element.getValue()));
			delim += "\t";
		}
		int i = 0;
		for (AnalyzerElement subTokens : element.getSubTokens()) {
			sb.append("\n");

			sb.append(toString(delim, subTokens));
			++i;
		}
		return sb.toString();
	}
}
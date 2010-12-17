package org.pjurski.gclog.analyzer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;
import org.pjurski.gclog.analyzer.AnalyzerElement;
import org.pjurski.gclog.analyzer.AnalyzerHelper;
import org.pjurski.gclog.parser.GcLogCCConstants;
import org.pjurski.gclog.parser.ParseException;
import org.pjurski.gclog.parser.ParserHelper;

public class AnalyzerHelperTest {

	@Test
	public void jdk16XloggcGC() throws ParseException {
		AnalyzerElement token = AnalyzerHelper
				.buildTree("0.064: [GC 2048K->992K(3136K), 0.0015430 secs]");
		assertNotNull(token);

		Collection<AnalyzerElement> tokens = token.getSubTokens();
		Iterator<AnalyzerElement> iterator = tokens.iterator();
		assertEquals(2, tokens.size());

		AnalyzerElement time = iterator.next();
		assertNotNull(time);
		assertEquals(64000000L, ((Number) time.getValue()).longValue());
		assertEquals(0.064, ParserHelper.convertNanosToSeconds(((Number) time
				.getValue()).longValue()));

		AnalyzerElement gcFull = iterator.next();
		assertEquals(GcLogCCConstants.GC_GC, gcFull.getKind());
		Iterator<AnalyzerElement> gcFullIterator = gcFull.getSubTokens()
				.iterator();
		assertEquals(4, gcFull.getSubTokens().size());

		AnalyzerElement memoryFrom = gcFullIterator.next();
		AnalyzerElement memoryTo = gcFullIterator.next();
		AnalyzerElement memoryMax = gcFullIterator.next();
		AnalyzerElement gcFullTime = gcFullIterator.next();

		assertEquals(GcLogCCConstants.GC_MEMORY, memoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryMax.getKind());
		assertEquals(GcLogCCConstants.GC_TIME, gcFullTime.getKind());

		assertEquals(2048L, memoryFrom.getValue());
		assertEquals(992L, memoryTo.getValue());
		assertEquals(3136L, memoryMax.getValue());

		assertEquals(1543000L, ((Number) gcFullTime.getValue()).longValue());
		assertEquals(0.001543,
				ParserHelper.convertNanosToSeconds(((Number) gcFullTime
						.getValue()).longValue()));
	}

	@Test
	public void jdk16XloggcFullGC() throws ParseException {
		AnalyzerElement token = AnalyzerHelper
				.buildTree("0.066: [Full GC 992K->983K(5248K), 0.0050660 secs]");
		assertNotNull(token);

		Collection<AnalyzerElement> tokens = token.getSubTokens();
		Iterator<AnalyzerElement> iterator = tokens.iterator();
		assertEquals(2, tokens.size());

		AnalyzerElement time = iterator.next();
		assertNotNull(time);
		assertEquals(66000000L, ((Number) time.getValue()).longValue());
		assertEquals(0.066, ParserHelper.convertNanosToSeconds(((Number) time
				.getValue()).longValue()));

		AnalyzerElement gcFull = iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, gcFull.getKind());
		Iterator<AnalyzerElement> gcFullIterator = gcFull.getSubTokens()
				.iterator();
		assertEquals(4, gcFull.getSubTokens().size());

		AnalyzerElement memoryFrom = gcFullIterator.next();
		AnalyzerElement memoryTo = gcFullIterator.next();
		AnalyzerElement memoryMax = gcFullIterator.next();
		AnalyzerElement gcFullTime = gcFullIterator.next();

		assertEquals(GcLogCCConstants.GC_MEMORY, memoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryMax.getKind());
		assertEquals(GcLogCCConstants.GC_TIME, gcFullTime.getKind());

		assertEquals(992L, memoryFrom.getValue());
		assertEquals(983L, memoryTo.getValue());
		assertEquals(5248L, memoryMax.getValue());

		assertEquals(5066000L, ((Number) gcFullTime.getValue()).longValue());
		assertEquals(0.005066,
				ParserHelper.convertNanosToSeconds(((Number) gcFullTime
						.getValue()).longValue()));
	}

	@Test
	public void jdk16DateSurvivor() throws ParseException {
		AnalyzerElement token = AnalyzerHelper
				.buildTree("2010-11-10T15:59:03.973+0000: 0.859: [GC Desired survivor size 5439488 bytes, new threshold 7 (max 15)  [PSYoungGen: 32000K->288K(37312K)] 32000K->288K(122688K), 0.0010000 secs] [Times: user=0.01 sys=0.02, real=0.03 secs]");
		assertNotNull(token);

		Collection<AnalyzerElement> tokens = token.getSubTokens();
		Iterator<AnalyzerElement> iterator = tokens.iterator();
		assertEquals(6, tokens.size());

		AnalyzerElement date = iterator.next();
		assertNotNull(date);
		assertEquals(1289404743973L, ((Date) date.getValue()).getTime());

		AnalyzerElement time = iterator.next();
		assertNotNull(time);
		assertEquals(859000000L, ((Number) time.getValue()).longValue());
		assertEquals(0.859, ParserHelper.convertNanosToSeconds(((Number) time
				.getValue()).longValue()));

		AnalyzerElement gcDesiredSurvivor = iterator.next();
		assertEquals(GcLogCCConstants.GC_GC, gcDesiredSurvivor.getKind());
		assertEquals(GcLogCCConstants.GC_DESIRED_SURVIVOR,
				gcDesiredSurvivor.getToken().kind);
		Iterator<AnalyzerElement> gcDesiredSurvivorIterator = gcDesiredSurvivor
				.getSubTokens().iterator();
		assertEquals(5, gcDesiredSurvivor.getSubTokens().size());

		AnalyzerElement psYoungGen = gcDesiredSurvivorIterator.next();
		AnalyzerElement gcDesiredSurvivorMemoryFrom = gcDesiredSurvivorIterator
				.next();
		AnalyzerElement gcDesiredSurvivorMemoryTo = gcDesiredSurvivorIterator
				.next();
		AnalyzerElement gcDesiredSurvivorMemoryMax = gcDesiredSurvivorIterator
				.next();
		AnalyzerElement gcDesiredSurvivorTime = gcDesiredSurvivorIterator
				.next();

		assertEquals(3, psYoungGen.getSubTokens().size());
		assertNotNull(gcDesiredSurvivorMemoryFrom);
		assertNotNull(gcDesiredSurvivorMemoryTo);
		assertNotNull(gcDesiredSurvivorMemoryMax);
		assertNotNull(gcDesiredSurvivorTime);

		assertEquals(GcLogCCConstants.GC_MEMORY,
				gcDesiredSurvivorMemoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY,
				gcDesiredSurvivorMemoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY,
				gcDesiredSurvivorMemoryMax.getKind());
		assertEquals(GcLogCCConstants.GC_TIME, gcDesiredSurvivorTime.getKind());

		assertEquals(32000L, gcDesiredSurvivorMemoryFrom.getValue());
		assertEquals(288L, gcDesiredSurvivorMemoryTo.getValue());
		assertEquals(122688L, gcDesiredSurvivorMemoryMax.getValue());

		assertEquals(1000000L,
				((Number) gcDesiredSurvivorTime.getValue()).longValue());
		assertEquals(0.001,
				ParserHelper
						.convertNanosToSeconds(((Number) gcDesiredSurvivorTime
								.getValue()).longValue()));

		Iterator<AnalyzerElement> psYoungGenIterator = psYoungGen
				.getSubTokens().iterator();

		AnalyzerElement memoryFrom = psYoungGenIterator.next();
		AnalyzerElement memoryTo = psYoungGenIterator.next();
		AnalyzerElement memoryMax = psYoungGenIterator.next();

		assertNotNull(memoryFrom);
		assertNotNull(memoryTo);
		assertNotNull(memoryMax);

		assertEquals(GcLogCCConstants.GC_MEMORY, memoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryMax.getKind());

		assertEquals(32000L, memoryFrom.getValue());
		assertEquals(288L, memoryTo.getValue());
		assertEquals(37312L, memoryMax.getValue());

		AnalyzerElement userTime = iterator.next();
		AnalyzerElement sysTime = iterator.next();
		AnalyzerElement realTime = iterator.next();

		assertNotNull(userTime);
		assertNotNull(sysTime);
		assertNotNull(realTime);

		assertEquals(GcLogCCConstants.GC_TIMES_USER, userTime.getKind());
		assertEquals(GcLogCCConstants.GC_TIMES_SYS, sysTime.getKind());
		assertEquals(GcLogCCConstants.GC_TIMES_REAL, realTime.getKind());

		assertEquals(10000000L, ((Number) userTime.getValue()).longValue());
		assertEquals(0.01,
				ParserHelper.convertNanosToSeconds(((Number) userTime
						.getValue()).longValue()));

		assertEquals(20000000L, ((Number) sysTime.getValue()).longValue());
		assertEquals(0.02, ParserHelper.convertNanosToSeconds(((Number) sysTime
				.getValue()).longValue()));

		assertEquals(30000000L, ((Number) realTime.getValue()).longValue());
		assertEquals(0.03,
				ParserHelper.convertNanosToSeconds(((Number) realTime
						.getValue()).longValue()));
	}

	@Test
	public void jdk16DateFullGCPSYoungGenPSOldGenPSPermGen()
			throws ParseException {
		AnalyzerElement token = AnalyzerHelper
				.buildTree("2010-11-11T12:52:34.807+0000: 0.065: [Full GC [PSYoungGen: 296K->231K(2368K)] [PSOldGen: 688K->748K(2816K)] 984K->979K(5184K) [PSPermGen: 2649K->2649K(21248K)], 0.0065200 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]");
		assertNotNull(token);

		Collection<AnalyzerElement> tokens = token.getSubTokens();
		Iterator<AnalyzerElement> iterator = tokens.iterator();
		assertEquals(6, tokens.size());

		AnalyzerElement date = iterator.next();
		assertNotNull(date);
		assertEquals(1289479954807L, ((Date) date.getValue()).getTime());

		AnalyzerElement time = iterator.next();
		assertNotNull(time);
		assertEquals(65000000L, ((Number) time.getValue()).longValue());
		assertEquals(0.065, ParserHelper.convertNanosToSeconds(((Number) time
				.getValue()).longValue()));

		AnalyzerElement gcFullGc = iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, gcFullGc.getKind());
		Iterator<AnalyzerElement> gcFullGcIterator = gcFullGc.getSubTokens()
				.iterator();
		assertEquals(7, gcFullGc.getSubTokens().size());

		AnalyzerElement psYoungGen = gcFullGcIterator.next();
		Iterator<AnalyzerElement> psYoungGenIterator = psYoungGen
				.getSubTokens().iterator();
		assertNotNull(psYoungGen);
		assertEquals(GcLogCCConstants.GC_GC, psYoungGen.getKind());
		assertEquals(GcLogCCConstants.GC_PS_YOUNG_GEN,
				psYoungGen.getToken().kind);
		assertEquals(3, psYoungGen.getSubTokens().size());
		AnalyzerElement memoryFrom = psYoungGenIterator.next();
		AnalyzerElement memoryTo = psYoungGenIterator.next();
		AnalyzerElement memoryMax = psYoungGenIterator.next();

		assertNotNull(memoryFrom);
		assertNotNull(memoryTo);
		assertNotNull(memoryMax);

		assertEquals(GcLogCCConstants.GC_MEMORY, memoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryMax.getKind());

		assertEquals(296L, memoryFrom.getValue());
		assertEquals(231L, memoryTo.getValue());
		assertEquals(2368L, memoryMax.getValue());

		AnalyzerElement psOldGen = gcFullGcIterator.next();
		Iterator<AnalyzerElement> psOldGenIterator = psOldGen.getSubTokens()
				.iterator();
		assertNotNull(psOldGen);
		assertEquals(GcLogCCConstants.GC_GC, psOldGen.getKind());
		assertEquals(GcLogCCConstants.GC_PS_OLD_GEN, psOldGen.getToken().kind);
		assertEquals(3, psOldGen.getSubTokens().size());
		memoryFrom = psOldGenIterator.next();
		memoryTo = psOldGenIterator.next();
		memoryMax = psOldGenIterator.next();

		assertNotNull(memoryFrom);
		assertNotNull(memoryTo);
		assertNotNull(memoryMax);

		assertEquals(GcLogCCConstants.GC_MEMORY, memoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryMax.getKind());

		assertEquals(688L, memoryFrom.getValue());
		assertEquals(748L, memoryTo.getValue());
		assertEquals(2816L, memoryMax.getValue());

		AnalyzerElement gcDesiredSurvivorMemoryFrom = gcFullGcIterator.next();
		assertNotNull(gcDesiredSurvivorMemoryFrom);

		AnalyzerElement gcDesiredSurvivorMemoryTo = gcFullGcIterator.next();
		assertNotNull(gcDesiredSurvivorMemoryTo);

		AnalyzerElement gcDesiredSurvivorMemoryMax = gcFullGcIterator.next();
		assertNotNull(gcDesiredSurvivorMemoryMax);

		AnalyzerElement psPermGen = gcFullGcIterator.next();
		Iterator<AnalyzerElement> psPermGenIterator = psPermGen.getSubTokens()
				.iterator();
		assertNotNull(psPermGen);
		assertEquals(GcLogCCConstants.GC_GC, psPermGen.getKind());
		assertEquals(GcLogCCConstants.GC_PS_PERM_GEN, psPermGen.getToken().kind);
		assertEquals(3, psPermGen.getSubTokens().size());
		memoryFrom = psPermGenIterator.next();
		memoryTo = psPermGenIterator.next();
		memoryMax = psPermGenIterator.next();

		assertNotNull(memoryFrom);
		assertNotNull(memoryTo);
		assertNotNull(memoryMax);

		assertEquals(GcLogCCConstants.GC_MEMORY, memoryFrom.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryTo.getKind());
		assertEquals(GcLogCCConstants.GC_MEMORY, memoryMax.getKind());

		assertEquals(2649L, memoryFrom.getValue());
		assertEquals(2649L, memoryTo.getValue());
		assertEquals(21248L, memoryMax.getValue());

		AnalyzerElement gcFullGcTime = gcFullGcIterator.next();
		assertNotNull(gcFullGcTime);
		assertEquals(GcLogCCConstants.GC_TIME, gcFullGcTime.getKind());

		assertEquals(6520000L, ((Number) gcFullGcTime.getValue()).longValue());
		assertEquals(0.00652,
				ParserHelper.convertNanosToSeconds(((Number) gcFullGcTime
						.getValue()).longValue()));

		AnalyzerElement gcUserTime = iterator.next();
		assertEquals(GcLogCCConstants.GC_TIMES_USER, gcUserTime.getKind());

		assertEquals(0L, ((Number) gcUserTime.getValue()).longValue());
		assertEquals(0.00,
				ParserHelper.convertNanosToSeconds(((Number) gcUserTime
						.getValue()).longValue()));

		AnalyzerElement gcSysTime = iterator.next();
		assertEquals(GcLogCCConstants.GC_TIMES_SYS, gcSysTime.getKind());

		assertEquals(0L, ((Number) gcSysTime.getValue()).longValue());
		assertEquals(0.00,
				ParserHelper.convertNanosToSeconds(((Number) gcSysTime
						.getValue()).longValue()));

		AnalyzerElement gcRealTime = iterator.next();
		assertEquals(GcLogCCConstants.GC_TIMES_REAL, gcRealTime.getKind());

		assertEquals(10000000L, ((Number) gcRealTime.getValue()).longValue());
		assertEquals(0.01,
				ParserHelper.convertNanosToSeconds(((Number) gcRealTime
						.getValue()).longValue()));
	}
}
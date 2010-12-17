package org.pjurski.gclog.parser;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.pjurski.gclog.parser.Parser;
import org.pjurski.gclog.parser.ParserHelper;

public class CmsConcurrentTest {

	@Test
	public void jdk16CMSInitialMark() throws ParseException {
		Token[] tokens = Parser
				.parse("31679.536: [GC [1 CMS-initial-mark: 298349K(303104K)] 298442K(327616K), 0.0033056 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31679.536", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_1_CMS_INITIAL_MARK);
		assertNotNull(cms);
		assertEquals("1 CMS-initial-mark", cms.image);

		Token memory = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_MEMORY);
		assertNotNull(memory);
		assertEquals("298349K", memory.image);

		memory = ParserHelper.findToken(memory, tokens,
				GcLogCCConstants.GC_MEMORY);
		assertNotNull(memory);
		assertEquals("303104K", memory.image);
	}

	@Test
	public void jdk16CMSConcurrentMarkStart() throws ParseException {
		Token[] tokens = Parser.parse("31679.540: [CMS-concurrent-mark-start]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31679.540", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_MARK_START);
		assertNotNull(cms);
		assertEquals("[CMS-concurrent-mark-start]", cms.image);
	}

	@Test
	public void jdk16CMSConcurrentMark() throws ParseException {
		Token[] tokens = Parser
				.parse("31680.017: [CMS-concurrent-mark: 0.477/0.478 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31680.017", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_MARK);
		assertNotNull(cms);
		assertEquals("CMS-concurrent-mark", cms.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.477", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.478", time.image);
	}

	@Test
	public void jdk16CMSConcurrentPrecleanStart() throws ParseException {
		Token[] tokens = Parser
				.parse("31680.017: [CMS-concurrent-preclean-start]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31680.017", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_PRECLEAN_START);
		assertNotNull(cms);
		assertEquals("[CMS-concurrent-preclean-start]", cms.image);
	}

	@Test
	public void jdk16CMSConcurrentSweepStart() throws ParseException {
		Token[] tokens = Parser
				.parse("31680.017: [CMS-concurrent-sweep-start]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31680.017", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_SWEEP_START);
		assertNotNull(cms);
		assertEquals("[CMS-concurrent-sweep-start]", cms.image);
	}

	@Test
	public void jdk16CMSConcurrentResetStart() throws ParseException {
		Token[] tokens = Parser
				.parse("31680.017: [CMS-concurrent-reset-start]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31680.017", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_RESET_START);
		assertNotNull(cms);
		assertEquals("[CMS-concurrent-reset-start]", cms.image);
	}

	@Test
	public void jdk16CMSConcurrentPreclean() throws ParseException {
		Token[] tokens = Parser
				.parse("31680.023: [CMS-concurrent-preclean: 0.006/0.007 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31680.023", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_PRECLEAN);
		assertNotNull(cms);
		assertEquals("CMS-concurrent-preclean", cms.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.006", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.007", time.image);
	}

	@Test
	public void jdk16CMSConcurrentSweep() throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-08T08:45:00.678+0000: 3524.164: [CMS-concurrent-sweep: 2.328/2.332 secs] [Times: user=2.49 sys=0.03, real=2.33 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNotNull(date);
		assertEquals("2010-11-08T08:45:00.678+0000", date.image);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("3524.164", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_SWEEP);
		assertNotNull(cms);
		assertEquals("CMS-concurrent-sweep", cms.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.328", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.332", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("2.49", user.next.image);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNotNull(sys);
		assertNotNull(sys.next);
		assertEquals(GcLogCCConstants.GC_TIME, sys.next.kind);
		assertEquals("0.03", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("2.33", real.next.image);
	}

	@Test
	public void jdk16CMSConcurrentReset() throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-08T08:45:00.953+0000: 3524.439: [CMS-concurrent-reset: 0.274/0.275 secs] [Times: user=0.27 sys=0.00, real=0.27 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNotNull(date);
		assertEquals("2010-11-08T08:45:00.953+0000", date.image);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("3524.439", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_RESET);
		assertNotNull(cms);
		assertEquals("CMS-concurrent-reset", cms.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.274", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.275", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.27", user.next.image);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNotNull(sys);
		assertNotNull(sys.next);
		assertEquals(GcLogCCConstants.GC_TIME, sys.next.kind);
		assertEquals("0.00", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.27", real.next.image);
	}

	@Test
	public void jdk16CMSConcurrentAbortablePrecleanStart()
			throws ParseException {
		Token[] tokens = Parser
				.parse("31680.023: [CMS-concurrent-abortable-preclean-start]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31680.023", time.image);

		Token cms = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN_START);
		assertNotNull(cms);
		assertEquals("[CMS-concurrent-abortable-preclean-start]", cms.image);
	}

	@Test
	public void jdk16CMSConcurrentCmsParNew() throws ParseException {
		Token[] tokens = Parser
				.parse("31768.430: [CMS 31768.430: [CMS-concurrent-abortable-preclean: 5.410/88.406 secs] 298349K->35861K(303104K), 0.9340408 secs] 322797K->35861K(327616K), 0.9345904 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31768.430", time.image);

		Token cms = ParserHelper.findToken(tokens, GcLogCCConstants.GC_CMS);
		assertNotNull(cms);

		Token cmsConcurrentAbortablePreclean = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN);
		assertNotNull(cmsConcurrentAbortablePreclean);

		Token cmsTime = ParserHelper.findToken(time, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(cmsTime);
		assertEquals("31768.430", cmsTime.image);

		Token cmsConcurrentAbortablePrecleanTime1 = ParserHelper.findToken(
				cmsTime, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(cmsConcurrentAbortablePrecleanTime1);
		assertEquals("5.410", cmsConcurrentAbortablePrecleanTime1.image);

		Token cmsConcurrentAbortablePrecleanTime2 = ParserHelper.findToken(
				cmsConcurrentAbortablePrecleanTime1, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(cmsConcurrentAbortablePrecleanTime2);
		assertEquals("88.406", cmsConcurrentAbortablePrecleanTime2.image);

		Token gcConcurrentModeFailed = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CONCURRENT_MODE_FAILED);
		assertNull(gcConcurrentModeFailed);
	}

	@Test
	public void jdk16CMSConcurrentCmsParNewConcurrentModeFailure()
			throws ParseException {
		Token[] tokens = Parser
				.parse("31768.430: [CMS 31768.430: [CMS-concurrent-abortable-preclean: 5.410/88.406 secs] (concurrent mode failure): 298349K->35861K(303104K), 0.9340408 secs] 322797K->35861K(327616K), 0.9345904 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("31768.430", time.image);

		Token cms = ParserHelper.findToken(tokens, GcLogCCConstants.GC_CMS);
		assertNotNull(cms);

		Token cmsConcurrentAbortablePreclean = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN);
		assertNotNull(cmsConcurrentAbortablePreclean);

		Token cmsTime = ParserHelper.findToken(time, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(cmsTime);
		assertEquals("31768.430", cmsTime.image);

		Token cmsConcurrentAbortablePrecleanTime1 = ParserHelper.findToken(
				cmsTime, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(cmsConcurrentAbortablePrecleanTime1);
		assertEquals("5.410", cmsConcurrentAbortablePrecleanTime1.image);

		Token cmsConcurrentAbortablePrecleanTime2 = ParserHelper.findToken(
				cmsConcurrentAbortablePrecleanTime1, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(cmsConcurrentAbortablePrecleanTime2);
		assertEquals("88.406", cmsConcurrentAbortablePrecleanTime2.image);

		Token gcConcurrentModeFailed = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_CONCURRENT_MODE_FAILED);
		assertNotNull(gcConcurrentModeFailed);
	}
}
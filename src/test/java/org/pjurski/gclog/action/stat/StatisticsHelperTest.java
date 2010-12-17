package org.pjurski.gclog.action.stat;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import org.pjurski.gclog.action.ActionException;
import org.pjurski.gclog.action.MemoryUnit;
import org.pjurski.gclog.parser.ParserHelper;

public class StatisticsHelperTest {

	@Test
	public void emptySource() throws ActionException {
		StatisticsHelper helper = new StatisticsHelper(MemoryUnit.getDefault(),
				new StringReader(""));
		helper.run();

		assertNotNull(helper.getCountEventHandler());
		assertNotNull(helper.getMeasuresHandler());
		assertNotNull(helper.getMemoryHandler());
		assertNotNull(helper.getPauseTimeHandler());
		assertEquals(0, helper.getErrors());

		assertEquals(0, helper.getCountEventHandler().getAllCount());
		assertEquals(0, helper.getCountEventHandler().getFullGcCount());
		assertEquals(0, helper.getCountEventHandler().getGcCount());

		assertNull(helper.getMeasuresHandler().getFullGcPerformance());
		assertNull(helper.getMeasuresHandler().getGcPerformance());

		assertNotNull(helper.getMeasuresHandler().getThroughput());
		assertEquals(100, helper.getMeasuresHandler().getThroughput()
				.longValue());

		assertNull(helper.getMemoryHandler().getFootprintOldTotalMaxValue());
		assertNull(helper.getMemoryHandler().getFootprintOldTotalMinValue());

		assertNull(helper.getMemoryHandler().getFootprintPermTotalMaxValue());
		assertNull(helper.getMemoryHandler().getFootprintPermTotalMinValue());

		assertNull(helper.getMemoryHandler().getFootprintTotalMaxValue());
		assertNull(helper.getMemoryHandler().getFootprintTotalMinValue());

		assertNull(helper.getMemoryHandler().getFreedAllMemoryValue());
		assertNull(helper.getMemoryHandler().getFreedFullGcMemoryValue());
		assertNull(helper.getMemoryHandler().getFreedGcMemoryValue());

		assertNotNull(helper.getPauseTimeHandler().getAllPause());
		assertEquals(BigDecimal.ZERO, helper.getPauseTimeHandler()
				.getAllPause());

		assertNotNull(helper.getPauseTimeHandler().getFullGcPause());
		assertEquals(BigDecimal.ZERO, helper.getPauseTimeHandler()
				.getFullGcPause());

		assertNotNull(helper.getPauseTimeHandler().getGcPause());
		assertEquals(BigDecimal.ZERO, helper.getPauseTimeHandler().getGcPause());

		assertNotNull(helper.getPauseTimeHandler().getMaxPause());
		assertEquals(BigDecimal.ZERO, helper.getPauseTimeHandler()
				.getMaxPause());

		assertNotNull(helper.getPauseTimeHandler().getMinPause());
		assertEquals(BigDecimal.ZERO, helper.getPauseTimeHandler()
				.getMinPause());
	}

	@Test
	public void line1Source() throws ActionException {
		StatisticsHelper helper = new StatisticsHelper(
				MemoryUnit.getDefault(),
				new StringReader(
						"2.563: [Full GC 2.563: [CMS: 0K->19866K(5408096K), 0.8235990 secs] 165624K->19866K(5253120K), [CMS Perm : 21248K->21226K(21248K)], 0.8238950 secs] [Times: user=0.46 sys=0.49, real=0.82 secs]"));
		helper.run();

		assertNotNull(helper.getCountEventHandler());
		assertNotNull(helper.getMeasuresHandler());
		assertNotNull(helper.getMemoryHandler());
		assertNotNull(helper.getPauseTimeHandler());
		assertEquals(0, helper.getErrors());

		assertEquals(1, helper.getCountEventHandler().getAllCount());
		assertEquals(1, helper.getCountEventHandler().getFullGcCount());
		assertEquals(0, helper.getCountEventHandler().getGcCount());

		assertNotNull(helper.getMeasuresHandler().getFullGcPerformance());
		assertEquals(new BigDecimal("172.767"), helper.getMeasuresHandler()
				.getFullGcPerformance().setScale(3, RoundingMode.HALF_UP));
		assertNull(helper.getMeasuresHandler().getGcPerformance());

		assertNotNull(helper.getMeasuresHandler().getThroughput());
		assertEquals(75, helper.getMeasuresHandler().getThroughput()
				.longValue());

		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintOldTotalMaxValue());
		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintOldTotalMinValue());

		assertEquals(new BigDecimal("21248"), helper.getMemoryHandler()
				.getFootprintPermTotalMaxValue());
		assertEquals(new BigDecimal("21248"), helper.getMemoryHandler()
				.getFootprintPermTotalMinValue());

		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintTotalMaxValue());
		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintTotalMinValue());

		assertEquals(new BigDecimal("145758"), helper.getMemoryHandler()
				.getFreedAllMemoryValue());
		assertEquals(new BigDecimal("145758"), helper.getMemoryHandler()
				.getFreedFullGcMemoryValue());
		assertNull(helper.getMemoryHandler().getFreedGcMemoryValue());

		assertNotNull(helper.getPauseTimeHandler().getAllPause());
		assertEquals(823895000, helper.getPauseTimeHandler().getAllPause()
				.longValue());

		assertNotNull(helper.getPauseTimeHandler().getFullGcPause());
		assertEquals(823895000, helper.getPauseTimeHandler().getFullGcPause()
				.longValue());

		assertNotNull(helper.getPauseTimeHandler().getGcPause());
		assertEquals(0, helper.getPauseTimeHandler().getGcPause().longValue());

		assertNotNull(helper.getPauseTimeHandler().getMaxPause());
		assertEquals(823895000, helper.getPauseTimeHandler().getMaxPause()
				.longValue());

		assertNotNull(helper.getPauseTimeHandler().getMinPause());
		assertEquals(823895000, helper.getPauseTimeHandler().getMinPause()
				.longValue());
	}

	@Test
	public void line2Source() throws ActionException {
		StatisticsHelper helper = new StatisticsHelper(
				MemoryUnit.getDefault(),
				new StringReader(
						"2.563: [Full GC 2.563: [CMS: 0K->19866K(5408096K), 0.8235990 secs] 165624K->19866K(5253120K), [CMS Perm : 21248K->21226K(21248K)], 0.8238950 secs] [Times: user=0.46 sys=0.49, real=0.82 secs]\n5.392: [GC 5.392: [ParNew\nDesired survivor size 19628032 bytes, new threshold 1 (max 4)\n- age   1:   38097216 bytes,   38097216 total\n: 306688K->37673K(345024K), 0.0280820 secs] 326554K->57539K(5353120K), 0.0281990 secs] [Times: user=0.27 sys=0.12, real=0.03 secs] "));
		helper.run();

		assertNotNull(helper.getCountEventHandler());
		assertNotNull(helper.getMeasuresHandler());
		assertNotNull(helper.getMemoryHandler());
		assertNotNull(helper.getPauseTimeHandler());
		assertEquals(0, helper.getErrors());

		assertEquals(2, helper.getCountEventHandler().getAllCount());
		assertEquals(1, helper.getCountEventHandler().getFullGcCount());
		assertEquals(1, helper.getCountEventHandler().getGcCount());

		assertNotNull(helper.getMeasuresHandler().getFullGcPerformance());
		assertEquals(new BigDecimal("172.767"), helper.getMeasuresHandler()
				.getFullGcPerformance().setScale(3, RoundingMode.HALF_UP));

		assertNotNull(helper.getMeasuresHandler().getGcPerformance());
		assertEquals(new BigDecimal("9316.286"), helper.getMeasuresHandler()
				.getGcPerformance().setScale(3, RoundingMode.HALF_UP));

		assertNotNull(helper.getMeasuresHandler().getThroughput());
		assertEquals(84, helper.getMeasuresHandler().getThroughput()
				.longValue());

		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintOldTotalMaxValue());
		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintOldTotalMinValue());

		assertEquals(new BigDecimal("21248"), helper.getMemoryHandler()
				.getFootprintPermTotalMaxValue());
		assertEquals(new BigDecimal("21248"), helper.getMemoryHandler()
				.getFootprintPermTotalMinValue());

		assertEquals(new BigDecimal("5353120"), helper.getMemoryHandler()
				.getFootprintTotalMaxValue());
		assertEquals(new BigDecimal("5253120"), helper.getMemoryHandler()
				.getFootprintTotalMinValue());

		assertEquals(new BigDecimal("414773"), helper.getMemoryHandler()
				.getFreedAllMemoryValue());
		assertEquals(new BigDecimal("145758"), helper.getMemoryHandler()
				.getFreedFullGcMemoryValue());
		assertEquals(new BigDecimal("269015"), helper.getMemoryHandler()
				.getFreedGcMemoryValue());

		assertNotNull(helper.getPauseTimeHandler().getAllPause());
		assertEquals(
				new BigDecimal("852094000.000"),
				helper.getPauseTimeHandler().getAllPause()
						.setScale(3, RoundingMode.HALF_UP));
		assertEquals(
				new BigDecimal("0.852"),
				ParserHelper.convertNanosToSeconds(
						helper.getPauseTimeHandler().getAllPause()).setScale(3,
						RoundingMode.HALF_UP));

		assertNotNull(helper.getPauseTimeHandler().getFullGcPause());
		assertEquals(823895000, helper.getPauseTimeHandler().getFullGcPause()
				.longValue());
		assertEquals(
				new BigDecimal("0.824"),
				ParserHelper.convertNanosToSeconds(
						helper.getPauseTimeHandler().getFullGcPause())
						.setScale(3, RoundingMode.HALF_UP));

		assertNotNull(helper.getPauseTimeHandler().getGcPause());
		assertEquals(28199000, helper.getPauseTimeHandler().getGcPause()
				.longValue());
		assertEquals(
				new BigDecimal("0.028"),
				ParserHelper.convertNanosToSeconds(
						helper.getPauseTimeHandler().getGcPause()).setScale(3,
						RoundingMode.HALF_UP));

		assertNotNull(helper.getPauseTimeHandler().getMaxPause());
		assertEquals(823895000, helper.getPauseTimeHandler().getMaxPause()
				.longValue());
		assertEquals(
				new BigDecimal("0.8239"),
				ParserHelper.convertNanosToSeconds(
						helper.getPauseTimeHandler().getMaxPause()).setScale(4,
						RoundingMode.HALF_UP));

		assertNotNull(helper.getPauseTimeHandler().getMinPause());
		assertEquals(28199000, helper.getPauseTimeHandler().getMinPause()
				.longValue());
		assertEquals(
				new BigDecimal("0.0282"),
				ParserHelper.convertNanosToSeconds(
						helper.getPauseTimeHandler().getMinPause()).setScale(4,
						RoundingMode.HALF_UP));
	}
}
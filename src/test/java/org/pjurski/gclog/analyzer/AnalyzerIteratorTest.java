package org.pjurski.gclog.analyzer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.Iterator;

import org.junit.Test;
import org.pjurski.gclog.model.CollectorInfo;
import org.pjurski.gclog.model.Event;
import org.pjurski.gclog.parser.GcLogCCConstants;
import org.pjurski.gclog.parser.ParseException;

public class AnalyzerIteratorTest {

	@Test
	public void jdk16FullGCParNew_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"283.736: [Full GC 283.736: [ParNew: 261599K->261599K(261952K), 0.0000615 secs] 926554K->826554K(1048384K), 1.000 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNull(event.getTimestamp());

		assertEquals(1, event.getInfo().getSubEvents().size());

		assertEquals(283736000000L, event.getInfo().getStats().getStartTime()
				.longValue());
		assertNull(event.getInfo().getStats().getPauseTime());

		CollectorInfo info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(IAnalyzerConstants.FULL_GC, info1.getGeneralKind());

		assertEquals(true, info1.isFull());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(283736000000L, info1.getStats().getStartTime().longValue());
		assertEquals(1000000000L, info1.getStats().getPauseTime().longValue());

		assertEquals(Long.valueOf(926554), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(826554), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(1048384), info1.getStats().getTotalMemory());

		assertEquals(1000000000, info1.getStats().getPauseTime().longValue());
		assertEquals(1, info1.getSubEvents().size());
	}

	@Test
	public void jdk16XloggcGC_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"0.064: [GC 2048K->992K(3136K), 0.0015430 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNull(event.getTimestamp());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, info1.getGeneralKind());
		assertEquals(false, info1.isFull());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(64000000L, info1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(2048), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(992), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(3136), info1.getStats().getTotalMemory());

		assertEquals(1543000, info1.getStats().getPauseTime().longValue());

		assertEquals(0, info1.getSubEvents().size());
	}

	@Test
	public void jdk16GCDesiredSurvivorMemoryAndTime_event()
			throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"2010-11-10T15:59:03.973+0000: 0.859: [GC Desired survivor size 5439488 bytes, new threshold 7 (max 15)  [PSYoungGen: 32000K->288K(37312K)] 32000K->288K(122688K), 0.0010000 secs] [Times: user=0.01 sys=0.02, real=0.03 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNotNull(event.getTimestamp());
		assertEquals("10 Nov 2010 15:59:03 GMT", event.getTimestamp()
				.toGMTString());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, info1.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_DESIRED_SURVIVOR, info1.getKind());
		assertEquals(false, info1.isFull());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(859000000L, info1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(32000), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(288), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(122688), info1.getStats().getTotalMemory());

		assertEquals(1000000, info1.getStats().getPauseTime().longValue());

		assertEquals(1, info1.getSubEvents().size());

		CollectorInfo info1_1 = info1.getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, info1_1.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_PS_YOUNG_GEN, info1_1.getKind());

		assertNull(info1_1.getStats().getStartTime());

		assertEquals(Long.valueOf(32000), info1_1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(288), info1_1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(37312), info1_1.getStats().getTotalMemory());

		assertNull(info1_1.getStats().getPauseTime());
	}

	@Test
	public void jdk16Multiline_GC_FullGC_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"0.064: [GC 2048K->992K(3136K), 0.0015430 secs]\n0.066: [Full GC 992K->983K(5248K), 0.0050660 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNull(event.getTimestamp());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, info1.getGeneralKind());
		assertEquals(false, info1.isFull());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(64000000L, info1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(2048), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(992), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(3136), info1.getStats().getTotalMemory());

		assertEquals(1543000, info1.getStats().getPauseTime().longValue());

		assertEquals(0, info1.getSubEvents().size());

		assertEquals(true, analyzer.hasNext());
		event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNull(event.getTimestamp());

		assertEquals(1, event.getInfo().getSubEvents().size());

		info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, info1.getGeneralKind());
		assertEquals(true, info1.isFull());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(66000000L, info1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(992), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(983), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(5248), info1.getStats().getTotalMemory());

		assertEquals(5066000, info1.getStats().getPauseTime().longValue());

		assertEquals(0, info1.getSubEvents().size());
	}

	@Test
	public void jdk16TimeForEachGeneration_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"2.452: [GC 2.453: [DefNew: 3711K->3711K(3712K), 0.0000235 secs]2.454: [Tenured: 2830K->1020K(4096K), 0.0088657 secs] 6542K->1020K(7808K), [Perm : 29K->29K(12288K)], 0.0089766 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNull(event.getTimestamp());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, info1.getGeneralKind());
		assertEquals(true, info1.isFull());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(2452000000L, info1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(6542), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(1020), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(7808), info1.getStats().getTotalMemory());

		assertEquals(8976600, info1.getStats().getPauseTime().longValue());

		assertEquals(3, info1.getSubEvents().size());

		Iterator<CollectorInfo> info1Iterator = info1.getSubEvents().iterator();

		CollectorInfo info1_1 = info1Iterator.next();
		assertEquals(GcLogCCConstants.GC_GC, info1_1.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_DEF_NEW, info1_1.getKind());

		assertNotNull(info1_1.getStats().getStartTime());
		assertEquals(2453000000L, info1_1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(3711), info1_1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(3711), info1_1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(3712), info1_1.getStats().getTotalMemory());

		assertEquals(23500, info1_1.getStats().getPauseTime().longValue());

		assertEquals(0, info1_1.getSubEvents().size());

		CollectorInfo info1_2 = info1Iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, info1_2.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_TENURED, info1_2.getKind());

		assertNotNull(info1_2.getStats().getStartTime());
		assertEquals(2454000000L, info1_2.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(2830), info1_2.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(1020), info1_2.getStats().getAfterMemory());
		assertEquals(Long.valueOf(4096), info1_2.getStats().getTotalMemory());

		assertEquals(8865700, info1_2.getStats().getPauseTime().longValue());

		assertEquals(0, info1_2.getSubEvents().size());

		CollectorInfo info1_3 = info1Iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, info1_3.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_PERM, info1_3.getKind());

		assertNull(info1_3.getStats().getStartTime());
		assertNull(info1_3.getStats().getPauseTime());

		assertEquals(Long.valueOf(29), info1_3.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(29), info1_3.getStats().getAfterMemory());
		assertEquals(Long.valueOf(12288), info1_3.getStats().getTotalMemory());

		assertNull(info1_3.getStats().getPauseTime());

		assertEquals(0, info1_3.getSubEvents().size());
	}

	@Test
	public void jdk16TimeForAllGeneration_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"2.452: [GC 2.453: [DefNew: 3711K->3711K(3712K), 0.0000235 secs]2.454: [Tenured: 2830K->1020K(4096K), 0.0088657 secs]2.455: [Perm : 29K->29K(12288K)] 6542K->1020K(7808K), 0.0089766 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertNotNull(event);

		assertEquals(-1, event.getInfo().getGeneralKind());

		assertNull(event.getTimestamp());

		assertNotNull(event.getTimes());
		assertEquals(20000000, event.getTimes().getGcUserTime().longValue());
		assertEquals(0, event.getTimes().getGcSysTime().longValue());
		assertEquals(20000000, event.getTimes().getGcRealTime().longValue());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo info1 = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, info1.getGeneralKind());

		assertNotNull(info1.getStats().getStartTime());
		assertEquals(2452000000L, info1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(6542), info1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(1020), info1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(7808), info1.getStats().getTotalMemory());

		assertEquals(8976600, info1.getStats().getPauseTime().longValue());

		assertEquals(3, info1.getSubEvents().size());

		Iterator<CollectorInfo> info1Iterator = info1.getSubEvents().iterator();

		CollectorInfo info1_1 = info1Iterator.next();
		assertEquals(GcLogCCConstants.GC_GC, info1_1.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_DEF_NEW, info1_1.getKind());

		assertNotNull(info1_1.getStats().getStartTime());
		assertEquals(2453000000L, info1_1.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(3711), info1_1.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(3711), info1_1.getStats().getAfterMemory());
		assertEquals(Long.valueOf(3712), info1_1.getStats().getTotalMemory());

		assertEquals(23500, info1_1.getStats().getPauseTime().longValue());

		assertEquals(0, info1_1.getSubEvents().size());

		CollectorInfo info1_2 = info1Iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, info1_2.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_TENURED, info1_2.getKind());

		assertNotNull(info1_2.getStats().getStartTime());
		assertEquals(2454000000L, info1_2.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(2830), info1_2.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(1020), info1_2.getStats().getAfterMemory());
		assertEquals(Long.valueOf(4096), info1_2.getStats().getTotalMemory());

		assertEquals(8865700, info1_2.getStats().getPauseTime().longValue());

		assertEquals(0, info1_2.getSubEvents().size());

		CollectorInfo info1_3 = info1Iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, info1_3.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_PERM, info1_3.getKind());

		assertNotNull(info1_3.getStats().getStartTime());
		assertEquals(2455000000L, info1_3.getStats().getStartTime().longValue());

		assertEquals(Long.valueOf(29), info1_3.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(29), info1_3.getStats().getAfterMemory());
		assertEquals(Long.valueOf(12288), info1_3.getStats().getTotalMemory());

		assertNull(info1_3.getStats().getPauseTime());

		assertEquals(0, info1_3.getSubEvents().size());
	}

	@Test
	public void jdk16GcPromotionFailed_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"4.490: [GC 4.490: [DefNew (promotion failed): 3569K->3712K(3712K), 0.0025361 secs]4.492: [Tenured: 4096K->1663K(4096K), 0.0099149 secs] 6343K->1663K(7808K), [Perm : 29K->29K(12288K)], 0.0125538 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo gc = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, gc.getGeneralKind());
		assertEquals(false, gc.isPromotionFailed());

		assertEquals(3, gc.getSubEvents().size());

		CollectorInfo defNew = gc.getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, defNew.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_DEF_NEW, defNew.getKind());
		assertEquals(true, defNew.isPromotionFailed());
	}

	@Test
	public void jdk16GcPromotionFailed2_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"4.490: [GC 4.490: [DefNew: 3712K->3569K(3712K), 0.0025361 secs]4.492: [Tenured (promotion failed): 4094K->4096K(4096K), 0.0099149 secs] 6343K->1663K(7808K), [Perm : 29K->29K(12288K)], 0.0125538 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();
		assertEquals(false, analyzer.hasNext());

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo gc = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, gc.getGeneralKind());
		assertEquals(false, gc.isPromotionFailed());

		assertEquals(3, gc.getSubEvents().size());

		Iterator<CollectorInfo> iterator = gc.getSubEvents().iterator();
		iterator.next();

		CollectorInfo defNew = iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, defNew.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_TENURED, defNew.getKind());
		assertEquals(true, defNew.isPromotionFailed());
	}

	@Test
	public void jdk16TryingAFullCollectionBecauseScavengeFailed_event()
			throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"283.736: [Full GC 283.736: [ParNew: 261599K->261599K(261952K), 0.0000615 secs] 826554K->826554K(1048384K), 0.0003259 secs]\nGC locker: Trying a full collection because scavenge failed\n283.736: [Full GC 283.736: [ParNew: 261599K->261599K(261952K), 0.0000288 secs] 826554K->826554K(1048384K), 0.0003259 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();

		assertEquals(1, event.getInfo().getSubEvents().size());

		CollectorInfo gc = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_FULL_GC, gc.getGeneralKind());
		assertEquals(false, gc.isPromotionFailed());

		assertEquals(1, gc.getSubEvents().size());

		Iterator<CollectorInfo> iterator = gc.getSubEvents().iterator();
		CollectorInfo parNew = iterator.next();
		assertEquals(GcLogCCConstants.GC_GC, parNew.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_PAR_NEW, parNew.getKind());
		assertEquals(false, parNew.isPromotionFailed());

		assertEquals(true, analyzer.hasNext());
		event = analyzer.next();

		assertEquals(false, analyzer.hasNext());
	}

	@Test
	public void jdk16GcCmsInitialMark_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"31679.536: [GC [1 CMS-initial-mark: 298349K(303104K)] 298442K(327616K), 0.0033056 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();

		assertEquals(1, event.getInfo().getSubEvents().size());

		assertNotNull(event.getInfo().getStats().getStartTime());
		assertEquals(31679536000000L, event.getInfo().getStats().getStartTime()
				.longValue());

		assertNull(event.getInfo().getStats().getPauseTime());

		CollectorInfo gc = event.getInfo().getSubEvents().iterator().next();
		assertEquals(GcLogCCConstants.GC_GC, gc.getGeneralKind());
		assertEquals(false, gc.isPromotionFailed());

		assertEquals(Long.valueOf(298442), gc.getStats().getBeforeMemory());
		assertEquals(Long.valueOf(327616), gc.getStats().getTotalMemory());
		assertNull(gc.getStats().getAfterMemory());

		assertNotNull(gc.getStats().getPauseTime());
		assertEquals(3305600L, gc.getStats().getPauseTime().longValue());

		assertEquals(1, gc.getSubEvents().size());

		Iterator<CollectorInfo> iterator = gc.getSubEvents().iterator();
		CollectorInfo gc1CmsInitialMark = iterator.next();
		assertEquals(GcLogCCConstants.GC_FULL_GC,
				gc1CmsInitialMark.getGeneralKind());
		assertEquals(GcLogCCConstants.GC_1_CMS_INITIAL_MARK,
				gc1CmsInitialMark.getKind());
		assertEquals(false, gc1CmsInitialMark.isPromotionFailed());
		assertEquals(Long.valueOf(298349), gc1CmsInitialMark.getStats()
				.getBeforeMemory());
		assertEquals(Long.valueOf(303104), gc1CmsInitialMark.getStats()
				.getTotalMemory());
		assertNull(gc1CmsInitialMark.getStats().getAfterMemory());

		assertNull(gc1CmsInitialMark.getStats().getPauseTime());

		assertEquals(false, analyzer.hasNext());
	}

	@Test
	public void jdk16GcYgOccupancyRescanParallel_event() throws ParseException {
		AnalyzerIterator analyzer = new AnalyzerIterator(
				"0.879: [GC[YG occupancy: 3535 K (14784 K)]0.879: [Rescan (parallel) , 1.0010445 secs]0.880: [weak refs processing, 0.0000073 secs] [1 CMS-remark: 2083K(4096K)] 5619K(18880K), 1.0011261 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]");
		assertEquals(true, analyzer.hasNext());
		Event event = analyzer.next();

		assertEquals(1, event.getInfo().getSubEvents().size());

		assertNotNull(event.getInfo().getStats().getStartTime());
		assertEquals(879000000L, event.getInfo().getStats().getStartTime()
				.longValue());

		CollectorInfo info = event.getInfo().getSubEvents().iterator().next();
		assertEquals(4, info.getSubEvents().size());

		Iterator<CollectorInfo> iterator = info.getSubEvents().iterator();
		CollectorInfo ygOccupancy = iterator.next();
		assertEquals(GcLogCCConstants.GC_YG_OCCUPANCY, ygOccupancy.getKind());

		CollectorInfo rescan = iterator.next();
		assertEquals(GcLogCCConstants.GC_RESCAN_PARALLEL, rescan.getKind());

		CollectorInfo weakRefs = iterator.next();
		assertEquals(GcLogCCConstants.GC_WEAK_REFS_PROCESSING,
				weakRefs.getKind());

		CollectorInfo cms1Remark = iterator.next();
		assertEquals(GcLogCCConstants.GC_1_CMS_REMARK, cms1Remark.getKind());
	}
}
package org.pjurski.gclog.parser;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.pjurski.gclog.parser.Parser;
import org.pjurski.gclog.parser.ParserHelper;

public class ParserTest {

	@Test
	public void jdk16XloggcGC() throws ParseException {
		Token[] tokens = Parser
				.parse("0.064: [GC 2048K->992K(3136K), 0.0015430 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.064", time.image);

		Token memory = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_MEMORY);
		assertNotNull(memory);

		Long[] memoryChange = ParserHelper.findMemoryChange(memory);
		assertNotNull(memoryChange);
		assertEquals(3, memoryChange.length);

		for (Long m : memoryChange) {
			assertNotNull(m);
		}
		assertEquals(Long.valueOf(2048), memoryChange[0]);
		assertEquals(Long.valueOf(992), memoryChange[1]);
		assertEquals(Long.valueOf(3136), memoryChange[2]);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNull(user);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNull(sys);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNull(real);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0015430", time.image);
	}

	@Test
	public void jdk16XloggcFullGC() throws ParseException {
		Token[] tokens = Parser
				.parse("0.066: [Full GC 992K->983K(5248K), 0.0050660 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.066", time.image);

		Token memory = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_MEMORY);
		assertNotNull(memory);

		Long[] memoryChange = ParserHelper.findMemoryChange(memory);
		assertNotNull(memoryChange);
		assertEquals(3, memoryChange.length);

		for (Long m : memoryChange) {
			assertNotNull(m);
		}
		assertEquals(Long.valueOf(992), memoryChange[0]);
		assertEquals(Long.valueOf(983), memoryChange[1]);
		assertEquals(Long.valueOf(5248), memoryChange[2]);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNull(user);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNull(sys);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNull(real);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0050660", time.image);
	}

	@Test
	public void jdk16DateSurvivor() throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-10T15:59:03.973+0000: 0.859: [GC Desired survivor size 5439488 bytes, new threshold 7 (max 15)  [PSYoungGen: 32000K->288K(37312K)] 32000K->288K(122688K), 0.0010000 secs] [Times: user=0.01 sys=0.02, real=0.03 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNotNull(date);
		assertEquals("2010-11-10T15:59:03.973+0000", date.image);

		Token time = ParserHelper.findToken(date, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.859", time.image);

		Token psYoungGenMemory = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_MEMORY);
		assertNotNull(psYoungGenMemory);

		Long[] psYoungGenMemoryChange = ParserHelper
				.findMemoryChange(psYoungGenMemory);
		assertNotNull(psYoungGenMemoryChange);
		assertEquals(3, psYoungGenMemoryChange.length);

		for (Long m : psYoungGenMemoryChange) {
			assertNotNull(m);
		}
		assertEquals(Long.valueOf(32000), psYoungGenMemoryChange[0]);
		assertEquals(Long.valueOf(288), psYoungGenMemoryChange[1]);
		assertEquals(Long.valueOf(37312), psYoungGenMemoryChange[2]);

		psYoungGenMemory = psYoungGenMemory.next;
		psYoungGenMemory = psYoungGenMemory.next;
		psYoungGenMemory = psYoungGenMemory.next;
		psYoungGenMemory = psYoungGenMemory.next;
		psYoungGenMemory = psYoungGenMemory.next;
		psYoungGenMemory = psYoungGenMemory.next;
		Token gcDesiredMemory = ParserHelper.findToken(psYoungGenMemory,
				tokens, GcLogCCConstants.GC_MEMORY);
		assertNotNull(psYoungGenMemory);

		Long[] gcDesiredMemoryMemoryChange = ParserHelper
				.findMemoryChange(gcDesiredMemory);
		assertNotNull(gcDesiredMemoryMemoryChange);
		assertEquals(3, gcDesiredMemoryMemoryChange.length);

		for (Long m : gcDesiredMemoryMemoryChange) {
			assertNotNull(m);
		}
		assertEquals(Long.valueOf(32000), gcDesiredMemoryMemoryChange[0]);
		assertEquals(Long.valueOf(288), gcDesiredMemoryMemoryChange[1]);
		assertEquals(Long.valueOf(122688), gcDesiredMemoryMemoryChange[2]);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.01", user.next.image);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNotNull(sys);
		assertNotNull(sys.next);
		assertEquals(GcLogCCConstants.GC_TIME, sys.next.kind);
		assertEquals("0.02", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.03", real.next.image);
	}

	@Test
	public void jdk16DateSurvivorPSYoungGen() throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-10T15:59:03.973+1234: 0.859: [GC\n Desired survivor size 5439488 bytes, new threshold 7 (max 15)\n [PSYoungGen: 32000K->288K(37312K)] 32000K->288K(122688K), 0.0010000 secs] [Times: user=0.02 sys=0.03, real=0.05 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNotNull(date);
		assertEquals("2010-11-10T15:59:03.973+1234", date.image);

		Token time = ParserHelper.findToken(date, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.859", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.02", user.next.image);

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
		assertEquals("0.05", real.next.image);
	}

	@Test
	public void jdk16TimeSurvivorPSYoungGen() throws ParseException {
		Token[] tokens = Parser
				.parse("0.859: [GC\n Desired survivor size 5439488 bytes, new threshold 7 (max 15)\n [PSYoungGen: 32000K->288K(37312K)] 32000K->288K(122688K), 0.0010000 secs] [Times: user=0.02 sys=0.03, real=0.05 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNull(date);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.859", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.02", user.next.image);

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
		assertEquals("0.05", real.next.image);
	}

	@Test
	public void jdk16DateFullGCPSYoungGenPSOldGenPSPermGen()
			throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-11T12:52:34.807+0000: 0.065: [Full GC [PSYoungGen: 296K->231K(2368K)] [PSOldGen: 688K->748K(2816K)] 984K->979K(5184K) [PSPermGen: 2649K->2649K(21248K)], 0.0065200 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNotNull(date);
		assertEquals("2010-11-11T12:52:34.807+0000", date.image);

		Token time = ParserHelper.findToken(date, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.065", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.00", user.next.image);

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
		assertEquals("0.01", real.next.image);
	}

	@Test
	public void jdk16TimeGCPSYoungGen() throws ParseException {
		Token[] tokens = Parser
				.parse("0.248: [GC [PSYoungGen: 64904K->1224K(69312K)] 64904K->1224K(154688K), 0.0015690 secs] [Times: user=0.00 sys=0.01, real=0.00 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNull(date);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.248", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.00", user.next.image);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNotNull(sys);
		assertNotNull(sys.next);
		assertEquals(GcLogCCConstants.GC_TIME, sys.next.kind);
		assertEquals("0.01", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.00", real.next.image);
	}

	@Test
	public void jdk16ApplicationConcurrentTime() throws ParseException {
		Token[] tokens = Parser
				.parse("Application time: 0.0837280 seconds\n0.119: [GC [PSYoungGen: 32000K->344K(37312K)] 32000K->344K(122688K), 0.0017450 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNull(date);

		Token appConc = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_APP_CONCURRENT_TIME);

		Token appConcTime = ParserHelper.findToken(appConc, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(appConcTime);
		assertEquals("0.0837280", appConcTime.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.01", user.next.image);

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
		assertEquals("0.00", real.next.image);
	}

	@Test
	public void jdk16ApplicationStoppedTime() throws ParseException {
		Token[] tokens = Parser
				.parse("0.062: [Full GC [PSYoungGen: 296K->215K(2368K)] [PSOldGen: 696K->767K(2880K)] 992K->983K(5248K) [PSPermGen: 2651K->2651K(21248K)], 0.0049910 secs] [Times: user=0.49 sys=0.51, real=1.00 secs]\nTotal time for which application threads were stopped: 0.0019171 seconds");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNull(date);

		Token appConc = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_APP_STOPPED_TIME);

		Token appConcTime = ParserHelper.findToken(appConc, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(appConcTime);
		assertEquals("0.0019171", appConcTime.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.49", user.next.image);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNotNull(sys);
		assertNotNull(sys.next);
		assertEquals(GcLogCCConstants.GC_TIME, sys.next.kind);
		assertEquals("0.51", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("1.00", real.next.image);
	}

	@Test
	public void jdk16ApplicationConcurrentTimeApplicationStoppedTime()
			throws ParseException {
		Token[] tokens = Parser
				.parse("Application time: 0.0837280 seconds\n0.119: [GC [PSYoungGen: 32000K->344K(37312K)] 32000K->344K(122688K), 0.0017450 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] \nTotal time for which application threads were stopped: 0.0019170 seconds");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNull(date);

		Token appConc = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_APP_CONCURRENT_TIME);

		Token appConcTime = ParserHelper.findToken(appConc, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(appConcTime);
		assertEquals("0.0837280", appConcTime.image);

		Token appStopped = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_APP_STOPPED_TIME);
		assertNotNull(appStopped);

		Token appStoppedTime = ParserHelper.findToken(appStopped, tokens,
				GcLogCCConstants.GC_TIME);
		assertNotNull(appStoppedTime);
		assertEquals("0.0019170", appStoppedTime.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.01", user.next.image);

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
		assertEquals("0.00", real.next.image);
	}

	@Test
	public void jdk16PrintTenuringDistribution() throws ParseException {
		Token[] tokens = Parser
				.parse("0.062: [GC\nDesired survivor size 393216 bytes, new threshold 1 (max 32)\n- age   1:   509624 bytes,   509624 total\n 20288K->497K(64768K), 0.0147963 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("0.062", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNull(user);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNull(sys);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNull(real);
	}

	@Test
	public void jdk16PrintTenuringDistributionExtraColon()
			throws ParseException {
		Token[] tokens = Parser
				.parse("0.062: [GC\nDesired survivor size 393216 bytes, new threshold 1 (max 32)\n- age   1:   509624 bytes,   509624 total\n: 20288K->497K(64768K), 0.0147963 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("0.062", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNull(user);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNull(sys);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNull(real);
	}

	@Test
	public void jdk16FullGcCmsCmsPerm() throws ParseException {
		Token[] tokens = Parser
				.parse("3.016: [Full GC 3.016: [CMS: 15206K->17918K(2075904K), 0.2163990 secs] 30527K->17918K(2095040K), [CMS Perm : 21247K->21227K(21248K)], 0.2166930 secs] [Times: user=0.43 sys=0.06, real=0.22 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("3.016", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.43", user.next.image);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNotNull(sys);
		assertNotNull(sys.next);
		assertEquals(GcLogCCConstants.GC_TIME, sys.next.kind);
		assertEquals("0.06", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.22", real.next.image);
	}

	@Test
	public void jdk16FullGcSystem() throws ParseException {
		Token[] tokens = Parser
				.parse("123.228: [Full GC (System) 123.228: [CMS: 1245449K->1123994K(2075904K), 3.6891790 secs] 1315149K->1123994K(2152576K), [CMS Perm : 139833K->139597K(202184K)], 3.6895430 secs] [Times: user=3.69 sys=0.00, real=3.69 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("123.228", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("3.69", user.next.image);

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
		assertEquals("3.69", real.next.image);
	}

	@Test
	public void jdk16GcDefNewTenured() throws ParseException {
		Token[] tokens = Parser
				.parse("111.042: [GC 111.042: [DefNew: 8128K->8128K(8128K), 0.0000505 secs] [Tenured: 18154K->2311K(24576K), 0.1290354 secs] 26282K->2311K(32704K), 0.1293306 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("111.042", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNull(user);

		Token sys = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_SYS);
		assertNull(sys);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNull(real);
	}

	@Test
	public void jdk16GcParNew() throws ParseException {
		Token[] tokens = Parser
				.parse("212.048: [GC 212.048: [ParNew: 815196K->31780K(827904K), 0.0353270 secs] 1604361K->820946K(3457536K), 0.0354920 secs] [Times: user=0.12 sys=0.00, real=0.04 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("212.048", time.image);

		Token user = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_USER);
		assertNotNull(user);
		assertNotNull(user.next);
		assertEquals(GcLogCCConstants.GC_TIME, user.next.kind);
		assertEquals("0.12", user.next.image);

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
		assertEquals("0.04", real.next.image);
	}

	@Test
	public void jdk16GcFullTenuredPerm() throws ParseException {
		Token[] tokens = Parser
				.parse("2.228: [Full GC 2.228: [Tenured: 348940K->348940K(349568K), 0.1496737 secs] 488012K->488005K(506816K), [Perm: 1653K->1653K(16384K)], 0.1497941 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.228", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.228", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.1496737", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.1497941", time.image);
	}

	@Test
	public void jdk16GcParNewDesiredSurvivorError1() throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-08T07:46:21.907+0000: 5.392: [GC 5.392: [ParNew\nDesired survivor size 19628032 bytes, new threshold 1 (max 4)\n- age   1:   38097216 bytes,   38097216 total\n: 306688K->37673K(345024K), 0.0280820 secs] 326554K->57539K(6253120K), 0.0281990 secs] [Times: user=0.27 sys=0.12, real=0.03 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("5.392", time.image);

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
		assertEquals("0.12", sys.next.image);

		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.03", real.next.image);
	}

	@Test
	public void jdk16GcParNewDesiredSurvivorManyAgesError2()
			throws ParseException {
		Token[] tokens = Parser
				.parse("1096.789: [GC 1096.789: [ParNew\nDesired survivor size 86232268 bytes, new threshold 6 (max 12)\n- age   1:   50754696 bytes,   50754696 total\n- age   2:   12147696 bytes,   62902392 total\n- age   3:   12295552 bytes,   75197944 total\n- age   4:    6537136 bytes,   81735080 total\n- age   5:    2435944 bytes,   84171024 total\n- age   6:    3013488 bytes,   87184512 total\n- age   7:     627368 bytes,   87811880 total\n- age   8:     999536 bytes,   88811416 total\n- age   9:     924656 bytes,   89736072 total\n- age  10:    1811480 bytes,   91547552 total\n: 554848K->89528K(561792K), 0.5317388 secs] 607743K->146164K(1217152K) icms_dc=18 , 0.5326526 secs]\n");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);

		assertNotNull(time);
		assertEquals("1096.789", time.image);
	}

	@Test
	public void jdk16TwoTimesError3() throws ParseException {
		Token[] tokens = Parser
				.parse("9.698: [GC 9.698: [DefNew: 3711K->3711K(3712K), 0.0000254 secs]9.698: [Tenured: 3320K->1677K(4096K), 0.0104564 secs] 7032K->1677K(7808K), [Perm : 23K->23K(12288K)], 0.0105793 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("9.698", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("9.698", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0000254", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("9.698", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0104564", time.image);
		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0105793", time.image);
	}

	@Test
	public void jdk16AllTimesNospacesError4() throws ParseException {
		Token[] tokens = Parser
				.parse("2.452: [GC 2.453: [DefNew: 3711K->3711K(3712K), 0.0000235 secs]2.454: [Tenured: 2830K->1020K(4096K), 0.0088657 secs]2.455: [Perm : 29K->29K(12288K)] 6542K->1020K(7808K), 0.0089766 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.452", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.453", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0000235", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.454", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0088657", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("2.455", time.image);

		time = ParserHelper.findToken(time, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0089766", time.image);
	}

	@Test
	public void jdk16SimpleGcError5() throws ParseException {
		Token[] tokens = Parser.parse("4.385: [GC, 0.0025341 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("4.385", time.image);

		Token gc = ParserHelper.findToken(tokens, GcLogCCConstants.GC_GC);
		assertNotNull(gc);

		time = ParserHelper.findToken(gc, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0025341", time.image);
	}

	@Test
	public void jdk16SimpleGcError6() throws ParseException {
		Token[] tokens = Parser.parse("4.385: [GC, 0.0025341 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("4.385", time.image);

		Token gc = ParserHelper.findToken(tokens, GcLogCCConstants.GC_GC);
		assertNotNull(gc);

		time = ParserHelper.findToken(gc, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.0025341", time.image);
	}

	@Test
	public void jdk16GcParNewDesiredSurvivor() throws ParseException {
		Token[] tokens = Parser
				.parse("2010-11-04T08:14:46.619+0000: 11.860: [GC 11.860: [ParNew\n	                                                   Desired survivor size 19628032 bytes, new threshold 4 (max 4)\n	                                                   - age   1:   16722496 bytes,   16722496 total\n	                                                   : 345023K->30127K(345024K), 0.0963650 secs] 458134K->167467K(6253120K), 0.0964920 secs] [Times: user=0.38 sys=0.09, real=0.09 secs]");
		assertNotNull(tokens);

		Token date = ParserHelper.findToken(tokens, GcLogCCConstants.GC_DATE);
		assertNotNull(date);
		assertEquals("2010-11-04T08:14:46.619+0000", date.image);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("11.860", time.image);
	}

	@Test
	public void jdk16GcYGOccupancy() throws ParseException {
		Token[] tokens = Parser
				.parse("0.879: [GC[YG occupancy: 3535 K (14784 K)]0.879: [Rescan (parallel) , 1.0010445 secs]0.880: [weak refs processing, 0.0000073 secs] [1 CMS-remark: 2083K(4096K)] 5619K(18880K), 1.0011261 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]");
		assertNotNull(tokens);

		Token time = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("0.879", time.image);

		Token rescan = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_RESCAN_PARALLEL);
		assertNotNull(rescan);
		
		time = ParserHelper.findToken(rescan, tokens, GcLogCCConstants.GC_TIME);
		assertNotNull(time);
		assertEquals("1.0010445", time.image);


		Token remark = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_1_CMS_REMARK);
		assertNotNull(remark);
	}
}
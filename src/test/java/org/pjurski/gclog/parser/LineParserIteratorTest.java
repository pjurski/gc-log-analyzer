package org.pjurski.gclog.parser;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.pjurski.gclog.parser.LineParserIterator;
import org.pjurski.gclog.parser.ParserHelper;

public class LineParserIteratorTest {

	private LineParserIterator parser;

	@Test
	public void oneEntry() throws ParseException {
		this.parser = new LineParserIterator(
				"2010-11-11T12:52:34.807+0000: 0.065: [Full GC [PSYoungGen: 296K->231K(2368K)] [PSOldGen: 688K->748K(2816K)] 984K->979K(5184K) [PSPermGen: 2649K->2649K(21248K)], 0.0065200 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]");
		assertEquals(true, this.parser.hasNext());
		assertNotNull(this.parser.next());
		assertEquals(false, this.parser.hasNext());
	}

	@Test
	public void oneEntry2() throws ParseException {
		this.parser = new LineParserIterator(
				"2010-11-11T12:52:34.807+0000: 0.065: [Full GC [PSYoungGen: 296K->231K(2368K)] [PSOldGen: 688K->748K(2816K)] 984K->979K(5184K) [PSPermGen: 2649K->2649K(21248K)], 0.0065200 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]");
		assertEquals(true, this.parser.hasNext());

		Token[] tokens = this.parser.next();
		assertNotNull(tokens);
		assertTrue(tokens.length > 0);

		assertEquals(false, this.parser.hasNext());
	}

	@Test
	public void oneMultilineEntry() throws ParseException {
		this.parser = new LineParserIterator(
				"2010-11-11T12:41:40.099+0000: 2.743: [GC\nDesired survivor size 327680 bytes, new threshold 6 (max 15)\n[PSYoungGen: 32224K->224K(31488K)] 32224K->224K(116864K), 0.0009910 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]");
		assertEquals(true, this.parser.hasNext());

		Token[] tokens = this.parser.next();
		assertNotNull(tokens);
		assertTrue(tokens.length > 0);

		assertEquals(false, this.parser.hasNext());
	}

	@Test
	public void twoOneLineEntry() throws ParseException {
		this.parser = new LineParserIterator(
				"2010-11-11T12:52:34.863+0000: 0.121: [Full GC [PSYoungGen: 480K->0K(1792K)] [PSOldGen: 2476K->1212K(3520K)] 2956K->1212K(5312K) [PSPermGen: 2665K->2665K(21248K)], 0.0070750 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]\n2010-11-11T12:52:34.863+0000: 0.121: [Full GC [PSYoungGen: 480K->0K(1792K)] [PSOldGen: 2476K->1212K(3520K)] 2956K->1212K(5312K) [PSPermGen: 2665K->2665K(21248K)], 0.0070750 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]");
		assertEquals(true, this.parser.hasNext());

		Token[] tokens = this.parser.next();
		assertNotNull(tokens);
		assertTrue(tokens.length > 0);
		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.01", real.next.image);

		assertEquals(true, this.parser.hasNext());
		tokens = this.parser.next();
		assertNotNull(tokens);
		assertTrue(tokens.length > 0);
		real = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.02", real.next.image);

		assertEquals(false, this.parser.hasNext());
	}

	@Test
	public void twoMultiLineEntry() throws ParseException {
		this.parser = new LineParserIterator(
				"2010-11-08T07:46:21.907+0000: 5.392: [GC 5.392: [ParNew\nDesired survivor size 19628032 bytes, new threshold 1 (max 4)\n- age   1:   38097216 bytes,   38097216 total\n: 306688K->37673K(345024K), 0.0280820 secs] 326554K->57539K(6253120K), 0.0281990 secs] [Times: user=0.27 sys=0.12, real=0.03 secs] \n2010-11-08T07:46:23.626+0000: 7.111: [GC 7.111: [ParNew\nDesired survivor size 19628032 bytes, new threshold 1 (max 4)\n- age   1:   33842032 bytes,   33842032 total\n: 344361K->38336K(345024K), 0.1936850 secs] 364227K->105319K(6253120K), 0.1938060 secs] [Times: user=0.54 sys=0.17, real=0.19 secs] \n");
		assertEquals(true, this.parser.hasNext());

		Token[] tokens = this.parser.next();
		assertNotNull(tokens);
		assertTrue(tokens.length > 0);
		Token real = ParserHelper.findToken(tokens,
				GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.03", real.next.image);

		assertEquals(true, this.parser.hasNext());
		tokens = this.parser.next();
		assertNotNull(tokens);
		assertTrue(tokens.length > 0);
		real = ParserHelper.findToken(tokens, GcLogCCConstants.GC_TIMES_REAL);
		assertNotNull(real);
		assertNotNull(real.next);
		assertEquals(GcLogCCConstants.GC_TIME, real.next.kind);
		assertEquals("0.19", real.next.image);

		assertEquals(false, this.parser.hasNext());
	}

	@Test
	public void oneLineErrorEntry() throws ParseException {
		final List<Boolean> errors = new ArrayList<Boolean>();

		this.parser = new LineParserIterator("abc", new ILineParserListener() {
			public void raiseError(int startLineNumber, String line, Throwable t) {
				errors.add(Boolean.TRUE);
			}
		});
		assertEquals(false, this.parser.hasNext());

		assertEquals(1, errors.size());
	}

	@Test
	public void oneLineOneLineErrorEntry() throws ParseException {
		final List<Boolean> errors = new ArrayList<Boolean>();
		final List<Integer> lines = new ArrayList<Integer>();

		this.parser = new LineParserIterator(
				"0.064: [GC 2048K->992K(3136K), 0.0015430 secs]\nabc",
				new ILineParserListener() {
					public void raiseError(int startLineNumber, String line,
							Throwable t) {
						lines.add(startLineNumber);
						errors.add(Boolean.TRUE);
					}
				});
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(false, this.parser.hasNext());

		assertEquals(1, errors.size());
		assertEquals(1, lines.size());
		assertEquals(Integer.valueOf(2), lines.get(0));
	}

	@Test
	public void oneLineOneLineErrorOneLineEntry() throws ParseException {
		final List<Boolean> errors = new ArrayList<Boolean>();
		final List<Integer> lines = new ArrayList<Integer>();

		this.parser = new LineParserIterator(
				"0.064: [GC 2048K->992K(3136K), 0.0015430 secs]\nabc\n0.066: [Full GC 992K->983K(5248K), 0.0050660 secs]",
				new ILineParserListener() {
					public void raiseError(int startLineNumber, String line,
							Throwable t) {
						lines.add(startLineNumber);
						errors.add(Boolean.TRUE);
					}
				});
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(true, this.parser.hasNext());
		this.parser.next();

		assertEquals(1, errors.size());
		assertEquals(1, lines.size());
		assertEquals(Integer.valueOf(2), lines.get(0));
	}

	@Test
	public void oneLineOneLineOneLineErrorEntry() throws ParseException {
		final List<Boolean> errors = new ArrayList<Boolean>();
		final List<Integer> lines = new ArrayList<Integer>();

		this.parser = new LineParserIterator(
				"0.064: [GC 2048K->992K(3136K), 0.0015430 secs]\n0.066: [Full GC 992K->983K(5248K), 0.0050660 secs]\nabc",
				new ILineParserListener() {
					public void raiseError(int startLineNumber, String line,
							Throwable t) {
						lines.add(startLineNumber);
						errors.add(Boolean.TRUE);
					}
				});
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(false, this.parser.hasNext());

		assertEquals(1, errors.size());
		assertEquals(1, lines.size());
		assertEquals(Integer.valueOf(3), lines.get(0));
	}

	@Test
	public void oneLineErrorOneLineOneLineEntry() throws ParseException {
		final List<Boolean> errors = new ArrayList<Boolean>();
		final List<Integer> lines = new ArrayList<Integer>();

		this.parser = new LineParserIterator(
				"abc\n0.064: [GC 2048K->992K(3136K), 0.0015430 secs]\n0.066: [Full GC 992K->983K(5248K), 0.0050660 secs]",
				new ILineParserListener() {
					public void raiseError(int startLineNumber, String line,
							Throwable t) {
						lines.add(startLineNumber);
						errors.add(Boolean.TRUE);
					}
				});
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(true, this.parser.hasNext());
		this.parser.next();

		assertEquals(1, errors.size());
		assertEquals(1, lines.size());
		assertEquals(Integer.valueOf(1), lines.get(0));
	}

	@Test
	public void multilineRandom1() throws ParseException {
		final List<Boolean> errors = new ArrayList<Boolean>();
		final List<Integer> lines = new ArrayList<Integer>();

		this.parser = new LineParserIterator(
				"9.698: [GC 9.698: [DefNew: 3711K->3711K(3712K), 0.0000254 secs]9.698: [Tenured: 3320K->1677K(4096K), 0.0104564 secs] 7032K->1677K(7808K), [Perm : 23K->23K(12288K)], 0.0105793 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]\n9.689: [GC 9.689: [DefNew: 3711K->383K(3712K), 0.0016044 secs] 5882K->3704K(7808K), 0.0016591 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]\nHeap\n def new generation   total 3712K, used 2053K [0x32190000, 0x32590000, 0x32590000)\n  eden space 3328K,  50% used [0x32190000, 0x323317e0, 0x324d0000)\n  from space 384K,  99% used [0x324d0000, 0x3252ffa0, 0x32530000)\n  to   space 384K,   0% used [0x32530000, 0x32530000, 0x32590000)\n tenured\ngeneration   total 4096K, used 2672K [0x32590000, 0x32990000, 0x32990000)\n   the space 4096K,  65% used [0x32590000, 0x3282c2e8, 0x3282c400, 0x32990000)\n compacting perm gen  total 12288K, used 23K [0x32990000, 0x33590000, 0x36990000)\n   the space 12288K,   0% used [0x32990000, 0x32995f98, 0x32996000, 0x33590000)\n    ro space 10240K,  51% used [0x36990000, 0x36ebae00, 0x36ebae00, 0x37390000)\n    rw space 12288K,  54% used [0x37390000, 0x37a272d8,\n0x37a27400, 0x37f90000)",
				new ILineParserListener() {
					public void raiseError(int startLineNumber, String line,
							Throwable t) {
						lines.add(startLineNumber);
						errors.add(Boolean.TRUE);
					}
				});
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(true, this.parser.hasNext());
		this.parser.next();
		assertEquals(false, this.parser.hasNext());

		assertTrue(errors.size() > 0);
		assertEquals(errors.size(), lines.size());

		assertEquals(Integer.valueOf(3), lines.get(0));
	}
}
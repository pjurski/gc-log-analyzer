package org.pjurski.gclog.action.stat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Formatter;
import java.util.Map;

import org.pjurski.gclog.action.ActionException;
import org.pjurski.gclog.action.ADefaultAction;
import org.pjurski.gclog.action.MemoryUnit;
import org.pjurski.gclog.action.stat.StatisticsAction.Output.Section;
import org.pjurski.gclog.action.stat.handler.CountEventHandler;
import org.pjurski.gclog.action.stat.handler.MeasuresHandler;
import org.pjurski.gclog.action.stat.handler.MemoryHandler;
import org.pjurski.gclog.action.stat.handler.PauseTimeHandler;
import org.pjurski.gclog.parser.ParserHelper;

public class StatisticsAction extends ADefaultAction {

	private static final BigDecimal ZERO = BigDecimal.ZERO;

	private static final String NA = "N/A";

	private static final int SCALE_PRECISION_LONG = 5;
	private static final int SCALE_PRECISION_SHORT = 3;

	@Override
	public void execute(Map<String, String> params) throws ActionException {
		String memunit = params.get("memunit");
		MemoryUnit memoryUnit = MemoryUnit.valueOf(memunit);

		String fileName = params.get("logfile");
		StatisticsHelper helper = null;
		try {
			helper = new StatisticsHelper(memoryUnit, new FileReader(fileName));
		} catch (FileNotFoundException e) {
			throw new ActionException("File not exists: " + fileName, e);
		}
		helper.run();

		Section generalInfo = this.createGeneralSection(
				helper.getCountEventHandler(), helper.getErrors());

		Section pauseInfo = this.createPauseSection(
				helper.getPauseTimeHandler(), helper.getCountEventHandler());

		Section memoryFootprintInfo = this.createMemoryFootprintSection(
				memoryUnit, helper.getMemoryHandler());

		Section memoryFreedInfo = this.createMemoryFreedSection(memoryUnit,
				helper.getMemoryHandler());

		Section measuresInfo = this.createMeasuresSection(memoryUnit,
				helper.getMeasuresHandler());

		new Output(generalInfo, pauseInfo, memoryFootprintInfo,
				memoryFreedInfo, measuresInfo).print();
	}

	private Section createMeasuresSection(MemoryUnit memoryUnit,
			MeasuresHandler handler) {
		Section memoryInfo = new Section("Measures",

		new Output.Message("Throughput",
				toStringThroughput(handler.getThroughput())),

		new Output.Message("Full GC performance",
				handler.getFullGcPerformance() == null ? NA
						: toStringPerformanceInSec(memoryUnit,
								handler.getFullGcPerformance())),

		new Output.Message("GC performance",
				handler.getGcPerformance() == null ? NA
						: toStringPerformanceInSec(memoryUnit,
								handler.getGcPerformance()))

		);
		return memoryInfo;
	}

	private Section createMemoryFootprintSection(MemoryUnit memoryUnit,
			MemoryHandler memoryHandler) {
		Section memoryInfo = new Section("Memory Footprint",

		new Output.Message("Total",
				memoryHandler.getFootprintTotalMinValue() == null ? NA
						: toStringMemory(memoryUnit,
								memoryHandler.getFootprintTotalMinValue(),
								memoryHandler.getFootprintTotalMaxValue())),

		new Output.Message("Old",
				memoryHandler.getFootprintOldTotalMinValue() == null ? NA
						: toStringMemory(memoryUnit,
								memoryHandler.getFootprintOldTotalMinValue(),
								memoryHandler.getFootprintOldTotalMaxValue())),

		new Output.Message("Perm",
				memoryHandler.getFootprintPermTotalMinValue() == null ? NA
						: toStringMemory(memoryUnit,
								memoryHandler.getFootprintPermTotalMinValue(),
								memoryHandler.getFootprintPermTotalMaxValue()))

		);
		return memoryInfo;
	}

	private Section createMemoryFreedSection(MemoryUnit memoryUnit,
			MemoryHandler memoryFootprintHandler) {
		Section memoryInfo = new Section(
				"Memory Freed",

				new Output.Message(
						"Total",
						memoryFootprintHandler.getFreedAllMemoryValue() == null ? NA
								: toStringMemory(memoryUnit,
										memoryFootprintHandler
												.getFreedAllMemoryValue())),

				new Output.Message("Full GC", memoryFootprintHandler
						.getFreedFullGcMemoryValue() == null ? NA
						: toStringMemory(memoryUnit, memoryFootprintHandler
								.getFreedFullGcMemoryValue())),

				new Output.Message("GC", memoryFootprintHandler
						.getFreedGcMemoryValue() == null ? NA : toStringMemory(
						memoryUnit,
						memoryFootprintHandler.getFreedGcMemoryValue()))

		);
		return memoryInfo;
	}

	private Section createPauseSection(PauseTimeHandler pauseMinMaxTimeHandler,
			CountEventHandler countEventHandler) {
		Section pauseInfo = new Section(
				"Pause",

				new Output.Message("Acc",
						toStringSeconds(pauseMinMaxTimeHandler.getAllPause())),

				new Output.Message(
						"Acc full GC",
						toStringSeconds(pauseMinMaxTimeHandler.getFullGcPause())),

				new Output.Message("Acc GC",
						toStringSeconds(pauseMinMaxTimeHandler.getGcPause())),

				new Output.Message("Min Pause", pauseMinMaxTimeHandler
						.getMinPause() == null ? 0
						: toStringSeconds(pauseMinMaxTimeHandler.getMinPause())),

				new Output.Message("Max Pause", pauseMinMaxTimeHandler
						.getMaxPause() == null ? 0
						: toStringSeconds(pauseMinMaxTimeHandler.getMaxPause())),

				new Output.Message("Avg Pause",
						countEventHandler.getAllCount() == 0 ? NA
								: toStringSeconds(pauseMinMaxTimeHandler
										.getAllPause()
										.divide(new BigDecimal(String
												.valueOf(countEventHandler
														.getAllCount())),
												SCALE_PRECISION_SHORT,
												RoundingMode.HALF_UP))),

				new Output.Message("Avg full GC", countEventHandler
						.getFullGcCount() == 0 ? NA
						: toStringSeconds(pauseMinMaxTimeHandler
								.getFullGcPause().divide(
										new BigDecimal(String
												.valueOf(countEventHandler
														.getFullGcCount())),
										SCALE_PRECISION_LONG,
										RoundingMode.HALF_UP))),

				new Output.Message("Avg GC",
						countEventHandler.getGcCount() == 0 ? NA
								: toStringSeconds(pauseMinMaxTimeHandler
										.getGcPause()
										.divide(new BigDecimal(String
												.valueOf(countEventHandler
														.getGcCount())),
												SCALE_PRECISION_LONG,
												RoundingMode.HALF_UP))));
		return pauseInfo;
	}

	private Section createGeneralSection(CountEventHandler countEventHandler,
			int errors) {
		Section generalInfo = new Section("Events",

		new Output.Message("All", String.valueOf(countEventHandler
				.getAllCount())),

		new Output.Message("Full GC", String.valueOf(countEventHandler
				.getFullGcCount())),

				new Output.Message("GC", String.valueOf(countEventHandler
						.getGcCount())),

				new Output.Message("Other", String.valueOf(errors))

		);
		return generalInfo;
	}

	private static String toStringThroughput(BigDecimal value) {
		return value.setScale(SCALE_PRECISION_SHORT, RoundingMode.HALF_UP)
				+ " (%)";
	}

	private static String toStringPerformanceInSec(MemoryUnit memoryUnit,
			BigDecimal value) {
		return value.setScale(SCALE_PRECISION_SHORT, RoundingMode.HALF_UP)
				+ " (" + memoryUnit.toString().toUpperCase() + "/s)";
	}

	private static String toStringMemory(MemoryUnit memoryUnit, BigDecimal value) {
		return memoryUnit.convert(value).setScale(SCALE_PRECISION_SHORT,
				RoundingMode.HALF_UP)
				+ " (" + memoryUnit.toString().toUpperCase() + ")";
	}

	private static String toStringMemory(MemoryUnit memoryUnit, BigDecimal min,
			BigDecimal max) {
		if (min == null) {
			return "("
					+ NA
					+ ")"
					+ "/"
					+ memoryUnit.convert(max).setScale(SCALE_PRECISION_SHORT,
							RoundingMode.HALF_UP) + " ("
					+ memoryUnit.toString().toUpperCase() + ")";
		} else {
			return memoryUnit.convert(min).setScale(SCALE_PRECISION_SHORT,
					RoundingMode.HALF_UP)
					+ "/"
					+ memoryUnit.convert(max).setScale(SCALE_PRECISION_SHORT,
							RoundingMode.HALF_UP)
					+ " ("
					+ memoryUnit.toString().toUpperCase() + ")";
		}
	}

	private static String toStringSeconds(BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("value cannot be null");
		}
		if (ZERO.compareTo(value) == 0) {
			return "0 (s)";
		} else {
			return ParserHelper.convertNanosToSeconds(value).setScale(
					SCALE_PRECISION_LONG, RoundingMode.HALF_UP)
					+ " (s)";
		}
	}

	static class Output {
		private static final String LINE = "=========================================";

		private static final String OUTPUT_FORMAT = "%1$-20s%2$-20s\n";

		private static final String LINE_SEPARATOR = System
				.getProperty("line.separator");

		private final Section[] sections;
		private final Formatter formatter = new Formatter(System.out);

		public Output(Section... sections) {
			this.sections = sections;
		}

		public void print() {
			this.formatter.format("%s", LINE);
			this.formatter.format("%s", LINE_SEPARATOR);

			for (int i = 0; i < this.sections.length; i++) {
				Section section = this.sections[i];
				section.print(this.formatter);
			}
		}

		static class Message {
			private final String name;
			private final Object value;

			public Message(String name, Object value) {
				this.name = name;
				this.value = value;
			}

			public String getName() {
				return this.name;
			}

			public Object getValue() {
				return this.value;
			}
		}

		static class Section {

			private final String name;
			private final Message[] messages;

			public Section(String name, Message... messages) {
				this.name = name;
				this.messages = messages;
			}

			public String getName() {
				return this.name;
			}

			public void print(Formatter formatter) {
				String nameLocal = this.getName();
				if (nameLocal != null) {
					formatter.format("%s", nameLocal);
					formatter.format("%s", LINE_SEPARATOR);
				}
				for (int i = 0; i < this.messages.length; i++) {
					Message m = this.messages[i];
					if (i > 0) {
						formatter.format("", LINE_SEPARATOR);
					}
					formatter.format(OUTPUT_FORMAT, m.getName(), m.getValue());
				}
				formatter.format("%s", LINE);
				formatter.format("%s", LINE_SEPARATOR);
			}
		}
	}
}
package org.pjurski.gclog.action.stat.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.pjurski.gclog.action.MemoryUnit;
import org.pjurski.gclog.action.stat.AHandler;
import org.pjurski.gclog.model.Event;

public class MeasuresHandler extends AHandler {

	private static final BigDecimal VALUE_100 = new BigDecimal("100");

	private static final BigDecimal VALUE_1000000000 = new BigDecimal(
			"1000000000");

	private final MemoryUnit memoryUnit;

	private BigDecimal runningTime = BigDecimal.ZERO;

	private BigDecimal throughput = VALUE_100;

	private BigDecimal fullGcPerformance = null;
	private BigDecimal gcPerformance = null;

	private PauseTimeHandler pauseHandler;
	private MemoryHandler memoryHandler;

	public MeasuresHandler(MemoryUnit memoryUnit, PauseTimeHandler handler,
			MemoryHandler memoryHandler) {
		this.memoryUnit = memoryUnit;
		this.pauseHandler = handler;
		this.memoryHandler = memoryHandler;
	}

	@Override
	protected void update(Event event) {
		this.updateRunningTime(event);
	}

	private void updateRunningTime(Event event) {
		BigDecimal starttime = event.getInfo().getStats().getStartTime();

		if (this.runningTime.compareTo(starttime) == -1) {
			this.runningTime = starttime;

			if (!event.getInfo().getSubEvents().isEmpty()) {
				BigDecimal pauseTime = event.getInfo().getSubEvents()
						.iterator().next().getStats().getPauseTime();
				if (pauseTime != null) {
					this.runningTime = this.runningTime.add(pauseTime);
				}
			}
		}
	}

	@Override
	public void finished() {
		super.finished();

		if (this.pauseHandler.getAllPause() != null
				&& this.pauseHandler.getAllPause().compareTo(BigDecimal.ZERO) != 0) {
			this.throughput = VALUE_100.multiply(
					this.runningTime.subtract(this.pauseHandler.getAllPause()))
					.divide(this.runningTime, 50, RoundingMode.HALF_UP);
		}

		if (this.memoryHandler.getFreedFullGcMemoryValue() != null
				&& this.pauseHandler.getFullGcPause() != null
				&& this.pauseHandler.getFullGcPause()
						.compareTo(BigDecimal.ZERO) != 0) {
			this.fullGcPerformance = this.memoryUnit
					.convert(this.memoryHandler.getFreedFullGcMemoryValue())
					.multiply(VALUE_1000000000)
					.divide(this.pauseHandler.getFullGcPause(), 50,
							RoundingMode.HALF_UP);
		}

		if (this.memoryHandler.getFreedGcMemoryValue() != null
				&& this.pauseHandler.getGcPause() != null
				&& this.pauseHandler.getGcPause().compareTo(BigDecimal.ZERO) != 0) {
			this.gcPerformance = this.memoryUnit
					.convert(this.memoryHandler.getFreedGcMemoryValue())
					.multiply(VALUE_1000000000)
					.divide(this.pauseHandler.getGcPause(), 50,
							RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getThroughput() {
		return this.throughput;
	}

	public BigDecimal getFullGcPerformance() {
		return this.fullGcPerformance;
	}

	public BigDecimal getGcPerformance() {
		return this.gcPerformance;
	}
}
package org.pjurski.gclog.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Stats implements Serializable {

	private final BigDecimal startTime;

	private final Long beforeMemory;
	private final Long afterMemory;
	private final Long totalMemory;

	private final BigDecimal pauseTime;

	public Stats(BigDecimal startTime, Long memoryBefore, Long memoryAfter,
			Long memoryTotal, BigDecimal pauseTime) {
		this.startTime = startTime;
		this.beforeMemory = memoryBefore;
		this.afterMemory = memoryAfter;
		this.totalMemory = memoryTotal;
		this.pauseTime = pauseTime;
	}

	public BigDecimal getStartTime() {
		return this.startTime;
	}

	public Long getBeforeMemory() {
		return this.beforeMemory;
	}

	public Long getAfterMemory() {
		return this.afterMemory;
	}

	public Long getTotalMemory() {
		return this.totalMemory;
	}

	public BigDecimal getPauseTime() {
		return this.pauseTime;
	}
}
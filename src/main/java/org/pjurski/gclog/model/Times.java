package org.pjurski.gclog.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Times implements Serializable {

	private final BigDecimal gcUserTime;
	private final BigDecimal gcSysTime;
	private final BigDecimal gcRealTime;

	public Times(BigDecimal gcUserTime, BigDecimal gcSysTime,
			BigDecimal gcRealTime) {
		this.gcUserTime = gcUserTime;
		this.gcSysTime = gcSysTime;
		this.gcRealTime = gcRealTime;
	}

	public BigDecimal getGcUserTime() {
		return this.gcUserTime;
	}

	public BigDecimal getGcSysTime() {
		return this.gcSysTime;
	}

	public BigDecimal getGcRealTime() {
		return this.gcRealTime;
	}
}
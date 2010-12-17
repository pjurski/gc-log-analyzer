package org.pjurski.gclog.model;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

	private final Date timestamp;

	private final CollectorInfo info;

	private final Times times;

	public Event(Date timestamp, CollectorInfo event, Times times) {
		this.timestamp = timestamp;
		this.info = event;
		this.times = times;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public CollectorInfo getInfo() {
		return this.info;
	}

	public Times getTimes() {
		return this.times;
	}
}
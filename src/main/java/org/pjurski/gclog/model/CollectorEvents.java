package org.pjurski.gclog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectorEvents implements Serializable {

	private final List<CollectorInfo> events = new ArrayList<CollectorInfo>(3);

	public CollectorEvents() {
	}

	public void addEvent(CollectorInfo event) {
		this.events.add(event);
	}

	public Collection<CollectorInfo> getEvents() {
		return Collections.unmodifiableCollection(this.events);
	}
}

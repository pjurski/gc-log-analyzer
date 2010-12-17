package org.pjurski.gclog.action.stat.handler;

import org.pjurski.gclog.action.stat.AHandler;
import org.pjurski.gclog.model.Event;

public class CountEventHandler extends AHandler {

	private int allCount = 0;

	private int fullGcCount = 0;

	private int gcCount = 0;

	@Override
	protected void update(Event event) {
		++this.allCount;
				
		if (event.getInfo().isFull()) {
			++this.fullGcCount;
		} else {
			++this.gcCount;
		}
	}

	public int getAllCount() {
		return this.allCount;
	}

	public int getFullGcCount() {
		return this.fullGcCount;
	}

	public int getGcCount() {
		return this.gcCount;
	}
}
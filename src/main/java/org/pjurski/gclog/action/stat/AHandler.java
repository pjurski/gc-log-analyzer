package org.pjurski.gclog.action.stat;

import org.pjurski.gclog.model.Event;

public abstract class AHandler {

	public void finished() {
	}

	protected abstract void update(Event event);
}
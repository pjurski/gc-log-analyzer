package org.pjurski.gclog.action.stat;

import java.io.Reader;
import java.util.Vector;

import org.pjurski.gclog.action.ActionException;
import org.pjurski.gclog.analyzer.AnalyzerIterator;
import org.pjurski.gclog.model.Event;

public final class EventSource implements Runnable {

	private boolean changed = false;
	private Vector<AHandler> obs = new Vector<AHandler>();

	private final AnalyzerIterator iterator;

	EventSource(Reader reader, ErrorCounterListener listener) throws ActionException {
		this.iterator = new AnalyzerIterator(reader, listener);
	}

	public void addObserver(AHandler o) {
		this.obs.add(o);
	}

	public void deleteObserver(AHandler o) {
		this.obs.remove(o);
	}

	private synchronized void setChanged() {
		this.changed = true;
	}

	private synchronized void clearChanged() {
		this.changed = false;
	}

	private void notifyObservers(Event event) {
		Object[] arrLocal;

		synchronized (this) {
			if (!this.changed)
				return;
			arrLocal = this.obs.toArray();
			this.clearChanged();
		}

		for (int i = 0, len = arrLocal.length; i < len; i++) {
			((AHandler) arrLocal[i]).update(event);
		}
	}

	public void run() {
		while (this.iterator.hasNext()) {
			Event event = this.iterator.next();

			this.setChanged();
			this.notifyObservers(event);
		}

		for (int i = 0; i < this.obs.size(); i++) {
			this.obs.get(i).finished();
		}
	}
}
package org.pjurski.gclog.action.stat;

import java.io.Reader;

import org.pjurski.gclog.action.ActionException;
import org.pjurski.gclog.action.MemoryUnit;
import org.pjurski.gclog.action.stat.handler.CountEventHandler;
import org.pjurski.gclog.action.stat.handler.MeasuresHandler;
import org.pjurski.gclog.action.stat.handler.MemoryHandler;
import org.pjurski.gclog.action.stat.handler.PauseTimeHandler;

class StatisticsHelper {

	private final PauseTimeHandler pauseTimeHandler;
	private final CountEventHandler countEventHandler;
	private final MemoryHandler memoryHandler;
	private final MeasuresHandler measuresHandler;

	private final ErrorCounterListener listener = new ErrorCounterListener();

	private final Reader reader;

	public StatisticsHelper(MemoryUnit memoryUnit, Reader reader) {
		this.reader = reader;
		this.pauseTimeHandler = new PauseTimeHandler();
		this.countEventHandler = new CountEventHandler();
		this.memoryHandler = new MemoryHandler();
		this.measuresHandler = new MeasuresHandler(memoryUnit,
				this.pauseTimeHandler, this.memoryHandler);
	}

	public void run() throws ActionException {
		EventSource eventSource = null;
		eventSource = new EventSource(this.reader, this.listener);

		eventSource.addObserver(this.pauseTimeHandler);
		eventSource.addObserver(this.countEventHandler);
		eventSource.addObserver(this.memoryHandler);
		eventSource.addObserver(this.measuresHandler);

		eventSource.run();
	}

	public CountEventHandler getCountEventHandler() {
		return this.countEventHandler;
	}

	public MeasuresHandler getMeasuresHandler() {
		return this.measuresHandler;
	}

	public MemoryHandler getMemoryHandler() {
		return this.memoryHandler;
	}

	public PauseTimeHandler getPauseTimeHandler() {
		return this.pauseTimeHandler;
	}

	public int getErrors() {
		return this.listener.getErrors();
	}
}
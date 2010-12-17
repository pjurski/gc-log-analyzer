package org.pjurski.gclog.action.stat.handler;

import java.math.BigDecimal;
import java.util.Collection;

import org.pjurski.gclog.action.stat.AHandler;
import org.pjurski.gclog.model.CollectorInfo;
import org.pjurski.gclog.model.Concurrency;
import org.pjurski.gclog.model.Event;

public class PauseTimeHandler extends AHandler {

	private BigDecimal allPause = BigDecimal.ZERO;

	private BigDecimal maxPause = null;
	private BigDecimal minPause = null;

	private BigDecimal fullGcPause = BigDecimal.ZERO;
	private BigDecimal gcPause = BigDecimal.ZERO;

	@Override
	protected void update(Event event) {
		CollectorInfo info = event.getInfo();

		Collection<CollectorInfo> subInfos = info.getSubEvents();
		if (!subInfos.isEmpty()) {
			CollectorInfo subInfo = subInfos.iterator().next();

			BigDecimal pause = subInfo.getStats().getPauseTime();
			if (pause == null) {
				pause = BigDecimal.ZERO;
				for (CollectorInfo subSubInfo : subInfo.getSubEvents()) {
					BigDecimal subSubPauseTime = subSubInfo.getStats()
							.getPauseTime();
					if (subSubPauseTime != null) {
						if (subSubInfo.getType().getConcurrency() == Concurrency.SERIAL) {
							pause = pause.add(subSubPauseTime);
						}
					}
				}
			}
			if (this.minPause == null || this.minPause.compareTo(pause) > 0) {
				this.minPause = pause;
			}
			if (this.maxPause == null || this.maxPause.compareTo(pause) < 0) {
				this.maxPause = pause;
			}

			if (subInfo.isFull()) {
				this.fullGcPause = this.fullGcPause.add(pause);
			} else {
				this.gcPause = this.gcPause.add(pause);
			}
			this.allPause = this.allPause.add(pause);
		}
	}

	@Override
	public void finished() {
		super.finished();

		if (this.minPause == null) {
			this.minPause = BigDecimal.ZERO;
		}

		if (this.maxPause == null) {
			this.maxPause = BigDecimal.ZERO;
		}
	}

	public BigDecimal getMinPause() {
		return this.minPause;
	}

	public BigDecimal getMaxPause() {
		return this.maxPause;
	}

	public BigDecimal getFullGcPause() {
		return this.fullGcPause;
	}

	public BigDecimal getGcPause() {
		return this.gcPause;
	}

	public BigDecimal getAllPause() {
		return this.allPause;
	}
}
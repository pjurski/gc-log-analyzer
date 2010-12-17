package org.pjurski.gclog.action.stat.handler;

import java.math.BigDecimal;

import org.pjurski.gclog.action.stat.AHandler;
import org.pjurski.gclog.model.CollectorInfo;
import org.pjurski.gclog.model.Event;
import org.pjurski.gclog.model.Generation;

public class MemoryHandler extends AHandler {

	private BigDecimal footprintTotalMaxValue = null;
	private BigDecimal footprintTotalMinValue = null;

	private BigDecimal footprintPermTotalMaxValue = null;
	private BigDecimal footprintPermTotalMinValue = null;

	private BigDecimal footprintOldTotalMaxValue = null;
	private BigDecimal footprintOldTotalMinValue = null;

	private BigDecimal freedAllMemoryValue = null;
	private BigDecimal freedFullGcMemoryValue = null;
	private BigDecimal freedGcMemoryValue = null;

	@Override
	protected void update(Event event) {
		// Footprint memory max min
		this.footprintTotalMaxValue = this.checkMemory(event.getInfo(),
				this.footprintTotalMaxValue, MAX_COMPARATOR, TOTAL_MEMORY);

		this.footprintTotalMinValue = this.checkMemory(event.getInfo(),
				this.footprintTotalMinValue, MIN_COMPARATOR, TOTAL_MEMORY);

		// Footprint perm memory max min
		this.footprintPermTotalMaxValue = this.checkMemory(event.getInfo(),
				this.footprintPermTotalMaxValue, MAX_COMPARATOR,
				TOTAL_PERM_MEMORY);

		this.footprintPermTotalMinValue = this.checkMemory(event.getInfo(),
				this.footprintPermTotalMinValue, MIN_COMPARATOR,
				TOTAL_PERM_MEMORY);

		// Footprint old memory max min
		this.footprintOldTotalMaxValue = this.checkMemory(event.getInfo(),
				this.footprintOldTotalMaxValue, MAX_COMPARATOR,
				TOTAL_OLD_MEMORY);

		this.footprintOldTotalMinValue = this.checkMemory(event.getInfo(),
				this.footprintOldTotalMinValue, MIN_COMPARATOR,
				TOTAL_OLD_MEMORY);

		// Freed memory
		if (this.freedAllMemoryValue == null) {
			this.freedAllMemoryValue = this.findFreedMemory(event.getInfo(),
					FREED_MEMORY);
		} else {
			this.freedAllMemoryValue = this.freedAllMemoryValue.add(this
					.findFreedMemory(event.getInfo(), FREED_MEMORY));
		}

		if (event.getInfo().isFull()) {
			if (this.freedFullGcMemoryValue == null) {
				this.freedFullGcMemoryValue = this.findFreedMemory(
						event.getInfo(), FREED_MEMORY);
			} else {
				this.freedFullGcMemoryValue = this.freedFullGcMemoryValue
						.add(this.findFreedMemory(event.getInfo(), FREED_MEMORY));
			}
		} else {
			if (this.freedGcMemoryValue == null) {
				this.freedGcMemoryValue = this.findFreedMemory(event.getInfo(),
						FREED_MEMORY);
			} else {
				this.freedGcMemoryValue = this.freedGcMemoryValue.add(this
						.findFreedMemory(event.getInfo(), FREED_MEMORY));
			}
		}
	}

	private BigDecimal findFreedMemory(CollectorInfo info, IMemoryReader reader) {
		BigDecimal memory = BigDecimal.ZERO;

		if (info != null && reader.getMemory(info) != null) {
			memory = memory.add(reader.getMemory(info));
		} else {
			for (CollectorInfo subInfo : info.getSubEvents()) {
				memory = memory.add(this.findFreedMemory(subInfo, reader));
			}
		}
		return memory;
	}

	private BigDecimal checkMemory(CollectorInfo info, BigDecimal value,
			IMemoryComparator comparator, IMemoryReader reader) {
		BigDecimal memory = null;

		if (info != null && reader.getMemory(info) != null) {
			if (value == null) {
				memory = reader.getMemory(info);
			} else {
				memory = comparator.comapare(value, reader.getMemory(info));
			}
		} else {
			for (CollectorInfo subInfo : info.getSubEvents()) {
				BigDecimal memoryValue = this.checkMemory(subInfo, value,
						comparator, reader);
				if (value == null) {
					memory = memoryValue;
				} else {
					memory = comparator.comapare(value, memoryValue);
				}
			}
		}
		if (value == null) {
			return memory;
		} else {
			if (memory == null) {
				return value;
			} else {
				return comparator.comapare(memory, value);
			}
		}
	}

	public BigDecimal getFootprintTotalMaxValue() {
		return this.footprintTotalMaxValue;
	}

	public BigDecimal getFootprintTotalMinValue() {
		return this.footprintTotalMinValue;
	}

	public BigDecimal getFootprintPermTotalMaxValue() {
		return this.footprintPermTotalMaxValue;
	}

	public BigDecimal getFootprintPermTotalMinValue() {
		return this.footprintPermTotalMinValue;
	}

	public BigDecimal getFootprintOldTotalMaxValue() {
		return this.footprintOldTotalMaxValue;
	}

	public BigDecimal getFootprintOldTotalMinValue() {
		return this.footprintOldTotalMinValue;
	}

	public BigDecimal getFreedFullGcMemoryValue() {
		return this.freedFullGcMemoryValue;
	}

	public BigDecimal getFreedGcMemoryValue() {
		return this.freedGcMemoryValue;
	}

	public BigDecimal getFreedAllMemoryValue() {
		return this.freedAllMemoryValue;
	}

	private static final IMemoryReader TOTAL_MEMORY = new IMemoryReader() {
		public BigDecimal getMemory(CollectorInfo info) {
			if (info.getStats() == null
					|| info.getStats().getTotalMemory() == null) {
				return null;
			}
			return BigDecimal.valueOf(info.getStats().getTotalMemory());
		}
	};

	private static final IMemoryReader TOTAL_OLD_MEMORY = new IMemoryReader() {
		public BigDecimal getMemory(CollectorInfo info) {
			if (info.getType() == null || !info.isFull()
					|| info.getStats() == null
					|| info.getStats().getTotalMemory() == null) {
				return null;
			}
			return BigDecimal.valueOf(info.getStats().getTotalMemory());
		}
	};

	private static final IMemoryReader TOTAL_PERM_MEMORY = new IMemoryReader() {
		public BigDecimal getMemory(CollectorInfo info) {
			if (info.getType() == null
					|| info.getType().getGeneration() != Generation.PERM
					|| info.getStats() == null
					|| info.getStats().getTotalMemory() == null
					|| info.getStats().getTotalMemory() == null) {
				return null;
			}
			return BigDecimal.valueOf(info.getStats().getTotalMemory());
		}
	};

	private static final IMemoryReader FREED_MEMORY = new IMemoryReader() {
		public BigDecimal getMemory(CollectorInfo info) {
			if (info.getStats().getBeforeMemory() == null
					|| info.getStats().getAfterMemory() == null) {
				return null;
			}
			return BigDecimal.valueOf(info.getStats().getBeforeMemory()
					- info.getStats().getAfterMemory());
		}
	};

	private static final IMemoryComparator MIN_COMPARATOR = new IMemoryComparator() {

		public BigDecimal comapare(BigDecimal l1, BigDecimal l2) {
			return l1.min(l2);
		}
	};

	private static final IMemoryComparator MAX_COMPARATOR = new IMemoryComparator() {

		public BigDecimal comapare(BigDecimal l1, BigDecimal l2) {
			return l1.max(l2);
		}
	};

	private static interface IMemoryReader {
		public BigDecimal getMemory(CollectorInfo info);
	}

	private static interface IMemoryComparator {
		public BigDecimal comapare(BigDecimal l1, BigDecimal l2);
	}
}
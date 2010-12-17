package org.pjurski.gclog.model;

import java.io.Serializable;
import java.util.Collection;

public class CollectorInfo implements Serializable {

	private final Type type;
	private final int generalKind;
	private final int kind;

	private final Stats stats;

	private final CollectorEvents subEvents;

	private final boolean promotionFailed;

	public CollectorInfo(int generalKind, int kind, Stats stats,
			CollectorEvents events, boolean promotionFailed) {
		this.generalKind = generalKind;
		this.kind = kind;
		this.type = Type.find(kind);
		this.stats = stats;
		this.subEvents = events;
		this.promotionFailed = promotionFailed;
	}

	public boolean isPromotionFailed() {
		return this.promotionFailed;
	}

	public Type getType() {
		return this.type;
	}

	public int getGeneralKind() {
		return this.generalKind;
	}

	public int getKind() {
		return this.kind;
	}

	public Stats getStats() {
		return this.stats;
	}

	public Collection<CollectorInfo> getSubEvents() {
		return this.subEvents.getEvents();
	}

	private boolean hasTenured() {
		for (CollectorInfo info : this.getSubEvents()) {
			if (info.getType() == Type.TENURED) {
				return true;
			}
		}
		return false;
	}

	public boolean isFull() {
		if (this.getType() == null) {
			Collection<CollectorInfo> infos = this.getSubEvents();
			if (!infos.isEmpty()) {
				return infos.iterator().next().isFull();
			}
		}
		return this.getType() != null
				&& (this.getType() == Type.FULL_GC
						|| this.getType().getGeneration() == Generation.TENURED || this
						.hasTenured());
	}
}
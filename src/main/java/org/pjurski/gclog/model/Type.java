package org.pjurski.gclog.model;

import java.util.Hashtable;
import java.util.Map;

import org.pjurski.gclog.parser.GcLogCCConstants;

public class Type {

	private static final Map<Integer, Type> TYPES = new Hashtable<Integer, Type>();

	public static final Type FULL_GC = Type.create("Full GC", Generation.ALL,
			GcLogCCConstants.GC_FULL_GC);

	public static final Type FULL_GC_SYSTEM = Type.create("Full GC System",
			Generation.ALL, GcLogCCConstants.GC_FULL_GC_SYSTEM);

	public static final Type GC = Type.create("GC", Generation.YOUNG,
			GcLogCCConstants.GC_GC);

	public static final Type DEF_NEW = Type.create("DefNew", Generation.YOUNG,
			GcLogCCConstants.GC_DEF_NEW);

	public static final Type PAR_NEW = Type.create("ParNew", Generation.YOUNG,
			GcLogCCConstants.GC_PAR_NEW);

	public static final Type PAR_OLD_GEN = Type.create("ParOldGen",
			Generation.TENURED, GcLogCCConstants.GC_PAR_OLD_GEN);

	public static final Type PS_YOUNG_GEN = Type.create("PSYoungGen",
			Generation.YOUNG, GcLogCCConstants.GC_PS_YOUNG_GEN);

	public static final Type PS_OLD_GEN = Type.create("PSOldGen",
			Generation.TENURED, GcLogCCConstants.GC_PS_OLD_GEN);

	public static final Type PS_PERM_GEN = Type.create("PSPermGen",
			Generation.PERM, GcLogCCConstants.GC_PS_PERM_GEN);

	public static final Type TENURED = Type.create("Tenured",
			Generation.TENURED, GcLogCCConstants.GC_TENURED);

	public static final Type INC_GC = Type.create("Inc GC", Generation.YOUNG,
			GcLogCCConstants.GC_INC_GC);

	public static final Type TRAIN = Type.create("Train", Generation.TENURED,
			GcLogCCConstants.GC_TRAIN);

	public static final Type TRAIN_MSC = Type.create("Train MSC",
			Generation.TENURED, GcLogCCConstants.GC_TRAIN_MSC);

	public static final Type PERM = Type.create("Perm", Generation.PERM,
			GcLogCCConstants.GC_PERM);

	public static final Type CMS = Type.create("CMS", Generation.TENURED,
			GcLogCCConstants.GC_CMS);

	public static final Type CMS_PERM = Type.create("CMS Perm",
			Generation.PERM, GcLogCCConstants.GC_CMS_PERM);

	// public static final Type CMS_CMF = Type.create(
	// "CMS (concurrent mode failure)", Generation.TENURED,
	// Concurrency.CONCURRENT, GcLogCCConstants.GC_CMS);

	public static final Type CMS_CONCURRENT_MARK_START = Type.create(
			"CMS-concurrent-mark-start", Generation.TENURED,
			Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_MARK_START);

	public static final Type CMS_CONCURRENT_MARK = Type.create(
			"CMS-concurrent-mark", Generation.TENURED, Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_MARK);

	public static final Type CMS_CONCURRENT_PRECLEAN_START = Type.create(
			"CMS-concurrent-preclean-start", Generation.TENURED,
			Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_PRECLEAN_START);

	public static final Type CMS_CONCURRENT_PRECLEAN = Type
			.create("CMS-concurrent-preclean", Generation.TENURED,
					Concurrency.CONCURRENT,
					GcLogCCConstants.GC_CMS_CONCURRENT_PRECLEAN);

	public static final Type CMS_CONCURRENT_SWEEP_START = Type.create(
			"CMS-concurrent-sweep-start", Generation.TENURED,
			Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_SWEEP_START);

	public static final Type CMS_CONCURRENT_SWEEP = Type.create(
			"CMS-concurrent-sweep", Generation.TENURED, Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_SWEEP);

	public static final Type CMS_CONCURRENT_RESET_START = Type.create(
			"CMS-concurrent-reset-start", Generation.TENURED,
			Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_RESET_START);

	public static final Type CMS_CONCURRENT_RESET = Type.create(
			"CMS-concurrent-reset", Generation.TENURED, Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_RESET);

	public static final Type CMS_CONCURRENT_ABORTABLE_PRECLEAN_START = Type
			.create("CMS-concurrent-abortable-preclean-start",
					Generation.TENURED, Concurrency.CONCURRENT,
					GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN_START);

	public static final Type CMS_CONCURRENT_ABORTABLE_PRECLEAN = Type.create(
			"CMS-concurrent-abortable-preclean", Generation.TENURED,
			Concurrency.CONCURRENT,
			GcLogCCConstants.GC_CMS_CONCURRENT_ABORTABLE_PRECLEAN);

	public static final Type CMS_INITIAL_MARK = Type.create("CMS-initial-mark",
			Generation.TENURED, GcLogCCConstants.GC_1_CMS_INITIAL_MARK);

	public static final Type CMS_REMARK = Type.create("CMS-remark",
			Generation.TENURED, GcLogCCConstants.GC_1_CMS_REMARK);

	private final String type;
	private Generation generation;
	private Concurrency concurrency;

	private int kind;

	private Type(String type, Generation generation, Concurrency concurrency,
			int kind) {
		this.type = type;
		this.generation = generation;
		this.concurrency = concurrency;
		this.kind = kind;
	}

	public int getKind() {
		return this.kind;
	}

	public String getType() {
		return this.type;
	}

	public Generation getGeneration() {
		return this.generation;
	}

	public Concurrency getConcurrency() {
		return this.concurrency;
	}

	static Type create(String type, Generation generation, int kind) {
		return create(type, generation, Concurrency.SERIAL, kind);
	}

	static Type create(String name, Generation generation,
			Concurrency concurrency, int kind) {
		Type type = find(kind);
		if (type == null) {
			type = new Type(name, generation, concurrency, kind);
			TYPES.put(kind, type);
		} else {
			throw new IllegalArgumentException("cant create type for kind: "
					+ kind);
		}
		return type;
	}

	public static Type find(int kind) {
		return TYPES.get(kind);
	}
}
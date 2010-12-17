package org.pjurski.gclog.model;

public class Concurrency {

	public static final Concurrency SERIAL = new Concurrency("Serial");
	public static final Concurrency PARALLEL = new Concurrency("Parallel");
	public static final Concurrency CONCURRENT = new Concurrency("Concurrent");

	private String name;

	private Concurrency(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}

package org.pjurski.gclog.model;

public class Generation {

	public static final Generation YOUNG = new Generation("Young");
	public static final Generation TENURED = new Generation("Tenured");
	public static final Generation PERM = new Generation("Perm");
	public static final Generation ALL = new Generation("All");

	private String name;

	private Generation(String name) {
		this.name = name.intern();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
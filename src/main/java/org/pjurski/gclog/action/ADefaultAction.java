package org.pjurski.gclog.action;

import java.util.Map;


public abstract class ADefaultAction {

	public abstract void execute(Map<String, String> params) throws ActionException;

	protected final void log(String format, Object... args) {
		System.out.printf(format, args);
	}
}
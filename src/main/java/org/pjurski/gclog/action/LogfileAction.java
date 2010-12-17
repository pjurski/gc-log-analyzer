package org.pjurski.gclog.action;

import java.util.Map;

public class LogfileAction extends ADefaultAction {
	
	@Override
	public void execute(Map<String, String> params) throws ActionException {
		System.out.println("Logfile: " + params.get("logfile"));
	}
}
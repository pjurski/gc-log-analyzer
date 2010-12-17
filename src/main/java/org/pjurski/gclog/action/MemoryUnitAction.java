package org.pjurski.gclog.action;

import java.util.Map;

public class MemoryUnitAction extends ADefaultAction {

	@Override
	public void execute(Map<String, String> params) throws ActionException {
		String memunit = params.get("memunit");

		boolean recognized = false;
		if (memunit != null) {
			try {
				recognized = MemoryUnit.valueOf(memunit) != null;
			} catch (IllegalArgumentException e) {
			}
		}
		System.out.println("Memory Unit " + memunit + " "
				+ (recognized ? "" : "not ") + "recognized ");

		if (!recognized) {
			System.out.println("Memory Unit "
					+ MemoryUnit.getDefault().toString().toUpperCase()
					+ " will be used");
			params.put("memunit", MemoryUnit.getDefault().toString()
					.toUpperCase());
		}
	}
}
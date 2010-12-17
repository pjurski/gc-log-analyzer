package org.pjurski.gclog;

import org.pjurski.gclog.action.ADefaultAction;

class Action {

	private final String name;

	private final String parameter;
	private final String argument;

	private final String description;

	private final boolean required;

	private final String order;

	private final Class<?> clazz;

	public Action(String name, String parameter, String argument,
			String description, String required, String order, Class<?> clazz) {
		this.name = name;
		this.parameter = parameter;
		this.argument = argument;
		this.description = description;
		this.required = required == null ? false : "TRUE".equalsIgnoreCase(required);
		this.order = order;
		this.clazz = clazz;
	}

	public String getName() {
		return this.name;
	}

	public String getOrder() {
		return this.order;
	}

	public ADefaultAction newActionIstance() throws InstantiationException, IllegalAccessException {
		return (ADefaultAction) this.clazz.newInstance();
	}

	public String getArgument() {
		return this.argument;
	}

	public String getDescription() {
		return this.description;
	}

	public String getParameter() {
		return this.parameter;
	}

	public boolean isRequired() {
		return this.required;
	}
}
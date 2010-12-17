package org.pjurski.gclog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.pjurski.gclog.action.ActionException;
import org.pjurski.gclog.action.ADefaultAction;

public class GcLogMain {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, ActionException {
		new GcLogMain().start(args);
	}

	public GcLogMain() {
	}

	public void start(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, ActionException {

		Collection<Action> actions = this.findActions();

		CommandLine line = this.createCommandLine(actions, args);
		if (line != null) {
			Map<String, String> params = this.createParameters(line);

			for (Action action : actions) {
				if (!action.isRequired()
						|| line.hasOption(action.getParameter())) {
					ADefaultAction actionInstance = action.newActionIstance();
					actionInstance.execute(params);
				}
			}
		}
	}

	private Map<String, String> createParameters(CommandLine line) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		for (Option option : line.getOptions()) {
			params.put(option.getOpt(), line.getOptionValue(option.getOpt()));
		}
		return params;
	}

	private CommandLine createCommandLine(Collection<Action> actions,
			String[] args) throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		Options options = this.createOptions(actions);
		if (args.length == 0) {
			this.printUsage(args, options);
			return null;
		}

		CommandLineParser parser = new PosixParser();
		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();

			this.printUsage(args, options);
			return null;
		}
	}

	private Collection<Action> findActions() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		Properties properties = new Properties();
		properties.load(this.getClass().getResource("action.properties")
				.openStream());

		List<String> actionNames = new ArrayList<String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			Object key = entry.getKey();
			if (key == null) {
				continue;
			}

			String keyString = key.toString();
			String[] part = keyString.split("[.]");
			if (part.length > 1) {
				String actionName = part[1];
				if (!actionNames.contains(actionName)) {
					actionNames.add(actionName);
				}
			}
		}

		List<Action> actions = new ArrayList<Action>();
		for (String actionName : actionNames) {
			String parameter = properties.getProperty("action." + actionName
					+ ".parameter");
			String argument = properties.getProperty("action." + actionName
					+ ".argument");
			String description = properties.getProperty("action." + actionName
					+ ".description");
			String required = properties.getProperty("action." + actionName
					+ ".required");
			String order = properties.getProperty("action." + actionName
					+ ".order");
			String clazz = properties.getProperty("action." + actionName
					+ ".class");

			Class<?> actionClazz = Class.forName(clazz);
			actions.add(new Action(actionName, parameter, argument,
					description, required, order, actionClazz));
		}
		Collections.sort(actions, new Comparator<Action>() {
			public int compare(Action a1, Action a2) {
				String o1 = a1.getOrder();
				String o2 = a2.getOrder();
				if (o1 == null) {
					if (o2 == null) {
						return 0;
					} else {
						return 1;
					}
				} else {
					if (o2 == null) {
						return -1;
					}
				}
				return Integer.parseInt(o1) - Integer.parseInt(o2);
			}
		});
		return actions;
	}

	private Options createOptions(Collection<Action> actions)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Options options = new Options();

		for (Action action : actions) {
			String argument = action.getArgument();
			boolean required = action.isRequired();
			String description = action.getDescription();

			if (argument != null && argument.trim().length() > 0) {
				OptionBuilder.hasArg();
				OptionBuilder.withArgName(argument);
			}

			OptionBuilder.isRequired(required);

			if (description != null && description.trim().length() > 0) {
				OptionBuilder.withDescription(description);
			}

			options.addOption(OptionBuilder.create(action.getParameter()));
		}
		return options;
	}

	private void printUsage(String[] args, Options options) {
		System.out.println(Arrays.asList(args));

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("GC Log Analyzer", options);
	}
}
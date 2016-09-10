package net.simpleframework.ctx.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ConsoleThread extends Thread {
	static ConsoleThread console;

	private static Map<Class<?>, IConsoleCommand> commands;

	public static void doInit() {
		if ((System.in != null) && (System.out != null)) {
			commands = new ConcurrentHashMap<Class<?>, IConsoleCommand>();
			registered(new IConsoleCommand.QuitCommand(), new IConsoleCommand.GcCommand(),
					new IConsoleCommand.DBCommand());

			console = new ConsoleThread(System.in);
			console.setPrintStream(System.out);
			console.start();
		}
	}

	public static void registered(final IConsoleCommand... cmds) {
		if (cmds != null) {
			for (final IConsoleCommand cmd : cmds) {
				commands.put(cmd.getClass(), cmd);
			}
		}
	}

	private final BufferedReader reader;

	private PrintStream printStream;

	public ConsoleThread(final InputStream inputStream) {
		setDaemon(true);
		reader = new BufferedReader(new InputStreamReader(inputStream));
	}

	@Override
	public void run() {
		while (true) {
			try {
				String text = reader.readLine();
				if (!StringUtils.hasText(text)) {
					continue;
				}
				for (final IConsoleCommand command : commands.values()) {
					text = text.trim().replaceAll("  +", " ");
					if (command.execute(text)) {
						break;
					}
				}
			} catch (final IOException e) {
				log.error(e);
				break;
			}
		}
	}

	public PrintStream getPrintStream() {
		return printStream;
	}

	public void setPrintStream(final PrintStream printStream) {
		this.printStream = printStream;
	}

	private final Log log = LogFactory.getLogger(ConsoleThread.class);
}

package net.simpleframework.ctx.common;

import java.io.PrintStream;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IConsoleCommand {

	/**
	 * 执行
	 * 
	 * @param command
	 * @return
	 */
	boolean execute(String command);

	static class QuitCommand implements IConsoleCommand {

		@Override
		public boolean execute(final String command) {
			if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")) {
				System.exit(0);
				return true;
			}
			return false;
		}
	}

	static class GcCommand implements IConsoleCommand {

		@Override
		public boolean execute(final String command) {
			if (command.equalsIgnoreCase("gc")) {
				String size = FileUtils.toFileSize(Runtime.getRuntime().totalMemory());
				final PrintStream stream = ConsoleThread.console.getPrintStream();
				stream.println("total memory: " + size);
				size = FileUtils.toFileSize(Runtime.getRuntime().freeMemory());
				stream.println("free memory before gc: " + size);
				stream.println();
				System.gc();
				System.out.println("garbage collection ok.");
				stream.println();
				size = FileUtils.toFileSize(Runtime.getRuntime().freeMemory());
				stream.println("free memory after gc: " + size);
				return true;
			}
			return false;
		}
	}

	static class DBCommand implements IConsoleCommand {
		@Override
		public boolean execute(final String command) {
			final String[] cmds = StringUtils.split(command, " ");
			if (cmds[0].equalsIgnoreCase("db")) {
				// final Map<Class<?>, IDbEntityManager> cache =
				// DataManagerFactory.entityManagerCache;
				// final PrintStream stream =
				// ConsoleThread.console.getPrintStream();
				if (cmds[1].equalsIgnoreCase("-list")) {
					// for (final IDbEntityManager service : cache.values()) {
					// stream.println(service);
					// }
				} else if (cmds[1].equalsIgnoreCase("-reset")) {
					// for (final IDbEntityManager service : cache.values()) {
					// service.reset();
					// }
				}
			}
			return false;
		}
	}
}

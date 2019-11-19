package net.simpleframework.ctx.common;

import java.io.PrintStream;
import java.util.Collection;

import net.simpleframework.ado.db.DbManagerFactory;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.ctx.ApplicationContextFactory;
import net.simpleframework.ctx.IApplicationContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IConsoleCommand {

	/**
	 * 执行
	 * 
	 * @param command
	 * @return
	 */
	boolean execute(ConsoleThread thread, String command);

	static class QuitCommand implements IConsoleCommand {

		@Override
		public boolean execute(final ConsoleThread thread, final String command) {
			if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")) {
				System.exit(0);
				return true;
			}
			return false;
		}
	}

	static class GcCommand implements IConsoleCommand {

		@Override
		public boolean execute(final ConsoleThread thread, final String command) {
			if (command.equalsIgnoreCase("gc")) {
				String size = FileUtils.toFileSize(Runtime.getRuntime().totalMemory());
				final PrintStream stream = thread.getPrintStream();
				stream.println("total memory: " + size);
				size = FileUtils.toFileSize(Runtime.getRuntime().freeMemory());
				stream.println("free memory before gc: " + size);
				stream.println();
				System.gc();
				ObjectEx.oprintln("garbage collection ok.");
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
		public boolean execute(final ConsoleThread thread, final String command) {
			final String[] cmds = StringUtils.split(command, " ");
			if (cmds[0].equalsIgnoreCase("db") && cmds.length > 1) {
				final IApplicationContext ctx = (IApplicationContext) ApplicationContextFactory.ctx();
				final DbManagerFactory dbFactory = (DbManagerFactory) ctx.getADOManagerFactory();
				final Collection<IDbEntityManager<?>> cache = dbFactory.allEntityManager();
				final PrintStream stream = thread.getPrintStream();
				if (cmds[1].equalsIgnoreCase("-list")) {
					for (final IDbEntityManager<?> service : cache) {
						stream.println(service);
					}
					return true;
				} else if (cmds[1].equalsIgnoreCase("-reset")) {
					for (final IDbEntityManager<?> service : cache) {
						service.reset();
						stream.println(service);
					}
					return true;
				}
			}
			return false;
		}
	}
}

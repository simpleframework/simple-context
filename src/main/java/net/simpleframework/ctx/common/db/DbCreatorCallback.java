package net.simpleframework.ctx.common.db;

import net.simpleframework.common.Version;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DbCreatorCallback extends ObjectEx {

	public static DbCreatorCallback defaultCallback = new DbCreatorCallback();

	public void execute(final String name, final Version version, final String description) {
	}

	public void execute(final String sql, final long timeMillis, final Exception exception,
			final String description) {
		if (exception != null) {
			log.info("execute: [" + sql + "]");
			log.info("Fail");
		}
	}
}

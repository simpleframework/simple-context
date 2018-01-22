package net.simpleframework.ctx.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class DeployUtils {

	public static final String RESOURCE_NAME = "$resource";

	static Map<String, Integer> SHORTPACKAGEs = new HashMap<>();
	static int COUNTER = 0;

	public static String getShortPackage(final String packageName) {
		Integer i = SHORTPACKAGEs.get(packageName);
		if (i == null) {
			SHORTPACKAGEs.put(packageName, (i = COUNTER++));
		}
		return "s" + i;
	}
}

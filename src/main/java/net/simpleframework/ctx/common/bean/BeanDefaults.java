package net.simpleframework.ctx.common.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.Convert;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class BeanDefaults {
	// 存放缺省属性
	private static Map<Class<?>, Map<String, Object>> defaultsCache;
	static {
		defaultsCache = new ConcurrentHashMap<Class<?>, Map<String, Object>>();
	}

	public static void set(final Class<?> beanClass, final String key, final Object val) {
		Map<String, Object> defaults = defaultsCache.get(beanClass);
		if (defaults == null) {
			defaultsCache.put(beanClass, defaults = new HashMap<String, Object>());
		}
		defaults.put(key, val);
	}

	public static Object get(final Class<?> beanClass, final String key, final Object defaultVal) {
		final Map<String, Object> defaults = defaultsCache.get(beanClass);
		Object val;
		if (defaults == null || (val = defaults.get(key)) == null) {
			val = defaultVal;
		}
		return val;
	}

	public static Object get(final Class<?> beanClass, final String key) {
		return get(beanClass, key, null);
	}

	public static String getString(final Class<?> beanClass, final String key,
			final String defaultVal) {
		return (String) get(beanClass, key, defaultVal);
	}

	public static boolean getBool(final Class<?> beanClass, final String key,
			final boolean defaultVal) {
		return Convert.toBool(get(beanClass, key), defaultVal);
	}

	public static int getInt(final Class<?> beanClass, final String key, final int defaultVal) {
		return Convert.toInt(get(beanClass, key), defaultVal);
	}

	public static double getDouble(final Class<?> beanClass, final String key,
			final double defaultVal) {
		return Convert.toDouble(get(beanClass, key), defaultVal);
	}
}

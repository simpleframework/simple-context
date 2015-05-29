package net.simpleframework.ctx.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.simpleframework.common.Convert;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PropertiesContextSettings extends ContextSettings {

	protected final Properties properties = new Properties();

	public PropertiesContextSettings() {
	}

	public PropertiesContextSettings(final InputStream iStream) throws IOException {
		load(iStream);
	}

	public void load(final InputStream iStream) throws IOException {
		if (iStream == null) {
			return;
		}
		try {
			properties.load(iStream);
		} finally {
			try {
				iStream.close();
			} catch (final IOException e) {
			}
		}
	}

	public String[] keys() {
		final Set<Object> keySet = properties.keySet();
		final String[] keys = new String[keySet.size()];
		int i = 0;
		for (final Object key : keySet) {
			keys[i++] = (String) key;
		}
		return keys;
	}

	public void setProperty(final String key, final Object value) {
		properties.setProperty(key, String.valueOf(value));
	}

	@Override
	public String getProperty(final String key) {
		return getProperty(key, null);
	}

	public String getProperty(final String key, final String defaultValue) {
		final String ret = properties.getProperty(key);
		final String val = ret != null ? ret : defaultValue;
		print(key, val);
		return val;
	}

	public void remove(final String key) {
		properties.remove(key);
	}

	public int getIntProperty(final String key, final int defaultValue) {
		final int val = Convert.toInt(properties.getProperty(key), defaultValue);
		print(key, val);
		return val;
	}

	public int getIntProperty(final String key) {
		return getIntProperty(key, 0);
	}

	public boolean getBoolProperty(final String key, final boolean defaultValue) {
		final boolean val = Convert.toBool(properties.getProperty(key), defaultValue);
		print(key, val);
		return val;
	}

	public boolean getBoolProperty(final String key) {
		return getBoolProperty(key, false);
	}

	private void print(final String key, final Object val) {
		if (mark.get(key) == null && val != null) {
			System.out.println("[load property] " + key + "=>" + val);
			mark.put(key, Boolean.TRUE);
		}
	}

	private final Map<String, Boolean> mark = new HashMap<String, Boolean>();
}

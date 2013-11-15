package net.simpleframework.ctx.settings;

import java.io.IOException;
import java.io.InputStream;
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

	public String getProperty(final String key) {
		return getProperty(key, null);
	}

	public String getProperty(final String key, final String defaultValue) {
		final String ret = properties.getProperty(key);
		return ret != null ? ret : defaultValue;
	}

	public void remove(final String key) {
		properties.remove(key);
	}

	public int getIntProperty(final String key, final int defaultValue) {
		return Convert.toInt(properties.getProperty(key), defaultValue);
	}

	public int getIntProperty(final String key) {
		return getIntProperty(key, 0);
	}

	public boolean getBoolProperty(final String key, final boolean defaultValue) {
		return Convert.toBool(properties.getProperty(key), defaultValue);
	}

	public boolean getBoolProperty(final String key) {
		return getBoolProperty(key, false);
	}
}

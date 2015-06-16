package net.simpleframework.ctx.settings;

import java.io.File;
import java.util.Locale;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.permission.PermissionConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContextSettings extends ObjectEx {

	protected ContextSettings applicationSettings;

	public void onInit(final IApplicationContext context) throws Exception {
	}

	public void setApplicationSettings(final ContextSettings applicationSettings) {
		this.applicationSettings = applicationSettings;
	}

	/**
	 * 是否在调试阶段
	 * 
	 * @return
	 */
	public boolean isDebug() {
		return applicationSettings != null ? applicationSettings.isDebug() : true;
	}

	/**
	 * 语言
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return applicationSettings != null ? applicationSettings.getLocale() : Locale.getDefault();
	}

	/**
	 * 获取系统的字符集
	 * 
	 * @return
	 */
	public String getCharset() {
		return applicationSettings != null ? applicationSettings.getCharset() : "UTF-8";
	}

	protected File homeDir, tmpDir;

	public File getHomeFileDir() {
		if (applicationSettings != null) {
			return applicationSettings.getHomeFileDir();
		} else {
			if (homeDir == null) {
				homeDir = new File(System.getProperty("java.home"));
				if (!homeDir.exists()) {
					FileUtils.createDirectoryRecursively(homeDir);
				}
			}
			return homeDir;
		}
	}

	public void setHomeFileDir(final File homeDir) {
		if (applicationSettings != null) {
			applicationSettings.setHomeFileDir(homeDir);
		} else {
			this.homeDir = homeDir;
		}
	}

	/**
	 * 获取系统的临时目录
	 * 
	 * @return
	 */
	public File getTmpFiledir() {
		if (applicationSettings != null) {
			return applicationSettings.getTmpFiledir();
		} else {
			if (tmpDir == null) {
				tmpDir = new File(getHomeFileDir().getAbsoluteFile() + File.separator + "$temp"
						+ File.separator);
				if (!tmpDir.exists()) {
					FileUtils.createDirectoryRecursively(tmpDir);
				}
			}
			return tmpDir;
		}
	}

	/**
	 * 获取缺省的访问角色
	 * 
	 * @return
	 */
	public String getDefaultRole() {
		return applicationSettings != null ? applicationSettings.getDefaultRole()
				: PermissionConst.ROLE_ANONYMOUS;
	}

	public String getContextNo() {
		return applicationSettings != null ? applicationSettings.getContextNo() : "0";
	}

	public String getProperty(final String key) {
		throw NotImplementedException.of(getClass(), "getProperty");
	}
}
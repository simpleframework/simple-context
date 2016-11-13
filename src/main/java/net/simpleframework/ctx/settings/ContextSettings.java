package net.simpleframework.ctx.settings;

import java.io.File;
import java.util.Locale;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;
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

	protected IApplicationContext context;

	public void onInit(final IApplicationContext context) throws Exception {
		this.context = context;
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

	public static String HOME_PATH = "/";

	protected File homeDir;

	public File getHomeFileDir() {
		if (applicationSettings != null) {
			return applicationSettings.getHomeFileDir();
		} else {
			if (homeDir == null) {
				homeDir = createDefaultHomeFileDir();
				if (!homeDir.exists()) {
					FileUtils.createDirectoryRecursively(homeDir);
				}
			}
			return homeDir;
		}
	}

	protected File createDefaultHomeFileDir() {
		return context.getRootDir();
	}

	public File getHomeFile(final String path) {
		return getHomeFile(path, null);
	}

	public File getHomeFile(final String path, final String file) {
		final File tmpFile = getHomeFileDir();
		final File tFile = new File(tmpFile.getAbsolutePath()
				+ StringUtils.replace(path, "/", File.separator));
		if (!tFile.exists()) {
			FileUtils.createDirectoryRecursively(tFile);
		}
		return StringUtils.hasText(file) ? new File(tFile.getAbsoluteFile() + File.separator + file)
				: tFile;
	}

	/**
	 * 获取系统的临时目录
	 * 
	 * @return
	 */
	// public File getTmpFiledir() {
	// if (applicationSettings != null) {
	// return applicationSettings.getTmpFiledir();
	// } else {
	// if (tmpDir == null) {
	// tmpDir = new File(getHomeFileDir().getAbsoluteFile() + File.separator +
	// "$temp"
	// + File.separator);
	// if (!tmpDir.exists()) {
	// FileUtils.createDirectoryRecursively(tmpDir);
	// }
	// }
	// return tmpDir;
	// }
	// }

	// public File getTmpFiledir(final String path) {
	// final File tmpFile = getTmpFiledir();
	// return new File(tmpFile.getAbsolutePath() + StringUtils.replace(path, "/",
	// File.separator));
	// }

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
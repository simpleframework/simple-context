package net.simpleframework.ctx.settings;

import java.io.File;
import java.lang.management.ManagementFactory;

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

	public void onInit(final IApplicationContext context) throws Exception {
	}

	/**
	 * 是否在调试阶段
	 * 
	 * @return
	 */
	public boolean isDebug() {
		return true;
	}

	/**
	 * 获取系统的字符集
	 * 
	 * @return
	 */
	public String getCharset() {
		return "UTF-8";
	}

	protected File homeDir, tmpDir;

	public File getHomeFileDir() {
		if (homeDir == null) {
			homeDir = new File(System.getProperty("java.home"));
			if (!homeDir.exists()) {
				FileUtils.createDirectoryRecursively(homeDir);
			}
		}
		return homeDir;
	}

	public void setHomeFileDir(final File homeDir) {
		this.homeDir = homeDir;
	}

	/**
	 * 获取系统的临时目录
	 * 
	 * @return
	 */
	public File getTmpFiledir() {
		if (tmpDir == null) {
			tmpDir = new File(getHomeFileDir().getAbsoluteFile() + File.separator + "$temp"
					+ File.separator);
			if (!tmpDir.exists()) {
				FileUtils.createDirectoryRecursively(tmpDir);
			}
		}
		return tmpDir;
	}

	/**
	 * 获取缺省的访问角色
	 * 
	 * @return
	 */
	public String getDefaultRole() {
		return PermissionConst.ROLE_ANONYMOUS;
	}

	private static String pid;
	static {
		final String name = ManagementFactory.getRuntimeMXBean().getName();
		pid = name.substring(0, name.indexOf("@"));
	}

	public String getContextNo() {
		// 获取服务编号
		return pid;
	}

	public String getProperty(final String key) {
		throw NotImplementedException.of(getClass(), "getProperty");
	}
}
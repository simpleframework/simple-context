package net.simpleframework.ctx.settings;

import java.io.File;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.ctx.IApplicationContextBase;
import net.simpleframework.ctx.permission.IPermissionConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContextSettings extends ObjectEx {

	public void onInit(final IApplicationContextBase context) throws Exception {
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
		return IPermissionConst.ROLE_ANONYMOUS;
	}

	public String getProperty(final String key) {
		throw NotImplementedException.of(getClass(), "getProperty");
	}

	{
		enableAttributes();
	}
}

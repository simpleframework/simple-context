package net.simpleframework.ctx;

import net.simpleframework.common.Version;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IApplicationContextBase {

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	void onInit() throws Exception;

	/**
	 * 获取扫描的包名
	 * 
	 * @return
	 */
	String[] getScanPackageNames();

	/**
	 * 获取异常的显示消息，子类覆盖添加友好消息
	 * 
	 * @param th
	 * @return
	 */
	String getThrowableMessage(Throwable th);

	/**
	 * 获取context的标题
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * 
	 * @return
	 */
	Version getVersion();
}

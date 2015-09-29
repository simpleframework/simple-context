package net.simpleframework.ctx.hdl;

import net.simpleframework.ctx.IApplicationContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IApplicationStartupHandler {

	/**
	 * 启动类
	 * 
	 * @param application
	 * @throws Exception
	 */
	void onStartup(IApplicationContext application) throws Exception;
}

package net.simpleframework.ctx.service;

import net.simpleframework.ctx.IModuleContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IBaseService {
	/**
	 * 获取模块的context环境
	 * 
	 * @return
	 */
	IModuleContext getModuleContext();

	void setModuleContext(IModuleContext context);

	/**
	 * 初始化
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	void onInit() throws Exception;
}
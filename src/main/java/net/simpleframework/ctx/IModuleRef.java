package net.simpleframework.ctx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IModuleRef {

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	void onInit(IModuleContext context) throws Exception;

	IModuleContext getModuleContext();
}

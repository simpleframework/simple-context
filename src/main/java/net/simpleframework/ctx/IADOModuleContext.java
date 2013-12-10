package net.simpleframework.ctx;

import net.simpleframework.ado.IADOManagerFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IADOModuleContext extends IModuleContext {

	/**
	 * 获取数据访问对象工厂
	 * 
	 * @return
	 */
	IADOManagerFactory getADOManagerFactory();
}

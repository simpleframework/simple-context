package net.simpleframework.ctx;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractModuleRef extends ObjectEx implements IModuleRef {

	@Override
	public void onInit(final IModuleContext refContext) throws Exception {
	}
}

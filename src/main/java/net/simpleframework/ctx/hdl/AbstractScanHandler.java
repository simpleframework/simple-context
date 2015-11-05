package net.simpleframework.ctx.hdl;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractScanHandler extends ObjectEx implements IScanHandlerAware {

	@Override
	public String toString() {
		return getClass().getName();
	}
}
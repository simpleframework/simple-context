package net.simpleframework.ctx.service;

import java.lang.reflect.Field;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.ctx.IModuleContextAware;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractBaseService extends ObjectEx implements IBaseService {

	private IModuleContext _context;

	@Override
	public void onInit() throws Exception {
	}

	@Override
	public IModuleContext getModuleContext() {
		if (_context == null && this instanceof IModuleContextAware) {
			for (final Class<?> iClass : ClassUtils.getAllInterfaces(getClass())) {
				if (IModuleContextAware.class.isAssignableFrom(iClass)) {
					for (final Field field : iClass.getDeclaredFields()) {
						if (IModuleContext.class.isAssignableFrom(field.getType())) {
							field.setAccessible(true);
							try {
								return (_context = (IModuleContext) field.get(this));
							} catch (final IllegalAccessException e) {
							}
						}
					}
				}
			}
		}
		return _context;
	}

	@Override
	public void setModuleContext(final IModuleContext context) {
		_context = context;
	}
}

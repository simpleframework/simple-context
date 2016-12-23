package net.simpleframework.ctx;

import java.lang.reflect.Field;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AbstractModuleContextAware extends ObjectEx {
	protected IModuleContext _context;

	public IModuleContext getModuleContext() {
		if (_context == null && this instanceof IModuleContextAware) {
			for (final Class<?> iClass : ClassUtils.getAllInterfaces(getClass())) {
				if (IModuleContextAware.class.isAssignableFrom(iClass)) {
					for (final Field field : iClass.getFields()) {
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

	public void setModuleContext(final IModuleContext context) {
		_context = context;
	}

	protected Log log = LogFactory.getLogger(getClass());
}

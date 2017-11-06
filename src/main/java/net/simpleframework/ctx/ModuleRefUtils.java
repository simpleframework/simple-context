package net.simpleframework.ctx;

import static net.simpleframework.common.I18n.$m;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.BeanUtils;
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
public abstract class ModuleRefUtils extends ObjectEx {

	public static void doRefInit(final IContextBase context) {
		for (final Method method : context.getClass().getMethods()) {
			final Class<?> type = method.getReturnType();
			if (IModuleRef.class.isAssignableFrom(type) && method.getParameterTypes().length == 0) {
				try {
					final IModuleRef ref = ((IModuleRef) method.invoke(context));
					if (ref != null) {
						ref.onInit(context);
					}
				} catch (final Exception e) {
					throw ModuleRefException.of(e);
				}
			}
		}
	}

	public static IModuleRef getRef(final String refClass) {
		try {
			return (IModuleRef) singleton(ClassUtils.forName(refClass));
		} catch (final ClassNotFoundException e) {
			logGetRef(refClass, e);
		} catch (final NoClassDefFoundError e) {
			logGetRef(refClass, e);
		}
		return null;
	}

	static void logGetRef(final String refClass, final Throwable e) {
		if (ERRs.get(refClass) == null) {
			log.warn($m("ModuleRefUtils.0", refClass));
			ERRs.put(refClass, Boolean.TRUE);
		}
	}

	static final Map<String, Boolean> ERRs = new HashMap<>();

	static Log log = LogFactory.getLogger(BeanUtils.class);
}

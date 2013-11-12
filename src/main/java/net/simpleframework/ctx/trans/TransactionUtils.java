package net.simpleframework.ctx.trans;

import java.lang.reflect.Method;

import net.simpleframework.ado.ADOException;
import net.simpleframework.ado.IADOManagerFactory;
import net.simpleframework.ado.db.DbManagerFactory;
import net.simpleframework.ado.trans.TransactionObjectCallback;
import net.simpleframework.common.object.IMethodInterceptor;
import net.simpleframework.common.object.MethodResult;
import net.simpleframework.common.object.ProxyUtils;
import net.simpleframework.ctx.ApplicationContextFactory;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.IApplicationContextBase;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.ctx.ModuleContextFactory;
import net.simpleframework.ctx.service.ado.db.IDbModuleContext;
import net.simpleframework.lib.net.sf.cglib.proxy.MethodProxy;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TransactionUtils {

	public static void registTransaction(final Class<?> oClass) {
		if (ProxyUtils.isRegistered(oClass, TransMethodInterceptor.class)) {
			return;
		}
		boolean mark = false;
		for (final Method method : oClass.getMethods()) {
			final Transaction trans = method.getAnnotation(Transaction.class);
			if (trans != null) {
				mark = true;
				break;
			}
		}
		if (mark) {
			ProxyUtils.regist(oClass, new TransMethodInterceptor());
		}
	}

	static class TransMethodInterceptor implements IMethodInterceptor {
		@Override
		public MethodResult intercept(final Object obj, final Method method, final Object[] args,
				final MethodProxy proxy) throws Throwable {
			final Transaction trans = method.getAnnotation(Transaction.class);
			if (trans == null) {
				return MethodResult.FAILURE;
			}

			if (trans.type() == ETransactionType.JDBC) {
				final Class<? extends IModuleContext> ctxClass = trans.context();
				if (ctxClass.equals(IModuleContext.class)) {
					final IApplicationContextBase app = ApplicationContextFactory.ctx();
					if (app instanceof IApplicationContext) {
						final IADOManagerFactory aFactory = ((IApplicationContext) app)
								.getADOManagerFactory();
						if (aFactory instanceof DbManagerFactory) {
							return new MethodResult(((DbManagerFactory) aFactory).getQueryManager()
									.doExecuteTransaction(callback(obj, args, proxy)));
						}
					}
				} else {
					final IModuleContext context = ModuleContextFactory.get(ctxClass);
					if (context instanceof IDbModuleContext) {
						return new MethodResult(((IDbModuleContext) context).getQueryManager()
								.doExecuteTransaction(callback(obj, args, proxy)));
					}
				}
			}
			return MethodResult.FAILURE;
		}

		private TransactionObjectCallback<Object> callback(final Object obj, final Object[] args,
				final MethodProxy proxy) {
			return new TransactionObjectCallback<Object>() {
				@Override
				public Object onTransactionCallback() {
					try {
						return proxy.invokeSuper(obj, args);
					} catch (final Throwable e) {
						throw ADOException.of(e);
					}
				}
			};
		}
	}
}

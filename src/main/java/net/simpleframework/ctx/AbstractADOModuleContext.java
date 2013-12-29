package net.simpleframework.ctx;

import net.simpleframework.ado.IADOManagerFactory;
import net.simpleframework.ado.IADOManagerFactoryAware;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractADOModuleContext extends AbstractModuleContext implements
		IModuleContext, IADOManagerFactoryAware {

	private IADOManagerFactory adoManagerFactory;

	@Override
	public IADOManagerFactory getADOManagerFactory() {
		if (adoManagerFactory == null) {
			adoManagerFactory = application.getADOManagerFactory(getDataSource());
		}
		return adoManagerFactory;
	}
}
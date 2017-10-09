package net.simpleframework.ctx;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.IADOManagerFactory;
import net.simpleframework.ado.IADOManagerFactoryAware;
import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.DbManagerFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractADOModuleContext extends AbstractModuleContext
		implements IModuleContext, IADOManagerFactoryAware {

	private IADOManagerFactory adoManagerFactory;

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		super.onInit(application);
		// init ado
		getADOManagerFactory();
	}

	private final Map<DataSource, Boolean> aLoaded = new HashMap<DataSource, Boolean>();

	@Override
	public IADOManagerFactory getADOManagerFactory() {
		if (adoManagerFactory == null) {
			final DataSource dataSource = getDataSource();
			final IADOManagerFactory mFactory = application.getADOManagerFactory(dataSource);
			if (aLoaded.get(dataSource) == null) {
				if (mFactory instanceof DbManagerFactory) {
					final DbEntityTable[] tbls = createEntityTables();
					if (tbls != null) {
						((DbManagerFactory) mFactory).regist(tbls);
					}
				}
				aLoaded.put(dataSource, Boolean.TRUE);
			}
			return mFactory;
		}
		return adoManagerFactory;
	}

	protected DbEntityTable[] createEntityTables() {
		return null;
	}
}

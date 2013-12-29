package net.simpleframework.ctx.common.db;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.simpleframework.ado.db.DbManagerFactory;
import net.simpleframework.ado.db.IDbEntityTableRegistry;
import net.simpleframework.ado.db.jdbc.JdbcUtils;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.IScanResourcesCallback;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.settings.ContextSettings;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class DbUtils {
	private static final String RESOURCE_NAME = "$resource";
	private static final String SCRIPT_FILENAME = "sql-script";

	public static void doExecuteSql(final IApplicationContext application) throws Exception {
		final DataSource dataSource = application.getDataSource();
		if (dataSource == null) {
			return;
		}

		final String[] packageNames = application.getScanPackageNames();
		if (packageNames != null) {
			System.out.println($m("DbUtils.0"));

			final List<String> sqlFiles = new ArrayList<String>();
			for (final String packageName : packageNames) {
				ClassUtils.scanResources(packageName, new IScanResourcesCallback() {
					@Override
					public void doResources(String filepath, final boolean isDirectory)
							throws IOException {
						if (filepath.endsWith("/")) {
							filepath = filepath.substring(0, filepath.length() - 1);
						}
						if (filepath.endsWith(SCRIPT_FILENAME + ".zip")) {
							final InputStream inputStream = ClassUtils.getResourceAsStream(filepath);
							if (inputStream != null) {
								String packageName = filepath.substring(0, filepath.lastIndexOf('/'))
										.replace('/', '.');
								final ContextSettings settings = application.getContextSettings();
								if (!settings.isDebug()) {
									packageName = ObjectUtils.hashStr(packageName);
								}
								final StringBuilder sb = new StringBuilder();
								sb.append("/").append(RESOURCE_NAME).append("/").append(packageName)
										.append("/").append(SCRIPT_FILENAME);
								final String target = settings.getHomeFileDir().getAbsoluteFile()
										+ sb.toString().replace("/", File.separator);
								FileUtils.unzip(inputStream, target, false);
								sqlFiles.add(target);
							}
						}
					}
				});
			}

			for (final String sqlFile : sqlFiles) {
				final StringBuilder sb = new StringBuilder();
				sb.append(sqlFile).append(File.separator)
						.append(JdbcUtils.getDatabaseMetaData(dataSource).getDatabaseProductName())
						.append(File.separator).append(SCRIPT_FILENAME).append(".xml");
				DbCreator.executeSql(dataSource, sb.toString());
			}
		}
	}

	public static void doEntityTable(final IApplicationContext application) throws Exception {
		final String[] packageNames = application.getScanPackageNames();
		// 注册EntityTable
		if (packageNames != null) {
			for (final String packageName : packageNames) {
				ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
					@Override
					public void doResources(final String filepath, final boolean isDirectory)
							throws Exception {
						final IDbEntityTableRegistry registry = getInstance(loadClass(filepath),
								IDbEntityTableRegistry.class);
						Object mFactory = null;
						if (registry != null
								&& (mFactory = registry.getADOManagerFactory()) instanceof DbManagerFactory) {
							((DbManagerFactory) mFactory).regist(registry.createEntityTables());
						}
					}
				});
			}
		}
	}
}
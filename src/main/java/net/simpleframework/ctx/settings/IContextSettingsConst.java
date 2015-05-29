package net.simpleframework.ctx.settings;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IContextSettingsConst {
	static final String DBPOOL_PROVIDER = "dbpool.provider";
	static final String DBPOOL_PROPERTIES = "dbpool.properties";

	static final String DBPOOL = "dbpool";
	static final String DBPOOL_ENTITYMANAGER = "entitymanager";

	static final String CTX_CHARSET = "ctx.charset";
	static final String CTX_RESOURCECOMPRESS = "ctx.resourcecompress";
	static final String CTX_PERMISSIONHANDLER = "ctx.permissionhandler";
	static final String CTX_DEBUG = "ctx.debug";
	static final String CTX_DEPLOY_DB = "ctx.deploydb";
	static final String CTX_NO = "ctx.no";

	static final String MVC_SERVERPORT = "mvc.serverport";
	static final String MVC_FILTERPATH = "mvc.filterpath";
	static final String MVC_LOGINPATH = "mvc.loginpath";
	static final String MVC_HOMEPATH = "mvc.homepath";
	static final String MVC_IEWARNPATH = "mvc.iewarnpath";
}

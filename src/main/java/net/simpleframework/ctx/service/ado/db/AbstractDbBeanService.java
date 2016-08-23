package net.simpleframework.ctx.service.ado.db;

import static net.simpleframework.common.I18n.$m;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.simpleframework.ado.ADOException;
import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EOrder;
import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.IADOManagerFactoryAware;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.bean.AbstractIdBean;
import net.simpleframework.ado.bean.IDomainBeanAware;
import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.bean.IOrderBeanAware;
import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.ado.db.DbManagerFactory;
import net.simpleframework.ado.db.DbTableColumn;
import net.simpleframework.ado.db.IDbDataQuery;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.IDbQueryManager;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.db.common.SQLValue;
import net.simpleframework.ado.db.event.DbEntityAdapter;
import net.simpleframework.ado.db.event.IDbEntityListener;
import net.simpleframework.ado.query.DataQueryUtils;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.trans.TransactionVoidCallback;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.common.object.ObjectFactory;
import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.ctx.ModuleContextException;
import net.simpleframework.ctx.permission.LoginUser;
import net.simpleframework.ctx.permission.LoginUser.LoginWrapper;
import net.simpleframework.ctx.permission.PermissionEntity;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.ctx.service.AbstractBaseService;
import net.simpleframework.ctx.service.ado.ITreeBeanServiceAware;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbBeanService<T extends Serializable> extends AbstractBaseService
		implements IDbBeanService<T> {

	@Override
	public IModuleContext getModuleContext() {
		final IModuleContext context = super.getModuleContext();
		if (context == null) {
			throw ModuleContextException.of("[" + getClass().getSimpleName() + "] "
					+ $m("AbstractDbBeanService.4"));
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createBean() {
		return (T) ObjectFactory.newInstance(getBeanClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getBean(final Object id) {
		if (id == null) {
			return null;
		}
		if (getBeanClass().isAssignableFrom(id.getClass())) {
			return (T) id;
		}
		return getEntityManager().getBean(getIdParam(id));
	}

	protected Object getIdParam(final Object bean) {
		if (bean instanceof String || bean instanceof ID) {
			return bean;
		} else if (bean instanceof IIdBeanAware) {
			return ((AbstractIdBean) bean).getId();
		} else if (bean instanceof PermissionEntity) {
			return ((PermissionEntity<?>) bean).getId();
		} else if (bean != null) {
			final Object id = BeanUtils.getProperty(bean, "id");
			return id != null ? id : bean;
		}
		return null;
	}

	@Override
	public T getBean(final CharSequence expr, final Object... params) {
		return getEntityManager().queryForBean(new ExpressionValue(expr, params));
	}

	@Override
	public IDataQuery<T> query(final CharSequence expr, final Object... params) {
		return getEntityManager().queryBeans(new ExpressionValue(expr, params));
	}

	public IDbDataQuery<T> query(final IParamsValue paramsValue) {
		return getEntityManager().queryBeans(paramsValue);
	}

	@Override
	public IDataQuery<T> queryByParams(final FilterItems params, final ColumnData... orderColumns) {
		final ExpressionValue eVal = toExpressionValue(params);
		final StringBuilder sql = new StringBuilder(eVal.getExpression());
		sql.append(toOrderSQL(orderColumns));
		eVal.setExpression(sql.toString());
		return getEntityManager().queryBeans(eVal);
	}

	protected String toOrderSQL(final ColumnData... orderColumns) {
		final ColumnData[] _orderColumns = ArrayUtils.isEmpty(orderColumns) ? getDefaultOrderColumns()
				: orderColumns;
		final StringBuilder sql = new StringBuilder();
		if (!ArrayUtils.isEmpty(_orderColumns)) {
			int i = 0;
			for (final ColumnData col : _orderColumns) {
				final String alias = col.getAlias();
				if (!StringUtils.hasText(alias)) {
					continue;
				}
				if (i++ > 0) {
					sql.append(", ");
				}
				sql.append(alias);
				final EOrder o = col.getOrder();
				if (o != EOrder.normal) {
					sql.append(" ").append(o);
				}
			}
		}
		if (sql.length() > 0) {
			sql.insert(0, " order by ");
		}
		return sql.toString();
	}

	public static final ColumnData[] ORDER_CREATEDATE = new ColumnData[] { ColumnData
			.DESC("createdate") };
	public static final ColumnData[] ORDER_OORDER = new ColumnData[] { ColumnData.ASC("oorder") };

	protected ColumnData[] getDefaultOrderColumns() {
		if (IOrderBeanAware.class.isAssignableFrom(getBeanClass())) {
			return ORDER_OORDER;
		} else if (BeanUtils.hasProperty(getBeanClass(), "createDate")) {
			return ORDER_CREATEDATE;
		}
		return null;
	}

	@Override
	public IDataQuery<T> queryAll() {
		return query("1=1" + toOrderSQL());
	}

	/**
	 * 取某一列的集合
	 * 
	 * @param column
	 * @param expr
	 * @param params
	 * @return
	 */
	public List<Object> list(final String column, final String expr, final Object... params) {
		final IDataQuery<Map<String, Object>> qs = getEntityManager().queryMapSet(
				new String[] { column }, new ExpressionValue(expr, params));
		final ArrayList<Object> al = new ArrayList<Object>();
		Map<String, Object> kv;
		while ((kv = qs.next()) != null) {
			al.add(kv.get(column));
		}
		return al;
	}

	@Override
	public List<Object> list(final String column) {
		return list(column, "1=1");
	}

	/**
	 * 求总数
	 * 
	 * @param expr
	 * @param params
	 * @return
	 */
	@Override
	public int count(final CharSequence expr, final Object... params) {
		return getEntityManager().count(new ExpressionValue(expr, params));
	}

	@Override
	public int count() {
		return count("1=1");
	}

	/**
	 * 求某一列的和
	 * 
	 * @param column
	 * @param expr
	 * @param params
	 * @return
	 */
	@Override
	public Number sum(final String column, final CharSequence expr, final Object... params) {
		return getEntityManager().sum(column, new ExpressionValue(expr, params));
	}

	@Override
	public Number sum(final String column) {
		return sum(column, "1=1");
	}

	/**
	 * 求某一列的最大值
	 * 
	 * @param column
	 * @param expr
	 * @param params
	 * @return
	 */
	@Override
	public Number max(final String column, final CharSequence expr, final Object... params) {
		return getEntityManager().max(column, new ExpressionValue(expr, params));
	}

	@Override
	public Number max(final String column) {
		return max(column, "1=1");
	}

	@Override
	public Number avg(final String column, final CharSequence expr, final Object... params) {
		return getEntityManager().avg(column, new ExpressionValue(expr, params));
	}

	@Override
	public Number avg(final String column) {
		return avg(column, "1=1");
	}

	public Object queryFor(final String column, final String expr, final Object... params) {
		return getEntityManager().queryFor(column, new ExpressionValue(expr, params));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(final T bean, final Map<String, Object> attris) {
		BeanUtils.setProperties(bean, attris);
		final Set<String> keys = attris.keySet();
		update(keys.toArray(new String[keys.size()]), bean);
	}

	@Override
	public void update(final T bean, final String attri, final Object val) {
		update(bean, new KVMap().add(attri, val));
	}

	@Override
	public void exchange(final DbTableColumn order, final T... beans) {
		getEntityManager().exchange(order, beans);
	}

	@Override
	public void exchange(final T... beans) {
		exchange(new DbTableColumn("oorder"), beans);
	}

	@Override
	public int delete(final Object... ids) {
		if (ids == null || ids.length == 0) {
			return 0;
		}
		int ret = 0;
		for (final Object id : ids) {
			ret += deleteWith("id=?", id);
		}
		return ret;
	}

	/**
	 * 删除表达式
	 * 
	 * @param expr
	 * @param params
	 * @return
	 */
	@Override
	public int deleteWith(final String expr, final Object... params) {
		return getEntityManager().delete(new ExpressionValue(expr, params));
	}

	@Override
	public void update(final T... beans) {
		update(null, beans);
	}

	@Override
	public void update(final String[] columns, final T... beans) {
		getEntityManager().update(columns, beans);
	}

	@Override
	public void insert(final T... beans) {
		getEntityManager().insert(beans);
	}

	@Override
	public void addListener(final IDbEntityListener<T> listener) {
		getEntityManager().addListener(listener);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<?> getBeanClass() {
		Type superclass = getClass().getGenericSuperclass();
		while (superclass instanceof Class<?>) {
			superclass = ((Class<?>) superclass).getGenericSuperclass();
			if (superclass == null) {
				break;
			}
		}
		if (!(superclass instanceof ParameterizedType)) {
			return null;
		}
		final Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
		return types.length == 1 ? (Class<T>) types[0] : (Class<T>) types[1];
	}

	@Override
	public String getTablename(final Class<?> beanClass) {
		return getEntityManager(beanClass).getEntityTable().getName();
	}

	@Override
	public String getTablename() {
		return getTablename(getBeanClass());
	}

	@Override
	public <P> IDbEntityManager<P> getEntityManager(final Class<P> beanClass) {
		return ((DbManagerFactory) ((IADOManagerFactoryAware) getModuleContext())
				.getADOManagerFactory()).getEntityManager(beanClass);
	}

	@Override
	public IDbQueryManager getQueryManager() {
		return ((DbManagerFactory) ((IADOManagerFactoryAware) getModuleContext())
				.getADOManagerFactory()).getQueryManager();
	}

	@Override
	public IDbEntityManager<T> getEntityManager() {
		@SuppressWarnings("unchecked")
		final Class<T> beanClass = (Class<T>) getBeanClass();
		final IDbEntityManager<T> mgr = getEntityManager(beanClass);
		if (mgr == null) {
			throw ModuleContextException.of($m("AbstractDbBeanService.3", beanClass.getName()));
		}
		return mgr;
	}

	protected void doExecuteTransaction(final TransactionVoidCallback callback) {
		getEntityManager().doExecuteTransaction(callback);
	}

	public static class DbEntityAdapterEx<T> extends DbEntityAdapter<T> {

		protected Collection<T> coll(final IDbEntityManager<T> manager, final IParamsValue paramsValue) {
			return ((ObjectEx) paramsValue).getAttrCache("coll", new CacheV<Collection<T>>() {
				@Override
				public Collection<T> get() {
					return DataQueryUtils.toList(manager.queryBeans(paramsValue));
				}
			});
		}
	}

	/*------------------------------------utils--------------------------------------*/

	protected boolean isManager(final Object user) {
		final IModuleContext ctx = getModuleContext();
		return ctx.getPermission().getUser(user).isMember(ctx.getModule().getManagerRole(null));
	}

	protected ExpressionValue toExpressionValue(final FilterItems params) {
		if (params == null) {
			return null;
		}
		if (params.size() == 0) {
			return new ExpressionValue("1=1");
		}
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		int i = 0;
		for (final FilterItem item : params) {
			final ExpressionValue ev = ExpressionValue.toExpressionValue(item);
			if (ev != null) {
				if (i++ > 0) {
					sql.append(" ").append(item.getOpe()).append(" ");
				}
				sql.append(ev.getExpression());
				final Object[] vals = ev.getValues();
				if (vals != null) {
					for (final Object val : vals) {
						al.add(getIdParam(val));
					}
				}
			}
		}
		return new ExpressionValue(sql, al.toArray());
	}

	protected ID getLoginId() {
		final LoginWrapper lw = LoginUser.get();
		return lw != null ? lw.getUserId() : null;
	}

	@Override
	public void onInit() throws Exception {
		super.onInit();

		if (this instanceof ITreeBeanServiceAware) {
			addListener(new DbEntityAdapterEx<T>() {
				@Override
				public void onBeforeUpdate(final IDbEntityManager<T> manager, final String[] columns,
						final T[] beans) throws Exception {
					super.onBeforeUpdate(manager, columns, beans);
					for (final T o : beans) {
						assertParentId(o);
					}
				}
			});
		}
	}

	protected void assetDomainId_delete(final ID domainId) {
		final PermissionUser puser = LoginUser.user();
		if (!puser.isManager() && !ObjectUtils.objectEquals(puser.getDomainId(), domainId)) {
			throw ADOException.of($m("AbstractDbBeanService.5"));
		}
	}

	protected void assertTimeInterval(final int second) {
		final Date createdate = (Date) getEntityManager().queryFor("dd",
				new SQLValue("select max(createdate) as dd from " + getTablename()));
		if (createdate != null) {
			if (System.currentTimeMillis() - createdate.getTime() < second * 1000l) {
				throw ModuleContextException.of($m("AbstractDbBeanService.6", second));
			}
		}
	}

	/*------------------------------ITreeBeanServiceAware--------------------------*/

	public IDataQuery<T> queryChildren(final T parent, final ID domainId,
			final ColumnData... orderColumns) {
		assertTreeBean();
		final StringBuilder sql = new StringBuilder();
		final List<Object> params = new ArrayList<Object>();

		if (parent == null) {
			sql.append("parentid is null");
		} else {
			sql.append("parentid=?");
			params.add(getIdParam(parent));
		}

		if (domainId != null) {
			sql.append(" and (domainid=? or domainid is null)");
			params.add(domainId);
		} else {
			if (IDomainBeanAware.class.isAssignableFrom(getBeanClass()) && !LoginUser.isManager()) {
				sql.append(" and domainid is null");
			}
		}

		sql.append(toOrderSQL(orderColumns));
		return query(sql, params.toArray());
	}

	public IDataQuery<T> queryChildren(final T parent, final ColumnData... orderColumns) {
		ID domainId = null;
		if (parent instanceof IDomainBeanAware) {
			domainId = ((IDomainBeanAware) parent).getDomainId();
		}
		return queryChildren(parent, domainId, orderColumns);
	}

	public boolean hasChild(final T parent) {
		assertTreeBean();
		final int c = parent == null ? count("parentid is null") : count("parentid=?",
				BeanUtils.getProperty(parent, "id"));
		return c > 0;
	}

	public int getLevel(final T node) {
		assertTreeBean();
		int l = 1;
		ITreeBeanAware p = (ITreeBeanAware) node;
		while ((p = (ITreeBeanAware) getBean(p.getParentId())) != null) {
			l++;
		}
		return l;
	}

	protected void assertTreeBean() {
		if (!ITreeBeanAware.class.isAssignableFrom(getBeanClass())) {
			throw NotImplementedException.of($m("AbstractDbBeanService.2",
					ITreeBeanAware.class.getName()));
		}
	}

	/**
	 * 验证parentId的合法性
	 * 
	 * @param node
	 */
	private void assertParentId(final T node) {
		if (!(this instanceof ITreeBeanServiceAware)) {
			return;
		}
		if (!(node instanceof IIdBeanAware) || !(node instanceof ITreeBeanAware)) {
			return;
		}
		final ID oId = ((IIdBeanAware) node).getId();
		final ID nParentId = ((ITreeBeanAware) node).getParentId();
		if (oId.equals(nParentId)) {
			throw ADOException.of($m("AbstractDbBeanService.0"));
		}

		/* 不能设置自己为父 */
		final IDbEntityManager<?> mgr = getEntityManager();
		final String[] columns = new String[] { "id", "parentId" };
		Map<String, Object> p = mgr.executeQuery(columns, new ExpressionValue("id=?", nParentId));
		while (p != null) {
			final ID pId = ID.of(p.get("parentId"));
			if (oId.equals(pId)) {
				throw ADOException.of($m("AbstractDbBeanService.1"));
			}
			p = mgr.executeQuery(columns, new ExpressionValue("id=?", p.get("parentId")));
		}
	}

	/*------------------------------IUserBeanServiceAware--------------------------*/

	public IDataQuery<T> queryByUser(final Object userId) {
		if (userId == null) {
			return DataQueryUtils.nullQuery();
		}
		return queryByParams(FilterItems.of("userid", userId));
	}

	protected void buildStatusSQL(final StringBuilder sql, final List<Object> params,
			final String alias, final Enum<?>... status) {
		if (status != null && status.length > 0) {
			sql.append(" and (");
			int i = 0;
			for (final Enum<?> s : status) {
				if (i++ > 0) {
					sql.append(" or ");
				}
				if (alias != null) {
					sql.append(alias).append(".");
				}
				sql.append("status=?");
				params.add(s);
			}
			sql.append(")");
		}
	}
}

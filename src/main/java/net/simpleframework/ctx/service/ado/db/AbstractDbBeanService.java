package net.simpleframework.ctx.service.ado.db;

import static net.simpleframework.common.I18n.$m;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.ado.ADOException;
import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EOrder;
import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.IADOListener;
import net.simpleframework.ado.IADOManagerFactoryAware;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.IParamsValue.AbstractParamsValue;
import net.simpleframework.ado.bean.AbstractIdBean;
import net.simpleframework.ado.bean.IDomainBeanAware;
import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.ado.db.DbManagerFactory;
import net.simpleframework.ado.db.DbTableColumn;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.IDbManager;
import net.simpleframework.ado.db.IDbQueryManager;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.db.event.DbEntityAdapter;
import net.simpleframework.ado.db.event.IDbEntityListener;
import net.simpleframework.ado.query.DataQueryUtils;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.object.ObjectFactory;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.ctx.ModuleContextException;
import net.simpleframework.ctx.permission.LoginUser;
import net.simpleframework.ctx.permission.LoginUser.LoginWrapper;
import net.simpleframework.ctx.service.AbstractBaseService;
import net.simpleframework.ctx.service.ado.ITreeBeanServiceAware;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbBeanService<T> extends AbstractBaseService implements
		IDbBeanService<T> {

	@Override
	public IModuleContext getModuleContext() {
		final IModuleContext context = super.getModuleContext();
		if (context == null) {
			throw ModuleContextException.of($m("AbstractDbBeanService.4"));
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createBean() {
		return (T) ObjectFactory.newInstance(getBeanClass());
	}

	@Override
	public T getBean(final Object id) {
		return getEntityManager().getBean(id);
	}

	public T getBean(final String expr, final Object... params) {
		return getEntityManager().queryForBean(new ExpressionValue(expr, params));
	}

	public IDataQuery<T> query(final String expr, final Object... params) {
		return getEntityManager().queryBeans(new ExpressionValue(expr, params));
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
			sql.append(" order by ");
			int i = 0;
			for (final ColumnData col : _orderColumns) {
				if (i++ > 0) {
					sql.append(", ");
				}
				sql.append(col.getAlias());
				final EOrder o = col.getOrder();
				if (o != EOrder.normal) {
					sql.append(" ").append(o);
				}
			}
		}
		return sql.toString();
	}

	protected final ColumnData[] DEFAULT_ORDER = new ColumnData[] { ColumnData.DESC("createdate") };
	protected final ColumnData[] ORDER_OORDER = new ColumnData[] { ColumnData.DESC("oorder") };

	protected ColumnData[] getDefaultOrderColumns() {
		return DEFAULT_ORDER;
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
	public int count(final String expr, final Object... params) {
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
	public Number sum(final String column, final String expr, final Object... params) {
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
	public Number max(final String column, final String expr, final Object... params) {
		return getEntityManager().max(column, new ExpressionValue(expr, params));
	}

	@Override
	public Number max(final String column) {
		return max(column, "1=1");
	}

	public Number avg(final String column, final String expr, final Object... params) {
		return getEntityManager().avg(column, new ExpressionValue(expr, params));
	}

	@Override
	public Number avg(final String column) {
		return avg(column, "1=1");
	}

	public Object queryFor(final String column, final String expr, final Object... params) {
		return getEntityManager().queryFor(column, new ExpressionValue(expr, params));
	}

	@Override
	public Object exchange(final T bean1, final T bean2, final DbTableColumn order, final boolean up) {
		return getEntityManager().exchange(bean1, bean2, order, up);
	}

	@Override
	public Object exchange(final T bean1, final T bean2, final boolean up) {
		return exchange(bean1, bean2, new DbTableColumn("oorder"), up);
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
	public void addListener(final IADOListener listener) {
		getEntityManager().addListener(listener);
	}

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
			throw ModuleContextException.of($m("AbstractDbBeanService.3"));
		}
		return mgr;
	}

	protected Map<String, Integer> COUNT_STATS = new ConcurrentHashMap<String, Integer>();

	protected class DbEntityAdapterEx extends DbEntityAdapter {
		@SuppressWarnings("unchecked")
		protected Collection<T> coll(final IParamsValue paramsValue) {
			final AbstractParamsValue<?> val = (AbstractParamsValue<?>) paramsValue;
			Collection<T> coll = (Collection<T>) val.getAttr("coll");
			if (coll == null) {
				val.setAttr("coll",
						coll = DataQueryUtils.toList(getEntityManager().queryBeans(paramsValue)));
			}
			return coll;
		}

		@Override
		protected void doAfterEvent(final IDbManager service, final Object params) {
			super.doAfterEvent(service, params);
			COUNT_STATS.clear();
		}
	}

	/*------------------------------------utils--------------------------------------*/

	protected boolean isManager(final Object user) {
		final IModuleContext ctx = getModuleContext();
		return ctx.getPermission().getUser(user).isMember(ctx.getManagerRole());
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
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	protected ID getLoginId() {
		final LoginWrapper lw = LoginUser.get();
		return lw != null ? lw.getUserId() : null;
	}

	protected Object getIdParam(final Object bean) {
		if (bean instanceof String || bean instanceof ID) {
			return bean;
		} else if (bean instanceof IIdBeanAware) {
			return ((AbstractIdBean) bean).getId();
		} else {
			final Object id = BeanUtils.getProperty(bean, "id");
			return id != null ? id : bean;
		}
	}

	private final IDbEntityListener CONTEXT_LISTENER = new DbEntityAdapterEx() {
		@Override
		public void onBeforeInsert(final IDbEntityManager<?> manager, final Object[] beans) {
			super.onBeforeInsert(manager, beans);
			Integer _domain = null;
			for (final Object t : beans) {
				IDomainBeanAware bean;
				if (t instanceof IDomainBeanAware && (bean = (IDomainBeanAware) t).getDomain() == 0) {
					if (_domain == null) {
						_domain = getModuleContext().getDomain();
					}
					bean.setDomain(_domain);
				}
			}
		}
	};

	{
		addListener(CONTEXT_LISTENER);
	}

	/*------------------------------ITreeBeanServiceAware--------------------------*/

	public IDataQuery<T> queryChildren(final T parent, final ColumnData... orderColumns) {
		final Class<?> beanClass = getBeanClass();
		if (!IIdBeanAware.class.isAssignableFrom(beanClass)) {
			throw NotImplementedException.of($m("AbstractDbBeanService.2",
					IIdBeanAware.class.getName()));
		}
		final FilterItems items = FilterItems.of();
		if (parent == null) {
			items.addIsNull("parentid");
		} else {
			items.addEqual("parentid", ((IIdBeanAware) parent).getId());
		}
		if (ArrayUtils.isEmpty(orderColumns)) {
			return queryByParams(items, ColumnData.ASC("oorder"));
		}
		return queryByParams(items, orderColumns);
	}

	public Map<ID, Collection<T>> queryAllTree() {
		final Class<?> beanClass = getBeanClass();
		if (!ITreeBeanAware.class.isAssignableFrom(beanClass)) {
			throw NotImplementedException.of($m("AbstractDbBeanService.2"));
		}
		return treeToMap(queryAll().setFetchSize(0));
	}

	protected Map<ID, Collection<T>> treeToMap(final IDataQuery<T> dq) {
		final Map<ID, Collection<T>> map = new HashMap<ID, Collection<T>>();
		T t;
		while ((t = dq.next()) != null) {
			ID k = ((ITreeBeanAware) t).getParentId();
			if (k == null) {
				k = ID.NULL_ID;
			}
			Collection<T> coll = map.get(k);
			if (coll == null) {
				map.put(k, coll = new ArrayList<T>());
			}
			coll.add(t);
		}
		return map;
	}

	public int getLevel(final T node) {
		final Class<?> beanClass = getBeanClass();
		if (!ITreeBeanAware.class.isAssignableFrom(beanClass)) {
			throw NotImplementedException.of($m("AbstractDbBeanService.2"));
		}
		final IDbEntityManager<?> mgr = getEntityManager();
		int l = 1;
		ITreeBeanAware p = (ITreeBeanAware) node;
		while ((p = (ITreeBeanAware) mgr.getBean(p.getParentId())) != null) {
			l++;
		}
		return l;
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

	@Override
	public void onInit() throws Exception {
		super.onInit();

		if (this instanceof ITreeBeanServiceAware) {
			addListener(new DbEntityAdapterEx() {
				@SuppressWarnings("unchecked")
				@Override
				public void onBeforeUpdate(final IDbEntityManager<?> manager, final String[] columns,
						final Object[] beans) {
					super.onBeforeUpdate(manager, columns, beans);
					for (final Object o : beans) {
						assertParentId((T) o);
					}
				}
			});
		}
	}

	/*------------------------------IUserBeanServiceAware--------------------------*/

	public IDataQuery<T> queryByUser(final Object userId) {
		if (userId == null) {
			return DataQueryUtils.nullQuery();
		}
		return queryByParams(FilterItems.of("userid", userId));
	}
}

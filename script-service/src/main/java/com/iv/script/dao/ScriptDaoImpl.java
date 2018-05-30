package com.iv.script.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.script.api.dto.ScriptQueryDto;
import com.iv.script.entity.AuthorEntity;
import com.iv.script.entity.ScriptEntity;
import com.iv.script.entity.ScriptPagingWrap;

@Repository
public class ScriptDaoImpl implements IScriptDao {

	@Override
	public void save(ScriptEntity entity) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.merge(entity);
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScriptEntity> selectAll(int first, int max) throws RuntimeException {
		return (List<ScriptEntity>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from ScriptEntity s order by s.creDate desc").setFirstResult(first)
						.setMaxResults(max).list();
			}
		});
	}

	@Override
	public ScriptEntity selectById(int id) throws RuntimeException {
		return (ScriptEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.get(ScriptEntity.class, id);
			}
		});
	}

	@Override
	public void delById(int id) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.load(ScriptEntity.class, id));
				return null;
			}
		});

	}

	@Override
	public ScriptEntity selectByName(String name, String type) throws RuntimeException {
		return (ScriptEntity) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("from ScriptEntity d where d.name=? and d.type=?").setParameter(0, name)
						.setParameter(1, type).uniqueResult();
			}
		});
	}

	@Override
	public long countAll() throws RuntimeException {

		return (long) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select count(*) from ScriptEntity").uniqueResult();

			}
		});
	}

	@Override
	public void delByName(String name) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.delete(ses.createQuery("from ScriptEntity d where d.name=?").setParameter(0, name).uniqueResult());
				return null;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ScriptPagingWrap selectByCondition(ScriptQueryDto query, List<AuthorEntity> creators)
			throws RuntimeException {
		return (ScriptPagingWrap) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ScriptPagingWrap pagingWrap = new ScriptPagingWrap();
				if(0 != query.getId()) {
					List<ScriptEntity> docInfoEntities = new ArrayList<>();
					ScriptEntity entity = ses.get(ScriptEntity.class, query.getId());
					if(null != entity) {
						docInfoEntities.add(entity);
						pagingWrap.setTotalCount(1);
					} else {
						pagingWrap.setTotalCount(0);
					}
					pagingWrap.setEntities(docInfoEntities);
					return pagingWrap;
				}
				StringBuilder hql = new StringBuilder("from ScriptEntity d where ");
				boolean flag = false;
				if (!StringUtils.isEmpty(query.getAlias())) {
					hql.append("d.alias like '%"  + query.getAlias() + "%'");
					flag = true;
				}
				if (!StringUtils.isEmpty(query.getType())) {
					if (flag) {
						hql.append(" and ");
					}
					hql.append("d.type='" + query.getType() + "'");
					flag = true;
				}
				if (!StringUtils.isEmpty(query.getItemType())) {
					if (flag) {
						hql.append(" and ");
					}
					hql.append("d.itemType=" + query.getItemType().ordinal());
					flag = true;
				}
				if (0 != query.getDateStart() && 0 != query.getDateEnd()) {
					if (flag) {
						hql.append(" and ");
					}
					hql.append("d.creDate>" + query.getDateStart() + " and " + "d.creDate<" + query.getDateEnd());
					flag = true;
				}
				if (!CollectionUtils.isEmpty(creators)) {
					if (flag) {
						hql.append(" and ");
					}
					hql.append("d.creator in (:creators)");
					pagingWrap.setEntities(ses.createQuery(hql.toString()).setParameterList("creators", creators)
							.setFirstResult((query.getCurPage() - 1) * query.getItems()).setMaxResults(query.getItems())
							.list());
					pagingWrap.setTotalCount((long) ses.createQuery("select count(*) " + hql.toString())
							.setParameterList("creators", creators).uniqueResult());
					return pagingWrap;
				}

				if (!flag) {
					pagingWrap.setEntities(ses.createQuery("from ScriptEntity d order by d.creDate desc")
							.setFirstResult((query.getCurPage() - 1) * query.getItems()).setMaxResults(query.getItems())
							.list());
					pagingWrap.setTotalCount(
							(long) ses.createQuery("select count(*) from ScriptEntity d order by d.creDate desc")
									.uniqueResult());
					return pagingWrap;
				}
				pagingWrap.setEntities(ses.createQuery(hql.append(" order by d.creDate desc").toString())
						.setFirstResult((query.getCurPage() - 1) * query.getItems()).setMaxResults(query.getItems())
						.list());
				pagingWrap.setTotalCount(
						(long) ses.createQuery("select count(*) " + hql.toString())
								.uniqueResult());
				return pagingWrap;
			}
		});
	}

}

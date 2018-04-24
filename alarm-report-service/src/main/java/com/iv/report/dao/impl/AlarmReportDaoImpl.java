package com.iv.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.CycleType;
import com.iv.common.util.hibernate.HibernateCallBack;
import com.iv.common.util.hibernate.HibernateTemplate;
import com.iv.report.dao.AlarmReportDao;
/**
 * 告警报表数据
 * @author zhangying
 * 2018年4月23日
 * aggregation-1.4.0-SNAPSHOT
 */
@Repository
public class AlarmReportDaoImpl implements AlarmReportDao{

	/**
	 * 特定时间段内告警总量:总体分析
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> selectAlarmByItemType(Long startTime,Long endTime) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub	
				return ses.createQuery("select a.item_type,e.tri_date,e.rec_date,e.res_date from alarm_life a,event_date e where a.id = e.id and e.tri_date>=? and e.tri_date<=?")
						.setParameter(0, startTime).setParameter(1, endTime).list();								
			}
		});
	}
	
	/**
	 * 特定时间段内人为处理告警分析：人为处理分析
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> alarmsSolvedByPerson(Long startTime,Long endTime) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub	
				return ses.createQuery("select a.item_type,e.tri_date,e.rec_date,e.res_date from alarm_life a,event_date e where a.id = e.id and e.tri_date>=? and e.tri_date<=? and a.handler_cur is not null")
						.setParameter(0, startTime).setParameter(1, endTime).list();								
			}
		});
	}
	
	/**
	 * 特定时间段内升级告警量
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> countUpAlarmByItemType(Long startTime,Long endTime) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select a.item_type,count(1) from alarm_life a,event_date e where a.id = e.id and e.tri_date>=? and e.tri_date<=? and a.upgrade>0 group by a.item_type")
						.setParameter(0, startTime).setParameter(1, endTime).list();
			}
		});
	}

	/**
	 * 压缩告警数据分析
	 * 条件：1》触发时间在特定时间段内
	 *     2》告警已恢复
	 *     3》告警没有当前处理人
	 * 结果：1》事件类型
	 *     2》事件总量
	 *     3》事件总故障时间（h）
	 *     4》平均处理时间(min)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> alarmsSolvedAuto(Long startTime, Long endTime) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select a.item_type,count(1),sum(e.rec_date-e.tri_date),avg(e.rec_date-tri_date) from alarm_life a,event_date e where a.id = e.id and e.tri_date>=? and e.tri_date<=? and e.rec_date!=0 and a.handlerCurrent is null group by a.item_type")
						.setParameter(0, startTime).setParameter(1, endTime).list();
			}
		});
	}

	/**
	 * 特定时间段，压缩告警数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> countpressAlarmByItemType(Long startTime, Long endTime) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select a.item_type,count(1) from alarm_life a,event_date e where a.id = e.id where e.tri_date>=? and e.tri_date<=? and e.rec_date!=0 and a.handlerCurrent is null group by a.item_type")
						.setParameter(0, startTime).setParameter(1, endTime).list();
			}
		});
	}

	@Override
	public Long countHostIps(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (Long) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select count(distinct s.host_ip) from alarm_life a,alarm_source s where a.alarm_id=s.alarm_id where a.id in (select e.id from event_date e where e.tri_date>?)")
						.setParameter(0, date).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> selectAlarmInfo(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select e.tri_date,e.rec_date,e.res_date from event_date e where e.tri_date>?")
						.setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByHostIp(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery(
						"select s.host_ip,count(1),s.host_name from alarm_life a,alarm_source s where a.alarm_id=s.alarm_id and a.id in (select e.id from event_date e where e.tri_date>?) group by s.host_ip")
						.setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByHostIpItemType(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select s.host_ip,a.item_type,count(1) from alarm_life a,alarm_source s where a.alarm_id=s.alarm_id and where a.id in (select e.id from event_date e where e.tri_date>?) group by s.host_ip,a.item_type")
						.setParameter(0, date).list();			
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupBySeverity(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery(
						"select a.severity,count(1),(select count(1) from event_date e where e.tri_date>?) "
								+ "from alarm_life a, event_date b where a.id=b.id and b.tri_date>? group by a.severity")
						.setParameter(0, date).setParameter(1, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByItemType(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				return ses.createQuery("select a.item_type,e.tri_date,e.rec_date,e.res_date from alarm_life a, event_date e where a.id=e.id and e.tri_date>?")
						.setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> groupByHostIpOpen(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery(
						"select count(1) from alarm_life a,alarm_source s where a.alarm_id=s.alarm_id and a.id in (select e.id from event_date e where e.tri_date>?) and a.alarm_status<>? group by host_ip")
						.setParameter(0, date).setParameter(1, AlarmStatus.CLOSED).list();
			}
		});
	}

	@Override
	public Long getAlarmNumOpen(Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (Long) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				return ses.createQuery("select count(1) from alarm_life a, event_date e where a.id=e.id and e.tri_date>? and a.alarm_status<>?")
						.setParameter(0, date).setParameter(1, AlarmStatus.CLOSED).uniqueResult();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> groupByCycle(CycleType cycle, Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				String hql = null;
				switch (cycle) {
				case DAY:
					hql = "select DAY(FROM_UNIXTIME(a.tri_date/1000)),HOUR(FROM_UNIXTIME(a.tri_date/1000)),count(1) "
							+ "from event_date a where a.tri_date>? "
							+ "group by DAY(FROM_UNIXTIME(a.tri_date/1000)),HOUR(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case WEEK:
				case TWO_WEEKS:
				case MONTH:
					hql = "select MONTH(FROM_UNIXTIME(a.tri_date/1000)),DAY(FROM_UNIXTIME(a.tri_date/1000)),count(1) "
							+ "from event_date a where a.tri_date>? "
							+ "group by MONTH(FROM_UNIXTIME(a.tri_date/1000)),DAY(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case TWO_MONTHS:
					hql = "select MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000)),count(1) "
							+ "from event_date a where a.tri_date>? "
							+ "group by MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case HALF_YEAR:
					hql = "select YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000)),count(1) "
							+ "from event_date a where a.tri_date>? "
							+ "group by YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case YEAR:
					hql = "select YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),count(1) "
							+ "from event_date a where a.tri_date>? "
							+ "group by YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				default:
					break;
				}

				return ses.createQuery(hql).setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAlarmByCycle(CycleType cycle, Long date) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				String sql = null;
				switch (cycle) {
				case DAY:
					sql = "select DAY(FROM_UNIXTIME(a.tri_date/1000)),HOUR(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from event_date a where a.tri_date>? "
							+ "group by DAY(FROM_UNIXTIME(a.tri_date/1000)),HOUR(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case WEEK:
				case TWO_WEEKS:
				case MONTH:
					sql = "select MONTH(FROM_UNIXTIME(a.tri_date/1000)),DAY(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from event_date a where a.tri_date>? "
							+ "group by MONTH(FROM_UNIXTIME(a.tri_date/1000)),DAY(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case TWO_MONTHS:
					sql = "select MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from event_date a where a.tri_date>? "
							+ "group by MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case HALF_YEAR:
					sql = "select YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from event_date a where a.tri_date>? "
							+ "group by YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),WEEK(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				case YEAR:
					sql = "select YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000)),GROUP_CONCAT(a.id) "
							+ "from event_date a where a.tri_date>? "
							+ "group by YEAR(FROM_UNIXTIME(a.tri_date/1000)),MONTH(FROM_UNIXTIME(a.tri_date/1000))";
					break;
				default:
					break;
				}

				return ses.createSQLQuery(sql).setParameter(0, date).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> selectBatchByIds(String[] ids) throws RuntimeException {
		// TODO Auto-generated method stub
		return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {
			
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				// TODO Auto-generated method stub
				List<Object[]> objects = new ArrayList<Object[]>();
				for (String id : ids) {
					Object[] object= (Object[] )ses.createQuery("select e.tri_date,e.rec_date,e.res_date from event_date e where e.id=?")
							.setParameter(0, id).uniqueResult();
					if(null != object) {
						objects.add(object);
					}					
				}
				return objects;
			}
		});
	}

}

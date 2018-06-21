package com.iv.wechat.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import com.iv.wechat.autoReply.EventSubMessage;

@Repository
public class MessageDaoImpl implements IMessageDao {
	
	@Override
	public void saveEventSubMessage(final EventSubMessage esm) throws RuntimeException {

		HibernateTemplate.execute(new HibernateCallBack() {
			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				ses.saveOrUpdate(esm);
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventSubMessage> selectEventSubMessageByEventKey(String eventKey) throws RuntimeException {
		return (List<EventSubMessage>) HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				List<EventSubMessage> list = ses.createCriteria(EventSubMessage.class).list();
				return list;
			}
		});
	}

	@Override
	public void deleteSubMessage(final String fromUserName) throws RuntimeException {
		HibernateTemplate.execute(new HibernateCallBack() {

			@Override
			public Object doInHibernate(Session ses) throws HibernateException {
				EventSubMessage esm = (EventSubMessage) ses.load(EventSubMessage.class, fromUserName);
				if (null != esm) {
					ses.delete(esm);
				}
				return null;
			}
		});
	}

}

package com.iv.aggregation.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import com.iv.aggregation.entity.AlarmLifeEntity;

public class AlarmGenerator implements Configurable, IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		AlarmLifeEntity entity = (AlarmLifeEntity) arg1;
		Date today = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuilder builder = new StringBuilder(formatDate.format(today));
		builder.append(entity.getAlarm().getEventId());
		return builder.toString();
	}

	@Override
	public void configure(Type arg0, Properties arg1, ServiceRegistry arg2) throws MappingException {
		// TODO Auto-generated method stub
		
	}
	
}

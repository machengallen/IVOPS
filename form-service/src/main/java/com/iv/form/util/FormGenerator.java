package com.iv.form.util;

import com.iv.form.entity.FormInfoEntity;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author liangk
 * @create 2018年 05月 08日
 **/
public class FormGenerator implements Configurable, IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
        return FormNumUtil.getOrderNo();
    }

    @Override
    public void configure(Type arg0, Properties arg1, ServiceRegistry arg2) throws MappingException {
        // TODO Auto-generated method stub

    }
}

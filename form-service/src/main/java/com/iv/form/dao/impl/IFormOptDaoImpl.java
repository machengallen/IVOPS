package com.iv.form.dao.impl;

import com.iv.dto.ClientConditionDto;
import com.iv.dto.CommonPage;
import com.iv.form.dao.IFormOptDao;
import com.iv.form.entity.FormClientEntity;
import com.iv.form.entity.FormCompanyEntity;
import com.iv.form.entity.FormDemandEntity;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 06月 01日
 **/
@Repository
public class IFormOptDaoImpl implements IFormOptDao {

    private static IFormOptDao dao = new IFormOptDaoImpl();


    public IFormOptDaoImpl() {

    }

    public static IFormOptDao getInstance() {
        return dao;
    }

    /**
     * 公司信息分页
     * @param clientConditionDto
     * @return
     */
    @Override
    public CommonPage selectCompanyPage(ClientConditionDto clientConditionDto) {
        return (CommonPage) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                CommonPage commonPage = new CommonPage();

                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.`name`,\n" +
                        "\tb.real_name AS createName,\n" +
                        "\ta.create_date createDate,\n" +
                        "\tc.real_name AS updateName,\n" +
                        "\ta.update_date updateDate\n" +
                        "FROM\n" +
                        "\tform_company a\n" +
                        "LEFT JOIN form_user b ON a.create_by = b.id\n" +
                        "LEFT JOIN form_user c ON a.update_by = c.id\n" +
                        "WHERE\n" +
                        "\t1 = 1");
                if (!StringUtils.isEmpty(clientConditionDto.getName())) {
                    sql.append(" and a.name like ").append("'%").append(clientConditionDto.getName()).append("%'");
                }
                commonPage.setCount(ses.createSQLQuery(sql.toString()).list().size());
                commonPage.setList(ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())
                        .setFirstResult((clientConditionDto.getCurPage()-1)* clientConditionDto.getItems()).setMaxResults(clientConditionDto.getItems())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list()
                );
                return commonPage;
            }
        });
    }

    /**
     * 客户分页
     * @param clientConditionDto
     * @return
     */
    @Override
    public CommonPage selectClientPage(ClientConditionDto clientConditionDto) {
        return (CommonPage) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                CommonPage commonPage = new CommonPage();

                StringBuffer sql = new StringBuffer("SELECT a.id,a.company_id companyId,a.email,a.phone,a.`name`,a.is_principal isPrincipal,b.`name` companyName from form_client a LEFT JOIN form_company b ON a.company_id=b.id WHERE 1=1");
                if (!StringUtils.isEmpty(clientConditionDto.getName())) {
                    sql.append(" and a.name like ").append("'%").append(clientConditionDto.getName()).append("%'");
                }
                commonPage.setCount(ses.createSQLQuery(sql.toString()).list().size());
                commonPage.setList(ses.createSQLQuery(sql.append(" order by company_id, a.create_date desc").toString())
                        .setFirstResult((clientConditionDto.getCurPage()-1)* clientConditionDto.getItems()).setMaxResults(clientConditionDto.getItems())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list()
                );
                return commonPage;
            }
        });
    }


    /**
     * 保存公司信息
     * @param formCompanyEntity
     */
    @Override
    public void saveOrUpdateCompany(FormCompanyEntity formCompanyEntity) {

        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formCompanyEntity);
                return null;
            }
        });
    }

    /**
     * 保存客户
     * @param formClientEntity
     */
    @Override
    public void saveOrUpdateClient(FormClientEntity formClientEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formClientEntity);
                return null;
            }
        });
    }

    /**
     * 批量删除客户
     * @param clientId
     */
    @Override
    public void delClientById(Integer clientId) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.createQuery("delete from FormClientEntity a where a.id=? ")
                        .setParameter(0,clientId).executeUpdate();
                return null;
            }
        });
    }

    /**
     * 批量删除公司
     * @param companyId
     */
    @Override
    public void delCompanyById(Integer companyId) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.createQuery("delete from FormCompanyEntity a where a.id=? ")
                        .setParameter(0,companyId).executeUpdate();
                return null;
            }
        });
    }

    /**
     * 查询公司信息
     * @param id
     * @return
     */
    @Override
    public FormCompanyEntity selectCompanyById(Integer id) {
        return (FormCompanyEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormCompanyEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }

    /**
     * 查询客户
     * @param id
     * @return
     */
    @Override
    public FormClientEntity selectClientById(Integer id) {
        return (FormClientEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormClientEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }


    /**
     * 查询所有
     * @return
     */
    @Override
    public List<FormDemandEntity> selectDemandAll() {
        return (List<FormDemandEntity>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormDemandEntity a where a.parent.id is null or a.parent.id=0").list();

            }
        });
    }

    /**
     * 查询服务目录信息及父类id
     * @param id
     * @return
     */
    @Override
    public Map selectDemandAndParentById(Integer id) {
        return (Map) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                return ses.createSQLQuery("select a.id,a.content,a.label,a.parent_id parentId from form_demand a where a.id=?")
                        .setParameter(0,id)
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();

            }
        });
    }

    /**
     * 查询服务目录信息及子类信息
     * @param id
     * @return
     */
    @Override
    public FormDemandEntity selectDemandAndChildrenById(Integer id) {
        return (FormDemandEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.get(FormDemandEntity.class,id);
            }
        });
    }


    /**
     * 保存修改需求
     * @param formDemandEntity
     */
    @Override
    public void saveOrUpdateDemand(FormDemandEntity formDemandEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formDemandEntity);
                return null;
            }
        });
    }

    /**
     * 删除需求
     * @param id
     */
    @Override
    public void delDemandById(Integer id) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                ses.createQuery("delete from FormDemandEntity a where a.id=? ")
                        .setParameter(0,id).executeUpdate();
                return null;
            }
        });
    }
}

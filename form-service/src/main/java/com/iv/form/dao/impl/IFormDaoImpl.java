package com.iv.form.dao.impl;

import com.iv.common.enumeration.CycleType;
import com.iv.dto.*;
import com.iv.enumeration.FormState;
import com.iv.form.dao.IFormDao;
import com.iv.form.entity.*;
import com.iv.jpa.util.hibernate.HibernateCallBack;
import com.iv.jpa.util.hibernate.HibernateTemplate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 工单相关接口实现
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
@Repository
public class IFormDaoImpl implements IFormDao {


    private static IFormDao dao = new IFormDaoImpl();


    public IFormDaoImpl() {

    }

    public static IFormDao getInstance() {
        return dao;
    }

    /**
     * 保存工单
     * @param formInfoEntity
     */
    @Override
    public void saveOrUpdateForm(FormInfoEntity formInfoEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formInfoEntity);
                return null;
            }
        });
    }



    /**
     * 查询工单信息
     * @param formId
     * @return
     */
    @Override
    public FormInfoEntity selectFormById(String formId) {
        return (FormInfoEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormInfoEntity a where a.id=?")
                        .setParameter(0, formId).uniqueResult();
            }
        });
    }

    /**
     * 查询工单信息
     * @param formId
     * @return
     */
    @Override
    public Map selectFormMapById(String formId) {
        return (Map) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.applicant_id applicantId,\n" +
                        "\tCASE\n" +
                        "WHEN b.real_name IS NULL THEN\n" +
                        "\tb.user_name\n" +
                        "ELSE\n" +
                        "\tb.real_name\n" +
                        "END AS applicantName,\n" +
                        " a.form_owner_id AS formOwnerId,\n" +
                        " c.`name` AS formOwnerName,\n" +
                        " a.form_owner_phone AS formOwnerPhone,\n" +
                        " a.demand_type_code AS demandTypeCode,\n" +
                        " d.`label` AS demandName,\n" +
                        " a.demand_content AS demandContent,\n" +
                        " a.form_apply_time AS formApplyTime,\n" +
                        " a.form_expect_end_time AS formExpectEndTime,\n" +
                        " a.form_real_end_time AS formRealEndTime,\n" +
                        " a.form_state AS formState,\n" +
                        " g.`name` AS formStateName,\n" +
                        " a.handler_id AS handlerId,\n" +
                        " a.group_id AS groupId,\n" +
                        " CASE\n" +
                        "WHEN f.real_name IS NULL THEN\n" +
                        "\tf.user_name\n" +
                        "ELSE\n" +
                        "\tf.real_name\n" +
                        "END AS handlerName,\n" +
                        " a.priority,\n" +
                        " h.name priorityName,\n" +
                        " a.relation_form_id relationFormId,\n" +
                        " a.unit_code unitCode,\n" +
                        " e.`name` AS unitName\n" +
                        "FROM\n" +
                        "\tform_info a\n" +
                        "LEFT JOIN form_user b ON a.applicant_id = b.id\n" +
                        "LEFT JOIN form_client c ON a.form_owner_id = c.id\n" +
                        "LEFT JOIN form_demand d ON a.demand_type_code = d.id\n" +
                        "LEFT JOIN form_company e ON a.unit_code = e.id\n" +
                        "LEFT JOIN form_user f ON a.handler_id = f.id\n" +
                        "LEFT JOIN form_state g ON a.form_state = g.id\n" +
                        "LEFT JOIN form_priority h ON a.priority = h.id\n" +
                        "WHERE\n" +
                        "\ta.id = "+formId+"");
                return ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
            }
        });
    }

    /**
     * 查询工单流转记录
     * @param formId
     * @return
     */
    @Override
    public List<FormChangeLogsEntity> selectFormChangeById(String formId) {
        return (List<FormChangeLogsEntity>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormChangeLogsEntity a where a.formId=?")
                        .setParameter(0, formId).list();
            }
        });
    }

    /**
     * 查询工单流转记录
     * @param formId
     * @return
     */
    @Override
    public List<Map> selectFormChangeMapById(String formId) {

        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.form_id as formId,\n" +
                        "\ta.form_state AS formState,\n" +
                        "\tb.name AS formStateName,\n" +
                        "\ta.change_content AS changeContent,\n" +
                        "\tCASE\n" +
                        "WHEN c.real_name IS NULL THEN\n" +
                        "\tc.user_name\n" +
                        "ELSE\n" +
                        "\tc.real_name\n" +
                        "END AS createName,\n" +
                        "\ta.create_date AS createDate\n" +
                        "FROM\n" +
                        "\tform_change_logs a\n" +
                        "LEFT JOIN form_state b ON a.form_state = b.id\n" +
                        "LEFT JOIN form_user c ON a.create_by=c.id\n" +
                        "WHERE a.form_id = "+formId+"");
                return ses.createSQLQuery(sql.append(" order by a.create_date").toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            }
        });
    }

    /**
     * 查询工单操作记录
     * @param formId
     * @return
     */
    @Override
    public List<FormOperateLogsEntity> selectFormOperateById(String formId) {
        return (List<FormOperateLogsEntity>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormOperateLogsEntity a where a.formId=?")
                        .setParameter(0, formId).list();
            }
        });
    }
    /**
     * 查询工单操作记录
     * @param formId
     * @return
     */
    @Override
    public List<Map> selectFormOperateMapById(String formId) {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.form_id formId,\n" +
                        "\ta.handler_id handlerId,\n" +
                        "\tCASE\n" +
                        "WHEN b.real_name IS NULL THEN\n" +
                        "\tb.user_name\n" +
                        "ELSE\n" +
                        "\tb.real_name\n" +
                        "END AS handlerName,\n" +
                        " a.start_time startTime,\n" +
                        " a.end_time endTime,\n" +
                        " a.demand_type_code demandTypeCode,\n" +
                        " c.label AS demandTypeCodeName,\n" +
                        " a.operate_details operateDetails,\n" +
                        " a.workload,\n" +
                        " a.create_date createDate\n" +
                        "FROM\n" +
                        "\tform_operate_logs a\n" +
                        "LEFT JOIN form_user b ON a.handler_id = b.id\n" +
                        "LEFT JOIN form_demand c ON a.demand_type_code = c.id\n" +
                        "WHERE a.form_id = "+formId+"");
                return ses.createSQLQuery(sql.append(" order by a.create_date").toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            }
        });
    }

    /**
     * 查询工单评价
     * @param formId
     * @return
     */
    @Override
    public FormEvaluateEntity selectFormEvaluateById(String formId) {
        return (FormEvaluateEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormEvaluateEntity a where a.formId=?")
                        .setParameter(0, formId).uniqueResult();
            }
        });
    }
    /**
     * 查询工单评价
     * @param formId
     * @return
     */
    @Override
    public Map selectFormEvaluateMapById(String formId) {
        return (Map) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.form_id formId,\n" +
                        "\ta.client_opinion clientOpinion,\n" +
                        "\ta.end_time endTime,\n" +
                        "\ta.evaluate_level evaluateLevel\n" +
                        "FROM\n" +
                        "\tform_evaluate a\n" +
                        "WHERE\n" +
                        "\ta.id = "+formId+"");
                return ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
            }
        });
    }


    /**
     *查询工单升级记录
     * @param formId
     * @return
     */
    @Override
    public List<FormUpgradeLogsEntity> selectFormUpgradeById(String formId) {
        return (List<FormUpgradeLogsEntity>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormUpgradeLogsEntity a where a.formId=?")
                        .setParameter(0, formId).list();
            }
        });
    }

    /**
     *查询工单升级记录
     * @param formId
     * @return
     */
    @Override
    public List<Map> selectFormUpgradeMapById(String formId) {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.form_id formId,\n" +
                        "\ta.upgrade_reason upgradeReason,\n" +
                        "\ta.create_by createBy,\n" +
                        "\tCASE\n" +
                        "WHEN b.real_name IS NULL THEN\n" +
                        "\tb.user_name\n" +
                        "ELSE\n" +
                        "\tb.real_name\n" +
                        "END AS createName,\n" +
                        " a.create_date createDate\n" +
                        "FROM\n" +
                        "\tform_upgrade_logs a\n" +
                        "LEFT JOIN form_user b ON a.create_by = b.id\n" +
                        "WHERE\n" +
                        "\ta.form_id = "+formId+"");
                return ses.createSQLQuery(sql.append(" order by a.create_date").toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            }
        });
    }

    /**
     * 我的工单分页查询
     * @param userId
     * @param formConditionDto
     * @return
     */
    @Override
    public FormInfoPage selectMyFormListPage(int userId, FormConditionDto formConditionDto,String demandIds) {
        return (FormInfoPage) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                FormInfoPage formInfoPage = new FormInfoPage();

                        StringBuffer sql = new StringBuffer("SELECT\n" +
                                "\ta.id,\n" +
                                "\ta.applicant_id AS applicantId,\n" +
                                "\tCASE\n" +
                                "WHEN f.real_name IS NULL THEN\n" +
                                "\tf.user_name\n" +
                                "ELSE\n" +
                                "\tf.real_name\n" +
                                "END AS applicantName,\n" +
                                " a.demand_type_code demandTypeCode,\n" +
                                " c.`label` AS demandTypeCodeName,\n" +
                                " a.demand_content demandContent,\n" +
                                " a.form_apply_time AS formApplyTime,\n" +
                                " a.form_state formState,\n" +
                                " a.handler_id handlerId,\n" +
                                " CASE\n" +
                                "WHEN g.real_name IS NULL THEN\n" +
                                "\tg.user_name\n" +
                                "ELSE\n" +
                                "\tg.real_name\n" +
                                "END AS handlerName,\n" +
                                " a.priority,\n" +
                                " h.name priorityName,\n" +
                                " a.relation_form_id relationFormId,\n" +
                                " a.group_id groupId,\n" +
                                " a.unit_code unitCode,\n" +
                                " b.`name` AS unitName,\n" +
                                " e.evaluate_level evaluateLevel,\n" +
                                " (\n" +
                                "\tSELECT\n" +
                                "\t\tCOUNT(d.id)\n" +
                                "\tFROM\n" +
                                "\t\tform_marks d\n" +
                                "\tWHERE\n" +
                                "\t\ta.id = d.form_id\n" +
                                "\tAND d.create_by = "+userId+"\n" +
                                ") AS isMark\n" +
                                "FROM\n" +
                                "\tform_info a\n" +
                                "LEFT JOIN form_company b ON a.unit_code = b.id\n" +
                                "LEFT JOIN form_demand c ON a.demand_type_code = c.id\n" +
                                "LEFT JOIN form_evaluate e ON a.id = e.form_id\n" +
                                "LEFT JOIN form_user f ON a.applicant_id = f.id\n" +
                                "LEFT JOIN form_user g ON a.handler_id = g.id\n" +
                                "LEFT JOIN form_priority h ON a.priority = h.id\n" +
                                "WHERE\n" +
                                "\tEXISTS (\n" +
                                "\t\tSELECT\n" +
                                "\t\t\td.form_id\n" +
                                "\t\tFROM\n" +
                                "\t\t\tform_change_logs d\n" +
                                "\t\tWHERE\n" +
                                "\t\t\ta.id = d.form_id\n" +
                                "\t\tAND d.create_by = "+userId+" or a.handler_id="+userId+"\n" +
                                "\t)\n" +
                                "AND a.del_flag = 0 OR " +
                                "EXISTS(SELECT j.form_id FROM form_audit_person j WHERE j.form_id=a.id AND j.user_id="+userId+")");
                if (!StringUtils.isEmpty(formConditionDto.getId())) {
                    sql.append(" and a.id = ").append("'").append(formConditionDto.getId()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getApplicantId())) {
                    sql.append(" and a.applicant_id = ").append("'").append(formConditionDto.getApplicantId()).append("'");
                }
                if (!StringUtils.isEmpty(demandIds)) {
                    sql.append(" and a.demand_type_code in ").append("(").append(demandIds).append(")");
                }
                if (!StringUtils.isEmpty(formConditionDto.getFormState())) {
                    sql.append(" and a.form_state = ").append("'").append(formConditionDto.getFormState()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getUnitCode())) {
                    sql.append(" and a.unit_code = ").append("'").append(formConditionDto.getUnitCode()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getStartTime())) {
                    sql.append(" and a.create_date > ").append("'").append(formConditionDto.getStartTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getEndTime())) {
                    sql.append(" and a.create_date < ").append("'").append(formConditionDto.getEndTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getMark())) {
                    sql.append(" and ( SELECT COUNT(d.id) FROM form_marks d WHERE a.id = d.form_id AND d.create_by = ").append(userId).append(") =").append(formConditionDto.getMark()==false?0:1);
                }
                if (!StringUtils.isEmpty(formConditionDto.getPriority())) {
                    sql.append(" and  a.priority = ").append("'").append(formConditionDto.getPriority()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getGroupId())) {
                    sql.append(" and  a.group_id = ").append("'").append(formConditionDto.getGroupId()).append("'");
                }
                formInfoPage.setCount(ses.createSQLQuery(sql.toString()).list().size());
                formInfoPage.setFormInfoList(ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())
                        .setFirstResult((formConditionDto.getCurPage()-1)* formConditionDto.getItems()).setMaxResults(formConditionDto.getItems())
                                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list()
                );
                return formInfoPage;
            }
        });


    }

    /**
     * 查询已删除的工单
     * @param userId
     * @param formConditionDto
     * @return
     */
    @Override
    public FormInfoPage selectDelFormListPage(int userId, FormConditionDto formConditionDto,String demandIds) {
        return (FormInfoPage) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                FormInfoPage formInfoPage = new FormInfoPage();

                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.applicant_id AS applicantId,\n" +
                        "\tCASE\n" +
                        "WHEN f.real_name IS NULL THEN\n" +
                        "\tf.user_name\n" +
                        "ELSE\n" +
                        "\tf.real_name\n" +
                        "END AS applicantName,\n" +
                        " a.demand_type_code demandTypeCode,\n" +
                        " c.`label` AS demandTypeCodeName,\n" +
                        " a.demand_content demandContent,\n" +
                        " a.form_apply_time AS formApplyTime,\n" +
                        " a.form_state formState,\n" +
                        " a.handler_id handlerId,\n" +
                        " CASE\n" +
                        "WHEN g.real_name IS NULL THEN\n" +
                        "\tg.user_name\n" +
                        "ELSE\n" +
                        "\tg.real_name\n" +
                        "END AS handlerName,\n" +
                        " a.priority,\n" +
                        " h.name priorityName,\n" +
                        " a.relation_form_id relationFormId,\n" +
                        " a.group_id groupId,\n" +
                        " a.unit_code unitCode,\n" +
                        " b.`name` AS unitName,\n" +
                        " e.evaluate_level evaluateLevel,\n" +
                        " (\n" +
                        "\tSELECT\n" +
                        "\t\tCOUNT(d.id)\n" +
                        "\tFROM\n" +
                        "\t\tform_marks d\n" +
                        "\tWHERE\n" +
                        "\t\ta.id = d.form_id\n" +
                        "\tAND d.create_by = "+userId+"\n" +
                        ") AS isMark\n" +
                        "FROM\n" +
                        "\tform_info a\n" +
                        "LEFT JOIN form_company b ON a.unit_code = b.id\n" +
                        "LEFT JOIN form_demand c ON a.demand_type_code = c.id\n" +
                        "LEFT JOIN form_evaluate e ON a.id = e.form_id\n" +
                        "LEFT JOIN form_user f ON a.applicant_id = f.id\n" +
                        "LEFT JOIN form_user g ON a.handler_id = g.id\n" +
                        "LEFT JOIN form_priority h ON a.priority = h.id\n" +
                        "WHERE\n" +
                        "\tEXISTS (\n" +
                        "\t\tSELECT\n" +
                        "\t\t\td.form_id\n" +
                        "\t\tFROM\n" +
                        "\t\t\tform_change_logs d\n" +
                        "\t\tWHERE\n" +
                        "\t\t\ta.id = d.form_id\n" +
                        "\t\tAND d.create_by = "+userId+"\n" +
                        "\t)\n" +
                        "AND a.del_flag = 1");
                if (!StringUtils.isEmpty(formConditionDto.getId())) {
                    sql.append(" and a.id = ").append("'").append(formConditionDto.getId()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getApplicantId())) {
                    sql.append(" and a.applicant_id = ").append("'").append(formConditionDto.getApplicantId()).append("'");
                }
                if (!StringUtils.isEmpty(demandIds)) {
                    sql.append(" and a.demand_type_code in ").append("(").append(demandIds).append(")");
                }
                if (!StringUtils.isEmpty(formConditionDto.getFormState())) {
                    sql.append(" and a.form_state = ").append("'").append(formConditionDto.getFormState()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getUnitCode())) {
                    sql.append(" and a.unit_code = ").append("'").append(formConditionDto.getUnitCode()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getStartTime())) {
                    sql.append(" and a.create_date > ").append("'").append(formConditionDto.getStartTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getEndTime())) {
                    sql.append(" and a.create_date < ").append("'").append(formConditionDto.getEndTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getMark())) {
                    sql.append(" and ( SELECT COUNT(d.id) FROM form_marks d WHERE a.id = d.form_id AND d.create_by = ").append(userId).append(") =").append(formConditionDto.getMark()==false?0:1);
                }
                if (!StringUtils.isEmpty(formConditionDto.getPriority())) {
                    sql.append(" and  a.priority = ").append("'").append(formConditionDto.getPriority()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getGroupId())) {
                    sql.append(" and  a.group_id = ").append("'").append(formConditionDto.getGroupId()).append("'");
                }
                formInfoPage.setCount(ses.createSQLQuery(sql.toString()).list().size());
                formInfoPage.setFormInfoList(ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())
                        .setFirstResult((formConditionDto.getCurPage()-1)* formConditionDto.getItems()).setMaxResults(formConditionDto.getItems())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list()
                );
                return formInfoPage;
            }
        });

    }

    /**
     * 查询标星的工单
     * @param userId
     * @param formConditionDto
     * @return
     */
    @Override
    public FormInfoPage selectMarkFormListPage(int userId, FormConditionDto formConditionDto,String demandIds) {
        return (FormInfoPage) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                FormInfoPage formInfoPage = new FormInfoPage();
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.applicant_id AS applicantId,\n" +
                        "\tCASE\n" +
                        "WHEN f.real_name IS NULL THEN\n" +
                        "\tf.user_name\n" +
                        "ELSE\n" +
                        "\tf.real_name\n" +
                        "END AS applicantName,\n" +
                        " a.demand_type_code demandTypeCode,\n" +
                        " c.`label` AS demandTypeCodeName,\n" +
                        " a.demand_content demandContent,\n" +
                        " a.form_apply_time AS formApplyTime,\n" +
                        " a.form_state formState,\n" +
                        " a.handler_id handlerId,\n" +
                        " CASE\n" +
                        "WHEN g.real_name IS NULL THEN\n" +
                        "\tg.user_name\n" +
                        "ELSE\n" +
                        "\tg.real_name\n" +
                        "END AS handlerName,\n" +
                        " a.priority,\n" +
                        " h.name priorityName,\n" +
                        " a.relation_form_id relationFormId,\n" +
                        " a.unit_code unitCode,\n" +
                        " b.`name` AS unitName,\n" +
                        " e.evaluate_level evaluateLevel,\n" +
                        " (\n" +
                        "\tSELECT\n" +
                        "\t\tCOUNT(d.id)\n" +
                        "\tFROM\n" +
                        "\t\tform_marks d\n" +
                        "\tWHERE\n" +
                        "\t\ta.id = d.form_id\n" +
                        "\tAND d.create_by = "+userId+"\n" +
                        ") AS isMark\n" +
                        "FROM\n" +
                        "\tform_info a\n" +
                        "LEFT JOIN form_company b ON a.unit_code = b.id\n" +
                        "LEFT JOIN form_demand c ON a.demand_type_code = c.id\n" +
                        "LEFT JOIN form_evaluate e ON a.id = e.form_id\n" +
                        "LEFT JOIN form_user f ON a.applicant_id = f.id\n" +
                        "LEFT JOIN form_user g ON a.handler_id = g.id\n" +
                        "LEFT JOIN form_priority h ON a.priority = h.id\n" +
                        "WHERE\n" +
                        "\tEXISTS ( SELECT d.form_id from form_marks d WHERE a.id=d.form_id AND d.create_by="+userId+") and a.del_flag=0 ");
                if (!StringUtils.isEmpty(formConditionDto.getId())) {
                    sql.append(" and a.id = ").append("'").append(formConditionDto.getId()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getApplicantId())) {
                    sql.append(" and a.applicant_id = ").append("'").append(formConditionDto.getApplicantId()).append("'");
                }
                if (!StringUtils.isEmpty(demandIds)) {
                    sql.append(" and a.demand_type_code in ").append("(").append(demandIds).append(")");
                }
                if (!StringUtils.isEmpty(formConditionDto.getFormState())) {
                    sql.append(" and a.form_state = ").append("'").append(formConditionDto.getFormState()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getUnitCode())) {
                    sql.append(" and a.unit_code = ").append("'").append(formConditionDto.getUnitCode()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getStartTime())) {
                    sql.append(" and a.create_date > ").append("'").append(formConditionDto.getStartTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getEndTime())) {
                    sql.append(" and a.create_date < ").append("'").append(formConditionDto.getEndTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getPriority())) {
                    sql.append(" and  a.priority = ").append("'").append(formConditionDto.getPriority()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getGroupId())) {
                    sql.append(" and  a.group_id = ").append("'").append(formConditionDto.getGroupId()).append("'");
                }
                formInfoPage.setCount(ses.createSQLQuery(sql.toString()).list().size());

                formInfoPage.setFormInfoList(ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())

                        .setFirstResult((formConditionDto.getCurPage()-1)* formConditionDto.getItems()).setMaxResults(formConditionDto.getItems())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list()

                );
                return formInfoPage;
            }
        });

    }

    /**
     * 逻辑删除工单
     * @param userId
     * @param formId
     */
    @Override
    public void delFormByLogic(int userId, String formId) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.createQuery("update FormInfoEntity a set a.delFlag=1 and a.updateBy=? where a.id=?").setParameter(0,userId).setParameter(1, formId)
                        .executeUpdate();
                return null;
            }
        });
    }

    /**
     * 逻辑删除工单
     * @param formInfoEntity
     */
    @Override
    public void delFormByLogic(FormInfoEntity formInfoEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.update(formInfoEntity);
                return null;
            }
        });
    }

    /**
     * 保存修改流转记录
     * @param formChangeLogsEntity
     */
    @Override
    public void saveOrUpdateFormChanges(FormChangeLogsEntity formChangeLogsEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formChangeLogsEntity);
                return null;
            }
        });
    }

    /**
     * 保存操作记录
     * @param formOperateLogsEntity
     */
    @Override
    public void saveOrUpdateFormOperate(FormOperateLogsEntity formOperateLogsEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formOperateLogsEntity);
                return null;
            }
        });
    }

    /**
     * 保存评价
     * @param formEvaluateEntity
     */
    @Override
    public void saveOrUpdateFormEvaluate(FormEvaluateEntity formEvaluateEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formEvaluateEntity);
                return null;
            }
        });
    }

    /**
     * 保存修改标星工单
     * @param formMarksEntity
     */
    @Override
    public void saveOrUpdateMarkForm(FormMarksEntity formMarksEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formMarksEntity);
                return null;
            }
        });
    }

    /**
     * 删除标星
     * @param userId
     * @param formId
     */
    @Override
    public void delMarkForm(int userId, String formId) {
        HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.createQuery("delete from FormMarksEntity a where a.createBy=? and a.formId=?")
                        .setParameter(0,userId).setParameter(1,formId).executeUpdate();
                return null;
            }
        });
    }

    /**
     * 饼状图
     * @return
     */
    @Override
    public List<Map> selectPieChartByKind(Long date) {

        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createSQLQuery("SELECT c.label as name,IFNULL(d.count,0) FROM form_demand c LEFT JOIN (SELECT count(1) count, a.demand_type_code FROM form_info a WHERE a.create_date > "+date+" GROUP BY a.demand_type_code) d ON c.id = d.demand_type_code WHERE c.parent_id = 0 or c.parent_id is null")
                        .list();
            }
        });
    }

    /**
     * 柱状图
     * @return
     */
//    @Override
//    public List selectBarGraphByGroup() {
//        return (List<GraphDataDto>) HibernateTemplate.execute(new HibernateCallBack() {
//
//            @Override
//            public Object doInHibernate(Session ses) throws HibernateException {
//                return ses.createQuery("select demandTypeCode ,COUNT(1) as value from  FormInfoEntity a group by a.demandTypeCode having count(a.demandTypeCode)>0").list();
//            }
//        });
//    }

    @Override
    public Map selectDataCollection(Long date) {
        return (Map) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                DataCollectionDto dataCollectionDto = new DataCollectionDto();

                return ses.createSQLQuery("SELECT (select COUNT(1)  from  form_info a where a.create_date>"+date+") as newCount" +
                        ",(select COUNT(1)  from  form_info a where a.form_state=1 or a.form_state=2 and a.create_date>"+date+") as processCount\n" +
                        ", (select COUNT(1) from  form_info a where a.form_state=6 and a.create_date>"+date+") as endCount\n" +
                        ", (SELECT IFNULL(SUM(IFNULL(b.evaluate_level,10))/COUNT(1),0)  FROM form_info a join form_evaluate b on a.id=b.form_id WHERE a.form_state=6 and a.create_date>"+date+") as satisfaction").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();

            }
        });
    }


    /**
     * 查询工单列表
     * @param formConditionDto
     * @return
     */
    @Override
    public FormInfoPage selectFormListPage(FormConditionDto formConditionDto,Integer userId,String demandIds) {

        return (FormInfoPage) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                FormInfoPage formInfoPage = new FormInfoPage();
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.applicant_id AS applicantId,\n" +
                        "\tCASE\n" +
                        "WHEN f.real_name IS NULL THEN\n" +
                        "\tf.user_name\n" +
                        "ELSE\n" +
                        "\tf.real_name\n" +
                        "END AS applicantName,\n" +
                        " a.demand_type_code demandTypeCode,\n" +
                        " c.`label` AS demandTypeCodeName,\n" +
                        " a.demand_content demandContent,\n" +
                        " a.form_apply_time AS formApplyTime,\n" +
                        " a.form_state formState,\n" +
                        " a.handler_id handlerId,\n" +
                        " CASE\n" +
                        "WHEN g.real_name IS NULL THEN\n" +
                        "\tg.user_name\n" +
                        "ELSE\n" +
                        "\tg.real_name\n" +
                        "END AS handlerName,\n" +
                        " a.priority,\n" +
                        " h.name priorityName,\n" +
                        " a.relation_form_id relationFormId,\n" +
                        " a.unit_code unitCode,\n" +
                        " a.group_id groupId,\n" +
                        " b.`name` AS unitName,\n" +
                        " e.evaluate_level evaluateLevel,\n" +
                        " (\n" +
                        "\tSELECT\n" +
                        "\t\tCOUNT(d.id)\n" +
                        "\tFROM\n" +
                        "\t\tform_marks d\n" +
                        "\tWHERE\n" +
                        "\t\ta.id = d.form_id\n" +
                        "\tAND d.create_by = "+userId+"\n" +
                        ") AS isMark\n" +
                        "FROM\n" +
                        "\tform_info a\n" +
                        "LEFT JOIN form_company b ON a.unit_code = b.id\n" +
                        "LEFT JOIN form_demand c ON a.demand_type_code = c.id\n" +
                        "LEFT JOIN form_evaluate e ON a.id = e.form_id\n" +
                        "LEFT JOIN form_user f ON a.applicant_id = f.id\n" +
                        "LEFT JOIN form_user g ON a.handler_id = g.id\n" +
                        "LEFT JOIN form_priority h ON a.priority = h.id\n" +
                        "WHERE\n" +
                        "\t a.del_flag = 0");
                if (!StringUtils.isEmpty(formConditionDto.getId())) {
                    sql.append(" and a.id = ").append("'").append(formConditionDto.getId()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getApplicantId())) {
                    sql.append(" and a.applicant_id = ").append("'").append(formConditionDto.getApplicantId()).append("'");
                }
                if (!StringUtils.isEmpty(demandIds)) {
                    sql.append(" and a.demand_type_code in ").append("(").append(demandIds).append(")");
                }
                if (!StringUtils.isEmpty(formConditionDto.getFormState())) {
                    sql.append(" and a.form_state = ").append("'").append(formConditionDto.getFormState()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getUnitCode())) {
                    sql.append(" and a.unit_code = ").append("'").append(formConditionDto.getUnitCode()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getStartTime())) {
                    sql.append(" and a.create_date > ").append("'").append(formConditionDto.getStartTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getEndTime())) {
                    sql.append(" and a.create_date < ").append("'").append(formConditionDto.getEndTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getMark())) {
                    sql.append(" and ( SELECT COUNT(d.id) FROM form_marks d WHERE a.id = d.form_id AND d.create_by = ").append(userId).append(") =").append(formConditionDto.getMark()==false?0:1);
                }
                if (!StringUtils.isEmpty(formConditionDto.getPriority())) {
                    sql.append(" and  a.priority = ").append("'").append(formConditionDto.getPriority()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getGroupId())) {
                    sql.append(" and  a.group_id = ").append("'").append(formConditionDto.getGroupId()).append("'");
                }
                formInfoPage.setCount(ses.createSQLQuery(sql.toString()).list().size());

                formInfoPage.setFormInfoList(ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())
                                .setFirstResult((formConditionDto.getCurPage()-1)* formConditionDto.getItems()).setMaxResults(formConditionDto.getItems())
                                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                                .list()
                        );
                return formInfoPage;
            }
        });
    }

    /**
     * 根据公司员工信息
     * @param id
     * @return
     */
    @Override
    public List<Map> selectCompanyClients(Integer id) {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.name,\n" +
                        "\ta.phone,\n" +
                        "\ta.is_principal AS isPrincipal,\n" +
                        "\tb.id AS companyId,\n" +
                        "\tb.name AS companyName\n" +
                        "FROM\n" +
                        "\tform_client a\n" +
                        "LEFT JOIN form_company b ON a.company_id = b.id\n" +
                        "WHERE\n" +
                        "\ta.company_id = "+id+"");
                return  ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())

                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    /**
     * 查询公司列表
     * @return
     */
    @Override
    public List<Map> selectCompanyList() {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormCompanyEntity ").list();
            }
        });
    }

    /**
     * 报表
     * @param userId
     * @param formConditionDto
     * @return
     */
    @Override
    public List<Map> selectFormListExcel(Integer userId , FormConditionDto formConditionDto) {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.applicant_name applicantName,\n" +
                        "\ta.demand_type_code demandTypeCode,\n" +
                        "\ta.demand_content demandContent,\n" +
                        //"\ta.handler_name handlerName,\n" +
                        "\tc.start_time startTime,\n" +
                        "\tc.end_time endTime,\n" +
                        "\ta.form_state formState,\n" +
                        "\tb.evaluate_level evaluateLevel\n" +
                        "FROM\n" +
                        "\tform_info a\n" +
                        "LEFT JOIN form_evaluate b ON a.id = b.form_id\n" +
                        "LEFT JOIN form_operate_logs c ON c.form_id = a.id");
                if (!StringUtils.isEmpty(formConditionDto.getId())) {
                    sql.append(" and a.id = ").append("'").append(formConditionDto.getId()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getApplicantId())) {
                    sql.append(" and a.applicant_id = ").append("'").append(formConditionDto.getApplicantId()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getDemandTypeCode())) {
                    sql.append(" and a.demand_type_code = ").append("'").append(formConditionDto.getDemandTypeCode()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getFormState())) {
                    sql.append(" and a.form_state = ").append("'").append(formConditionDto.getFormState()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getUnitCode())) {
                    sql.append(" and a.unit_code = ").append("'").append(formConditionDto.getUnitCode()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getStartTime())) {
                    sql.append(" and a.create_date > ").append("'").append(formConditionDto.getStartTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getEndTime())) {
                    sql.append(" and a.create_date < ").append("'").append(formConditionDto.getEndTime()).append("'");
                }
                if (!StringUtils.isEmpty(formConditionDto.getMark())) {
                    sql.append(" and ( SELECT COUNT(d.id) FROM form_marks d WHERE a.id = d.form_id AND d.create_by = ").append(userId).append(") =").append(formConditionDto.getMark()==false?0:1);
                }

                return ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())
                        .addScalar("id",StandardBasicTypes.STRING)
                        .addScalar("applicantName",StandardBasicTypes.STRING)
                        .addScalar("demandTypeCode",StandardBasicTypes.STRING)
                        .addScalar("demandContent",StandardBasicTypes.STRING)
                        //.addScalar("handlerName",StandardBasicTypes.STRING)
                        .addScalar("startTime",StandardBasicTypes.DATE)
                        .addScalar("endTime",StandardBasicTypes.DATE)
                        .addScalar("formState",StandardBasicTypes.INTEGER)
                        .addScalar("evaluateLevel",StandardBasicTypes.INTEGER)
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();


            }
        });
    }



    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @Override
    public FormUserEntity selectUserById(Integer userId) {
        return (FormUserEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormUserEntity a where a.id=?")
                        .setParameter(0, userId).uniqueResult();
            }
        });
    }

    /**
     * 保存更新用户信息
     * @param formUserEntity
     */
    @Override
    public void saveOrUpdateUser(FormUserEntity formUserEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formUserEntity);
                return null;
            }
        });
    }

    /**
     * 查询工单状态
     * @return
     */
    @Override
    public List<Map> selectFormStateList() {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("SELECT a.id,a.name from form_state a");
                return  ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    /**
     * 查询申请人
     * @return
     */
    @Override
    public List<Map> selectApplicantList() {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("SELECT DISTINCT a.applicant_id as applicantId, CASE WHEN b.real_name is null THEN b.user_name ELSE b.real_name END as applicantName from form_info a LEFT JOIN form_user b  on a.applicant_id=b.id");
                return  ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    /**
     * 查询需求
     * @return
     */
    @Override
    public List<Map> selectDemandList() {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {

                StringBuffer sql = new StringBuffer("select a.id,a.label from form_demand a WHERE a.parent_id=0");
                return  ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    /**
     * 审核记录查询
     * @param formId
     * @return
     */
    @Override
    public List<Map> selectFormAuditMapById(String formId) {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "\ta.id,\n" +
                        "\ta.form_id formId,\n" +
                        "\ta.pass,\n" +
                        "\ta.content,\n" +
                        "\ta.create_by createBy,\n" +
                        "\tCASE\n" +
                        "WHEN b.real_name IS NULL THEN\n" +
                        "\tb.user_name\n" +
                        "ELSE\n" +
                        "\tb.real_name\n" +
                        "END AS createName,\n" +
                        " a.create_date createDate,\n" +
                        " a.grade\n" +
                        "FROM\n" +
                        "\tform_audit a\n" +
                        "LEFT JOIN form_user b ON a.create_by = b.id\n" +
                        "WHERE\n" +
                        "\ta.form_id = "+formId+"");
                return  ses.createSQLQuery(sql.append(" order by a.create_date desc").toString())

                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    /**
     * 优先级
     * @return
     */
    @Override
    public List<Map> selectPriorityList() {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("select a.id,a.name from form_priority a");
                return  ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    @Override
    public FormPriorityEntity selectPriorityById(Integer id) {
        return (FormPriorityEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormPriorityEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }

    /**
     * 升级记录
     * @param formUpgradeLogsEntity
     */
    @Override
    public void saveOrUpdateUpgrade(FormUpgradeLogsEntity formUpgradeLogsEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formUpgradeLogsEntity);
                return null;
            }
        });
    }

    /**
     * 需求树
     * @param parentId
     * @return
     */
    @Override
    public List<Map> selectDemandByParent(Integer parentId) {
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("select a.id,a.label,a.parent_id parentId from form_demand a where a.parent_id="+parentId+"");
                return  ses.createSQLQuery(sql.toString())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                        .list();
            }
        });
    }

    /**
     * 需求
     * @param id
     * @return
     */
    @Override
    public FormDemandEntity selectDemandById(Integer id) {
        return (FormDemandEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormDemandEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }

    /**
     * 审核记录
     * @param formAuditEntity
     */
    @Override
    public void saveOrUpdateAudit(FormAuditEntity formAuditEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formAuditEntity);
                return null;
            }
        });
    }

    @Override
    public List<Object[]> groupByCycle(CycleType cycle, Long date,FormState formState) {
        return (List<Object[]>) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer();
                switch (cycle) {
                    case DAY:
                        sql.append( "select DAY(FROM_UNIXTIME(a.create_date/1000)),HOUR(FROM_UNIXTIME(a.create_date/1000)),COUNT(a.id) "
                                + "from form_info a where a.create_date>? ");
                        if (!StringUtils.isEmpty(formState)) {
                            if (FormState.INHAND.equals(formState)||FormState.UPGRADE.equals(formState)){
                                sql.append(" and a.form_state in (1,2)");
                            }else if(FormState.END.equals(formState)){
                                sql.append(" and a.form_state =6");
                            }

                        }
                        sql.append( " group by DAY(FROM_UNIXTIME(a.create_date/1000)),HOUR(FROM_UNIXTIME(a.create_date/1000))");
                        break;
                    case WEEK:
                    case TWO_WEEKS:
                    case MONTH:
                        sql.append( "select MONTH(FROM_UNIXTIME(a.create_date/1000)),DAY(FROM_UNIXTIME(a.create_date/1000)),COUNT(a.id) "
                                + "from form_info a where a.create_date>? ");
                        if (!StringUtils.isEmpty(formState)) {
                            if (FormState.INHAND.equals(formState)||FormState.UPGRADE.equals(formState)){
                                sql.append(" and a.form_state in (1,2)");
                            }else if(FormState.END.equals(formState)){
                                sql.append(" and a.form_state =6");
                            }

                        }
                        sql.append( " group by MONTH(FROM_UNIXTIME(a.create_date/1000)),DAY(FROM_UNIXTIME(a.create_date/1000))");
                        break;
                    case TWO_MONTHS:
                        sql.append("select MONTH(FROM_UNIXTIME(a.create_date/1000)),WEEK(FROM_UNIXTIME(a.create_date/1000)),COUNT(a.id) "
                                + "from form_info a where a.create_date>? ");
                        if (!StringUtils.isEmpty(formState)) {
                            if (FormState.INHAND.equals(formState)||FormState.UPGRADE.equals(formState)){
                                sql.append(" and a.form_state in (1,2)");
                            }else if(FormState.END.equals(formState)){
                                sql.append(" and a.form_state =6");
                            }

                        }
                                sql.append( " group by MONTH(FROM_UNIXTIME(a.create_date/1000)),WEEK(FROM_UNIXTIME(a.create_date/1000))");
                        break;
                    case HALF_YEAR:
                        sql.append("select YEAR(FROM_UNIXTIME(a.create_date/1000)),MONTH(FROM_UNIXTIME(a.create_date/1000)),WEEK(FROM_UNIXTIME(a.create_date/1000)),COUNT(a.id) "
                                + "from form_info a where a.create_date>? ");
                        if (!StringUtils.isEmpty(formState)) {
                            if (FormState.INHAND.equals(formState)||FormState.UPGRADE.equals(formState)){
                                sql.append(" and a.form_state in (1,2)");
                            }else if(FormState.END.equals(formState)){
                                sql.append(" and a.form_state =6");
                            }

                        }
                                sql.append(" group by YEAR(FROM_UNIXTIME(a.create_date/1000)),MONTH(FROM_UNIXTIME(a.create_date/1000)),WEEK(FROM_UNIXTIME(a.create_date/1000))");
                        break;
                    case YEAR:
                        sql.append("select YEAR(FROM_UNIXTIME(a.create_date/1000)),MONTH(FROM_UNIXTIME(a.create_date/1000)),COUNT(a.id) "
                                + "from form_info a where a.create_date>? ");
                        if (!StringUtils.isEmpty(formState)) {
                            if (FormState.INHAND.equals(formState)||FormState.UPGRADE.equals(formState)){
                                sql.append(" and a.form_state in (1,2)");
                            }else if(FormState.END.equals(formState)){
                                sql.append(" and a.form_state =6");
                            }

                        }
                                sql.append(" group by YEAR(FROM_UNIXTIME(a.create_date/1000)),MONTH(FROM_UNIXTIME(a.create_date/1000))");
                        break;
                    default:
                        break;
                }

                return ses.createSQLQuery(sql.toString()).setParameter(0, date).list();
            }
        });
    }

    /**
     * 工单状态
     * @param id
     * @return
     */
    @Override
    public FormStateEntity selectFormStateById(Integer id) {
        return (FormStateEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormStateEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }

    @Override
    public FormUserEntity selectFormUser(Integer id) {
        return (FormUserEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormUserEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }

    @Override
    public FormClientEntity selectCompanyById(Integer id) {
        return (FormClientEntity) HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                return ses.createQuery("from FormClientEntity a where a.id=?")
                        .setParameter(0, id).uniqueResult();
            }
        });
    }

    @Override
    public List<Map> selectGroupMeirt(FormState formState,Long startTime,Long endTime,Integer demandCode){
        return (List<Map>) HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer sql = new StringBuffer("select COUNT(a.id) as num, a.group_id as groupId from form_info a where 1=1 ");
                if (!StringUtils.isEmpty(formState)) {
                    if (FormState.INHAND.equals(formState)||FormState.UPGRADE.equals(formState)){
                        sql.append(" and a.form_state in (1,2)");
                    }else if(FormState.END.equals(formState)){
                        sql.append(" and a.form_state =6");
                    }
                }
                if(!StringUtils.isEmpty(startTime)) {
                    sql.append(" and a.create_date>"+startTime+"");
                }
                if(!StringUtils.isEmpty(endTime)) {
                    sql.append(" and a.create_date<"+endTime+"");
                }
                if(!StringUtils.isEmpty(demandCode)) {
                    sql.append(" and a.demand_type_code<"+demandCode+"");
                }
                sql.append( " group by a.group_id ORDER BY a.group_id");
                return ses.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            }
        });
    }

    @Override
    public Map selectTotalMeirt(Long startTime,Long endTime,Integer demandCode){
        return (Map) HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                StringBuffer condition = new StringBuffer();

                if(!StringUtils.isEmpty(startTime)) {
                    condition.append(" and a.create_date>"+startTime+"");
                }
                if(!StringUtils.isEmpty(endTime)) {
                    condition.append(" and a.create_date<"+endTime+"");
                }
                if(!StringUtils.isEmpty(demandCode)) {
                    condition.append(" and a.demand_type_code="+demandCode+"");
                }
                StringBuffer sql = new StringBuffer("SELECT\n" +
                        "(SELECT COUNT(a.id) FROM  form_info a where 1=1 "+condition.toString()+") as totalNum,\n" +
                        "(SELECT COUNT(a.id) FROM  form_info a WHERE a.form_state in (1,2) "+condition.toString()+") as inHandNum,\n" +
                        "(SELECT COUNT(a.id) FROM  form_info a where a.form_state =6 "+condition.toString()+") as endNum,\n" +
                        "(SELECT SUM(b.evaluate_level)/COUNT(a.id) FROM  form_info a LEFT JOIN form_evaluate b on a.id=b.form_id where a.form_state =6 "+condition.toString()+") as satisfaction");

                return ses.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
            }
        });
    }

    /**
     * 个人绩效
     * @param meritConditionDto
     * @return
     */
    @Override
    public CommonPage selectPersonMeirt(MeritConditionDto meritConditionDto) {
        return (CommonPage) HibernateTemplate.execute(new HibernateCallBack() {
            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                CommonPage commonPage = new CommonPage();

                StringBuffer condition = new StringBuffer();
                if(!StringUtils.isEmpty(meritConditionDto.getStartTime())) {
                    condition.append(" and create_date>"+meritConditionDto.getStartTime()+"");
                }
                if(!StringUtils.isEmpty(meritConditionDto.getEndTime().toString())) {
                    condition.append(" and create_date<"+meritConditionDto.getEndTime()+"");
                }
                if(!StringUtils.isEmpty(meritConditionDto.getDemandCode())) {
                    condition.append(" and demand_type_code="+meritConditionDto.getDemandCode()+"");
                }
                if(!StringUtils.isEmpty(meritConditionDto.getGroupId())) {
                    condition.append(" and group_id="+meritConditionDto.getGroupId()+"");
                }
                StringBuffer sql = new StringBuffer("SELECT u.real_name,\n" +
                        "                        (SELECT COUNT(a.id) FROM  form_info a where 1=1 AND a.handler_id=c.handler_id AND a.group_id=c.group_id "+condition.toString()+") as totalNum,\n" +
                        "                        (SELECT COUNT(a.id) FROM  form_info a WHERE a.form_state in (1,2) AND a.handler_id=c.handler_id  AND a.group_id=c.group_id "+condition.toString()+") as inHandNum,\n" +
                        "                        (SELECT COUNT(a.id) FROM  form_info a where a.form_state =6 AND a.handler_id=c.handler_id AND a.group_id=c.group_id  "+condition.toString()+") as endNum,\n" +
                        "                        IFNULL((SELECT SUM(b.evaluate_level)/COUNT(a.id) FROM  form_info a LEFT JOIN form_evaluate b on a.id=b.form_id where a.form_state =6 AND a.handler_id=c.handler_id AND a.group_id=c.group_id "+condition.toString()+"),0 )as satisfaction \n" +
                        "from form_info c LEFT JOIN form_user u on c.handler_id=u.id where 1=1 "+condition.toString()+" GROUP BY c.handler_id ,group_id");
                commonPage.setCount(ses.createSQLQuery(sql.toString()).list().size());
                 commonPage.setList(ses.createSQLQuery(sql.toString())
                        .setFirstResult((meritConditionDto.getCurPage()-1)* meritConditionDto.getItems()).setMaxResults(meritConditionDto.getItems())
                        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
                return commonPage;
            }
        });
    }

    @Override
    public void saveOrUpdateFormAuditPerson(FormAuditPersonEntity formAuditPersonEntity) {
        HibernateTemplate.execute(new HibernateCallBack() {

            @Override
            public Object doInHibernate(Session ses) throws HibernateException {
                ses.saveOrUpdate(formAuditPersonEntity);
                return null;
            }
        });
    }

}

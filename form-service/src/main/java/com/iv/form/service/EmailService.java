package com.iv.form.service;

import com.iv.common.enumeration.FormSendType;
import com.iv.common.util.spring.DateUtil;
import com.iv.dto.FormInfoEntityDto;
import com.iv.dto.FormInfoTemplate;
import com.iv.form.dao.IFormDao;
import com.iv.form.dao.IFormOptDao;
import com.iv.form.dao.impl.IFormDaoImpl;
import com.iv.form.dao.impl.IFormOptDaoImpl;
import com.iv.form.entity.FormClientEntity;
import com.iv.form.entity.FormInfoEntity;
import com.iv.form.entity.FormUserEntity;
import com.iv.form.feign.clients.EmailServiceClient;
import com.iv.form.feign.clients.WechatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liangk
 * @create 2018年 06月 11日
 **/
@Service
public class EmailService {

    private static final IFormDao FORM_DAO = IFormDaoImpl.getInstance();

    private static final IFormOptDao FORM_OPT_DAO = IFormOptDaoImpl.getInstance();
    @Autowired
    private EmailServiceClient emailServiceClient;
    @Autowired
    private WechatServiceClient wechatServiceClient;

    public Boolean sendEmail(FormInfoEntity formInfoEntity){

        Boolean flag=true;
        try {
            FormInfoTemplate formInfoTemplate=new FormInfoTemplate();
            FormInfoEntityDto formInfoEntityDto=new FormInfoEntityDto();
            formInfoEntityDto.setId(formInfoEntity.getId());
            formInfoEntityDto.setDemandContent(formInfoEntity.getDemandContent());

            String demandName = FORM_DAO.selectDemandById(formInfoEntity.getDemandTypeCode()).getLabel();
            formInfoEntityDto.setDemandTypeCode(demandName);
            //formInfoEntityDto.setFormApplyTime(DateUtil.timestampToString(formInfoEntity.getFormApplyTime()));
            //字段问题
            formInfoEntityDto.setFormExpectEndTime(DateUtil.timestampToString(formInfoEntity.getFormApplyTime()));
            String formStateName = FORM_DAO.selectFormStateById(formInfoEntity.getFormState()).getName();
            formInfoEntityDto.setFormState(formStateName);
            FormUserEntity handler = FORM_DAO.selectFormUser(formInfoEntity.getHandlerId());
            formInfoEntityDto.setHandler(handler.getRealName());
            String priorityName = FORM_DAO.selectPriorityById(formInfoEntity.getPriority()).getName();
            formInfoEntityDto.setPriority(priorityName);
            String companyName = FORM_OPT_DAO.selectCompanyById(formInfoEntity.getUnitCode()).getName();
            FormClientEntity formClientEntity = FORM_OPT_DAO.selectClientById(formInfoEntity.getApplicantId());
            formInfoEntityDto.setUnitCode(companyName+" "+formClientEntity.getName());
            formInfoEntityDto.setTel(formClientEntity.getPhone());

            formInfoTemplate.setFormInfoEntityDto(formInfoEntityDto);
            formInfoTemplate.setFormSendType(FormSendType.TOBETREATED);
            formInfoTemplate.setToEmails(new String[]{handler.getEmail()});
            //formInfoTemplate.setToEmails(new String[]{"liangk@inno-view.cn","494408452@qq.com"});
            emailServiceClient.formToMail(formInfoTemplate);
        } catch (Exception e) {
             flag=false;
        }
        return flag;
    }

}

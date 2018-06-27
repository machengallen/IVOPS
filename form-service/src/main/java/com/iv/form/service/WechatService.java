package com.iv.form.service;

import com.iv.dto.FormContent;
import com.iv.dto.Note;
import com.iv.dto.TemplateFormMessageDto;
import com.iv.form.dao.IFormDao;
import com.iv.form.dao.IFormOptDao;
import com.iv.form.dao.impl.IFormDaoImpl;
import com.iv.form.dao.impl.IFormOptDaoImpl;
import com.iv.form.entity.FormInfoEntity;
import com.iv.form.feign.clients.WechatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liangk
 * @create 2018年 06月 11日
 **/
@Service
public class WechatService {

    private static final IFormDao FORM_DAO = IFormDaoImpl.getInstance();
    private static final IFormOptDao FORM_OPT_DAO = IFormOptDaoImpl.getInstance();

    @Autowired
    private WechatServiceClient wechatServiceClient;

    @Value("${iv.wechat.redirectUri}")
    private String redirectUri;

    public Boolean sendWechat(FormInfoEntity formInfoEntity,String code){
        Boolean flag=true;
        try {
            TemplateFormMessageDto templateFormMessageDto = new TemplateFormMessageDto();
            FormContent formContent = new FormContent();
            String companyName = FORM_OPT_DAO.selectCompanyById(formInfoEntity.getUnitCode()).getName();
            String demandName = FORM_DAO.selectDemandById(formInfoEntity.getDemandTypeCode()).getLabel();
            String priorityName = FORM_DAO.selectPriorityById(formInfoEntity.getPriority()).getName();
            String formStateName = FORM_DAO.selectFormStateById(formInfoEntity.getFormState()).getName();
            formContent.setKeyword1(new Note(companyName,"#000000"));
            formContent.setKeyword2(new Note(demandName,"#000000"));
            formContent.setKeyword3(new Note(priorityName,"#000000"));
            formContent.setKeyword4(new Note(formStateName,"#000000"));
            formContent.setFirst(new Note(formInfoEntity.getDemandContent(),"#000000"));
            formContent.setRemark(new Note("请相关运维人员尽快处理","#FF0000"));
            templateFormMessageDto.setData(formContent);
            List<Integer> userIds = new ArrayList<>();
            userIds.add(formInfoEntity.getHandlerId());
            templateFormMessageDto.setUserIds(userIds);
            templateFormMessageDto.setRedirect_uri(redirectUri+"$"+code);
            wechatServiceClient.SendFormWeChatInfo(templateFormMessageDto);
        } catch (Exception e) {
            flag=false;
        }
        return flag;
    }
}

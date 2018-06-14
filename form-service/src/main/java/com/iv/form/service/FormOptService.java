package com.iv.form.service;

import com.iv.constant.BusException;
import com.iv.constant.ErrorMsg;
import com.iv.dto.ClientConditionDto;
import com.iv.dto.CommonPage;
import com.iv.dto.FormClientDto;
import com.iv.dto.FormCompanyDto;
import com.iv.form.dao.IFormOptDao;
import com.iv.form.dao.impl.IFormOptDaoImpl;
import com.iv.form.dto.FormDemandDto;
import com.iv.form.entity.FormClientEntity;
import com.iv.form.entity.FormCompanyEntity;
import com.iv.form.entity.FormDemandEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liangk
 * @create 2018年 06月 01日
 **/
@Service
public class FormOptService {

    private static final IFormOptDao FORM_OPT_DAO = IFormOptDaoImpl.getInstance();

    /**
     * 公司列表
     * @param clientConditionDto
     * @return
     */
    public CommonPage selectCompanyPage(ClientConditionDto clientConditionDto) {
        clientConditionDto.setCurPage(clientConditionDto.getCurPage()==0?1:clientConditionDto.getCurPage());
        clientConditionDto.setItems(clientConditionDto.getItems()==0?10:clientConditionDto.getItems());

        CommonPage commonPage=FORM_OPT_DAO.selectCompanyPage(clientConditionDto);

        return commonPage;
    }

    /**
     * 客户列表
     * @param clientConditionDto
     * @return
     */
    public CommonPage selectClientList(ClientConditionDto clientConditionDto) {
        clientConditionDto.setCurPage(clientConditionDto.getCurPage()==0?1:clientConditionDto.getCurPage());
        clientConditionDto.setItems(clientConditionDto.getItems()==0?10:clientConditionDto.getItems());
        CommonPage commonPage=FORM_OPT_DAO.selectClientPage(clientConditionDto);

        return commonPage;
    }



    /**
     * 保存更新公司信息
     * @param userId
     * @param formCompanyDto
     */
    public FormCompanyEntity saveOrUpdateCompany(int userId, FormCompanyDto formCompanyDto)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        FormCompanyEntity formCompanyEntity = new FormCompanyEntity();
        if (StringUtils.isEmpty(formCompanyDto.getId())){//新增

            formCompanyEntity.setName(formCompanyDto.getName());
            formCompanyEntity.setCreateBy(userId);
            formCompanyEntity.setCreateDate(System.currentTimeMillis());
        }else{//更新

            formCompanyEntity =FORM_OPT_DAO.selectCompanyById(formCompanyDto.getId());
            formCompanyEntity.setId(formCompanyDto.getId());
            formCompanyEntity.setName(formCompanyDto.getName());
            formCompanyEntity.setUpdateBy(userId);
            formCompanyEntity.setUpdateDate(System.currentTimeMillis());
        }

        FORM_OPT_DAO.saveOrUpdateCompany(formCompanyEntity);
        return formCompanyEntity;

    }

    /**
     * 保存客户信息
     * @param userId
     * @param formClientDto
     * @return
     */
    public FormClientEntity saveOrUpdateClient(int userId, FormClientDto formClientDto)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }
        FormClientEntity formClientEntity = new FormClientEntity();


        if (StringUtils.isEmpty(formClientDto.getId())){//新增
            //formClientEntity.setId(formClientDto.getId());
            formClientEntity.setName(formClientDto.getName());
            formClientEntity.setPhone(formClientDto.getPhone());
            formClientEntity.setEmail(formClientDto.getEmail());
            formClientEntity.setPrincipal(formClientDto.getPrincipal());
            formClientEntity.setCompanyId(formClientDto.getCompanyId());
            formClientEntity.setCreateBy(userId);
            formClientEntity.setCreateDate(System.currentTimeMillis());
        }else{//更新
            formClientEntity = FORM_OPT_DAO.selectClientById(formClientDto.getId());

            formClientEntity.setId(formClientDto.getId());
            formClientEntity.setName(formClientDto.getName());
            formClientEntity.setPhone(formClientDto.getPhone());
            formClientEntity.setEmail(formClientDto.getEmail());
            formClientEntity.setPrincipal(formClientDto.getPrincipal());
            formClientEntity.setCompanyId(formClientDto.getCompanyId());
            formClientEntity.setUpdateBy(userId);
            formClientEntity.setUpdateDate(System.currentTimeMillis());
        }

        FORM_OPT_DAO.saveOrUpdateClient(formClientEntity);

        return formClientEntity;
    }

    /**
     * 批量删除客户
     * @param userId
     * @param clientIds
     */
    public void delClientList(int userId, String[] clientIds) {
        if (clientIds.length==0){
            return;
        }else{
            for (String clientId :clientIds){
                FORM_OPT_DAO.delClientById(Integer.parseInt(clientId));
            }
        }
    }

    /**
     * 批量删除公司
     * @param userId
     * @param companyIds
     */
    public void delCompanyList(int userId, String[] companyIds) {
        if (companyIds.length==0){
            return;
        }else{
            for (String companyId :companyIds){
                FORM_OPT_DAO.delCompanyById(Integer.parseInt(companyId));
            }
        }
    }



    /**
     * 需求树
     * @return
     */
    public List<FormDemandEntity> selectDemandTree() {
        List<FormDemandEntity> demands=FORM_OPT_DAO.selectDemandAll();
        return demands;
    }

    /**
     * 根据id查需求信息
     * @return
     */
    public Map selectDemandAndParentById(Integer id) {
        return FORM_OPT_DAO.selectDemandAndParentById(id);
    }


    /**
     * 保存需求
     * @param formDemandDto
     */
    public void saveOrUpdateDemand(FormDemandDto formDemandDto){
        FormDemandEntity formDemandEntity = new FormDemandEntity();
        if(!StringUtils.isEmpty(formDemandDto.getId())){
            formDemandEntity.setId(formDemandDto.getId());
        }
        formDemandEntity.setLabel(formDemandDto.getLabel());
        formDemandEntity.setContent(formDemandDto.getContent());
        FormDemandEntity entity = new FormDemandEntity();
        entity.setId(formDemandDto.getParentId());
        formDemandEntity.setParent(entity.getId()==null?null:entity);

        FORM_OPT_DAO.saveOrUpdateDemand(formDemandEntity);
    }

    /**
     * 删除目录
     * @param id
     */

    public void delDemand(Integer id){
        FormDemandEntity formDemandEntity = FORM_OPT_DAO.selectDemandAndChildrenById(id);

        delDemandById(formDemandEntity);

    }


    public  void delDemandById(FormDemandEntity formDemandEntity){
        //删除节点
        Set<FormDemandEntity> children = formDemandEntity.getChildren();
        Integer entityId = formDemandEntity.getId();
        if(children.size()!=0){
            //删除节点
            for(FormDemandEntity entity:children){
                delDemandById(entity);
            }
        }
        FORM_OPT_DAO.delDemandById(entityId);
    }


}

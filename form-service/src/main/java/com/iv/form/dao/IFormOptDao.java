package com.iv.form.dao;

import com.iv.dto.ClientConditionDto;
import com.iv.dto.CommonPage;
import com.iv.form.entity.FormClientEntity;
import com.iv.form.entity.FormCompanyEntity;
import com.iv.form.entity.FormDemandEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 06月 01日
 **/
public interface IFormOptDao {
    //公司分页
    CommonPage selectCompanyPage(ClientConditionDto clientConditionDto);

    //客户分页
    CommonPage selectClientPage(ClientConditionDto clientConditionDto);

    //保存公司信息
    void saveOrUpdateCompany(FormCompanyEntity formCompanyEntity);

    //报讯客户信息
    void saveOrUpdateClient(FormClientEntity formClientEntity);

    //批量删除客户
    void delClientById(Integer clientId);

    //批量删除公司
    void delCompanyById(Integer companyId);

    //根据公司Id查询公司信息
    FormCompanyEntity selectCompanyById(Integer id);

    //根据客户Id查询客户信息
    FormClientEntity selectClientById(Integer id);


    //查询所有需求
    List<FormDemandEntity> selectDemandAll();

    //根据Id查询需求查出父类Id
    Map selectDemandAndParentById(Integer id);

    //根据Id查询需求，查出子类信息
    FormDemandEntity selectDemandAndChildrenById(Integer id);

    //保存修改需求
    void saveOrUpdateDemand(FormDemandEntity formDemandEntity);

    //删除需求
    void delDemandById(Integer id);


}

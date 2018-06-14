package com.iv.service;

import com.iv.common.response.ResponseDto;
import com.iv.dto.ClientConditionDto;
import com.iv.dto.FormClientDto;
import com.iv.dto.FormCompanyDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 工单设置中心
 * @author liangk
 * @create 2018年 05月 22日
 **/
public interface IFormOptionsService {

    //保存修改公司信息
    @RequestMapping(value = "/saveOrUpdateCompany", method = RequestMethod.POST)
    ResponseDto saveOrUpdateCompany(@RequestParam("request") HttpServletRequest request, @RequestBody FormCompanyDto formCompanyDto);


    //保存客户信息
    @RequestMapping(value = "/saveOrUpdateClient", method = RequestMethod.POST)
    ResponseDto saveOrUpdateClient(@RequestParam("request") HttpServletRequest request, @RequestBody FormClientDto formClientDto);

    //查询公司列表
    @RequestMapping(value = "/select/companyList", method = RequestMethod.GET)
    ResponseDto selectCompanyList(@RequestBody ClientConditionDto clientConditionDto);

    //查询客户列表
    @RequestMapping(value = "/select/clientList", method = RequestMethod.GET)
    ResponseDto selectClientList(@RequestBody ClientConditionDto clientConditionDto);

    @RequestMapping(value = "/del/clientList", method = RequestMethod.GET)
    ResponseDto delClientList(@RequestParam("request") HttpServletRequest  request,@RequestParam("clientIds") String[] clientIds);

    @RequestMapping(value = "/del/companyList", method = RequestMethod.GET)
    ResponseDto delCompanyList(@RequestParam("request") HttpServletRequest  request,@RequestParam("companyIds") String[] companyIds);


}

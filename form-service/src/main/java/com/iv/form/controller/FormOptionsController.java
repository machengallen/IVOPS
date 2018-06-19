package com.iv.form.controller;

import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.constant.BusException;
import com.iv.constant.ErrorMsg;
import com.iv.dto.ClientConditionDto;
import com.iv.dto.CommonPage;
import com.iv.dto.FormClientDto;
import com.iv.dto.FormCompanyDto;
import com.iv.form.dto.FormDemandDto;
import com.iv.form.entity.FormClientEntity;
import com.iv.form.entity.FormCompanyEntity;
import com.iv.form.entity.FormDemandEntity;
import com.iv.form.service.FormActService;
import com.iv.form.service.FormOptService;
import com.iv.service.IFormOptionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 22日
 **/
@RestController
@Api(description = "工单设置中心Api")
public class FormOptionsController implements IFormOptionsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);


    @Autowired
    private FormOptService formOptService;
    @Autowired
    private FormActService formActService;


    @Override
    @ApiOperation(value="添加需改公司信息",notes = "90130")
    public ResponseDto saveOrUpdateCompany(HttpServletRequest request,@RequestBody FormCompanyDto formCompanyDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {

            FormCompanyEntity formCompanyEntity = formOptService.saveOrUpdateCompany(userId, formCompanyDto);
            dto.setData(formCompanyEntity);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString());
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="添加客户信息",notes = "90131")
    public ResponseDto saveOrUpdateClient(HttpServletRequest request,@RequestBody FormClientDto formClientDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {

            FormClientEntity formClientEntity = formOptService.saveOrUpdateClient(userId, formClientDto);
            dto.setData(formClientEntity);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;

    }

    /**
     * 查询公司列表
     * @param clientConditionDto
     * @return
     */
    @Override
    @ApiOperation(value="查询公司列表",notes = "90132")
    public ResponseDto selectCompanyList(ClientConditionDto clientConditionDto) {
        ResponseDto dto = new ResponseDto();
        try {
            CommonPage commonPage = formOptService.selectCompanyPage(clientConditionDto);
            dto.setData(commonPage);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        }  catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    /**
     * 查询客户列表
     * @param clientConditionDto
     * @return
     */
    @Override
    @ApiOperation(value="查询客户列表",notes = "90133")
    public ResponseDto selectClientList(ClientConditionDto clientConditionDto) {
        ResponseDto dto = new ResponseDto();

        try {

            CommonPage commonPage =  formOptService.selectClientList(clientConditionDto);
            dto.setData(commonPage);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    /**
     * 批量删除客户
     * @param request
     * @param clientIds
     * @return
     */
    @Override
    @ApiOperation(value="批量删除客户",notes = "90134")
    public ResponseDto delClientList(HttpServletRequest request, String[] clientIds) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            formOptService.delClientList(userId,clientIds);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }

    /**
     * 批量删除公司
     * @param request
     * @param companyIds
     * @return
     */
    @Override
    @ApiOperation(value="批量删除公司",notes = "90135")
    public ResponseDto delCompanyList(HttpServletRequest request, String[] companyIds) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            formOptService.delCompanyList(userId,companyIds);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        }  catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }


    /**
     * 需求树查询
     * @return
     */
    @ApiOperation(value="服务目录树",notes = "90125")
    @RequestMapping(value = "select/demandTree" ,method = RequestMethod.GET)
    public ResponseDto selectDemandTree() {
        ResponseDto dto = new ResponseDto();
        try {
            List<FormDemandEntity> formDemandEntities = formOptService.selectDemandTree();
            dto.setData(formDemandEntities);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }



    @ApiOperation(value="根据Id查服务目录信息及父Id",notes = "90136")
    @RequestMapping(value = "/select/demandAndParentById" ,method = RequestMethod.GET)
    public ResponseDto selectDemandAndParentById(Integer id) {
        ResponseDto dto = new ResponseDto();
        try {
            Map demandInfo = formOptService.selectDemandAndParentById(id);
            dto.setData(demandInfo);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @ApiOperation(value="保存服务目录",notes = "90137")
    @RequestMapping(value = "/saveOrUpdateDemand" ,method = RequestMethod.POST)
    public ResponseDto saveOrUpdateDemand(@RequestBody FormDemandDto formDemandDto) {
        ResponseDto dto = new ResponseDto();
        try {
            formOptService.saveOrUpdateDemand(formDemandDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_INFO_SAVE_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_INFO_SAVE_FAILED);
        }
        return dto;
    }

    @ApiOperation(value="删除服务目录",notes = "90138")
    @RequestMapping(value = "/del/demand" ,method = RequestMethod.GET)
    public ResponseDto delDemand(@RequestParam("id")Integer id) {
        ResponseDto dto = new ResponseDto();
        try {
            formOptService.delDemand(id);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }

    @ApiOperation(value="上传文件",notes = "90139")
    @RequestMapping(value = "/uploadFile" ,method = RequestMethod.POST)
    public ResponseDto uploadFile(@RequestParam("file")MultipartFile file) {
        ResponseDto dto = new ResponseDto();
        try {

            Map map = formOptService.uploadFile(file);
            dto.setData(map);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.UPLOAD_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.UPLOAD_FAILED);
        }
        return dto;
    }

    @ApiOperation(value="下载文件",notes = "90140")
    @RequestMapping(value = "/downloadFile" ,method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @RequestParam("id")Integer id) {
        ResponseDto dto = new ResponseDto();
        try {
            formOptService.download(response,id);

        } catch(Exception e) {
            LOGGER.info(ErrorMsg.UPLOAD_FILE_HAS_DEL.toString(),e);
            dto.setErrorMsg(ErrorMsg.UPLOAD_FILE_HAS_DEL);
        }
    }

    @ApiOperation(value="删除文件",notes = "90141")
    @RequestMapping(value = "/del/file" ,method = RequestMethod.GET)
    public ResponseDto delFile(@RequestParam("id")Integer id) {
        ResponseDto dto = new ResponseDto();
        try {
            formOptService.delFile(id);
            dto.setErrorMsg(ErrorMsg.OK);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }

    @ApiOperation(value="测试")
    @RequestMapping(value = "test" ,method = RequestMethod.POST)
    @ApiIgnore
    public ResponseDto test() {
        ResponseDto dto = new ResponseDto();
        try {
            formActService.test();

            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        }  catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }

}

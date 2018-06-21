package com.iv.form.service;

import com.iv.common.response.ResponseDto;
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
import com.iv.form.entity.FormFileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author liangk
 * @create 2018年 06月 01日
 **/
@Service
public class FormOptService {

    private static final IFormOptDao FORM_OPT_DAO = IFormOptDaoImpl.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(FormOptService.class);
    @Value("${iv.upload.savePath}")
    private String savePath;



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


    /**
     * 上传
     * @param file
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public Map uploadFile(MultipartFile file) throws ServletException, IOException {
        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = this.savePath;
        System.out.println(file.getContentType());
        File tmpFile = new File(savePath);
        if (!tmpFile.exists()) {
            //创建临时目录
            tmpFile.mkdir();
        }

        if (!file.isEmpty()) {
            //如果fileitem中封装的是上传文件
            //得到上传的文件名称，
            String filename = file.getOriginalFilename();
            System.out.println(filename);
            if (filename == null || filename.trim().equals("")) {
                return null;
            }
            //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
            //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
            filename = filename.substring(filename.lastIndexOf("\\") + 1);
            //得到文件保存的名称
            String saveFilename = makeFileName(filename);
            //得到文件的保存目录
            String realSavePath = makePath(saveFilename, savePath);
            File realSaveFile = new File(realSavePath);
            if (!realSaveFile.exists()) {

                realSaveFile.mkdir();
            }

            file.transferTo(new File(realSavePath + "\\" + saveFilename));

            FormFileEntity formFileEntity = new FormFileEntity();
            formFileEntity.setName(filename);
            formFileEntity.setPath(realSavePath);
            formFileEntity.setRealName(saveFilename);
            FORM_OPT_DAO.saveOrUpdateFormFile(formFileEntity);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",formFileEntity.getId());
            map.put("name",formFileEntity.getName());
            map.put("url","");
            return map;

        }
        return null;
    }

    /**
     * 生成上传文件的文件名
     * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
     * @param filename 文件的原始名称
     * @return uuid+"_"+文件的原始名称
     */
    private String makeFileName(String filename){  //2.jpg
        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        String s = filename.substring(filename.lastIndexOf(".") + 1);
        return UUID.randomUUID().toString() +"."+s;
    }

    /**
     * 为防止一个目录下面出现太多文件，要使用年月
     * @param filename 文件名，要根据文件名生成存储目录
     * @param savePath 文件存储路径
     * @return 新的存储目录
     */
    private String makePath(String filename,String savePath){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        //构造新的保存目录
        String dir = savePath + "\\" + year + "\\" + month;  //upload\2\3  upload\3\5
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }
        return dir;
    }


    /**
     * 下载
     * @param response
     */
    public void download(HttpServletRequest request, HttpServletResponse response, Integer id) {

        FormFileEntity formFileEntity=FORM_OPT_DAO.selectFormFile(id);

        String fileName = formFileEntity.getName();
        String characterEncoding = request.getCharacterEncoding();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding(StringUtils.isEmpty(characterEncoding)?"UTF-8":characterEncoding);

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(formFileEntity.getPath()+"\\"+formFileEntity.getRealName())));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public ResponseDto download(int id, HttpServletResponse response) {

        FormFileEntity formFileEntity=FORM_OPT_DAO.selectFormFile(id);
        File file = new File(formFileEntity.getPath()+"\\"+formFileEntity.getRealName());
        String fileName = formFileEntity.getName();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            LOGGER.error("IO异常", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOGGER.error("IO异常", e);
                }
            }
        }
        return null;
    }


    /**
     * 删除文件
     * @param id
     */
    public void delFile(Integer id) {
        FORM_OPT_DAO.delFile(id);
    }
}

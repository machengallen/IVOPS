package com.iv.form.service;


import com.iv.form.dao.IFormDao;
import com.iv.form.dao.impl.IFormDaoImpl;
import com.iv.form.entity.FormInfoEntity;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 工作流服务
 * @author liangk
 * @create 2018年 05月 11日
 **/
@Service
public class FormActService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    private static final IFormDao FORM_DAO = IFormDaoImpl.getInstance();



    /**
     * 开启流程
     * @param variables
     * @param userId
     * @param userName
     * @return
     */
    public String startProcess(Map<String, Object> variables, String businessKey, Integer userId , String userName){

        identityService.setAuthenticatedUserId(String.valueOf(userId));
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("formDefaultProcess",
                businessKey,variables);
        System.out.println("taskId:"+instance.getId());
        runtimeService.addUserIdentityLink(instance.getId(), String.valueOf(userId), userName);
        return instance.getId();
    }

    /**
     * 获取加入项目组的审批人
     *
     * @param execution
     * @return
     */
    public Integer findCandidateUsersJoin(DelegateExecution execution) {

        String processBusinessKey = execution.getProcessBusinessKey();
        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(processBusinessKey);

        return formInfoEntity.getHandlerId();
    }



    /**
     * 完成请假申请
     */

    public void testQingjia(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("104"); //查看act_ru_task表
    }

    /**
     * 小明学习的班主任小毛查询当前正在执行任务
     */

    public void testQueryTask(){
        //下面代码中的小毛，就是我们之前设计那个流程图中添加的班主任内容
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("小毛")
                .list();
        for (Task task : tasks) {
            System.out.println(task.getName());
        }
    }

    /**
     * 班主任小毛完成任务
     */

    public void testFinishTask_manager(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getTaskService()
                .complete("202"); //查看act_ru_task数据表
    }

    /*
     * 教务处的大毛完成的任务
     */

    public void testFinishTask_Boss(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("302");  //查看act_ru_task数据表
    }


    public void userTasksEnterpriseRegis(int userId, int first, int max) {
//        Map<String, Object> variables1 = new HashMap<String, Object>();
//        variables1.put("userId", userId);
//
//
//        ProcessInstance instance = runtimeService.startProcessInstanceByKey("formDefaultProcess",
//                variables1);
//        System.out.println("taskId:"+instance.getId());
//        runtimeService.addUserIdentityLink(instance.getId(), String.valueOf(userId), "测试lk");
        ProcessEngine processEngine =ProcessEngines.getDefaultProcessEngine();
        List<Task> list =processEngine.getTaskService()
                .createTaskQuery()
                .taskCandidateUser("1")
                .list();
        System.out.println("totalNum:"+list.size());
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("formDefaultProcess")
                .taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().listPage(first, max);

        for (Task task : tasks) {
            System.out.println("taskId:"+task.getId());
            Map<String, Object> variables = taskService.getVariables(task.getId(),Arrays.asList("userId", "formInfoEntity"));
            int applicant = (int) variables.get("userId");
            System.out.println("userId:"+applicant);
            FormInfoEntity formInfoEntity = (FormInfoEntity) variables.get("formInfoEntity");
            System.out.println("formId:"+formInfoEntity.getId());

        }


    }



    public static void main(String[] args){

    }

    public void test() {
//        Map<String, Object> variables = new HashMap<>();
//        FormInfoEntity formInfoEntity = new FormInfoEntity();
//        formInfoEntity.setId("100002");
//        formInfoEntity.setDemandContent("ceshi测试测试测试111");
//        variables.put("userId",1);
//        variables.put("formInfoEntity",formInfoEntity);
//        startProcess(variables,"100002" ,1, "测试lk");
        //userTasksEnterpriseRegis(2,1,10);
        List<Task> list = taskService.createTaskQuery().processInstanceBusinessKey("100002").list();
        System.out.println(list.get(0).getExecutionId());
    }
}

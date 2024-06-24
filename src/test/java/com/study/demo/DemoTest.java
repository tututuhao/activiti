package com.study.demo;

import com.study.common.TaskConstant;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

/**
 * 例子1
 */
@Slf4j
@SpringBootTest
public class DemoTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 部署
     */
    @Test
    public void initDeployment(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/Process_1.bpmn20.xml")
                .name("任务流程")
                .deploy();
        log.info("部署成功，流程定义id：{}",
                repositoryService.createProcessDefinitionQuery()
                        .deploymentId(deployment.getId())
                        .singleResult()
                        .getId()
                );
    }

    /**
     * 提交注册
     */
    @Test
    public void submitRegisterMessage(){
        //业务信息
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put(TaskConstant.PHONE_NUM, "15607183262");
        messageMap.put(TaskConstant.NAME, "小明");
        messageMap.put(TaskConstant.COLLEGE, "北清大学");

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process_1", messageMap);
        log.info("启动实例成功，实例id:{}", instance.getProcessInstanceId());

        //完成注册任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .taskName("提交注册信息")
                .singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 管理员审核（表示不同意）
     */
    @Test
    public void manageNoAgree(){
        String taskId = "117509";
        String userId = "1";
        String groupId = "admin";
        //不同意
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("agree", false);
        messageMap.put(TaskConstant.MESSAGE, "信息太少，不通过");
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        boolean isCandidateUser = identityLinksForTask.stream().anyMatch(link -> link.getUserId() != null &&
                userId.equals(link.getUserId()));
        boolean isCandidateGroupMember  = identityLinksForTask.stream().anyMatch(link -> link.getGroupId() != null &&
                groupId.equals(link.getGroupId()));
        if(isCandidateUser || isCandidateGroupMember){
            taskService.complete(taskId, messageMap);
            log.info("{} complete task", userId);
        }else{
            log.info("没有权限");
        }
    }



    /**
     * 管理员审核（表示同意）
     */
    @Test
    public void manageAgree(){
        String taskId = "122510";
        String userId = "1";
        String groupId = "admin";
        //不同意
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("agree", true);
        messageMap.put(TaskConstant.MESSAGE, "同意，可以创建账号");
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        boolean isCandidateUser = identityLinksForTask.stream().anyMatch(link -> link.getUserId() != null &&
                userId.equals(link.getUserId()));
        boolean isCandidateGroupMember  = identityLinksForTask.stream().anyMatch(link -> link.getGroupId() != null &&
                groupId.equals(link.getGroupId()));
        if(isCandidateUser || isCandidateGroupMember){
            taskService.complete(taskId, messageMap);
            log.info("{} complete task", userId);
        }else{
            log.info("没有权限");
        }
    }


}

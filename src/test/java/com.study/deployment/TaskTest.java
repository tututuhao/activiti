package com.study.deployment;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tuhao
 * @description
 * @date
 */
@Slf4j
@SpringBootTest
public class TaskTest {

    @Autowired
    private TaskService taskService;

    /**
     * 查询任务
     */
    @Test
    public void getTasks(){
        log.info("query all task");
        List<Task> list = taskService.createTaskQuery().list();
        log.info("count : {}", list.size());

        log.info("query bajie task");
        List<Task> bajieTasks = taskService.createTaskQuery().taskAssignee("bajie").list();
        for(Task tk : bajieTasks){
            log.info("id : {}", tk.getId());
            log.info("name : {}", tk.getName());
            log.info("assignee:{}", tk.getAssignee());
        }

        log.info("dog task count :{} ",
                taskService.createTaskQuery().taskCandidateGroup("dog").list().size());
    }

    /**
     * 执行任务
     */
    @Test
    public void completeTask(){
        List<Task> shaseng = taskService.createTaskQuery().taskAssignee("shaseng").list();
        List<Task> wukong = taskService.createTaskQuery().taskAssignee("wukong").list();
        List<Task> tangseng = taskService.createTaskQuery().taskAssignee("tangseng").list();
        log.info("user task : {}, {}, {}", shaseng.size(), wukong.size(), tangseng.size());

        Map<String, Object> map = new HashMap<>();
        map.put("agree", true);
        map.put("day", 9);
        taskService.complete("40002",map);
        log.info("bajie complete task => assigne user");

        List<Task> shaseng2 = taskService.createTaskQuery().taskAssignee("shaseng").list();
        List<Task> wukong2 = taskService.createTaskQuery().taskAssignee("wukong").list();
        List<Task> tangseng2 = taskService.createTaskQuery().taskAssignee("tangseng").list();
        log.info("user task : {}, {}, {}", shaseng2.size(), wukong2.size(), tangseng2.size());
    }

    /**
     * wukong task
     */
    @Test
    public void wukongTask(){
        List<Task> wukong = taskService.createTaskQuery().taskAssignee("wukong").list();
        taskService.complete(wukong.get(0).getId());
    }

    /**
     * 判断当前用户对应任务是否有执行权限
     */
    @Test
    public void authCompleteTask(){
        String currentUser = "shaseng";
        String groupId = "xiyou";
        String taskId = "25005";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(currentUser.equals(task.getAssignee())){
            taskService.complete(task.getId());
            log.info("complete task");
        }else{
            List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
            boolean isCandidateUser = identityLinksForTask.stream().anyMatch(link -> link.getUserId() != null &&
                    currentUser.equals(link.getUserId()));
            boolean isCandidateGroupMember  = identityLinksForTask.stream().anyMatch(link -> link.getGroupId() != null &&
                    groupId.equals(link.getGroupId()));
            if(isCandidateUser || isCandidateGroupMember){
                taskService.complete(taskId);
                log.info("{} complete task",currentUser);
            }else{
                log.info("未完成任务");
            }

        }

    }


}

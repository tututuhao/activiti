package com.study.deployment;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author tuhao
 * @description
 * @date
 */
@SpringBootTest
public class HistoryTest {

    @Autowired
    private HistoryService historyService;

    @Test
    public void queryHistory(){
        //查询实例的历史信息
        List<HistoricProcessInstance> list = historyService
                .createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime()
                .asc()
                .list();
        for (HistoricProcessInstance historicProcessInstance : list){
            System.out.println("Process Instance ID: " + historicProcessInstance.getId());
            System.out.println("Process Definition ID: " + historicProcessInstance.getProcessDefinitionId());
            System.out.println("Start Time: " + historicProcessInstance.getStartTime());
            System.out.println("End Time: " + historicProcessInstance.getEndTime());
        }

        System.out.println("\n");
        //查询流程实例的所有活动节点执行历史
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId("12501").list();
        for (HistoricActivityInstance activity : activityList) {
            System.out.println("Activity ID: " + activity.getActivityId());
            System.out.println("Activity Name: " + activity.getActivityName());
            System.out.println("Activity Type: " + activity.getActivityType());
            System.out.println("Start Time: " + activity.getStartTime());
            System.out.println("End Time: " + activity.getEndTime());
        }

        System.out.println("\n");
        // 查询流程实例的所有任务执行历史
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("12501")
                .orderByHistoricTaskInstanceStartTime().asc().list();
        for (HistoricTaskInstance task : taskList) {
            System.out.println("Task ID: " + task.getId());
            System.out.println("Task Name: " + task.getName());
            System.out.println("Assignee: " + task.getAssignee());
            System.out.println("Start Time: " + task.getStartTime());
            System.out.println("End Time: " + task.getEndTime());
            // 其他任务信息
        }

        System.out.println("\n");
        // 查询流程实例的变量历史
        List<HistoricVariableInstance> variableList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("12501").list();
        for (HistoricVariableInstance variable : variableList) {
            System.out.println("Variable Name: " + variable.getVariableName());
            System.out.println("Variable Value: " + variable.getValue());
            // 其他变量信息
        }

    }
}

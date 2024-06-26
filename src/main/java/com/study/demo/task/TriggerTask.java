package com.study.demo.task;

import cn.hutool.core.collection.CollUtil;
import com.study.common.TaskConstant;
import com.study.demo.entity.Leave;
import com.study.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TriggerTask {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final LeaveService leaveService;

    /**
     * 检查任务是否已经完成，若未完成发送邮件进行提示
     */
    public void checkTaskCompletion(DelegateExecution execution){
        //查询业务数据
        String businessKey = execution.getProcessInstanceBusinessKey();
        Leave leave = leaveService.getById(Long.valueOf(businessKey));

        log.info("isEnded:{}, isActive:{}", execution.isEnded(), execution.isActive());
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        if(instance != null){
           log.info("正在发送给{}邮件，提示他领导还没有处理，线下进行提示领导", leave.getName());
        }

//        List<Task> list = taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).list();
//        if(CollUtil.isNotEmpty(list)){
//            log.info("正在发送给{}邮件，提示他领导还没有处理，线下进行提示领导", leave.getName());
//        }
    }

    /**
     * 审核完成后发送邮件
     */
    public void auditAfterSendEmail(DelegateExecution execution){
        //查询业务数据
        String businessKey = execution.getProcessInstanceBusinessKey();
        Leave leave = leaveService.getById(Long.valueOf(businessKey));

        Boolean isAgree = (Boolean) execution.getVariable(TaskConstant.AGREE);
        if(isAgree){
            log.info("正在发送给{}邮件，告诉审核通过了", leave.getName());
        }else{
            log.info("正在发送给{}邮件，告诉它审核不通过了，请重新写请假事由", leave.getName());
        }
    }
}

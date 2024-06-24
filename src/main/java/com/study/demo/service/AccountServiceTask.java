package com.study.demo.service;

import com.study.common.TaskConstant;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

/**
 * 创建账号任务
 */
@Slf4j
public class AccountServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String name = (String) delegateExecution.getVariable(TaskConstant.NAME);
        String college = (String) delegateExecution.getVariable(TaskConstant.COLLEGE);
        log.info("创建账号成功, 姓名:{},  学校名称:{}", name, college);
    }
}

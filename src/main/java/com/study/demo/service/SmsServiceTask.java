package com.study.demo.service;

import com.study.common.TaskConstant;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 发送短信
 */
@Slf4j
public class SmsServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String phoneNum = (String) delegateExecution.getVariable(TaskConstant.PHONE_NUM);
        String message = (String) delegateExecution.getVariable(TaskConstant.MESSAGE);
        //调取短信服务进行发送短信
        log.info("调用短信服务，进行发送短信======");
        log.info("电话号码：{}, 短信：{}", phoneNum, message);
    }
}

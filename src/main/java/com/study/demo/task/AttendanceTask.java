package com.study.demo.task;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

@Slf4j
public class AttendanceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
       log.info("考勤系统进行处理，避免月底认为矿工一天");
    }
}

package com.study.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部署 controller
 */
@Slf4j
@RestController
@RequestMapping("deployment")
@RequiredArgsConstructor
public class DeploymentController {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;

    /**
     * 部署
     */
    @GetMapping("initDeploymentBpmn")
    public void initDeploymentBpmn(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/demo_task.bpmn")
                .name("任务例子")
                .deploy();
        log.info("部署成功,流程定义id:{}", repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list().get(0).getId());
    }
}

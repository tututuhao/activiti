package com.study.deployment;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 部署
 * @author tuhao
 * @description
 * @date
 */
@Slf4j
@SpringBootTest
public class DeloymentTest {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void initDeploymentBpmn(){
        log.info("开始部署流程");
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/Part7_Inclusive.bpmn")
                .addClasspathResource("bpmn/Part7_Inclusive.png")
                .name("测试多个流程")
                .deploy();
        log.info("name:{},id:{},key:{},category:{},deploymentTime:{}, projectReleaseVersion:{},tenantId:{},version:{}",
                deployment.getName(), deployment.getId(), deployment.getKey(),deployment.getCategory(), deployment.getDeploymentTime(),
                deployment.getProjectReleaseVersion(), deployment.getTenantId(), deployment.getVersion());
    }
}

package com.study.deployment;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程部署
     */
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

    /**
     * 查看部署内容
     */
    @Test
    public void queryBpmn(){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("myProcess_Inclusive:1:4")
                .singleResult();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName()
        );

        File file = FileUtil.file(FileUtil.getWebRoot(), "src/main/resourcces/bpmn2");
        FileUtil.writeFromStream(resourceAsStream, file);
        log.info("absolute path:{}", file.getAbsolutePath());
        IoUtil.close(resourceAsStream);


    }

    /**
     * 获取部署的流程
     */
    @Test
    public void getDeployments(){
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for(Deployment dep : list){
            System.out.println("Id："+dep.getId());
            System.out.println("Name："+dep.getName());
            System.out.println("DeploymentTime："+dep.getDeploymentTime());
            System.out.println("Key："+dep.getKey());
        }
    }


    /**
     * 获取流程定义
     */
    @Test
    public void getDefinitions(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();
        for(ProcessDefinition pd : list){
            System.out.println("------流程定义--------");
            System.out.println("Name："+pd.getName());
            System.out.println("Key："+pd.getKey());
            System.out.println("ResourceName："+pd.getResourceName());
            System.out.println("DeploymentId："+pd.getDeploymentId());
            System.out.println("Version："+pd.getVersion());
        }
    }

    /**
     * 初始化流程实例
     */
    @Test
    public void initProcessInstance(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_Inclusive");
        log.info("instanceId : {}", processInstance.getProcessInstanceId());
    }

    /**
     * 查询单个流程实例
     */
    @Test
    public void getProcessInstances() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("12501")
                .singleResult();
        System.out.println("Process Instance ID: " + processInstance.getId());
        System.out.println("Process Definition ID: " + processInstance.getProcessDefinitionId());
        System.out.println("Business Key: " + processInstance.getBusinessKey());
        System.out.println("Process Variables: " + processInstance.getProcessVariables());
        System.out.println("Start Time: " + processInstance.getStartTime());
        System.out.println("Is Ended: " + processInstance.isEnded());
        System.out.println("Is Suspended: " + processInstance.isSuspended());
        System.out.println("Tenant ID: " + processInstance.getTenantId());
    }


    /**
     * 暂停与激活流程实例
     */
    @Test
    public void activitieProcessInstance(){
//        runtimeService.suspendProcessInstanceById("12501");
//        log.info("挂起流程实例");

        runtimeService.activateProcessInstanceById("12501");
        log.info("激活流程实例");
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance(){
        runtimeService.deleteProcessInstance("12501", "就删怎么了");
    }

    @Test
    public void updateProcessInstance(){
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myProcess_Inclusive")
                .orderByProcessDefinitionVersion()
                .asc()
                .list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("key:" + processDefinition.getKey());
            System.out.println("version:" + processDefinition.getVersion());
            System.out.println("id:" + processDefinition.getId());
        }
    }



}

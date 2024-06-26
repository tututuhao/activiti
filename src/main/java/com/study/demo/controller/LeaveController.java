package com.study.demo.controller;

import com.study.common.TaskConstant;
import com.study.demo.entity.Leave;
import com.study.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.io.BytesStreamSource;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 请假流程 前端控制器
 * </p>
 *
 * @author tuhao
 * @since 2024-06-25
 */
@Slf4j
@RestController
@RequestMapping("/demo/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    /**
     * 上传文件进行部署
     * @param file
     */
    @SneakyThrows
    @PostMapping("deployment")
    @Transactional(rollbackFor = Exception.class)
    public void deployment(@RequestParam("file") MultipartFile file) {
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(file.getOriginalFilename(), file.getInputStream())
                .name("请假流程")
                .deploy();
        log.info("部署成功，deploymentId:{}", deployment.getId());

        //校验部署的文件是否正确
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(new BytesStreamSource(file.getBytes()), false, false);
        ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
        ProcessValidator processValidator = processValidatorFactory.createDefaultProcessValidator();
        List<ValidationError> errors = processValidator.validate(bpmnModel);
        for (ValidationError error : errors) {
            log.error(error.toString());
        }
    }

    /**
     * 开始请假
     */
    @PostMapping("startLeave")
    public void startLeave(@RequestBody Leave leave){
        leaveService.save(leave);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Process_1", leave.getId().toString());
        log.info("实例启动成功:{}", instance.getId());

        //完成任务
        Task task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 职能上级审核
     */
    @PostMapping("audit1")
    public void audit1(@NotNull(message = "同意状态不能为空") Boolean agree){
        String assignee = "li";
        Task task = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .desc()
                .list()
                .get(0);

        //获取业务id
        String businessKey = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();
        //从业务库中查询信息
        Leave leave = leaveService.getById(Long.valueOf(businessKey));

        //传递参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put(TaskConstant.DAY, leave.getLeaveDay());
        paramMap.put(TaskConstant.AGREE, agree);

        //完成任务
        taskService.complete(task.getId(), paramMap);
    }

    /**
     * 行政上级审核
     */
    @PostMapping("audit2")
    public void audit2(@NotNull(message = "同意状态不能为空") Boolean agree){
        String assignee = "shi";
        Task task = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .desc()
                .list()
                .get(0);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put(TaskConstant.AGREE, agree);
        taskService.complete(task.getId(), paramMap);
    }

}

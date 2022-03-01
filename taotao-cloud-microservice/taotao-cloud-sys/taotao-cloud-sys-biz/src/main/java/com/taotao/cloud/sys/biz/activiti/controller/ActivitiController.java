package com.taotao.cloud.sys.biz.activiti.controller;

import java.io.InputStream;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.ApiParam;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import boot.spring.pagemodel.Process;
import boot.spring.mapper.LeaveApplyMapper;
import boot.spring.pagemodel.DataGrid;
import boot.spring.pagemodel.HistoryProcess;
import boot.spring.pagemodel.LeaveTask;
import boot.spring.pagemodel.MSG;
import boot.spring.po.LeaveApply;
import boot.spring.po.Permission;
import boot.spring.po.Role;
import boot.spring.po.Role_permission;
import boot.spring.po.User;
import boot.spring.po.User_role;
import boot.spring.service.LeaveService;
import boot.spring.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "请假流程接口")
@Controller
public class ActivitiController {
	@Autowired
	RepositoryService rep;
	
	@Autowired
	RuntimeService runservice;
	
	@Autowired
	FormService formservice;
	
	@Autowired
	IdentityService identityservice;
	
	@Autowired
	LeaveService leaveservice;
	
	@Autowired
	TaskService taskservice;
	
	@Autowired
	HistoryService histiryservice;
	
	@Autowired
	SystemService systemservice;
	
	@Autowired
	LeaveApplyMapper leaveApplyMapper;

	@Autowired
	ProcessEngineConfiguration configuration;

	@RequestMapping(value = "/processlist", method = RequestMethod.GET)
	String process() {
		return "activiti/processlist";
	}
	
	@ApiOperation("上传一个工作流文件")
	@RequestMapping(value = "/uploadworkflow", method = RequestMethod.POST)
	public String fileupload(@RequestParam MultipartFile uploadfile, HttpServletRequest request) {
		try {
			MultipartFile file = uploadfile;
			String filename = file.getOriginalFilename();
			InputStream is = file.getInputStream();
			rep.createDeployment().addInputStream(filename, is).deploy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	@ApiOperation("查询已部署工作流列表")
	@RequestMapping(value = "/getprocesslists", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<Process> getlist(@RequestParam("current") int current, @RequestParam("rowCount") int rowCount) {
		int firstrow = (current - 1) * rowCount;
		List<ProcessDefinition> list = rep.createProcessDefinitionQuery().listPage(firstrow, rowCount);
		int total = rep.createProcessDefinitionQuery().list().size();
		List<Process> mylist = new ArrayList<Process>();
		for (int i = 0; i < list.size(); i++) {
			Process p = new Process();
			p.setDeploymentId(list.get(i).getDeploymentId());
			p.setId(list.get(i).getId());
			p.setKey(list.get(i).getKey());
			p.setName(list.get(i).getName());
			p.setResourceName(list.get(i).getResourceName());
			p.setDiagramresourcename(list.get(i).getDiagramResourceName());
			mylist.add(p);
		}
		DataGrid<Process> grid = new DataGrid<Process>();
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setRows(mylist);
		grid.setTotal(total);
		return grid;
	}
	
	@ApiOperation("查看工作流图片")
	@RequestMapping(value = "/showresource", method = RequestMethod.GET)
	public void export(@RequestParam("pdid") String pdid, @RequestParam("resource") String resource,
			HttpServletResponse response) throws Exception {
		ProcessDefinition def = rep.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
		InputStream is = rep.getResourceAsStream(def.getDeploymentId(), resource);
		ServletOutputStream output = response.getOutputStream();
		IOUtils.copy(is, output);
	}

	@RequestMapping(value = "/deletedeploy", method = RequestMethod.POST)
	public String deletedeploy(@RequestParam("deployid") String deployid) throws Exception {
		rep.deleteDeployment(deployid, true);
		return "activiti/processlist";
	}

	@RequestMapping(value = "/runningprocess", method = RequestMethod.GET)
	public String task() {
		return "activiti/runningprocess";
	}

	@RequestMapping(value = "/deptleaderaudit", method = RequestMethod.GET)
	public String mytask() {
		return "activiti/deptleaderaudit";
	}

	@RequestMapping(value = "/hraudit", method = RequestMethod.GET)
	public String hr() {
		return "activiti/hraudit";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String my() {
		return "index";
	}

	@RequestMapping(value = "/leaveapply", method = RequestMethod.GET)
	public String leave() {
		return "activiti/leaveapply";
	}

	@RequestMapping(value = "/reportback", method = RequestMethod.GET)
	public String reprotback() {
		return "activiti/reportback";
	}

	@RequestMapping(value = "/modifyapply", method = RequestMethod.GET)
	public String modifyapply() {
		return "activiti/modifyapply";
	}
	
	@ApiOperation("发起一个请假流程")
	@RequestMapping(value = "/startleave", method = RequestMethod.POST)
	@ResponseBody
	public MSG start_leave(LeaveApply apply, HttpSession session) {
		String userid = (String) session.getAttribute("username");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("applyuserid", userid);
		variables.put("deptleader", apply.getDeptleader());
		leaveservice.startWorkflow(apply, userid, variables);
		return new MSG("sucess");
	}

	@ApiOperation("获取部门领导审批待办列表")
	@RequestMapping(value = "/depttasklist", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<LeaveTask> getdepttasklist(HttpSession session, @RequestParam("current") int current,
			@RequestParam("rowCount") int rowCount) {
		String username = (String) session.getAttribute("username");
		DataGrid<LeaveTask> grid = new DataGrid<LeaveTask>();
		grid.setRowCount(rowCount);
		grid.setCurrent(current);
		int firstrow = (current - 1) * rowCount;
		List<LeaveApply> results = leaveservice.getpagedepttask(username, firstrow, rowCount);
		int totalsize = leaveservice.getalldepttask(username);
		List<LeaveTask> tasks = new ArrayList<LeaveTask>();
		for (LeaveApply apply : results) {
			LeaveTask task = new LeaveTask();
			task.setApply_time(apply.getApply_time());
			task.setUser_id(apply.getUser_id());
			task.setEnd_time(apply.getEnd_time());
			task.setId(apply.getId());
			task.setLeave_type(apply.getLeave_type());
			task.setProcess_instance_id(apply.getProcess_instance_id());
			task.setProcessdefid(apply.getTask().getProcessDefinitionId());
			task.setReason(apply.getReason());
			task.setStart_time(apply.getStart_time());
			task.setTaskcreatetime(apply.getTask().getCreateTime());
			task.setTaskid(apply.getTask().getId());
			task.setTaskname(apply.getTask().getName());
			tasks.add(task);
		}
		grid.setTotal(totalsize);
		grid.setRows(tasks);
		return grid;
	}

	@ApiOperation("获取人事审批待办列表")
	@RequestMapping(value = "/hrtasklist", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<LeaveTask> gethrtasklist(HttpSession session, @RequestParam("current") int current,
			@RequestParam("rowCount") int rowCount) {
		DataGrid<LeaveTask> grid = new DataGrid<LeaveTask>();
		grid.setRowCount(rowCount);
		grid.setCurrent(current);
		String username = (String) session.getAttribute("username");
		int firstrow = (current - 1) * rowCount;
		List<LeaveApply> results = leaveservice.getpagehrtask(username, firstrow, rowCount);
		int totalsize = leaveservice.getallhrtask(username);
		List<LeaveTask> tasks = new ArrayList<LeaveTask>();
		for (LeaveApply apply : results) {
			LeaveTask task = new LeaveTask();
			task.setApply_time(apply.getApply_time());
			task.setUser_id(apply.getUser_id());
			task.setEnd_time(apply.getEnd_time());
			task.setId(apply.getId());
			task.setLeave_type(apply.getLeave_type());
			task.setProcess_instance_id(apply.getProcess_instance_id());
			task.setProcessdefid(apply.getTask().getProcessDefinitionId());
			task.setReason(apply.getReason());
			task.setStart_time(apply.getStart_time());
			task.setTaskcreatetime(apply.getTask().getCreateTime());
			task.setTaskid(apply.getTask().getId());
			task.setTaskname(apply.getTask().getName());
			tasks.add(task);
		}
		grid.setRowCount(rowCount);
		grid.setCurrent(current);
		grid.setTotal(totalsize);
		grid.setRows(tasks);
		return grid;
	}

	@ApiOperation("获取销假任务列表")
	@RequestMapping(value = "/xjtasklist", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<LeaveTask> getXJtasklist(HttpSession session, @RequestParam("current") int current,
			@RequestParam("rowCount") int rowCount) {
		int firstrow = (current - 1) * rowCount;
		String userid = (String) session.getAttribute("username");
		List<LeaveApply> results = leaveservice.getpageXJtask(userid, firstrow, rowCount);
		int totalsize = leaveservice.getallXJtask(userid);
		List<LeaveTask> tasks = new ArrayList<LeaveTask>();
		for (LeaveApply apply : results) {
			LeaveTask task = new LeaveTask();
			task.setApply_time(apply.getApply_time());
			task.setUser_id(apply.getUser_id());
			task.setEnd_time(apply.getEnd_time());
			task.setId(apply.getId());
			task.setLeave_type(apply.getLeave_type());
			task.setProcess_instance_id(apply.getProcess_instance_id());
			task.setProcessdefid(apply.getTask().getProcessDefinitionId());
			task.setReason(apply.getReason());
			task.setStart_time(apply.getStart_time());
			task.setTaskcreatetime(apply.getTask().getCreateTime());
			task.setTaskid(apply.getTask().getId());
			task.setTaskname(apply.getTask().getName());
			tasks.add(task);
		}
		DataGrid<LeaveTask> grid = new DataGrid<LeaveTask>();
		grid.setRowCount(rowCount);
		grid.setCurrent(current);
		grid.setTotal(totalsize);
		grid.setRows(tasks);
		return grid;
	}
	
	@ApiOperation("获取调整休假申请任务列表")
	@RequestMapping(value = "/updatetasklist", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<LeaveTask> getupdatetasklist(HttpSession session, @RequestParam("current") int current,
			@RequestParam("rowCount") int rowCount) {
		int firstrow = (current - 1) * rowCount;
		String userid = (String) session.getAttribute("username");
		List<LeaveApply> results = leaveservice.getpageupdateapplytask(userid, firstrow, rowCount);
		int totalsize = leaveservice.getallupdateapplytask(userid);
		List<LeaveTask> tasks = new ArrayList<LeaveTask>();
		for (LeaveApply apply : results) {
			LeaveTask task = new LeaveTask();
			task.setApply_time(apply.getApply_time());
			task.setUser_id(apply.getUser_id());
			task.setEnd_time(apply.getEnd_time());
			task.setId(apply.getId());
			task.setLeave_type(apply.getLeave_type());
			task.setProcess_instance_id(apply.getProcess_instance_id());
			task.setProcessdefid(apply.getTask().getProcessDefinitionId());
			task.setReason(apply.getReason());
			task.setStart_time(apply.getStart_time());
			task.setTaskcreatetime(apply.getTask().getCreateTime());
			task.setTaskid(apply.getTask().getId());
			task.setTaskname(apply.getTask().getName());
			tasks.add(task);
		}
		DataGrid<LeaveTask> grid = new DataGrid<LeaveTask>();
		grid.setRowCount(rowCount);
		grid.setCurrent(current);
		grid.setTotal(totalsize);
		grid.setRows(tasks);
		return grid;
	}

	@ApiOperation("使用任务id获取请假业务数据")
	@RequestMapping(value = "/dealtask", method = RequestMethod.POST)
	@ResponseBody
	public LeaveApply taskdeal(@RequestParam("taskid") String taskid, HttpServletResponse response) {
		Task task = taskservice.createTaskQuery().taskId(taskid).singleResult();
		ProcessInstance process = runservice.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
				.singleResult();
		LeaveApply leave = leaveservice.getleave(new Integer(process.getBusinessKey()));
		return leave;
	}

	@RequestMapping(value = "/activiti/task-deptleaderaudit", method = RequestMethod.GET)
	String url() {
		return "/activiti/task-deptleaderaudit";
	}

	@ApiOperation("完成部门领导审批待办")
	@RequestMapping(value = "/task/deptcomplete/{taskid}", method = RequestMethod.POST)
	@ResponseBody
	public MSG deptcomplete(HttpSession session, @PathVariable("taskid") String taskid, HttpServletRequest req) {
		String username = (String) session.getAttribute("username");
		Map<String, Object> variables = new HashMap<String, Object>();
		String approve = req.getParameter("deptleaderapprove");
		String hr = req.getParameter("hr");
		variables.put("deptleaderapprove", approve);
		variables.put("hr", hr);
		taskservice.claim(taskid, username);
		taskservice.complete(taskid, variables);
		return new MSG("success");
	}
	
	@ApiOperation("完成hr审批待办")
	@RequestMapping(value = "/task/hrcomplete/{taskid}", method = RequestMethod.POST)
	@ResponseBody
	public MSG hrcomplete(HttpSession session, @PathVariable("taskid") String taskid, HttpServletRequest req) {
		String userid = (String) session.getAttribute("username");
		Map<String, Object> variables = new HashMap<String, Object>();
		String approve = req.getParameter("hrapprove");
		variables.put("hrapprove", approve);
		taskservice.claim(taskid, userid);
		taskservice.complete(taskid, variables);
		return new MSG("success");
	}
	
	@ApiOperation("完成销假待办")
	@RequestMapping(value = "/task/reportcomplete/{taskid}", method = RequestMethod.POST)
	@ResponseBody
	public MSG reportbackcomplete(@PathVariable("taskid") String taskid, HttpServletRequest req) {
		String realstart_time = req.getParameter("realstart_time");
		String realend_time = req.getParameter("realend_time");
		leaveservice.completereportback(taskid, realstart_time, realend_time);
		return new MSG("success");
	}

	@ApiOperation("完成调整申请待办")
	@RequestMapping(value = "/task/updatecomplete/{taskid}", method = RequestMethod.POST)
	@ResponseBody
	public MSG updatecomplete(@PathVariable("taskid") String taskid, @ModelAttribute("leave") LeaveApply leave,
			@RequestParam("reapply") String reapply) {
		leaveservice.updatecomplete(taskid, leave, reapply);
		return new MSG("success");
	}

	@RequestMapping(value = "/getfinishprocess", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<HistoryProcess> getHistory(HttpSession session, @RequestParam("current") int current,
			@RequestParam("rowCount") int rowCount) {
		String userid = (String) session.getAttribute("username");
		HistoricProcessInstanceQuery process = histiryservice.createHistoricProcessInstanceQuery()
				.processDefinitionKey("leave").startedBy(userid).finished();
		int total = (int) process.count();
		int firstrow = (current - 1) * rowCount;
		List<HistoricProcessInstance> info = process.listPage(firstrow, rowCount);
		List<HistoryProcess> list = new ArrayList<HistoryProcess>();
		for (HistoricProcessInstance history : info) {
			HistoryProcess his = new HistoryProcess();
			String bussinesskey = history.getBusinessKey();
			LeaveApply apply = leaveservice.getleave(Integer.parseInt(bussinesskey));
			his.setLeaveapply(apply);
			his.setBusinessKey(bussinesskey);
			his.setProcessDefinitionId(history.getProcessDefinitionId());
			list.add(his);
		}
		DataGrid<HistoryProcess> grid = new DataGrid<HistoryProcess>();
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setTotal(total);
		grid.setRows(list);
		return grid;
	}

	@RequestMapping(value = "/historyprocess", method = RequestMethod.GET)
	public String history() {
		return "activiti/historyprocess";
	}
	
	@ApiOperation("使用流程实例编号获取历史流程数据")
	@RequestMapping(value = "/processinfo", method = RequestMethod.POST)
	@ResponseBody
	public List<HistoricActivityInstance> processinfo(@RequestParam("instanceid") String instanceid) {
		List<HistoricActivityInstance> his = histiryservice.createHistoricActivityInstanceQuery()
				.processInstanceId(instanceid).orderByHistoricActivityInstanceStartTime().asc().list();
		return his;
	}
	
	@ApiOperation("使用业务号获取历史流程数据")
	@RequestMapping(value = "/processhis", method = RequestMethod.POST)
	@ResponseBody
	public List<HistoricActivityInstance> processhis(@RequestParam("ywh") String ywh) {
		String instanceid = histiryservice.createHistoricProcessInstanceQuery().processDefinitionKey("purchase")
				.processInstanceBusinessKey(ywh).singleResult().getId();
		List<HistoricActivityInstance> his = histiryservice.createHistoricActivityInstanceQuery()
				.processInstanceId(instanceid).orderByHistoricActivityInstanceStartTime().asc().list();
		return his;
	}

	@RequestMapping(value = "myleaveprocess", method = RequestMethod.GET)
	String myleaveprocess() {
		return "activiti/myleaveprocess";
	}

	/**
	@ApiOperation("使用executionid追踪流程图进度")
	@RequestMapping(value = "traceprocess/{executionid}", method = RequestMethod.GET)
	public void traceprocess(@PathVariable("executionid") String executionid, HttpServletResponse response)
			throws Exception {
		ProcessInstance process = runservice.createProcessInstanceQuery().processInstanceId(executionid).singleResult();
		BpmnModel bpmnmodel = rep.getBpmnModel(process.getProcessDefinitionId());
		List<String> activeActivityIds = runservice.getActiveActivityIds(executionid);
		DefaultProcessDiagramGenerator gen = new DefaultProcessDiagramGenerator();
		// 获得历史活动记录实体（通过启动时间正序排序，不然有的线可以绘制不出来）
		List<HistoricActivityInstance> historicActivityInstances = histiryservice.createHistoricActivityInstanceQuery()
				.executionId(executionid).orderByHistoricActivityInstanceStartTime().asc().list();
		// 计算活动线
		List<String> highLightedFlows = leaveservice
				.getHighLightedFlows(
						(ProcessDefinitionEntity) ((RepositoryServiceImpl) rep)
								.getDeployedProcessDefinition(process.getProcessDefinitionId()),
						historicActivityInstances);

		InputStream in = gen.generateDiagram(bpmnmodel, "png", activeActivityIds, highLightedFlows, "宋体", "宋体", null,
				null, 1.0);
		// InputStream in=gen.generateDiagram(bpmnmodel, "png",
		// activeActivityIds);
		ServletOutputStream output = response.getOutputStream();
		IOUtils.copy(in, output);
	}
	**/

	@RequestMapping(value = {"/traceprocess/{processInstanceId}"}, method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<byte[]> traceprocess(@ApiParam(name = "processInstanceId",value = "The id of the process instance to get the diagram for.") @PathVariable String processInstanceId, HttpServletResponse response) {
		ProcessInstance processInstance = runservice.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
		ProcessDefinition pde = rep.getProcessDefinition(processInstance.getProcessDefinitionId());
		if (pde != null && pde.hasGraphicalNotation()) {
			BpmnModel bpmnModel = rep.getBpmnModel(pde.getId());
			ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
			InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "png", runservice.getActiveActivityIds(processInstance.getId()), Collections.emptyList(), "宋体", "宋体", "宋体", configuration.getClassLoader(), 1.0D);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "image/png");

			try {
				return new ResponseEntity(IOUtils.toByteArray(resource), responseHeaders, HttpStatus.OK);
			} catch (Exception var10) {
				throw new ActivitiIllegalArgumentException("Error exporting diagram", var10);
			}
		} else {
			throw new ActivitiIllegalArgumentException("Process instance with id '" + processInstance.getId() + "' has no graphical notation defined.");
		}
	}


	@RequestMapping(value = "myleaves", method = RequestMethod.GET)
	String myleaves() {
		return "activiti/myleaves";
	}
	
	@ApiOperation("我发起的请假流程")
	@RequestMapping(value = "setupprocess", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<LeaveApply> setupprocess(HttpSession session, @RequestParam("current") int current,
			@RequestParam("rowCount") int rowCount) {
		String username = (String) session.getAttribute("username");
		List<LeaveApply> list = leaveservice.getPageByApplyer(username, current, rowCount);
		for (LeaveApply apply : list) {
			ProcessInstance process = runservice.createProcessInstanceQuery().processInstanceId(apply.getProcess_instance_id()).singleResult();
			if (process == null) {
				apply.setState("已结束");
				apply.setActivityid("无");
			} else {
				apply.setState("运行中");
				apply.setActivityid(taskservice.createTaskQuery().processInstanceId(process.getId()).singleResult().getName());
			}
		}
		DataGrid<LeaveApply> grid = new DataGrid<LeaveApply>();
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setTotal(leaveservice.getAllByApplyer(username));
		grid.setRows(list);
		return grid;
	}
	
}

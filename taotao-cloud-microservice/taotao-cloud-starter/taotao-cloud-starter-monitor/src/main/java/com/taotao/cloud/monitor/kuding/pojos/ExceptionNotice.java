package com.taotao.cloud.monitor.kuding.pojos;

import com.taotao.cloud.monitor.kuding.properties.enums.ProjectEnviroment;
import org.springframework.util.DigestUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class ExceptionNotice extends PromethuesNotice {

	/**
	 * 工程名
	 */
	protected String project;

	/**
	 * 异常的标识码
	 */
	protected String uid;

	/**
	 * 方法名
	 */
	protected String methodName;

	/**
	 * 方法参数信息
	 */
	protected List<Object> parames;

	/**
	 * 类路径
	 */
	protected String classPath;

	/**
	 * 异常信息
	 */
	protected List<String> exceptionMessage;

	/**
	 * 异常追踪信息
	 */
	protected List<String> traceInfo = new ArrayList<>();

	/**
	 * 出现次数
	 */
	protected Long showCount = 1L;

	public ExceptionNotice(Throwable ex, String filterTrace, Object[] args, ProjectEnviroment projectEnviroment,
						   String title) {
		super(title, projectEnviroment);
		this.exceptionMessage = gainExceptionMessage(ex);
		this.parames = args == null ? null : Arrays.stream(args).collect(toList());
		List<StackTraceElement> list = stackTrace(ex, filterTrace);
		if (list.size() > 0) {
			this.traceInfo = list.stream().map(StackTraceElement::toString).collect(toList());
			this.methodName = list.get(0).getMethodName();
			this.classPath = list.get(0).getClassName();
		}
		this.uid = calUid();
	}

	public ExceptionNotice(Throwable ex, String filterTrace, Long showCount, Object[] args,
						   ProjectEnviroment projectEnviroment, String title) {
		super(title, projectEnviroment);
		this.exceptionMessage = gainExceptionMessage(ex);
		this.showCount = showCount;
		this.parames = args == null ? null : Arrays.stream(args).collect(toList());
		List<StackTraceElement> list = stackTrace(ex, filterTrace);
		if (list.size() > 0) {
			this.traceInfo = list.stream().map(StackTraceElement::toString).collect(toList());
			this.methodName = list.get(0).getMethodName();
			this.classPath = list.get(0).getClassName();
		}
		this.uid = calUid();
	}

	private List<StackTraceElement> stackTrace(Throwable throwable, String filterTrace) {
		List<StackTraceElement> list = new LinkedList<StackTraceElement>();
		addStackTrace(list, throwable, filterTrace);
		Throwable cause = throwable.getCause();
		while (cause != null) {
			addStackTrace(list, cause, filterTrace);
			cause = cause.getCause();
		}
		return list;
	}

	public void addStackTrace(List<StackTraceElement> list, Throwable throwable, String filterTrace) {
		list.addAll(0,
			Arrays.stream(throwable.getStackTrace())
				.filter(x -> filterTrace == null ? true : x.getClassName().startsWith(filterTrace))
				.filter(x -> !x.getFileName().equals("<generated>")).collect(toList()));
	}

	private List<String> gainExceptionMessage(Throwable exception) {
		List<String> list = new LinkedList<String>();
		gainExceptionMessage(exception, list);
		return list;
	}

	private void gainExceptionMessage(Throwable throwable, List<String> list) {
		list.add(String.format("%s:%s", throwable.getClass().getName(), throwable.getMessage()));
		Throwable cause = throwable.getCause();
		if (cause != null)
			gainExceptionMessage(cause, list);
	}

	private String calUid() {
		String md5 = DigestUtils.md5DigestAsHex(
			String.format("%s-%s", exceptionMessage, traceInfo.size() > 0 ? traceInfo.get(0) : "").getBytes());
		return md5;
	}

	public String createText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("工程信息：").append(project).append("(").append(projectEnviroment.getName()).append(")")
			.append("\r\n");
		stringBuilder.append("类路径：").append(classPath).append("\r\n");
		stringBuilder.append("方法名：").append(methodName).append("\r\n");
		if (parames != null && parames.size() > 0) {
			stringBuilder.append("参数信息：")
				.append(String.join(",", parames.stream().map(x -> x.toString()).collect(toList()))).append("\r\n");
		}
		stringBuilder.append("异常信息：").append(String.join("\r\n caused by: ", exceptionMessage)).append("\r\n");
		stringBuilder.append("异常追踪：").append("\r\n").append(String.join("\r\n", traceInfo)).append("\r\n");
		stringBuilder.append("最后一次出现时间：").append(createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
			.append("\r\n");
		stringBuilder.append("出现次数：").append(showCount).append("\r\n");
		return stringBuilder.toString();

	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the parames
	 */
	public List<Object> getParames() {
		return parames;
	}

	/**
	 * @param parames the parames to set
	 */
	public void setParames(List<Object> parames) {
		this.parames = parames;
	}

	/**
	 * @return the classPath
	 */
	public String getClassPath() {
		return classPath;
	}

	/**
	 * @param classPath the classPath to set
	 */
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	/**
	 * @return the exceptionMessage
	 */
	public List<String> getExceptionMessage() {
		return exceptionMessage;
	}

	/**
	 * @param exceptionMessage the exceptionMessage to set
	 */
	public void setExceptionMessage(List<String> exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * @return the traceInfo
	 */
	public List<String> getTraceInfo() {
		return traceInfo;
	}

	/**
	 * @param traceInfo the traceInfo to set
	 */
	public void setTraceInfo(List<String> traceInfo) {
		this.traceInfo = traceInfo;
	}

	/**
	 * @return the showCount
	 */
	public Long getShowCount() {
		return showCount;
	}

	/**
	 * @param showCount the showCount to set
	 */
	public void setShowCount(Long showCount) {
		this.showCount = showCount;
	}

	public ProjectEnviroment getProjectEnviroment() {
		return projectEnviroment;
	}

	public void setProjectEnviroment(ProjectEnviroment projectEnviroment) {
		this.projectEnviroment = projectEnviroment;
	}

	@Override
	public String toString() {
		return "ExceptionNotice [project=" + project + ", uid=" + uid + ", methodName=" + methodName + ", parames="
			+ parames + ", classPath=" + classPath + ", exceptionMessage=" + exceptionMessage + ", traceInfo="
			+ traceInfo + ", showCount=" + showCount + ", title=" + title + ", projectEnviroment="
			+ projectEnviroment + ", createTime=" + createTime + "]";
	}

}

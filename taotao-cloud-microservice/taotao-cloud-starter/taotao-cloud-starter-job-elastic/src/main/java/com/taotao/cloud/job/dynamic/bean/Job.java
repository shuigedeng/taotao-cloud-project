package com.taotao.cloud.job.dynamic.bean;

public class Job {
	
/*********************DataflowJobConfiguration START********************/
	
	/**
	 * 作业名称
	 * @return
	 */
	private String jobName;

	/**
	 * 作业类型（SIMPLE，DATAFLOW，SCRIPT）
	 */
	private String jobType;
	
	/**
	 * 任务类路径
	 */
	private String jobClass;
	
	/**
	 * cron表达式，用于控制作业触发时间
	 * @return
	 */
	private String cron;
	
	/**
	 * 作业分片总数
	 * @return
	 */
	private int shardingTotalCount = 1;

	/**
	 * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
	 * <p>分片序列号从0开始，不可大于或等于作业分片总数<p>
	 * <p>如：<p>
	 * <p>0=a,1=b,2=c<p>
	 * @return
	 */
	private String shardingItemParameters = "";

	/**
	 * 作业自定义参数
	 * <p>作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业<p>
	 * <p>例：每次获取的数据量、作业实例从数据库读取的主键等<p>
	 * @return
	 */
	private String jobParameter = "";
	
	/**
	 * 是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
	 * @return
	 */
	private boolean failover = false;

	/**
	 * 是否开启错过任务重新执行
	 * @return
	 */
	private boolean misfire = false;
	
	/**
	 * 作业描述信息
	 * @return
	 */
	private String description = "";

	private boolean overwrite = false;

	/*********************DataflowJobConfiguration END********************/


	
	/*********************DataflowJobConfiguration START********************/
	
	/**
	 * 是否流式处理数据
	 * <p>如果流式处理数据, 则fetchData不返回空结果将持续执行作业<p>
	 * <p>如果非流式处理数据, 则处理数据完成后作业结束<p>
	 * @return
	 */
	private boolean streamingProcess = false;

	/*********************DataflowJobConfiguration END********************/
	
	
	
	/*********************ScriptJobConfiguration START********************/
	
	/**
	 * 脚本型作业执行命令行
	 * @return
	 */
	private String scriptCommandLine = "";
	
	/*********************ScriptJobConfiguration END********************/

	
	
	/*********************LiteJobConfiguration START********************/
	
	/**
	 * 监控作业运行时状态
	 * <p>每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。<p>
	 * <p>因为是瞬时状态，所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。<p>
	 * <p>每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。<p>
	 * @return
	 */
	private boolean monitorExecution = true;
	
	/**
	 * 作业监控端口
	 * <p>建议配置作业监控端口, 方便开发者dump作业信息。<p>
	 * <p>使用方法: echo “dump” | nc 127.0.0.1 9888<p>
	 * @return
	 */
	private int monitorPort = -1;
	
	/**
	 * 大允许的本机与注册中心的时间误差秒数
	 * <p>如果时间误差超过配置秒数则作业启动时将抛异常<p>
	 * <p>配置为-1表示不校验时间误差<p>
	 * @return
	 */
	private int maxTimeDiffSeconds = -1;
	
	/**
	 * 作业分片策略实现类全路径,默认使用平均分配策略
	 * @return
	 */
	private String jobShardingStrategyClass = "";
	
	/**
	 * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复,单位：分钟
	 * @return
	 */
	private int reconcileIntervalMinutes = 10;
	
	/**
	 * 作业事件追踪的数据源Bean引用
	 * @return
	 */
	private String eventTraceRdbDataSource = "";
	
	/*********************LiteJobConfiguration END********************/
	
	/**
	 * 前置后置任务监听实现类，需实现ElasticJobListener接口
	 * @return
	 */
	private String listener = "";
	
	/**
	 * 作业是否禁止启动,可用于部署作业时，先禁止启动，部署结束后统一启动
	 * @return
	 */
	private boolean disabled = false;
	
	/**
	 * 前置后置任务分布式监听实现类，需继承AbstractDistributeOnceElasticJobListener类
	 * @return
	 */
	private String distributedListener = "";
	
	/**
	 * 最后一个作业执行前的执行方法的超时时间,单位：毫秒
	 * @return
	 */
	private long startedTimeoutMilliseconds = Long.MAX_VALUE;
	
	/**
	 * 最后一个作业执行后的执行方法的超时时间,单位：毫秒
	 * @return
	 */
	private long completedTimeoutMilliseconds = Long.MAX_VALUE;
	
	/**
	 * 扩展属性
	 */
	private JobProperties jobProperties = new JobProperties();

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public int getShardingTotalCount() {
		return shardingTotalCount;
	}

	public void setShardingTotalCount(int shardingTotalCount) {
		this.shardingTotalCount = shardingTotalCount;
	}

	public String getShardingItemParameters() {
		return shardingItemParameters;
	}

	public void setShardingItemParameters(String shardingItemParameters) {
		this.shardingItemParameters = shardingItemParameters;
	}

	public String getJobParameter() {
		return jobParameter;
	}

	public void setJobParameter(String jobParameter) {
		this.jobParameter = jobParameter;
	}

	public boolean isFailover() {
		return failover;
	}

	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	public boolean isMisfire() {
		return misfire;
	}

	public void setMisfire(boolean misfire) {
		this.misfire = misfire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public boolean isStreamingProcess() {
		return streamingProcess;
	}

	public void setStreamingProcess(boolean streamingProcess) {
		this.streamingProcess = streamingProcess;
	}

	public String getScriptCommandLine() {
		return scriptCommandLine;
	}

	public void setScriptCommandLine(String scriptCommandLine) {
		this.scriptCommandLine = scriptCommandLine;
	}

	public boolean isMonitorExecution() {
		return monitorExecution;
	}

	public void setMonitorExecution(boolean monitorExecution) {
		this.monitorExecution = monitorExecution;
	}

	public int getMonitorPort() {
		return monitorPort;
	}

	public void setMonitorPort(int monitorPort) {
		this.monitorPort = monitorPort;
	}

	public int getMaxTimeDiffSeconds() {
		return maxTimeDiffSeconds;
	}

	public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
		this.maxTimeDiffSeconds = maxTimeDiffSeconds;
	}

	public String getJobShardingStrategyClass() {
		return jobShardingStrategyClass;
	}

	public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
		this.jobShardingStrategyClass = jobShardingStrategyClass;
	}

	public int getReconcileIntervalMinutes() {
		return reconcileIntervalMinutes;
	}

	public void setReconcileIntervalMinutes(int reconcileIntervalMinutes) {
		this.reconcileIntervalMinutes = reconcileIntervalMinutes;
	}

	public String getEventTraceRdbDataSource() {
		return eventTraceRdbDataSource;
	}

	public void setEventTraceRdbDataSource(String eventTraceRdbDataSource) {
		this.eventTraceRdbDataSource = eventTraceRdbDataSource;
	}

	public String getListener() {
		return listener;
	}

	public void setListener(String listener) {
		this.listener = listener;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDistributedListener() {
		return distributedListener;
	}

	public void setDistributedListener(String distributedListener) {
		this.distributedListener = distributedListener;
	}

	public long getStartedTimeoutMilliseconds() {
		return startedTimeoutMilliseconds;
	}

	public void setStartedTimeoutMilliseconds(long startedTimeoutMilliseconds) {
		this.startedTimeoutMilliseconds = startedTimeoutMilliseconds;
	}

	public long getCompletedTimeoutMilliseconds() {
		return completedTimeoutMilliseconds;
	}

	public void setCompletedTimeoutMilliseconds(long completedTimeoutMilliseconds) {
		this.completedTimeoutMilliseconds = completedTimeoutMilliseconds;
	}

	public JobProperties getJobProperties() {
		return jobProperties;
	}

	public void setJobProperties(JobProperties jobProperties) {
		this.jobProperties = jobProperties;
	}
}

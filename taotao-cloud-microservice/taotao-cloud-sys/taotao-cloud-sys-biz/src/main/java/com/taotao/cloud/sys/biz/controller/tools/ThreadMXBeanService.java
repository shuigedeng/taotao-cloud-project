package com.taotao.cloud.sys.biz.controller.tools;

import com.sun.management.ThreadMXBean;
import com.taotao.cloud.sys.api.dto.jvm.InvokeParam;
import com.taotao.cloud.sys.api.dto.jvm.ThreadPoolInfo;
import com.taotao.cloud.sys.biz.controller.tools.MBeanMonitorService;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ReflectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThreadMXBeanService {
    @Autowired
    private MBeanMonitorService mBeanMonitorService;

    public ThreadInfo[] threads(String connName) throws IOException {
        final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
        ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
        final long[] allThreadIds = threadMXBean.getAllThreadIds();
        final ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(allThreadIds);
        return threadInfo;
    }

    public ThreadInfo thread(String connName,long threadId) throws IOException {
        final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
        ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
        return threadMXBean.getThreadInfo(threadId,100);
    }

    /**
     * 将线程进行分组, 如前 10 位是一致的, 则为一组, 当线程池处理
     * @param connName
     * @return
     * @throws IOException
     */
    public List<ThreadPoolInfo> threadPoolInfos(String connName) throws IOException {
        List<ThreadPoolInfo> threadPoolInfos = new ArrayList<>();

        final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
        ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);
        final long[] allThreadIds = threadMXBean.getAllThreadIds();
        final ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(allThreadIds);

        // 获取所有的线程名, 并照字典序排序
        final List<ThreadPoolInfo.ThreadInfo> threadSimpleInfo = Arrays.stream(threadInfo).map(th -> new ThreadPoolInfo.ThreadInfo(th.getThreadId(),th.getThreadName(),th.getThreadState())).collect(Collectors.toList());
        Collections.sort(threadSimpleInfo);

        int j = 0 ;
        for (int i = 0; i < threadSimpleInfo.size(); i = j) {
            List<ThreadPoolInfo.ThreadInfo> group = new ArrayList<>();
            group.add(threadSimpleInfo.get(i));
            final ThreadPoolInfo.ThreadInfo needCompareThread = threadSimpleInfo.get(i);
            for (j = (i+1); j < threadSimpleInfo.size(); j++) {
                final String prefix = longestCommonPrefix(Arrays.asList(needCompareThread.getName(), threadSimpleInfo.get(j).getName()));
                if (prefix.length() > 10){
                    // 超过 10 位相同的为一组
                    group.add(threadSimpleInfo.get(j));
                }else{
                    break;
                }
            }

            // 求当前组的最大公共前缀
            final String maxPublicPrefix = longestCommonPrefix(group.stream().map(ThreadPoolInfo.ThreadInfo::getName).collect(Collectors.toList()));
            final ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo(maxPublicPrefix);
            threadPoolInfo.setChildren(group);
            threadPoolInfos.add(threadPoolInfo);
        }

        return threadPoolInfos;
    }

    /**
     * 检查死锁
     * @param connName
     * @return
     */
    public ThreadInfo[] checkDeadLock(String connName) throws IOException, IntrospectionException, ReflectionException, InstanceNotFoundException, MBeanException, ClassNotFoundException {
        final MBeanServerConnection mBeanServerConnection = mBeanMonitorService.loadMBeanConnection(connName);
        ThreadMXBean threadMXBean = JMX.newMXBeanProxy(mBeanServerConnection, mBeanMonitorService.createBeanName(ManagementFactory.THREAD_MXBEAN_NAME), ThreadMXBean.class);

        final InvokeParam findDeadlockedThreads = new InvokeParam(connName, "java.lang:type=Threading", "findDeadlockedThreads");
        final long[] deadLockThreadIds = (long[])mBeanMonitorService.invokeMBean(findDeadlockedThreads);
        if (deadLockThreadIds != null && deadLockThreadIds.length > 0) {
            return threadMXBean.getThreadInfo(deadLockThreadIds);
        }
        return null;
    }

    /**
     * 查询字符串列表中的最大公共前缀
     * @param strs
     * @return
     */
    String longestCommonPrefix(List<String> strs) {
		if (strs == null || strs.size() == 0) {
			return "";
		}
		if (strs.size() == 1){
		    return strs.get(0);
        }
		String ans = strs.get(0);
		for (int j = 1; j < strs.size(); j++) {
			int i = 0;
			for (; i < ans.length() && i < strs.get(j).length(); i++) {
				if (ans.charAt(i) != strs.get(j).charAt(i)) {
					break;
				}
			}
			ans = ans.substring(0, i);
			if ("".equals(ans)) {
				return "";
			}
		}
		return ans;
	}
}

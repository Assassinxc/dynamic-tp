package com.dtp.core.converter;

import com.dtp.core.support.ExecutorWrapper;
import com.dtp.common.entity.ThreadPoolStats;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.support.ExecutorAdapter;

/**
 * MetricsConverter related
 *
 * @author yanhom
 * @since 1.0.0
 **/
public class MetricsConverter {

    private MetricsConverter() { }

    public static ThreadPoolStats convert(DtpExecutor executor) {
        if (executor == null) {
            return null;
        }
        ThreadPoolStats poolStats = convertCommon(executor);
        poolStats.setPoolName(executor.getThreadPoolName());
        poolStats.setRejectHandlerName(executor.getRejectHandlerName());
        poolStats.setRejectCount(executor.getRejectCount());
        poolStats.setRunTimeoutCount(executor.getRunTimeoutCount().sum());
        poolStats.setQueueTimeoutCount(executor.getQueueTimeoutCount().sum());
        poolStats.setDynamic(true);
        return poolStats;
    }

    public static ThreadPoolStats convert(ExecutorWrapper wrapper) {
        ExecutorAdapter<?> executor = wrapper.getExecutor();
        if (executor == null) {
            return null;
        }
        ThreadPoolStats poolStats = convertCommon(executor);
        poolStats.setPoolName(wrapper.getThreadPoolName());
        if (executor instanceof DtpExecutor) {
            DtpExecutor dtpExecutor = (DtpExecutor) executor;
            poolStats.setRejectHandlerName(dtpExecutor.getRejectHandlerName());
            poolStats.setRejectCount(dtpExecutor.getRejectCount());
            poolStats.setRunTimeoutCount(dtpExecutor.getRunTimeoutCount().sum());
            poolStats.setQueueTimeoutCount(dtpExecutor.getQueueTimeoutCount().sum());
        }
//        poolStats.setDynamic(false);
        return poolStats;
    }

    public static ThreadPoolStats convertCommon(ExecutorAdapter<?> executor) {
        return ThreadPoolStats.builder()
                .corePoolSize(executor.getCorePoolSize())
                .maximumPoolSize(executor.getMaximumPoolSize())
                .poolSize(executor.getPoolSize())
                .activeCount(executor.getActiveCount())
                .taskCount(executor.getTaskCount())
                .queueType(executor.getQueue().getClass().getSimpleName())
                .queueCapacity(executor.getQueue().size() + executor.getQueue().remainingCapacity())
                .queueSize(executor.getQueue().size())
                .queueRemainingCapacity(executor.getQueue().remainingCapacity())
                .completedTaskCount(executor.getCompletedTaskCount())
                .largestPoolSize(executor.getLargestPoolSize())
                .waitTaskCount(executor.getQueue().size())
                .build();
    }
}

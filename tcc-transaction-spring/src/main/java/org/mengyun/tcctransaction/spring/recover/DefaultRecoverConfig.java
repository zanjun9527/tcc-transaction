package org.mengyun.tcctransaction.spring.recover;

import org.mengyun.tcctransaction.OptimisticLockException;
import org.mengyun.tcctransaction.recover.RecoverConfig;

import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by changming.xie on 6/1/16.
 */
public class DefaultRecoverConfig implements RecoverConfig {

    public static final RecoverConfig INSTANCE = new DefaultRecoverConfig();

    private int maxRetryCount = 30;

    private int recoverDuration = 120; //120 seconds

    private String cronExpression = "0 */1 * * * ?";

    private int asyncTerminateThreadCorePoolSize = 512;

    private int asyncTerminateThreadMaxPoolSize = 1024;

    private int asyncTerminateThreadWorkQueueSize = 512;

    private Set<Class<? extends Exception>> delayCancelExceptions = new HashSet<Class<? extends Exception>>();

    public DefaultRecoverConfig() {
        delayCancelExceptions.add(OptimisticLockException.class);
        delayCancelExceptions.add(SocketTimeoutException.class);
    }

    @Override
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * 默认120秒
     * @return
     */
    @Override
    public int getRecoverDuration() {
        return recoverDuration;
    }

    @Override
    public String getCronExpression() {
        return cronExpression;
    }


    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public void setRecoverDuration(int recoverDuration) {
        this.recoverDuration = recoverDuration;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void setAsyncTerminateThreadCorePoolSize(int asyncTerminateThreadCorePoolSize) {
        this.asyncTerminateThreadCorePoolSize = asyncTerminateThreadCorePoolSize;
    }

    public void setAsyncTerminateThreadMaxPoolSize(int asyncTerminateThreadMaxPoolSize) {
        this.asyncTerminateThreadMaxPoolSize = asyncTerminateThreadMaxPoolSize;
    }

    public void setAsyncTerminateThreadWorkQueueSize(int asyncTerminateThreadWorkQueueSize) {
        this.asyncTerminateThreadWorkQueueSize = asyncTerminateThreadWorkQueueSize;
    }

    @Override
    public void setDelayCancelExceptions(Set<Class<? extends Exception>> delayCancelExceptions) {
        this.delayCancelExceptions.addAll(delayCancelExceptions);
    }

    @Override
    public Set<Class<? extends Exception>> getDelayCancelExceptions() {
        return this.delayCancelExceptions;
    }

    @Override
    public int getAsyncTerminateThreadCorePoolSize() {
        return this.asyncTerminateThreadCorePoolSize;
    }

    @Override
    public int getAsyncTerminateThreadMaxPoolSize() {
        return this.asyncTerminateThreadMaxPoolSize;
    }

    @Override
    public int getAsyncTerminateThreadWorkQueueSize() {
        return this.asyncTerminateThreadWorkQueueSize;
    }

}

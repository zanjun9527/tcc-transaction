package org.mengyun.tcctransaction.recover;

import java.util.Set;

/**
 * Created by changming.xie on 6/1/16.
 */
public interface RecoverConfig {

    public int getMaxRetryCount();

    /**
     * 默认是120秒
     * @return
     */
    public int getRecoverDuration();

    public String getCronExpression();

    public Set<Class<? extends Exception>> getDelayCancelExceptions();

    public void setDelayCancelExceptions(Set<Class<? extends Exception>> delayRecoverExceptions);

    public int getAsyncTerminateThreadCorePoolSize();

    public int getAsyncTerminateThreadMaxPoolSize();

    public int getAsyncTerminateThreadWorkQueueSize();
}

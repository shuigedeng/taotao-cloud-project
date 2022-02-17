package com.taotao.cloud.redis.delay.config;

import com.taotao.cloud.redis.delay.consts.ServerType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.Assert;


@RefreshScope
@ConfigurationProperties(prefix = "spring.smart-redisson")
public class RedissonProperties {

    private ServerType serverType = ServerType.SINGLE;

    private String serverAddress = "redis://localhost:6379";

    private String password = "";

    private int database = 0;

    private int threads = 16;

    private int nettyThreads = 32;

    private int lockWatchdogTimeoutMillis = 30000;

    private int pingTimeoutMillis = 1000;

    private int connectTimeoutMillis = 10000;

    private int socketTimeoutMillis = 3000;

    private boolean keepAlive;

    private int retryAttempts = 3;

    private int retryIntervalMillis = 1500;

    private int maxPoolSize = 64;

    private int minIdleSize = 24;

    private int maxIdleMillis = 10000;

    private MasterSlaveProperties master;

    private MasterSlaveProperties slave;

    public void setDatabase(int database) {
        Assert.isTrue(database >= 0, "database must be gte 0");
        this.database = database;
    }

    /**
     * @param threads 为零表示cpu核数的2倍线程
     */
    public void setThreads(int threads) {
        Assert.isTrue(threads >= 0, "threads must be gte 0");
        this.threads = threads;
    }

    /**
     * @param nettyThreads 为零表示cpu核数的2倍线程
     */
    public void setNettyThreads(int nettyThreads) {
        Assert.isTrue(nettyThreads >= 0, "threads must be gte 0");
        this.nettyThreads = nettyThreads;
    }

    public void setLockWatchdogTimeoutMillis(int lockWatchdogTimeoutMillis) {
        Assert.isTrue(lockWatchdogTimeoutMillis > 0, "lockWatchdogTimeoutMillis must be gt 0");
        this.lockWatchdogTimeoutMillis = lockWatchdogTimeoutMillis;
    }


    public void setPingTimeoutMillis(int pingTimeoutMillis) {
        Assert.isTrue(pingTimeoutMillis > 0, "pingTimeoutMillis must be gt 0");
        this.pingTimeoutMillis = pingTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        Assert.isTrue(connectTimeoutMillis > 0, "connectTimeoutMillis must be gt 0");
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setSocketTimeoutMillis(int socketTimeoutMillis) {
        Assert.isTrue(socketTimeoutMillis > 0, "socketTimeoutMillis must be gt 0");
        this.socketTimeoutMillis = socketTimeoutMillis;
    }

    public void setRetryAttempts(int retryAttempts) {
        Assert.isTrue(retryAttempts >= 0, "retryAttempts must be gte 0");
        this.retryAttempts = retryAttempts;
    }

    public void setRetryIntervalMillis(int retryIntervalMillis) {
        Assert.isTrue(retryIntervalMillis > 0, "retryIntervalMillis must be gt 0");
        this.retryIntervalMillis = retryIntervalMillis;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        Assert.isTrue(maxPoolSize > 0, "maxPoolSize must be gt 0");
        this.maxPoolSize = maxPoolSize;
    }

    public void setMinIdleSize(int minIdleSize) {
        Assert.isTrue(minIdleSize >= 0, "minIdleSize must be gte 0");
        this.minIdleSize = minIdleSize;
    }

    public void setMaxIdleMillis(int maxIdleMillis) {
        Assert.isTrue(maxIdleMillis >= 0, "maxIdleMillis must be gte 0");
        this.maxIdleMillis = maxIdleMillis;
    }

    public static class MasterSlaveProperties {

        private int maxPoolSize = 64;

        private int minIdleSize = 24;

        public void setMaxPoolSize(int maxPoolSize) {
            Assert.isTrue(maxPoolSize > 0, "maxPoolSize must be gt 0");
            this.maxPoolSize = maxPoolSize;
        }

        public void setMinIdleSize(int minIdleSize) {
            Assert.isTrue(minIdleSize >= 0, "minIdleSize must be gte 0");
            this.minIdleSize = minIdleSize;
        }

	    public int getMaxPoolSize() {
		    return maxPoolSize;
	    }

	    public int getMinIdleSize() {
		    return minIdleSize;
	    }
    }

	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public int getThreads() {
		return threads;
	}

	public int getNettyThreads() {
		return nettyThreads;
	}

	public int getLockWatchdogTimeoutMillis() {
		return lockWatchdogTimeoutMillis;
	}

	public int getPingTimeoutMillis() {
		return pingTimeoutMillis;
	}

	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public int getSocketTimeoutMillis() {
		return socketTimeoutMillis;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public int getRetryAttempts() {
		return retryAttempts;
	}

	public int getRetryIntervalMillis() {
		return retryIntervalMillis;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public int getMinIdleSize() {
		return minIdleSize;
	}

	public int getMaxIdleMillis() {
		return maxIdleMillis;
	}

	public MasterSlaveProperties getMaster() {
		return master;
	}

	public void setMaster(
		MasterSlaveProperties master) {
		this.master = master;
	}

	public MasterSlaveProperties getSlave() {
		return slave;
	}

	public void setSlave(MasterSlaveProperties slave) {
		this.slave = slave;
	}
}

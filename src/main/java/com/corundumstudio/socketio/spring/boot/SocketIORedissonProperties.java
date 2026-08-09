/*
 * Copyright (c) 2010-2020, hiwepy (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.corundumstudio.socketio.spring.boot;

import org.redisson.config.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Redisson-backed Socket.IO session store.
 *
 * <p>Binds properties under the {@code socket-io.cache.redisson} prefix and exposes
 * the Redisson server configuration (single, sentinel, master/slave, replicated or
 * cluster) along with thread pool, transport and cleanup tuning options.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(SocketIORedissonProperties.PREFIX)
public class SocketIORedissonProperties {

	/** Configuration property prefix shared by all Redisson store properties. */
	public static final String PREFIX = "socket-io.cache.redisson";

	/**
	 * Supported Redis deployment topologies for the Redisson client.
	 */
	public enum RedisServerMode {

		/**
		 * Cluster Mode.
		 */
		CLUSTER,
		/**
		 * Master Slave  Mode
		 */
		MASTERSLAVE,
		/**
		 * Replicated  Mode
		 */
		REPLICATED,
		/**
		 * Sentinel  Mode
		 */
		SENTINEL,
		/**
		 * Single  Mode
		 */
		SINGLE,

	}

	/**
	 * Enable SocketIO Redis Store With Redisson.
	 */
	private boolean enabled = false;

	/**
	 * Threads amount shared between all redis node clients
	 */
	private int threads = 16;

	/**
	 * Number of Netty threads shared between all Redis node clients. Defaults to {@code 32}.
	 */
	private int nettyThreads = 32;

	/**
	 * Config option for enabling Redisson Reference feature. Default value is TRUE
	 */
	private boolean referenceEnabled = true;

	/**
	 * Transport mode used by the Redisson client. Defaults to {@link TransportMode#NIO}.
	 */
	private TransportMode transportMode = TransportMode.NIO;

	/**
	 * Active Redis deployment topology. Defaults to {@link RedisServerMode#SINGLE}.
	 */
	private RedisServerMode server = RedisServerMode.SINGLE;

	/**
	 * Lock watchdog timeout in milliseconds used for distributed lock expiration. Defaults to {@code 30000}.
	 */
	private long lockWatchdogTimeout = 30 * 1000;

	/**
	 * Whether to preserve pub/sub message order across subscriptions. Defaults to {@code true}.
	 */
	private boolean keepPubSubOrder = true;

	/**
	 * Whether messages should be decoded in a separate executor. Defaults to {@code false}.
	 */
	private boolean decodeInExecutor = false;

	/**
	 * Whether to cache Lua scripts on the Redis side. Defaults to {@code false}.
	 */
	private boolean useScriptCache = false;

	/**
	 * Minimum delay (in seconds) between cleanup runs. Defaults to {@code 5}.
	 */
	private int minCleanUpDelay = 5;
	/**
	 * Maximum delay (in seconds) between cleanup runs. Defaults to {@code 1800}.
	 */
	private int maxCleanUpDelay = 30 * 60;

	/**
	 * Sentinel-mode Redisson server configuration.
	 */
	private SentinelServersConfig sentinel;

	/**
	 * Master/slave-mode Redisson server configuration.
	 */
	private MasterSlaveServersConfig masterSlave;

	/**
	 * Single-node Redisson server configuration.
	 */
	private SingleServerConfig single;

	/**
	 * Cluster-mode Redisson server configuration.
	 */
	private ClusterServersConfig cluster;

	/**
	 * Replicated-mode Redisson server configuration.
	 */
	private ReplicatedServersConfig replicated;

	/** Returns whether the enabled is enabled.
	 * @return the result
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/** Sets the enabled.
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/** Returns the threads.
	 * @return the result
	 */
	public int getThreads() {
		return threads;
	}

	/** Sets the threads.
	 * @param threads the threads
	 */
	public void setThreads(int threads) {
		this.threads = threads;
	}

	/** Returns the netty threads.
	 * @return the result
	 */
	public int getNettyThreads() {
		return nettyThreads;
	}

	/** Sets the netty threads.
	 * @param nettyThreads the nettyThreads
	 */
	public void setNettyThreads(int nettyThreads) {
		this.nettyThreads = nettyThreads;
	}

	/** Returns whether the reference enabled is enabled.
	 * @return the result
	 */
	public boolean isReferenceEnabled() {
		return referenceEnabled;
	}

	/** Sets the reference enabled.
	 * @param referenceEnabled the referenceEnabled
	 */
	public void setReferenceEnabled(boolean referenceEnabled) {
		this.referenceEnabled = referenceEnabled;
	}

	/** Returns the transport mode.
	 * @return the result
	 */
	public TransportMode getTransportMode() {
		return transportMode;
	}

	/** Sets the transport mode.
	 * @param transportMode the transportMode
	 */
	public void setTransportMode(TransportMode transportMode) {
		this.transportMode = transportMode;
	}

	/** Returns the server.
	 * @return the result
	 */
	public RedisServerMode getServer() {
		return server;
	}

	/** Sets the server.
	 * @param server the server
	 */
	public void setServer(RedisServerMode server) {
		this.server = server;
	}

	/** Returns the lock watchdog timeout.
	 * @return the result
	 */
	public long getLockWatchdogTimeout() {
		return lockWatchdogTimeout;
	}

	/** Sets the lock watchdog timeout.
	 * @param lockWatchdogTimeout the lockWatchdogTimeout
	 */
	public void setLockWatchdogTimeout(long lockWatchdogTimeout) {
		this.lockWatchdogTimeout = lockWatchdogTimeout;
	}

	/** Returns whether the keep pub sub order is enabled.
	 * @return the result
	 */
	public boolean isKeepPubSubOrder() {
		return keepPubSubOrder;
	}

	/** Sets the keep pub sub order.
	 * @param keepPubSubOrder the keepPubSubOrder
	 */
	public void setKeepPubSubOrder(boolean keepPubSubOrder) {
		this.keepPubSubOrder = keepPubSubOrder;
	}

	/** Returns whether the decode in executor is enabled.
	 * @return the result
	 */
	public boolean isDecodeInExecutor() {
		return decodeInExecutor;
	}

	/** Sets the decode in executor.
	 * @param decodeInExecutor the decodeInExecutor
	 */
	public void setDecodeInExecutor(boolean decodeInExecutor) {
		this.decodeInExecutor = decodeInExecutor;
	}

	/** Returns whether the use script cache is enabled.
	 * @return the result
	 */
	public boolean isUseScriptCache() {
		return useScriptCache;
	}

	/** Sets the use script cache.
	 * @param useScriptCache the useScriptCache
	 */
	public void setUseScriptCache(boolean useScriptCache) {
		this.useScriptCache = useScriptCache;
	}

	/** Returns the min clean up delay.
	 * @return the result
	 */
	public int getMinCleanUpDelay() {
		return minCleanUpDelay;
	}

	/** Sets the min clean up delay.
	 * @param minCleanUpDelay the minCleanUpDelay
	 */
	public void setMinCleanUpDelay(int minCleanUpDelay) {
		this.minCleanUpDelay = minCleanUpDelay;
	}

	/** Returns the max clean up delay.
	 * @return the result
	 */
	public int getMaxCleanUpDelay() {
		return maxCleanUpDelay;
	}

	/** Sets the max clean up delay.
	 * @param maxCleanUpDelay the maxCleanUpDelay
	 */
	public void setMaxCleanUpDelay(int maxCleanUpDelay) {
		this.maxCleanUpDelay = maxCleanUpDelay;
	}

	/** Returns the sentinel.
	 * @return the result
	 */
	public SentinelServersConfig getSentinel() {
		return sentinel;
	}

	/** Sets the sentinel.
	 * @param sentinel the sentinel
	 */
	public void setSentinel(SentinelServersConfig sentinel) {
		this.sentinel = sentinel;
	}

	/** Returns the master slave.
	 * @return the result
	 */
	public MasterSlaveServersConfig getMasterSlave() {
		return masterSlave;
	}

	/** Sets the master slave.
	 * @param masterSlave the masterSlave
	 */
	public void setMasterSlave(MasterSlaveServersConfig masterSlave) {
		this.masterSlave = masterSlave;
	}

	/** Returns the single.
	 * @return the result
	 */
	public SingleServerConfig getSingle() {
		return single;
	}

	/** Sets the single.
	 * @param single the single
	 */
	public void setSingle(SingleServerConfig single) {
		this.single = single;
	}

	/** Returns the cluster.
	 * @return the result
	 */
	public ClusterServersConfig getCluster() {
		return cluster;
	}

	/** Sets the cluster.
	 * @param cluster the cluster
	 */
	public void setCluster(ClusterServersConfig cluster) {
		this.cluster = cluster;
	}

	/** Returns the replicated.
	 * @return the result
	 */
	public ReplicatedServersConfig getReplicated() {
		return replicated;
	}

	/** Sets the replicated.
	 * @param replicated the replicated
	 */
	public void setReplicated(ReplicatedServersConfig replicated) {
		this.replicated = replicated;
	}

}

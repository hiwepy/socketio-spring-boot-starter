/*
 * Copyright (c) 2010-2020, hiwepy (https://github.com/hiwepy).
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

import org.redisson.config.ClusterServersConfig;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(SocketioRedissonProperties.PREFIX)
public class SocketioRedissonProperties {

	public static final String PREFIX = "socketio.redis.redisson";

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
	 * Enable Socketio Redis Store With Redisson.
	 */
	private boolean enabled = false;

	/**
	 * Threads amount shared between all redis node clients
	 */
	private int threads = 16;

	private int nettyThreads = 32;

	/**
	 * Config option for enabling Redisson Reference feature. Default value is TRUE
	 */
	private boolean referenceEnabled = true;

	private TransportMode transportMode = TransportMode.NIO;

	private RedisServerMode server = RedisServerMode.SINGLE;

	private long lockWatchdogTimeout = 30 * 1000;

	private boolean keepPubSubOrder = true;

	private boolean decodeInExecutor = false;

	private boolean useScriptCache = false;

	private int minCleanUpDelay = 5;
	private int maxCleanUpDelay = 30 * 60;

	private SentinelServersConfig sentinel;

	private MasterSlaveServersConfig masterSlave;

	private SingleServerConfig single;

	private ClusterServersConfig cluster;

	private ReplicatedServersConfig replicated;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getNettyThreads() {
		return nettyThreads;
	}

	public void setNettyThreads(int nettyThreads) {
		this.nettyThreads = nettyThreads;
	}

	public boolean isReferenceEnabled() {
		return referenceEnabled;
	}

	public void setReferenceEnabled(boolean referenceEnabled) {
		this.referenceEnabled = referenceEnabled;
	}

	public TransportMode getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(TransportMode transportMode) {
		this.transportMode = transportMode;
	}

	public RedisServerMode getServer() {
		return server;
	}

	public void setServer(RedisServerMode server) {
		this.server = server;
	}

	public long getLockWatchdogTimeout() {
		return lockWatchdogTimeout;
	}

	public void setLockWatchdogTimeout(long lockWatchdogTimeout) {
		this.lockWatchdogTimeout = lockWatchdogTimeout;
	}

	public boolean isKeepPubSubOrder() {
		return keepPubSubOrder;
	}

	public void setKeepPubSubOrder(boolean keepPubSubOrder) {
		this.keepPubSubOrder = keepPubSubOrder;
	}

	public boolean isDecodeInExecutor() {
		return decodeInExecutor;
	}

	public void setDecodeInExecutor(boolean decodeInExecutor) {
		this.decodeInExecutor = decodeInExecutor;
	}

	public boolean isUseScriptCache() {
		return useScriptCache;
	}

	public void setUseScriptCache(boolean useScriptCache) {
		this.useScriptCache = useScriptCache;
	}

	public int getMinCleanUpDelay() {
		return minCleanUpDelay;
	}

	public void setMinCleanUpDelay(int minCleanUpDelay) {
		this.minCleanUpDelay = minCleanUpDelay;
	}

	public int getMaxCleanUpDelay() {
		return maxCleanUpDelay;
	}

	public void setMaxCleanUpDelay(int maxCleanUpDelay) {
		this.maxCleanUpDelay = maxCleanUpDelay;
	}

	public SentinelServersConfig getSentinel() {
		return sentinel;
	}

	public void setSentinel(SentinelServersConfig sentinel) {
		this.sentinel = sentinel;
	}

	public MasterSlaveServersConfig getMasterSlave() {
		return masterSlave;
	}

	public void setMasterSlave(MasterSlaveServersConfig masterSlave) {
		this.masterSlave = masterSlave;
	}

	public SingleServerConfig getSingle() {
		return single;
	}

	public void setSingle(SingleServerConfig single) {
		this.single = single;
	}

	public ClusterServersConfig getCluster() {
		return cluster;
	}

	public void setCluster(ClusterServersConfig cluster) {
		this.cluster = cluster;
	}

	public ReplicatedServersConfig getReplicated() {
		return replicated;
	}

	public void setReplicated(ReplicatedServersConfig replicated) {
		this.replicated = replicated;
	}

}

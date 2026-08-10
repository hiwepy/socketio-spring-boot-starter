/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
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
package org.redisson.config;

/**
 * Extended Redisson {@link Config} that exposes convenient constructors for the
 * supported Redis deployment topologies.
 *
 * <p>Provides convenience constructors for cluster, master/slave, replicated,
 * sentinel and single-server modes, so that the active topology can be selected at
 * construction time from the bound Socket.IO properties.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

public class RedissonConfig extends Config {

	/**
	 * Construct an empty Redisson configuration.
	 */
	public RedissonConfig() {
		super();
	}

	/**
	 * Construct a Redisson configuration by copying the settings of an existing one.
	 * @param oldConf the configuration to copy
	 */
	public RedissonConfig(Config oldConf) {
		super(oldConf);
	}

	/**
	 * Construct a Redisson configuration pre-configured for cluster mode.
	 * @param clusterServersConfig the cluster server configuration
	 */
	public RedissonConfig(ClusterServersConfig clusterServersConfig) {
		super();
		useClusterServers(clusterServersConfig);
	}

	/**
	 * Construct a Redisson configuration pre-configured for master/slave mode.
	 * @param masterSlaveServersConfig the master/slave server configuration
	 */
	public RedissonConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
		super();
		useMasterSlaveServers(masterSlaveServersConfig);
	}

	/**
	 * Construct a Redisson configuration pre-configured for replicated mode.
	 * @param replicatedServersConfig the replicated server configuration
	 */
	public RedissonConfig(ReplicatedServersConfig replicatedServersConfig) {
		super();
		useReplicatedServers(replicatedServersConfig);
	}

	/**
	 * Construct a Redisson configuration pre-configured for sentinel mode.
	 * @param sentinelServersConfig the sentinel server configuration
	 */
	public RedissonConfig(SentinelServersConfig sentinelServersConfig) {
		super();
		useSentinelServers(sentinelServersConfig);
	}

	/**
	 * Construct a Redisson configuration pre-configured for single-server mode.
	 * @param singleServerConfig the single server configuration
	 */
	public RedissonConfig(SingleServerConfig singleServerConfig) {
		super();
		useSingleServer(singleServerConfig);
	}

	/**
	 * Construct a Redisson configuration carrying all supported topology
	 * configurations at once, so the active one can be selected later.
	 * @param clusterServersConfig the cluster server configuration
	 * @param masterSlaveServersConfig the master/slave server configuration
	 * @param replicatedServersConfig the replicated server configuration
	 * @param sentinelServersConfig the sentinel server configuration
	 * @param singleServerConfig the single server configuration
	 */
	public RedissonConfig(ClusterServersConfig clusterServersConfig,
			MasterSlaveServersConfig masterSlaveServersConfig,
			ReplicatedServersConfig replicatedServersConfig,
			SentinelServersConfig sentinelServersConfig,
			SingleServerConfig singleServerConfig) {
		super();
		setClusterServersConfig(clusterServersConfig);
		setMasterSlaveServersConfig(masterSlaveServersConfig);
		setReplicatedServersConfig(replicatedServersConfig);
		setSentinelServersConfig(sentinelServersConfig);
		setSingleServerConfig(singleServerConfig);
	}


}

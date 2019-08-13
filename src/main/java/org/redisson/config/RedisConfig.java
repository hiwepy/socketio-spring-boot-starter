/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
 * TODO
 * 
 * @author ： <a href="https://github.com/vindell">wandl</a>
 */

public class RedisConfig extends Config {

	public RedisConfig() {
		super();
	}

	public RedisConfig(Config oldConf) {
		super(oldConf);
	}

	public RedisConfig(ClusterServersConfig clusterServersConfig) {
		super();
		useClusterServers(clusterServersConfig);
	}

	public RedisConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
		super();
		useMasterSlaveServers(masterSlaveServersConfig);
	}

	public RedisConfig(ReplicatedServersConfig replicatedServersConfig) {
		super();
		useReplicatedServers(replicatedServersConfig);
	}

	public RedisConfig(SentinelServersConfig sentinelServersConfig) {
		super();
		useSentinelServers(sentinelServersConfig);
	}

	public RedisConfig(SingleServerConfig singleServerConfig) {
		super();
		useSingleServer(singleServerConfig);
	}
	
	public RedisConfig(ClusterServersConfig clusterServersConfig,
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

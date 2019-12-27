/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
 * @author ： <a href="https://github.com/hiwepy">wandl</a>
 */

public class RedissonConfig extends Config {

	public RedissonConfig() {
		super();
	}

	public RedissonConfig(Config oldConf) {
		super(oldConf);
	}

	public RedissonConfig(ClusterServersConfig clusterServersConfig) {
		super();
		useClusterServers(clusterServersConfig);
	}

	public RedissonConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
		super();
		useMasterSlaveServers(masterSlaveServersConfig);
	}

	public RedissonConfig(ReplicatedServersConfig replicatedServersConfig) {
		super();
		useReplicatedServers(replicatedServersConfig);
	}

	public RedissonConfig(SentinelServersConfig sentinelServersConfig) {
		super();
		useSentinelServers(sentinelServersConfig);
	}

	public RedissonConfig(SingleServerConfig singleServerConfig) {
		super();
		useSingleServer(singleServerConfig);
	}
	
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

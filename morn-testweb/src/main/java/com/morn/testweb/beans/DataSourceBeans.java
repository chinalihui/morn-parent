/**
 * Copyright 2016- the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morn.testweb.beans;

import org.apache.commons.dbcp.BasicDataSource;
import org.mornframework.context.beans.annotation.Bean;
import org.mornframework.context.beans.annotation.Beans;
import org.mornframework.context.beans.annotation.Property;

@Beans
public abstract class DataSourceBeans {

	@Bean(propertys={
			@Property(name="driverClassName",value="${oracle.driver}"),
			@Property(name="url",value="${oracle.url}"),
			@Property(name="username",value="${oracle.username}"),
			@Property(name="password",value="${oracle.password}")
	})
	BasicDataSource core_oracle_ds_rw;
	
	@Bean(propertys={
			@Property(name="driverClassName",value="${oracle.driver}"),
			@Property(name="url",value="${local.oracle.url}"),
			@Property(name="username",value="${local.oracle.username}"),
			@Property(name="password",value="${local.oracle.password}")
	})
	BasicDataSource local_dataSource;
	
	@Bean(propertys={
			@Property(name="driverClassName",value="${mysql.driver}"),
			@Property(name="url",value="${mysql.url}"),
			@Property(name="username",value="${mysql.username}"),
			@Property(name="password",value="${mysql.password}")
	})
	BasicDataSource mysql_dataSource;
	
}

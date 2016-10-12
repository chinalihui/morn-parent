/**
* Copyright 2016-2026 the original author or authors.
*
* Welcome hot like Java Person,Contribution code for org.
* This framework Has been published on Git
*     https://github.com/chinalihui/morn-parent,
*     https://git.oschina.net/osjeff/morn-parent
* 
* If there are problems or ideas can be submitted to the
*     https://github.com/chinalihui/morn-parent/issues
*
*                                    Thanks.
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
	
}

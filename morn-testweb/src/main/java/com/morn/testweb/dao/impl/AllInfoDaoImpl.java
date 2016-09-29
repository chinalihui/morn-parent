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

package com.morn.testweb.dao.impl;

import org.mornframework.context.annotation.Dao;

import com.morn.testweb.dao.IAllInfoDao;

@Dao
public class AllInfoDaoImpl implements IAllInfoDao{

	public String search(String id) {
		
		return "Dao:"+id;
	}

}

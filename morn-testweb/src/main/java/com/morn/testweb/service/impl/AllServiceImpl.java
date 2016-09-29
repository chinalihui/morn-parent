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

package com.morn.testweb.service.impl;

import org.mornframework.context.annotation.Inject;
import org.mornframework.context.annotation.Service;

import com.morn.testweb.dao.IAllInfoDao;
import com.morn.testweb.service.IAllService;

@Service
public class AllServiceImpl implements IAllService{
	
	@Inject
	private IAllInfoDao allInfoDao;
	
	public String search(String name) {
		System.out.println(allInfoDao);
		return allInfoDao.search(name);
	}

}

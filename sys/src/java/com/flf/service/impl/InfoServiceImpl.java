package com.flf.service.impl;

import java.util.List;

import com.flf.entity.Info;
import com.flf.entity.Page;
import com.flf.mapper.InfoMapper;
import com.flf.service.InfoService;

public class InfoServiceImpl implements InfoService{

	private InfoMapper infoMapper;
	
	public List<Info> listPageInfo(Page page) {
		// TODO Auto-generated method stub
		return infoMapper.listPageInfo(page);
	}

	public InfoMapper getInfoMapper() {
		return infoMapper;
	}

	public void setInfoMapper(InfoMapper infoMapper) {
		this.infoMapper = infoMapper;
	}
}

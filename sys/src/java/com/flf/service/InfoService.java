package com.flf.service;

import java.util.List;

import com.flf.entity.Info;
import com.flf.entity.Page;

public interface InfoService {
	List<Info> listPageInfo(Page page);
}

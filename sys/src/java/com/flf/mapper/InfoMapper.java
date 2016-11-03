package com.flf.mapper;

import java.util.List;


import com.flf.entity.Info;
import com.flf.entity.Page;
public interface InfoMapper {
	List<Info> listPageInfo(Page page);
}

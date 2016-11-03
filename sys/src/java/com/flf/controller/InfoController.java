package com.flf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flf.entity.Info;
import com.flf.entity.Page;
import com.flf.service.InfoService;

@Controller
@RequestMapping(value="/info")
public class InfoController {
	
	@Autowired
	private InfoService infoService;
	
	@RequestMapping
	public String info(Model model,Page page){
		List<Info> infoList = infoService.listPageInfo(page);
		model.addAttribute("infoList", infoList);
		model.addAttribute("page", page);
		return "info";
	}
	
}

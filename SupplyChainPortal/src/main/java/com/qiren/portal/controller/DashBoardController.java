package com.qiren.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Logger;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {


	@GetMapping(path = "/")
	public String getMainPage() {
		Logger.log(this, "getMain");
		return CommonUtils.page("entry/main");
	}
	
}

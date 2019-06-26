package com.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
	// 首页
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	// 查询订单
	@RequestMapping("/showOrder")
	public String showOrder() {
		return "showOrder";
	}

	// 添加订单
	@RequestMapping("/addOrder")
	public String addOrder() {
		return "addOrder";
	}

	// 修改订单
	@RequestMapping("/updateOrder")
	public String updateOrder() {
		return "updateOrder";
	}

	// 删除订单
	@RequestMapping("/deleteOrder")
	public String deleteOrder() {
		return "deleteOrder";
	}

	// 自定义登陆页面------是为了能够直接访问login页面
	/*@GetMapping("/login")
	public String loging() {
        System.out.println("进去loginG");
		return "login";
	}*/

	//是为了能接收提交的表单
	@RequestMapping("/login")
	public String login(){
        System.out.println("进入login");
		return "login";
	}

//测试
    @RequestMapping("/find")
    @ResponseBody
    public String findOreder(){
	    return "ok";
    }

}

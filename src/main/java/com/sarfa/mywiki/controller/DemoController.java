package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.domain.Demo;
import com.sarfa.mywiki.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    //测试MyBatis
    @Resource
    private DemoService demoService;
    @GetMapping("/list")
    public List<Demo> list(){
        return demoService.list();
    }
    
}

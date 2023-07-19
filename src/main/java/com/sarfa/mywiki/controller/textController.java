package com.sarfa.mywiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class textController {
//    自定义配置的使用，当没有这个配置的时候就会使用：后的提前设定的值
    @Value("${test.hello:Test}")
    private String myName;

    @RequestMapping("/hello")
    public String hello(){

        return "Hello " + myName;
    }
    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello " + name;
    }
      /*
    restful风格请求：
    原本样式：/user/id=1
    restful风格：/user/1
    GET , POST , PUT , DELETE
     */
    // @GetMapping
    //PostMapping
    //PutMapping
    //DeleteMapping
    //RequestMapping   包括了以上四种注释，通常可以直接用RequestMapping 来代替。

//    @GetMapping: 用于处理HTTP GET请求。通常用于获取数据。
//    @PostMapping: 用于处理HTTP POST请求。通常用于提交数据以创建新的资源。
//    @PutMapping: 用于处理HTTP PUT请求。通常用于更新现有资源的全部数据。
//    @DeleteMapping: 用于处理HTTP DELETE请求。通常用于删除资源。
//    @RequestMapping: 这是一种通用注解，可以处理所有类型的HTTP请求。通过将method属性设置为适当的RequestMethod，
//    你可以指定它处理何种类型的请求。例如，@RequestMapping(method = RequestMethod.GET)等同于@GetMapping。
}

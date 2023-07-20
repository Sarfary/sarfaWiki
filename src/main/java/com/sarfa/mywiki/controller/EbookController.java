package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    //测试MyBatis
    @Resource
    private EbookService ebookService;
    @GetMapping("/list")
    public List<Ebook> list(){
        return ebookService.list();
    }
    
}

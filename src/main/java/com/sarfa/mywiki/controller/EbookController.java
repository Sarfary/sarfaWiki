package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.resp.CommonResp;
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
    public CommonResp<List<Ebook>> list(){
        CommonResp<List<Ebook>> objectCommonResp = new CommonResp<>();
        List<Ebook> list= ebookService.list();
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    
}

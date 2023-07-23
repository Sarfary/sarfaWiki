package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.EbookReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.EbookResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;
    @GetMapping("/list")
    public CommonResp<PageResp<EbookResp>> list(EbookReq req){
        CommonResp<PageResp<EbookResp>> objectCommonResp = new CommonResp<>();
        PageResp<EbookResp> list= ebookService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    
}

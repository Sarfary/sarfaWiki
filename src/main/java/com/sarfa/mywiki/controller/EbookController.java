package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.EbookReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.EbookResp;
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
    public CommonResp<List<EbookResp>> list(EbookReq req){
        CommonResp<List<EbookResp>> objectCommonResp = new CommonResp<>();
        List<EbookResp> list= ebookService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    
}

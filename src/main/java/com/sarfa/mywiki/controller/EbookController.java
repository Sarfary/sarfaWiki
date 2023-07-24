package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.EbookQueryReq;
import com.sarfa.mywiki.req.EbookSaveReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.EbookQueryResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;
    @GetMapping("/list")
    public CommonResp<PageResp<EbookQueryResp>> list(EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<EbookQueryResp> list= ebookService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }

    /*
        保存
        传入参数：Ebook
     */
    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp objectCommonResp = new CommonResp<>();
        ebookService.save(req);
        return objectCommonResp;

    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp objectCommonResp = new CommonResp<>();
        ebookService.delete(id);
        return objectCommonResp;
    }

}

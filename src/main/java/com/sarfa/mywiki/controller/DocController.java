package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.DocQueryReq;
import com.sarfa.mywiki.req.DocSaveReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.DocQueryResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;
    @GetMapping("/list")
    public CommonResp<PageResp<DocQueryResp>> list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<DocQueryResp> list= docService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    /*
        保存
        传入参数：Doc
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp objectCommonResp = new CommonResp<>();
        docService.save(req);
        return objectCommonResp;

    }

    @DeleteMapping("/delete/{ids}")
    public CommonResp delete(@PathVariable String ids){
        CommonResp objectCommonResp = new CommonResp<>();
        List<String> list = Arrays.asList(ids.split(","));
        List<Long> longList = list.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        docService.delete(longList);
        return objectCommonResp;

    }

}

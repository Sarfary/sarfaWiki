package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.CategoryQueryReq;
import com.sarfa.mywiki.req.CategorySaveReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.CategoryQueryResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController{

    @Resource
    private CategoryService categoryService;
    @GetMapping("/list")
    public CommonResp<PageResp<CategoryQueryResp>> list(@Valid CategoryQueryReq req){
        CommonResp<PageResp<CategoryQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<CategoryQueryResp> list= categoryService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    /*
        保存
        传入参数：Category
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        CommonResp objectCommonResp = new CommonResp<>();
        categoryService.save(req);
        return objectCommonResp;

    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp objectCommonResp = new CommonResp<>();
        categoryService.delete(id);
        return objectCommonResp;

    }

}

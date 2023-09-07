package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.ArticlesQueryReq;
import com.sarfa.mywiki.req.ArticlesSaveReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.ArticlesQueryResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.service.ArticlesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

    @Resource
    private ArticlesService articlesService;

    /**
     *
     * @param req 分页 ，name模糊查询，分类查询, id查询
     * @return CommonResp{
     *   "success":
     *   "message":
     *   "content": {}
     *   }
     *
     */
    @GetMapping("/list")
    public CommonResp<PageResp<ArticlesQueryResp>> list(@Valid ArticlesQueryReq req){
        CommonResp<PageResp<ArticlesQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<ArticlesQueryResp> list= articlesService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    @GetMapping("/findArticle/{id}")
    public CommonResp<ArticlesQueryResp> findArticle(@PathVariable Long id){
        CommonResp<ArticlesQueryResp> objectCommonResp = new CommonResp<>();
        ArticlesQueryResp articlesQueryResp = articlesService.findArticle(id);
        objectCommonResp.setContent(articlesQueryResp);
        return objectCommonResp;
    }
    @GetMapping("/findContent/{id}")
    public CommonResp<String> findContent(@PathVariable Long id){
        CommonResp<String> objectCommonResp = new CommonResp<>();
        String content = articlesService.findContent(id);
        objectCommonResp.setContent(content);
        return objectCommonResp;
    }

    /**
     *
     * @param req 新增，更新的数据
     * @return CommonResp
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody ArticlesSaveReq req){
        CommonResp objectCommonResp = new CommonResp<>();
        articlesService.save(req);
        return objectCommonResp;

    }
    /**
     *
     * @param id 根据id删除数据
     * @return CommonResp
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp objectCommonResp = new CommonResp<>();
        articlesService.delete(id);
        return objectCommonResp;
    }

}

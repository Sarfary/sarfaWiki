package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.aspect.LogAspect;
import com.sarfa.mywiki.domain.Articles;
import com.sarfa.mywiki.domain.ArticlesExample;
import com.sarfa.mywiki.domain.Content;
import com.sarfa.mywiki.mapper.ArticlesMapper;
import com.sarfa.mywiki.mapper.ContentMapper;
import com.sarfa.mywiki.mapper.MyArticlesMapper;
import com.sarfa.mywiki.req.ArticlesQueryReq;
import com.sarfa.mywiki.req.ArticlesSaveReq;
import com.sarfa.mywiki.resp.ArticlesQueryResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.util.CopyUtil;
import com.sarfa.mywiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticlesService {
    //    @Autowired 也可以用这个来注入，这个是springboot自带的
    //打印日志
    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    @Resource//这个是jdk自带的
    private ArticlesMapper articlesMapper;
    @Resource
    private MyArticlesMapper myArticlesMapper;
    @Resource
    private ContentMapper contentMapper;

    public PageResp<ArticlesQueryResp> list(ArticlesQueryReq req) {
        ArticlesExample articlesExample = new ArticlesExample();
        ArticlesExample.Criteria criteria = articlesExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(req.getId())){
            criteria.andIdEqualTo(req.getId());
        }
        if(!ObjectUtils.isEmpty(req.getCategory1Id())){
            criteria.andCategory1IdEqualTo(req.getCategory1Id());
            criteria.andCategory2IdEqualTo(req.getCategory2Id());
        }
        //分页
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Articles> articlesList = articlesMapper.selectByExample(articlesExample);

        List<ArticlesQueryResp> list = CopyUtil.copyList(articlesList, ArticlesQueryResp.class);
        PageInfo<Articles> pageInfo = new PageInfo<>(articlesList);
        PageResp<ArticlesQueryResp> objectPageResp = new PageResp<ArticlesQueryResp>();
        objectPageResp.setTotal(pageInfo.getTotal());
        objectPageResp.setList(list);
//        List<ArticlesResp> respList = new ArrayList<>();
//        for (Articles articles:articlesList) {
////            ArticlesResp articlesResp = new ArticlesResp();
////            BeanUtils.copyProperties(articles,articlesResp);
        //      单体复制
//            ArticlesResp articlesResp = CopyUtil.copy(articles, ArticlesResp.class);
//            respList.add(articlesResp);
//        }
        //列表复制
        return objectPageResp;

    }

    @Resource
    private SnowFlake snowFlake;

    public void save(ArticlesSaveReq req) {
        Articles articles = CopyUtil.copy(req, Articles.class);
        Content content = CopyUtil.copy(req,Content.class);
        if (ObjectUtils.isEmpty(articles.getId())) {
            //id为空，是新增
            articles.setId(snowFlake.nextId());
            articles.setViewCount(0);
            articles.setVoteCount(0);
            content.setId(articles.getId());
            articlesMapper.insert(articles);
            contentMapper.insert(content);
        } else {
            //id不为空，是更新
            int a = articlesMapper.updateByPrimaryKey(articles);
            int b = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            //显示是否执行
            // LOG.info("{}",a);

        }

    }
    public void delete(Long id){
        articlesMapper.deleteByPrimaryKey(id);
        contentMapper.deleteByPrimaryKey(id);
    }

    public String findContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        myArticlesMapper.updataViewCount(id);
        return content.getContent();
    }

    public ArticlesQueryResp findArticle(Long id){
        Articles article = articlesMapper.selectByPrimaryKey(id);
        ArticlesQueryResp articlesQueryResp = CopyUtil.copy(article, ArticlesQueryResp.class);

        return articlesQueryResp;
    }


}

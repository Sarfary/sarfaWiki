package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.aspect.LogAspect;
import com.sarfa.mywiki.domain.Doc;
import com.sarfa.mywiki.domain.DocExample;
import com.sarfa.mywiki.mapper.DocMapper;
import com.sarfa.mywiki.req.DocQueryReq;
import com.sarfa.mywiki.req.DocSaveReq;
import com.sarfa.mywiki.resp.DocQueryResp;
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
public class DocService {
    //    @Autowired 也可以用这个来注入，这个是springboot自带的
    //打印日志
    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    @Resource//这个是jdk自带的
    private DocMapper docMapper;

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(req.getId())){
            criteria.andIdEqualTo(req.getId());
        }
        if(!ObjectUtils.isEmpty(req.getEbookId())){
            criteria.andEbookIdEqualTo(req.getEbookId());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        PageResp<DocQueryResp> objectPageResp = new PageResp<>();
        objectPageResp.setTotal(pageInfo.getTotal());
        objectPageResp.setList(list);
//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc:docList) {
////            DocResp docResp = new DocResp();
////            BeanUtils.copyProperties(doc,docResp);
        //      单体复制
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }
        //列表复制
        return objectPageResp;

    }

    @Resource
    private SnowFlake snowFlake;

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(doc.getId())) {
            //id为空，是新增
            doc.setId(snowFlake.nextId());
            if(ObjectUtils.isEmpty(doc.getParent())){
                doc.setParent(0L);
            }
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
        } else {
            //id不为空，是更新
            int a = docMapper.updateByPrimaryKey(doc);
            //显示是否执行
            // LOG.info("{}",a);

        }

    }
    public void delete(List<Long> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

}

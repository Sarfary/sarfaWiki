package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.aspect.LogAspect;
import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.domain.EbookExample;
import com.sarfa.mywiki.mapper.EbookMapper;
import com.sarfa.mywiki.req.EbookQueryReq;
import com.sarfa.mywiki.req.EbookSaveReq;
import com.sarfa.mywiki.resp.EbookQueryResp;
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
public class EbookService {
    //    @Autowired 也可以用这个来注入，这个是springboot自带的
    //打印日志
    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    @Resource//这个是jdk自带的
    private EbookMapper ebookMapper;

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(req.getCategory1Id())){
            criteria.andCategory1IdEqualTo(req.getCategory1Id());
            criteria.andCategory2IdEqualTo(req.getCategory2Id());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        PageResp<EbookQueryResp> objectPageResp = new PageResp<EbookQueryResp>();
        objectPageResp.setTotal(pageInfo.getTotal());
        objectPageResp.setList(list);
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook:ebookList) {
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
        //      单体复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }
        //列表复制
        return objectPageResp;

    }

    @Resource
    private SnowFlake snowFlake;

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(ebook.getId())) {
            //id为空，是新增
            ebook.setId(snowFlake.nextId());
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebookMapper.insert(ebook);
        } else {
            //id不为空，是更新
            int a = ebookMapper.updateByPrimaryKey(ebook);
            //显示是否执行
            // LOG.info("{}",a);

        }

    }
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }

}

package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.domain.EbookExample;
import com.sarfa.mywiki.mapper.EbookMapper;
import com.sarfa.mywiki.req.EbookReq;
import com.sarfa.mywiki.resp.EbookResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
//    @Autowired 也可以用这个来注入，这个是springboot自带的

    @Resource//这个是jdk自带的
    private EbookMapper ebookMapper;
    public PageResp<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        if(!ObjectUtils.isEmpty((req.getPage()))|!ObjectUtils.isEmpty(req.getSize())){
            PageHelper.startPage(req.getPage(), req.getSize());
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        PageResp<EbookResp> objectPageResp = new PageResp<EbookResp>();
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

}

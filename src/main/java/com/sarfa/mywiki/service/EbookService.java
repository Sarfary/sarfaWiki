package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.domain.EbookExample;
import com.sarfa.mywiki.mapper.EbookMapper;
import com.sarfa.mywiki.req.EbookQueryReq;
import com.sarfa.mywiki.req.EbookSaveReq;
import com.sarfa.mywiki.resp.EbookQueryResp;
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
    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        if(!ObjectUtils.isEmpty((req.getPage()))|!ObjectUtils.isEmpty(req.getSize())){
            PageHelper.startPage(req.getPage(), req.getSize());
        }
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


    public void save(EbookSaveReq req){
        Ebook ebook = new Ebook();
        ebook = CopyUtil.copy(req,Ebook.class);
        if(ObjectUtils.isEmpty(ebook.getId())){
            //id为空，是新增
            ebookMapper.insert(ebook);
        }
        else {
            //id不为空，是更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }

}

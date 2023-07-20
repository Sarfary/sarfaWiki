package com.sarfa.mywiki.service;

import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.domain.EbookExample;
import com.sarfa.mywiki.mapper.EbookMapper;
import com.sarfa.mywiki.req.EbookReq;
import com.sarfa.mywiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
//    @Autowired 也可以用这个来注入，这个是springboot自带的

    @Resource//这个是jdk自带的
    private EbookMapper ebookMapper;
    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> respList = new ArrayList<>();
        for (Ebook ebook:ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return respList;

    }

}

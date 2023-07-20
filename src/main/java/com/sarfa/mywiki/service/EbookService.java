package com.sarfa.mywiki.service;

import com.sarfa.mywiki.domain.Ebook;
import com.sarfa.mywiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
//    @Autowired 也可以用这个来注入，这个是springboot自带的

    @Resource//这个是jdk自带的
    private EbookMapper ebookMapper;
    public List<Ebook> list(){
        return ebookMapper.selectByExample(null);
    }

}

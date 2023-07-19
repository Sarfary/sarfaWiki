package com.sarfa.mywiki.service;

import com.sarfa.mywiki.domain.Test;
import com.sarfa.mywiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {
//    @Autowired 也可以用这个来注入，这个是springboot自带的

    @Resource//这个是jdk自带的
    private TestMapper testMapper;
    public List<Test> list(){
        return testMapper.list();
    }

}

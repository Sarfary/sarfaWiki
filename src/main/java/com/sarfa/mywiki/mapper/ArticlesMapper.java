package com.sarfa.mywiki.mapper;

import com.sarfa.mywiki.domain.Articles;
import com.sarfa.mywiki.domain.ArticlesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticlesMapper {
    long countByExample(ArticlesExample example);

    int deleteByExample(ArticlesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Articles record);

    int insertSelective(Articles record);

    List<Articles> selectByExample(ArticlesExample example);

    Articles selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Articles record, @Param("example") ArticlesExample example);

    int updateByExample(@Param("record") Articles record, @Param("example") ArticlesExample example);

    int updateByPrimaryKeySelective(Articles record);

    int updateByPrimaryKey(Articles record);
}
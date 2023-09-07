package com.sarfa.mywiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface MyArticlesMapper {
    public void updataViewCount(@Param("id") Long id);
    public void updataAddVoteCount(@Param("id") Long id);
    public void updataSubVoteCount(@Param("id") Long id);


}

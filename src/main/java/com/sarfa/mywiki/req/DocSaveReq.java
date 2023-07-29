package com.sarfa.mywiki.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sarfa.mywiki.helper.LongJsonDeserializer;
import com.sarfa.mywiki.helper.LongJsonSerializer;

import javax.validation.constraints.NotNull;

public class DocSaveReq {

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

    private Long parent;
    @NotNull(message = "属于的书名不能为空")
    private Long ebookId;

    @NotNull(message = "名称不能为空")
    private String name;

    @NotNull(message = "顺序不能为空")
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", parent=" + parent +
                ", ebookId=" + ebookId +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
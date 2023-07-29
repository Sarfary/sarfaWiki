package com.sarfa.mywiki.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sarfa.mywiki.helper.LongJsonDeserializer;
import com.sarfa.mywiki.helper.LongJsonSerializer;

public class DocQueryReq extends PageReq{
    /**
     * id
     */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;
    private Long ebookId;
    private String name;

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", name='" + name + '\'' +
                '}';
    }


}
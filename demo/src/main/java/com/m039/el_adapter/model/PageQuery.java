package com.m039.el_adapter.model;

public class PageQuery {

    public int page;
    public int offset;
    public int count;

    public PageQuery(int page, int offset, int count) {
        this.page = page;
        this.offset = offset;
        this.count = count;
    }
}
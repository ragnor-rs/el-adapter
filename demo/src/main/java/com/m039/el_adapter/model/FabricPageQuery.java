package com.m039.el_adapter.model;

public class FabricPageQuery {

    public static PageQuery getQuery(PageQuery old, int count) {
        if (old != null) {
            return new PageQuery(old.page + 1, old.offset + count, count);
        }
        return new PageQuery(0, 0, count);
    }
}
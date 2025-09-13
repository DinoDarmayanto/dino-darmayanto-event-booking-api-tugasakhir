package com.sinaukoding.eventbooking.model.app;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class AppPage<T> extends PageImpl<T> {

    public AppPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public AppPage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public static <T> AppPage<T> create(List<T> content, Pageable pageable, long total) {
        return new AppPage<>(content, pageable, total);
    }

    public static <M> AppPage<M> create(Page<M> page) {
        return new AppPage<>(page);
    }

    public static <F> AppPage<F> create(List<F> content, Pageable pageable) {
        if (pageable.isUnpaged()) {
            return new AppPage<>(content, pageable, content.size());
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), content.size());
        return new AppPage<>(content.subList(start, end), pageable, content.size());
    }

}

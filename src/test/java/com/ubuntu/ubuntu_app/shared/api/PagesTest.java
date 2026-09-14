package com.ubuntu.ubuntu_app.shared.api;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class PagesTest {

    @Test
    void defaultsOnInvalidInput() {
        Pageable pageable = Pages.of(-1, 0);

        assertEquals(0, pageable.getPageNumber());
        assertEquals(Pages.DEFAULT_SIZE, pageable.getPageSize());
    }

    @Test
    void capsOversizedRequests() {
        Pageable pageable = Pages.of(2, 10_000);

        assertEquals(2, pageable.getPageNumber());
        assertEquals(Pages.MAX_SIZE, pageable.getPageSize());
    }

    @Test
    void keepsValidInputAndSort() {
        Sort sort = Sort.by(Sort.Direction.DESC, "fecha");
        Pageable pageable = Pages.of(1, 10, sort);

        assertEquals(1, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(sort, pageable.getSort());
    }

    @Test
    void nullSortBecomesUnsorted() {
        Pageable pageable = Pages.of(0, 10, null);

        assertTrue(pageable.getSort().isUnsorted());
    }
}

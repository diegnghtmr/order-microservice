package com.pragma.powerup.ordermicroservice.domain.model;

import java.util.List;

public class OrderPage {
    private List<Order> content;
    private int totalPages;
    private long totalElements;

    public OrderPage(List<Order> content, int totalPages, long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<Order> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }
}

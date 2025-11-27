package com.pragma.powerup.ordermicroservice.application.dto.response;

import java.util.List;

public class OrderPageResponse {
    private List<OrderResponse> content;
    private int totalPages;
    private long totalElements;

    public OrderPageResponse() {
    }

    public OrderPageResponse(List<OrderResponse> content, int totalPages, long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<OrderResponse> getContent() {
        return content;
    }

    public void setContent(List<OrderResponse> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}

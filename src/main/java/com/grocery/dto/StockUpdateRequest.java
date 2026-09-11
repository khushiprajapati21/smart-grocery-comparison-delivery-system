package com.grocery.dto;

public class StockUpdateRequest {

    private Integer stockQuantity;

    public StockUpdateRequest() {
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

}
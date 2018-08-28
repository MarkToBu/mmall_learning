package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class OrderProducVo {
    private List<OrderItemVo>  orderItemVoList;
    private BigDecimal prodcutTotalPrice;
    private String imageHost;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public BigDecimal getProdcutTotalPrice() {
        return prodcutTotalPrice;
    }

    public void setProdcutTotalPrice(BigDecimal prodcutTotalPrice) {
        this.prodcutTotalPrice = prodcutTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}

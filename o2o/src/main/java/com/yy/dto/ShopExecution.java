package com.yy.dto;

import com.yy.entity.Shop;
import com.yy.enums.ShopStateEnum;

import java.util.List;

public class ShopExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop（增删改店铺的时候用到）
    private Shop shop;

    //shop列表（查询店铺列表的时候使用）
    private List<Shop> shopList;

    public ShopExecution(){

    }

    public ShopExecution(ShopStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
}

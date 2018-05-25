package com.yy.dao;

import com.yy.entity.Shop;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface ShopDao {
    /**
     * @param shopId
     * @return shop
     */
    Shop queryByShopId(long shopId);

    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}

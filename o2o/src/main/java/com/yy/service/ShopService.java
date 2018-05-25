package com.yy.service;

import com.yy.dto.ShopExecution;
import com.yy.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;


public interface ShopService {
    /**
     * 根据店铺id获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     *
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName);   //涉及对店铺操作的，都会返回ShopExecution对象

    /**
     * 更新店铺信息，包括对图片的处理
     *
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream , String fileName);
}

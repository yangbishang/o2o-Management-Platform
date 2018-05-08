package com.yy.service.impl;

import com.yy.dao.ShopDao;
import com.yy.dto.ShopExecution;
import com.yy.entity.Shop;
import com.yy.enums.ShopStateEnum;
import com.yy.service.ShopService;
import com.yy.util.ImageUtil;
import com.yy.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Date;

@Service("shopService")
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Override
 /*   @Transactional*/
    public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg) {
       //空值判断
        if(shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            //给店铺信息赋初始值
            shop.setEnableStatus(0);                            //未上架，CHECK
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if(effectedNum<=0){
                throw new RuntimeException("店铺创建失败"); //RuntimeException事务失败后会终止并回滚
            }else{
                if(shopImg!=null){
                    //存储图片
                    try{
                        addShopImg(shop,shopImg);
                    }catch(Exception e){
                        throw new RuntimeException("addShopImg error:" + e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new RuntimeException("更新图片地址失败");
                    }
                }
            }

        }catch (Exception e){
            throw new RuntimeException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop, CommonsMultipartFile shopImg) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg,dest);
        //更新shopImg的值
        shop.setShopImg(shopImgAddr);
    }
}

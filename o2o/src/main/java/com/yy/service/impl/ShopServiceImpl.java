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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.InputStream;
import java.util.Date;

@Service("shopService")
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;



    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,
            rollbackFor = Exception.class)  //出现异常就回滚
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
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
                if(shopImgInputStream !=null){

                    //存储图片
                    try{
                        addShopImg(shop, shopImgInputStream , fileName);
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

    private void addShopImg(Shop shop, InputStream shopImgInputStream , String fileName) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream,fileName,dest);
        //更新shopImg的值
        shop.setShopImg(shopImgAddr);
    }



    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,
            rollbackFor = Exception.class)  //出现异常就回滚
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) {

        if(shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            try{

                //1,判断是否需要处理图片
                if(shopImgInputStream != null && fileName != null && !"".equals(fileName)){
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    //如果原先的shop中的shopImg不为空，则删除shopImg（原先图片地址）
                    if(tempShop.getShopImg() != null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop,shopImgInputStream,fileName);
                }

                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if(effectedNum <= 0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else{
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS,shop);
                }
            }catch (Exception e){
                throw new RuntimeException("modifyShop error :" + e.getMessage());
            }
        }
    }
}

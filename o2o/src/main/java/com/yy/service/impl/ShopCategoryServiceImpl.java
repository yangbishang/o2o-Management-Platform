package com.yy.service.impl;

import com.yy.dao.ShopCategoryDao;
import com.yy.entity.ShopCategory;
import com.yy.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shopCategoryService")
public class ShopCategoryServiceImpl implements ShopCategoryService{
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(shopCategoryCondition);
    }
}

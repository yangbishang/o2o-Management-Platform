package com.yy.service;

import com.yy.dto.ShopExecution;
import com.yy.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;


public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream , String fileName);
}

package com.yy.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yy.dto.ShopExecution;
import com.yy.entity.Area;
import com.yy.entity.PersonInfo;
import com.yy.entity.Shop;
import com.yy.entity.ShopCategory;
import com.yy.enums.ShopStateEnum;
import com.yy.service.AreaService;
import com.yy.service.ShopCategoryService;
import com.yy.service.ShopService;
import com.yy.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value="/getshopinitinfo",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);

        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return  modelMap;
    }

    @RequestMapping(value="/registershop",method= RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //1.接收并转换相应的参数，包括店铺信息以及图片的信息
        //1.1  店铺  （将传过来的json转换成实体类）
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);          //json --> Shop(将shopStr转换成Shop实例)
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        //1.2  图片
        CommonsMultipartFile shopImg = null;   //spring自带的
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());   //request从本次会话的上下文去获取本次上传文件的上下文
        if(commonsMultipartResolver.isMultipart(request)){  //判断request里面是否有上传的文件流
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;   //将request做类型转换，multipartHttpServletReques能够获取文件流
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");               //将获取的文件流强制转换成spring能获取的文件流类型CommonsMultipartFile
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }


        //2.注册店铺
        if(shop!=null&shopImg!=null){

            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se = shopService.addShop(shop,shopImg);
            if(se.getState()== ShopStateEnum.CHECK.getState()){
                modelMap.put("success",true);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
            }
            return modelMap;

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
        //3.返回结果


    }
}

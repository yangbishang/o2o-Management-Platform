
/*       $(function(){          */

$(window).on("load", () => {
    var shopId = getQueryString('shopId');                                //getQueryString方法为自动匹配url后面参数所带的值
    var isEdit = shopId ? true : false ;                                   //如果存在shopId，则是修改，如果不存在，则是注册
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var shopInfoUrl = "/o2o/shopadmin/getshopbyid?shopId=" + shopId;
    var editShopUrl = "/o2o/shopadmin/modifyshop";
    var submitUrl ;


    //如果存在shopId，则是修改，如果不存在，则是注册
    if(!isEdit){
        //js文件被加载的时候,就自动加载getShopInitInfo();然后去后台获取店铺信息和区域信息
        getShopInitInfo();
    }else{
        getShopInfo(shopId);
    }


    function getShopInfo(shopId){

        //调试信息
        alert(shopInfoUrl);


        var tempAreaHtml01 = '';

        $.ajax({
            type:"get",
            url:shopInfoUrl,
            dataType:"json",
            success:function(data){
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);

                var shopCategory =  '<option data-id="'+ shop.shopCategory.shopCategoryId + '"selected> '
                                 + shop.shopCategory.shopCategoryName + '</option>>';

                //区域信息
                $.each(data.areaList,(i,item)=>{
                    tempAreaHtml01 +=  '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                })


                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled','disabled');                                      //店铺的类别是不能修改的
                $('#area').html(tempAreaHtml01);
                $("#area option[data-id='" + shop.area.areaId + "']").attr("selected","selected");  //默认选择现在这个店铺的信息

            }
        })
    }


    //获取店铺的基本信息,然后填充至控件里面去
    function getShopInitInfo(){                                             //第一个参数使我们要访问的url，第二个参数是回调函数
    /*
        $.getJSON(initUrl,function (data) {                                // data有后台接收回来的数据
             if(data.success){
                 var tempHtml = '';
                 var tempAreaHtml = '';                                     // 用于获取区域信息
                 data.shopCategoryList.map(function (item,index) {          // 店铺分类的列表信息，用map的形式遍历列表
                 tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                   + item.shopCategoryName + '</option>>';
                })
            //区域信息
            data.areaList.map(function(item,index){
                 tempAreaHtml += '<option data-id="' + item.areaId + '">'
                 + item.areaName +'</option>';
             })
          /获取到了信息以后，将它显示到前台
          $("#shop-category").html(tempHtml)
          $("#area").html(tempAreaHtml);
          }
       });
 */


        var tempHtml = '';
        var tempAreaHtml = '';
        $.ajax({
            type:"post",
            url: initUrl,
            dataType:"json",
            success:function (data) {
                // 店铺分类的列表信息
                $.each(data.shopCategoryList,(i,item)=>{
                    tempHtml +=  '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                })
                //区域信息
                $.each(data.areaList,(i,item)=>{
                    tempAreaHtml +=  '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                })

                $('#shop-category').html(tempHtml)
                $('#area').html(tempAreaHtml);
            }


        })






        $('#submit').click(function () {
            //模拟shop的一个实体
            var shop = {};
            if(isEdit){
                shop.shopId = shopId;
            }

            shop.shopName = $('#shop-name').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopDesc = $('#shop-desc').val();
            //定位到它下面的一个子item下
            shop.shopCategory = {
                //从列表里面找到shopCategoryId      // ???
                shopCategoryId : $('#shop-category').find('option').not(function(){   //双重否定为肯定
                    return !this.selected;
                }).data('id')
            };
            shop.area = {
                areaId : $('#area').find('option').not(function(){
                    return !this.selected;
                }).data('id')
            };
            //获取文件(图片)流
            var shopImg = $('#shop-img')[0].files[0];
            //定义一个formData来接收表单的内容
            var formData = new FormData();
            formData.append('shopImg',shopImg);
            formData.append('shopStr',JSON.stringify(shop));  //转换成字符流的形式传进去

            var verifyCodeActual = $('#j_kaptcha').val();
            if(!verifyCodeActual){
                $.toast('请输入验证码！');
                return;
            }
            formData.append('verifyCodeActual',verifyCodeActual);
            //接收完数据就提交到后台

            if(isEdit){
                submitUrl = editShopUrl;
            }else{
                submitUrl = registerShopUrl;
            }
            $.ajax({
                url:submitUrl,
                type:'POST',
                data:formData,
                contentType:false, //因为既要传图片也要传文字
                processData:false,
                cache:false,
                success:function(data){
                    if(data.success){
                        $.toast('提交成功！');  //弹出信息
                    }else{
                        $.toast('提交失败！'+data.errMsg);
                    }

                    $('#kaptcha_img').click();
                }

            })
        })
    }


})
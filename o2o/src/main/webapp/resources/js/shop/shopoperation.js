/**
 *g
 */
$(function(){
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    //调试信息
    alert(initUrl);
    //js文件被加载的时候,就自动加载getShopInitInfo();然后去后台获取店铺信息和区域信息
    getShopInitInfo();

    //获取店铺的基本信息,然后填充至控件里面去
    function getShopInitInfo(){                                             //第一个参数使我们要访问的url，第二个参数是回调函数
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

                //获取到了信息以后，将它显示到前台
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });

        $('#submit').click(function () {
            //模拟shop的一个实体
            var shop = {};
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
            //接收完数据就提交到后台
            $.ajax({
                url:registerShopUrl,
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
                }

            })
        })
    }


})
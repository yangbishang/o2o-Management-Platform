package com.yy.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Random r = new Random();


    //处理缩略图
    public static String generateThumbnail(CommonsMultipartFile thumbnail,String targetAddr){//spring自带的文件处理对象
        String realFileName = getRandomFileName();                   //获取文件的随机名称
        String extension = getFileExtension(thumbnail);               //获取文件的拓展名
        makeDirPath(targetAddr);                                       //创建目录
        String relativeAddr = targetAddr + realFileName + extension;   //相对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);  //新的文件
        try{
            Thumbnails.of(thumbnail.getInputStream()).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
                    .outputQuality(0.8f).toFile(dest);
        }catch (IOException e){
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 生成随机的文件名，当前年月日小时分钟分秒+五位随机数
     * @return
     */
    private static String getRandomFileName() {
        //获取随机的五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());

        return nowTimeStr + rannum;
    }


    /**
     * 获取输入文件流的扩展名
     * @param cFile
     * @return
     */
    private static String getFileExtension(CommonsMultipartFile cFile) {
        String originalFileName = cFile.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/xiangze/xxx.jpg
     * 那么 home work xiangze 这三个文件夹都得自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;//目标文件的全路径
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }







    //测试！
    public static void main(String[] args) throws IOException {
        //往图片上打水印（前提导入thumbnail的jar包）

        Thumbnails.of(new File("D:/IdeaProject/image/timg.jpg"))
        .size(200,200).watermark(Positions.BOTTOM_RIGHT,
                ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.8f)
                .toFile("D:/IdeaProject/image/xiaohuangrennew.jpg");

    }

}

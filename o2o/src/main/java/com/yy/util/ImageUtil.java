package com.yy.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Random r = new Random();


    //处理缩略图
    public static String generateThumbnail(InputStream thumbnailInputStream, String fileName,String targetAddr){//spring自带的文件处理对象
        String realFileName = getRandomFileName();                   //获取文件的随机名称

        String extension = getFileExtension(fileName);               //获取文件的拓展名
        makeDirPath(targetAddr);                                       //创建目录

        String relativeAddr = targetAddr + realFileName + extension;   //相对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);  //新的文件
        System.out.println(dest+"    "+relativeAddr);          //test
        try{
/*            Thumbnails.of(thumbnailInputStream).size(200,200)   //Thumbnails.of可以接受很多种类型
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/watermark.jpg")),0.25f)
                    .outputQuality(0.8f).toFile(dest);*/
            Thumbnails.of(thumbnailInputStream).size(600, 300).outputQuality(0.5f).toFile(dest);
        }catch (IOException e){
            e.printStackTrace();
        }
        return dest.getPath();
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
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {

        return fileName.substring(fileName.lastIndexOf("."));
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

    /**
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件丼则删除改文件
     * 如果storePath是目录路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File[] files = fileOrPath.listFiles();
                for(int i=0 ; i< files.length ; i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
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

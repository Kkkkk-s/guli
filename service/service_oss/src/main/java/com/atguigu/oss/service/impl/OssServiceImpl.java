package com.atguigu.oss.service.impl;

import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        //获取阿里云存储相关常量
        String endPoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;


        try {
            //创建oss实例
            OSS oss = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

            //上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String filename = file.getOriginalFilename();

            String s = UUID.randomUUID().toString().replace("-", "");
            filename = s + filename;

            String date = new DateTime().toString("yyyy/MM/dd");

            filename = date + "/" + filename;

            //调用oss方法上传
            oss.putObject(bucketName, filename, inputStream);

            oss.shutdown();

            String url = "https://" + bucketName + "." + endPoint + "/" + filename;

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

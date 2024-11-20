package com.example.gonggong_server.global.util;

import java.io.IOException;

import com.example.gonggong_server.global.exception.GlobalException;
import com.example.gonggong_server.global.status.ErrorStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile, String filePath) throws IOException {

        if (multipartFile == null) {
            throw new GlobalException(ErrorStatus.NOT_FOUND);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, filePath, multipartFile.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, filePath).toString();
    }

}
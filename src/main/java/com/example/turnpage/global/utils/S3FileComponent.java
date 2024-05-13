package com.example.turnpage.global.utils;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileComponent {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String category, MultipartFile multipartFile) {
        //파일명 생성
        String fileName = createFileName(category, Objects.requireNonNull(multipartFile.getOriginalFilename()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        /*
        1. multipartFile의 stream을 읽고, PutObejctRequest 객체 생성
        2. 생성된 객체를 amazonS3Client를 통해 S3로 전송
         */
        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new BusinessException(S3ErrorCode.FAILED_UPLOAD_S3);
        }

        //s3에 업로드된 파일명 return
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /*
    파일명 생성
     */
    public String createFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(".");
        String ext = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String random = String.valueOf(UUID.randomUUID());

        return category + "/"+ fileName + "_" + random + ext;
    }

    /**
     * 이미지 삭제
     */
    public void deleteFile(String fileUrl) {
        //"https://example.com/path/to/file.txt" 일 경우 path/to/file.txt만 추출
        String[] deleteUrl = fileUrl.split("/", 4);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, deleteUrl[3]));
    }
}

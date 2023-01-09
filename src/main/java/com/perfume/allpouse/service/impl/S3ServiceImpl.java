package com.perfume.allpouse.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.perfume.allpouse.controller.ReviewController;
import com.perfume.allpouse.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final Logger LOGGER = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // 저장
    @Override
    public List<String> upload(List<MultipartFile> multipartFileList) throws IOException {
        List<String> dataList = new ArrayList<>();
        multipartFileList.forEach(multipartFile -> {
            try {
                File uploadFile = convert(multipartFile)
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
                dataList.add(upload(uploadFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return dataList;
    }


    private String upload(File uploadFile) {
        String s3FileName = UUID.randomUUID() + "-" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, s3FileName);
        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        LOGGER.info("[upload] 파일 업로드 완료 Link : {}" ,uploadImageUrl);

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }


    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }


    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            LOGGER.info("[removeNewFile]파일이 삭제되었습니다.");
        } else {
            LOGGER.info("[removeNewFile]파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


    // 삭제
    @Override
    public void delete(String path) {

        // path -> key
        String amazonUrl = "https://perfume-log.s3.ap-northeast-2.amazonaws.com/";
        int start = amazonUrl.length();
        String key = path.substring(start);

        // DeleteRequest 보냄
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, key);
        amazonS3.deleteObject(request);
    }

}

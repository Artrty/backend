package com.springboot.backend.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3Client;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB 파일 용량 제한
    private static final int MAX_FILE_COUNT = 6; // 파일 개수 제한

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    // 단일 파일 업로드 메서드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        System.out.println("MultipartFile을 전달받아 File로 전환한 후 S3에 업로드");

        // 파일 용량 체크
        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 10MB를 초과할 수 없습니다. 현재 파일 크기: " + (multipartFile.getSize() / (1024 * 1024)) + "MB");
        }

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    // 다중 파일 업로드 메서드
    public List<String> uploadMultiple(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        System.out.println("여러 MultipartFile을 S3에 업로드");

        // 파일 개수 체크
        if (multipartFiles.size() > MAX_FILE_COUNT) {
            throw new IllegalArgumentException("최대 " + MAX_FILE_COUNT + "개의 파일만 업로드할 수 있습니다. 현재 파일 개수: " + multipartFiles.size());
        }

        List<String> uploadImageUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // 파일 용량 체크
            if (multipartFile.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("파일 크기가 10MB를 초과할 수 없습니다. 현재 파일 크기: " + (multipartFile.getSize() / (1024 * 1024)) + "MB");
            }

            File uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
            String uploadImageUrl = upload(uploadFile, dirName);
            uploadImageUrls.add(uploadImageUrl); // 업로드된 URL을 리스트에 추가
        }
        return uploadImageUrls; // 모든 업로드된 파일의 S3 URL 반환
    }

    private String upload(File uploadFile, String dirName) {
        System.out.println("S3 업로드 후 로컬에 생성된 임시 파일을 삭제");
        String fileName = dirName + "/main/resources/static/images/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        System.out.println("PublicRead 권한으로 파일 업로드");
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        System.out.println("임시로 생성된 이미지 파일 삭제를 진행");
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        System.out.println("MultipartFile을 File 객체로 변환");
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}
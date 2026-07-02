package com.project.app.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${spring.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public AwsS3DTO uploadFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "업로드할 파일이 없습니다.");
        }

        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드할 수 있습니다.");
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String ext = "";
        if (originalFileName != null) {
            int dot = originalFileName.lastIndexOf(".");
            if (dot != -1) {
                ext = originalFileName.substring(dot);
            }
        }
        String uniqueFileName = UUID.randomUUID() + ext;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(contentType);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(bucket, uniqueFileName, inputStream, metadata);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 실패");
        }

        String fileUrl = amazonS3.getUrl(bucket, uniqueFileName).toString();

        return AwsS3DTO.builder()
                .fileName(uniqueFileName)
                .fileUrl(fileUrl)
                .build();
    }

    public void deleteFile(String fileName) {
        if (amazonS3.doesObjectExist(bucket, fileName)) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }
    }
}

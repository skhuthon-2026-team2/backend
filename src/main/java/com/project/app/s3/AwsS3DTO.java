package com.project.app.s3;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AwsS3DTO {
    private String fileName;
    private String fileUrl;
}

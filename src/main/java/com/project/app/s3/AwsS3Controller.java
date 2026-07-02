package server.global.aws;

import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.s3.AwsS3DTO;
import com.project.app.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public ResponseEntity<ApiResTemplate<AwsS3DTO>> uploadFile(@RequestPart("image") MultipartFile multipartFile) {
        AwsS3DTO response = awsS3Service.uploadFile(multipartFile);
        return ResponseEntity
                .status(SuccessCode.FILE_UPLOAD_SUCCESS.getHttpStatus())
                .body(ApiResTemplate.success(
                        SuccessCode.FILE_UPLOAD_SUCCESS.getHttpStatusCode(),
                        SuccessCode.FILE_UPLOAD_SUCCESS.getMessage(),
                        response
                ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResTemplate<String>> deleteFile(@RequestParam String fileName) {
        awsS3Service.deleteFile(fileName);
        return ResponseEntity
                .status(SuccessCode.FILE_DELETE_SUCCESS.getHttpStatus())
                .body(ApiResTemplate.success(
                        SuccessCode.FILE_DELETE_SUCCESS.getHttpStatusCode(),
                        SuccessCode.FILE_DELETE_SUCCESS.getMessage(),
                        "삭제 완료: " + fileName
                ));
    }
}

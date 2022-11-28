package sideproject.petmeeting.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sideproject.petmeeting.common.exception.BusinessException;
import sideproject.petmeeting.common.exception.ErrorCode;
import sideproject.petmeeting.post.domain.Post;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Service
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지 파일 저장
     * @param image
     * @param imagePath
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile image, String imagePath) throws IOException{
        validateFileExists(image);

        File uploadFile = convert(image).orElseThrow(
                () -> new IllegalArgumentException("MultipartFile -> File 변환 실패"));

        String fileName = imagePath + "/" + UUID.randomUUID() + uploadFile.getName();

        String uploadImageUrl = putS3(uploadFile, fileName); //S3로 업로드

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드 된 파일 S3 URL 주소 반환

    }


    private void validateFileExists(MultipartFile image) {
        if (image.isEmpty()) {
            throw new BusinessException("파일이 존재하지 않습니다.", ErrorCode.FILE_NOT_EXIST);
        }
    }


    /**
     * MultipartFile -> File 전환
     * 전환 시 로컬에 파일 생성됨
     * @param image
     * @return
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile image) throws IOException {
        File convertFile = new File(image.getOriginalFilename());
        // 지정된 경로에 파일 생성
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(image.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


    /**
     * S3로 업로드
     * @param uploadFile
     * @param fileName
     * @return
     */
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드(누구나 읽을 수 있음)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    /**
     * MultipartFile -> File 전환하면서 생성된 로컬에 저장된 파일 삭제
     * @param targetFile
     */
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    /**
     * 이미지 파일 삭제
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public void deleteImage(String fileName) throws UnsupportedEncodingException {
        fileName = URLDecoder.decode(fileName,"UTF-8"); //한글 인코딩
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

}



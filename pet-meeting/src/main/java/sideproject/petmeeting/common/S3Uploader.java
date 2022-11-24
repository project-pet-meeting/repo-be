package sideproject.petmeeting.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile image, String imgPath) throws IOException {
        File uploadFile = convert(image)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 변환 실패"));
        return upload(uploadFile, imgPath);
    }


    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new IllegalArgumentException("잘못된 입력 이미지");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new IllegalArgumentException("잘못된 이미지 포멧(jpg,jpeg,png 포맷이어야 합니다)");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }


    // 이미지 파일명 중복 방지
    private String createFileName(String fileName, String imgPath) {
        return imgPath + "/" + UUID.randomUUID() + fileName;
    }

    private String upload(File uploadFile, String imgPath) {
        getFileExtension(uploadFile.getName());
        String fileName = createFileName(uploadFile.getName(), imgPath);
        String uploadImageUrl = putS3(uploadFile, fileName); //S3로 업로드

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    /**
     * 이미지 삭제
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public void deleteImage(String fileName) throws UnsupportedEncodingException {
        fileName = URLDecoder.decode(fileName,"UTF-8"); //한글 인코딩
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    //S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //로컬 저장 이미지 삭제
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    //로컬 파일 업로드
    private Optional<File> convert(MultipartFile file) throws IOException {
        //로컬에 파일저장 : 파일의 기존 이름 가져와서 저장
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        //지정된 경로에 파일이 생성
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}



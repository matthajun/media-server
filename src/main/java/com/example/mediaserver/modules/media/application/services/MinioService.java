package com.example.mediaserver.modules.media.application.services;

import com.example.mediaserver.configs.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
  private final MinioClient minioClient;
  private final MinioConfig minioConfig;

  public void createBucketIfNotExists()
      throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
    boolean found =
        minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
    if (!found) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
      log.info("MinIO bucket '{}' created.", minioConfig.getBucketName());
    } else {
      log.info("MinIO bucket '{}' already exists.", minioConfig.getBucketName());
    }
  }

  /**
   * 파일을 MinIO 버킷에 업로드합니다.
   *
   * @param file 업로드할 MultipartFile
   * @return 업로드된 파일의 MinIO 객체 이름 (유니크한 이름)
   * @throws Exception 업로드 중 발생할 수 있는 예외
   */
  public String uploadFile(MultipartFile file) throws Exception {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
    }

    // 버킷이 없으면 생성
    createBucketIfNotExists();

    // 파일 이름 생성 (중복 방지를 위해 UUID와 원본 파일명 조합)
    String originalFilename = file.getOriginalFilename();
    String objectName = UUID.randomUUID().toString() + "-" + originalFilename;

    log.info(
        "Uploading file '{}' to MinIO bucket '{}' with object name '{}'",
        originalFilename,
        minioConfig.getBucketName(),
        objectName);

    try (InputStream is = file.getInputStream()) {
      minioClient.putObject(
          PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).stream(
                  is, file.getSize(), -1) // -1은 MinIO가 스트림에서 길이를 알아내도록 함
              .contentType(file.getContentType())
              .build());
      log.info("File '{}' uploaded successfully as '{}'.", originalFilename, objectName);
      return objectName; // MinIO에 저장된 객체 이름 반환
    } catch (MinioException e) {
      log.error("MinIO error while uploading file: {}", e.getMessage(), e);
      throw new RuntimeException("MinIO 파일 업로드 실패: " + e.getMessage(), e);
    } catch (IOException e) {
      log.error("IO error while reading file stream: {}", e.getMessage(), e);
      throw new RuntimeException("파일 스트림 읽기 실패: " + e.getMessage(), e);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      log.error("Security error while uploading file: {}", e.getMessage(), e);
      throw new RuntimeException("보안 오류 발생: " + e.getMessage(), e);
    }
  }

  /**
   * MinIO에서 파일을 다운로드합니다.
   *
   * @param objectName MinIO에 저장된 객체 이름
   * @return 파일의 InputStream
   * @throws Exception 다운로드 중 발생할 수 있는 예외
   */
  public InputStream downloadFile(String objectName) throws Exception {
    log.info(
        "Downloading file object '{}' from MinIO bucket '{}'.",
        objectName,
        minioConfig.getBucketName());
    try {
      return minioClient.getObject(
          GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).build());
    } catch (MinioException e) {
      log.error("MinIO error while downloading file: {}", e.getMessage(), e);
      throw new RuntimeException("MinIO 파일 다운로드 실패: " + e.getMessage(), e);
    }
  }
}

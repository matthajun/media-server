package com.example.mediaserver.modules.media.presentation;

import com.example.mediaserver.modules.media.application.services.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

class abc {
  public String result;
}

@Tag(name = "MinIO File Operations", description = "MinIO 버킷에 파일 업로드/다운로드/삭제 API")
@RestController
@RequestMapping("/api/minio")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {
  private final MinioService minioService;

  @Operation(summary = "파일 업로드", description = "단일 파일을 MinIO 버킷에 업로드합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 없음 등)"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<abc> uploadFile(
      @Parameter(
              description = "업로드할 파일",
              required = true,
              content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
          @RequestPart("file")
          MultipartFile file) {
    try {
      String objectName = minioService.uploadFile(file);
      abc result = new abc();
      result.result = "File uploaded successfully! Object Name: " + objectName;

      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
      log.error("File upload failed: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      log.error("File upload failed due to server error: {}", e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패", e);
    }
  }

  @Operation(summary = "파일 다운로드", description = "MinIO 버킷에 저장된 파일을 다운로드합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @GetMapping("/download/{objectName}")
  public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectName) {
    try {
      InputStream inputStream = minioService.downloadFile(objectName);
      InputStreamResource resource = new InputStreamResource(inputStream);

      String originalFilename = objectName;
      if (objectName.contains("-")) {
        originalFilename = objectName.substring(objectName.indexOf('-') + 1);
      }
      String encodedFileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

      HttpHeaders headers = new HttpHeaders();
      headers.add(
          HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
      headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

      return ResponseEntity.ok().headers(headers).body(resource);
    } catch (IllegalArgumentException e) {
      log.error("File download failed: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (Exception e) {
      log.error("File download failed due to server error: {}", e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 실패", e);
    }
  }
}

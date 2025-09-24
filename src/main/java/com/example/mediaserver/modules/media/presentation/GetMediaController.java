package com.example.mediaserver.modules.media.presentation;

import com.example.mediaserver.modules.media.application.services.GetMediaService;
import com.example.mediaserver.modules.media.domain.entities.MediaEntity;
import com.example.mediaserver.modules.media.presentation.dtos.CommonPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "Media")
@RestController
@RequestMapping()
public class GetMediaController {
  private final GetMediaService getMediaService;

  @Operation(summary = "미디어정보 조회", description = "미디어 정보를 조회합니다.")
  @GetMapping("/media")
  public ResponseEntity<CommonPageResponse<MediaEntity>> getMedia(
      @PageableDefault(size = 10) Pageable pageable) {
    Page<MediaEntity> result = getMediaService.getAllMedia(pageable);

    return ResponseEntity.ok(CommonPageResponse.fromPage(result));
  }
}

package com.example.mediaserver.modules.media.application.services;

import com.example.mediaserver.modules.media.infra.persistence.entities.MediaEntity;
import com.example.mediaserver.modules.media.infra.persistence.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetMediaService {
  private final MediaRepository mediaRepository;

  public Page<MediaEntity> getAllMedia(Pageable pageable) {
    Page<MediaEntity> result = mediaRepository.findAll(pageable);
    return result;
  }
}

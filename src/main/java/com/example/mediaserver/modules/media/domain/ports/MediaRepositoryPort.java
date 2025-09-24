package com.example.mediaserver.modules.media.domain.ports;

import com.example.mediaserver.modules.media.domain.entities.MediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaRepositoryPort {
  Page<MediaEntity> findAll(Pageable pageable);
}

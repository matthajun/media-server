package com.example.mediaserver.modules.media.infra.persistence.repositories;

import com.example.mediaserver.modules.media.domain.entities.MediaEntity;
import com.example.mediaserver.modules.media.domain.ports.MediaRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, String>, MediaRepositoryPort {
  Page<MediaEntity> findAll(Pageable pageable);
}

package com.example.mediaserver.modules.media.infra.persistence.repositories;

import com.example.mediaserver.modules.media.infra.persistence.entities.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, String> {}

package com.example.mediaserver.modules.media.infra.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "media")
@Getter()
public class MediaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String type;

  private String name;
}

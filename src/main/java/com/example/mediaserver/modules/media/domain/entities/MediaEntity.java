package com.example.mediaserver.modules.media.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "media")
@Getter
@Setter
public class MediaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public String id;

  private String type;

  private String name;
}

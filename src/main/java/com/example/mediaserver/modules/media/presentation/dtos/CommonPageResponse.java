package com.example.mediaserver.modules.media.presentation.dtos;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class CommonPageResponse<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private boolean isFirst;
  private boolean isLast;
  private boolean hasNext;
  private boolean hasPrevious;

  public static <T> CommonPageResponse<T> fromPage(Page<T> page) {
    CommonPageResponse<T> response = new CommonPageResponse<>();
    response.content = page.getContent();
    response.pageNumber = page.getNumber();
    response.pageSize = page.getSize();
    response.totalElements = page.getTotalElements();
    response.totalPages = page.getTotalPages();
    response.isFirst = page.isFirst();
    response.isLast = page.isLast();
    response.hasNext = page.hasNext();
    response.hasPrevious = page.hasPrevious();

    return response;
  }
}

package com.appsmart.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;
}

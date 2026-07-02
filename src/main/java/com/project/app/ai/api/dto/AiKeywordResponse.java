package com.project.app.ai.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AiKeywordResponse {
    private boolean success;
    private List<String> tags;
    private String image_description;
}
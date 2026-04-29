package com.adsboard.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Название категории обязательно")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String name;

    private Long parentId;
    private String parentName;

    private List<CategoryDTO> children;

    private Integer adsCount;

    @Builder.Default
    private Boolean isActive = true;
}

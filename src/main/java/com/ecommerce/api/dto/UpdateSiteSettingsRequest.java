package com.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSiteSettingsRequest(
        @NotBlank @Size(max = 160) String promoMessage1,
        @NotBlank @Size(max = 160) String promoMessage2,
        @NotBlank @Size(max = 160) String promoMessage3
) {
}

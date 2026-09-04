package com.ecommerce.api.controller;

import com.ecommerce.api.dto.SiteSettingsResponse;
import com.ecommerce.api.dto.UpdateSiteSettingsRequest;
import com.ecommerce.api.service.SiteSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/site-settings")
@RequiredArgsConstructor
public class SiteSettingsController {

    private final SiteSettingsService siteSettingsService;

    @GetMapping
    public ResponseEntity<SiteSettingsResponse> get() {
        return ResponseEntity.ok(siteSettingsService.get());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> update(@Valid @RequestBody UpdateSiteSettingsRequest request) {
        return ResponseEntity.ok(siteSettingsService.update(request));
    }
}

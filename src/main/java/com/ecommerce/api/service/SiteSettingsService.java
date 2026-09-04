package com.ecommerce.api.service;

import com.ecommerce.api.dto.SiteSettingsResponse;
import com.ecommerce.api.dto.UpdateSiteSettingsRequest;
import com.ecommerce.api.entity.SiteSettings;
import com.ecommerce.api.repository.SiteSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteSettingsService {

    private final SiteSettingsRepository siteSettingsRepository;

    public SiteSettingsResponse get() {
        return siteSettingsRepository.findAll().stream().findFirst()
                .map(this::toResponse)
                .orElseGet(this::defaultResponse);
    }

    @Transactional
    public SiteSettingsResponse update(UpdateSiteSettingsRequest request) {
        SiteSettings settings = siteSettingsRepository.findAll().stream().findFirst().orElseGet(this::newSettings);
        settings.setPromoMessage1(request.promoMessage1().trim());
        settings.setPromoMessage2(request.promoMessage2().trim());
        settings.setPromoMessage3(request.promoMessage3().trim());
        return toResponse(siteSettingsRepository.save(settings));
    }

    private SiteSettings newSettings() {
        return SiteSettings.builder()
                .promoMessage1("🔥 10% OFF pagando en efectivo")
                .promoMessage2("🔨 Herrería artesanal, hecha a tu medida")
                .promoMessage3("📍 Retirás tu pedido en el local — sin envíos")
                .build();
    }

    private SiteSettingsResponse toResponse(SiteSettings settings) {
        return new SiteSettingsResponse(settings.getPromoMessage1(), settings.getPromoMessage2(), settings.getPromoMessage3());
    }

    private SiteSettingsResponse defaultResponse() {
        return toResponse(newSettings());
    }
}

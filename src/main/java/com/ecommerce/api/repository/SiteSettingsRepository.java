package com.ecommerce.api.repository;

import com.ecommerce.api.entity.SiteSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SiteSettingsRepository extends JpaRepository<SiteSettings, UUID> {
}

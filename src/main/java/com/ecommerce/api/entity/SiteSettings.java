package com.ecommerce.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** Textos que se muestran en la barra promocional de la tienda. */
@Entity
@Table(name = "site_settings")
@Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
public class SiteSettings extends BaseEntity {

    @Column(nullable = false, length = 160)
    private String promoMessage1;

    @Column(nullable = false, length = 160)
    private String promoMessage2;

    @Column(nullable = false, length = 160)
    private String promoMessage3;
}

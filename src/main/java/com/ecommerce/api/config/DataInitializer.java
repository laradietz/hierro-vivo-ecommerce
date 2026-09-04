package com.ecommerce.api.config;

import com.ecommerce.api.entity.*;
import com.ecommerce.api.repository.CategoryRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.SiteSettingsRepository;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Carga datos iniciales para poder probar la tienda de Hierro Vivo apenas se
 * levanta el proyecto: un usuario admin y un catálogo de ejemplo de herrería
 * artesanal (piezas a medida, con días estimados de fabricación).
 * Se puede desactivar con app.seed.enabled=false.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SiteSettingsRepository siteSettingsRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${app.seed.admin-username:admin}")
    private String adminUsername;

    @Value("${app.seed.admin-password:admin123}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) return;

        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = User.builder()
                    .username(adminUsername)
                    .email("admin@hierrovivo.com.ar")
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .firstName("Administrador")
                    .lastName("Hierro Vivo")
                    .active(true)
                    .role(RoleName.ADMIN)
                    .build();
            userRepository.save(admin);
        }

        if (siteSettingsRepository.count() == 0) {
            siteSettingsRepository.save(SiteSettings.builder()
                    .promoMessage1("🔥 10% OFF pagando en efectivo")
                    .promoMessage2("🔨 Herrería artesanal, hecha a tu medida")
                    .promoMessage3("📍 Retirás tu pedido en el local — sin envíos")
                    .build());
        }

        if (categoryRepository.count() > 0) return; // catálogo ya cargado

        Category portones = category("Portones", "Portones de hierro a medida, corredizos y de dos hojas.");
        Category rejas = category("Rejas y Barandas", "Rejas de seguridad y barandas para escaleras y balcones.");
        Category muebles = category("Muebles de Hierro", "Mesas, sillas y estructuras en hierro y hierro-madera.");
        Category decoracion = category("Herrería Artística", "Piezas decorativas: faroles, macetas, esculturas.");
        Category parrillas = category("Parrillas y Exterior", "Parrillas, quinchos y estructuras para exterior.");
        Category reparaciones = category("Reparaciones y Soldadura", "Arreglos, refuerzos y trabajos de soldadura en general.");

        // sku, nombre, descripción, precio, stock (piezas ya hechas listas para
        // retirar), días de fabricación (si hay que hacerla a medida), categoría
        product("PT-0001", "Portón corredizo 4m", "Hierro macizo, terminación antióxido. A medida según tu abertura.", "620000", 0, 12, portones);
        product("PT-0002", "Portón de dos hojas 3m", "Diseño clásico con barrotes verticales.", "410000", 0, 9, portones);
        product("PT-0003", "Reja para ventana 1.2x1m", "Barrotes de 3/4\", incluye colocación.", "58000", 3, 4, rejas);

        product("RJ-0001", "Baranda de escalera (por metro)", "Hierro redondo con pasamanos de madera.", "34000", 0, 6, rejas);
        product("RJ-0002", "Reja de seguridad corrediza", "Para puertas de acceso, con candado.", "97000", 2, 7, rejas);
        product("RJ-0003", "Baranda de balcón (por metro)", "Diseño simple, pintura negro forja.", "29500", 0, 5, rejas);

        product("MB-0001", "Mesa ratona hierro y madera", "Estructura de hierro, tapa de pino maciza.", "89000", 4, 0, muebles);
        product("MB-0002", "Silla de hierro forjado", "Asiento de madera, terminación rústica.", "45000", 6, 0, muebles);
        product("MB-0003", "Estantería industrial 1.8m", "3 estantes de madera, estructura de caño.", "76000", 2, 5, muebles);

        product("HA-0001", "Farol de hierro forjado", "Para exterior, vidrio incluido.", "23500", 8, 0, decoracion);
        product("HA-0002", "Maceta de hierro con base", "Diseño geométrico, terminación negro mate.", "15800", 10, 0, decoracion);
        product("HA-0003", "Cruz de hierro forjado 60cm", "Trabajo artesanal, para pared o jardín.", "19900", 5, 3, decoracion);

        product("PR-0001", "Parrilla a medida (chapa + hierro)", "Con parante y campana, a medida del quincho.", "185000", 0, 10, parrillas);
        product("PR-0002", "Parrilla portátil con patas", "Lista para retirar, uso inmediato.", "42000", 6, 0, parrillas);

        product("RP-0001", "Soldadura de refuerzo (por hora)", "Reparación de estructuras existentes.", "12000", 0, 1, reparaciones);
        product("RP-0002", "Arreglo de portón / reja", "Diagnóstico y reparación en el local.", "18000", 0, 2, reparaciones);
    }

    private Category category(String name, String description) {
        return categoryRepository.save(Category.builder()
                .name(name).description(description).active(true).build());
    }

    private void product(String sku, String name, String description, String price,
                          int stock, int productionDays, Category category) {
        productRepository.save(Product.builder()
                .sku(sku).name(name).description(description)
                .price(new BigDecimal(price)).stock(stock).productionDays(productionDays)
                .category(category).active(true).build());
    }
}

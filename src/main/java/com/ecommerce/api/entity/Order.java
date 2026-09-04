package com.ecommerce.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
public class Order extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    // Sin envíos: todo se retira en el local. Estas notas son para
    // especificaciones del encargo (medidas, terminación, color, etc.).
    @Column(length = 500)
    private String notes;

    // Calculada al crear el pedido, según el producto que más tarda.
    @Column(name = "estimated_ready_date")
    private LocalDate estimatedReadyDate;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY,
              cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;
}

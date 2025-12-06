package com.david.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PurchaseOrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = PurchaseOrderStatus.RECEIVED;
    }
}

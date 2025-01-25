package com.fullstackbootcamp.capstoneBackend.business.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_statements")
public class FinancialStatementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    // TODO: Add the rest of the fields

    public void setId(Long id) {
        this.id = id;
    }
}

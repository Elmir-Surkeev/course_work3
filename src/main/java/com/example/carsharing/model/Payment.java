package com.example.carsharing.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private Long id;
    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Rental rental;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;

    public Payment() {
    }

    public Payment(Long id) {
        this.id = id;
    }

    public enum Status {
        PENDING, PAID
    }

    public enum Type {
        PAYMENT, FINE
    }
}

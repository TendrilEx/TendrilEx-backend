package com.example.parcel_delivery.models.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.parcel_delivery.models.enums.ParcelStatus;
import com.example.parcel_delivery.models.enums.ParcelType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "parcels", indexes = {
        @Index(name = "idx_parcel_status", columnList = "status"),
        @Index(name = "idx_parcel_driver_status", columnList = "driver_id, status")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double width;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double depth;

    @Column(nullable = false)
    private Double mass;

    @Column(nullable = false)
    private String description;

    @Column
    private String unregisteredRecipientName;

    @Column
    private String unregisteredRecipientPhone;

    @Column
    private String unregisteredRecipientEmail;

    @Column
    private String unregisteredRecipientAddress;

    @Column
    private String unregisteredRecipientPostcode;

    @Column
    private String unregisteredRecipientCity;

    @Column
    private Boolean isRecipientRegistered = false;

    @Column(nullable = false, unique = true)
    private Integer senderTransactionCode;

    @Column(nullable = false)
    private Boolean senderTransactionCodeActive = true;

    @Column(nullable = false)
    private LocalDateTime senderTransactionCodeValidUntil;

    // @Column(nullable = false, unique = true)
    private Integer recipientTransactionCode;

    // @Column(nullable = false)
    private Boolean recipientTransactionCodeActive = true;

    // @Column(nullable = false)
    private LocalDateTime recipientTransactionCodeValidUntil;

    @Column(nullable = false)
    private Boolean deliverToRecipientLocker = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParcelStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParcelType parcelType;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @JsonBackReference
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private Customer recipient;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cabinet_id", referencedColumnName = "id")
    @JsonManagedReference
    private Cabinet cabinet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    private Storage storage;

    @ManyToOne
    @JoinColumn(name = "selected_locker_location_id")
    private ParcelLocker selectedLockerLocation;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = true)
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    @Column(updatable = true)
    private LocalDateTime statusUpdatedAt;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private LocalDateTime idempotencyKeyCreatedAt;

}

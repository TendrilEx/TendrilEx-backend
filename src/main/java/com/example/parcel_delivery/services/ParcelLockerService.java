package com.example.parcel_delivery.services;

import java.util.List;

import com.example.parcel_delivery.models.entities.ParcelLocker;

public interface ParcelLockerService {

    List<ParcelLocker> getFiveNearestAvailablelockers();

    ParcelLocker getParcelLockerById(Long selectedLockerId);
    
}

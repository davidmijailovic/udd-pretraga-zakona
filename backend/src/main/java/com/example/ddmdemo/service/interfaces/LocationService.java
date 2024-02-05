package com.example.ddmdemo.service.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface LocationService {
    Double[] getCoordinates(String address);
}

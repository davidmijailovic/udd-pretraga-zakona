package com.example.ddmdemo.service.impl;

import com.example.ddmdemo.service.interfaces.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final RestTemplate restTemplate;
    @Override
    public Double[] getCoordinates(String address) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://us1.locationiq.com/v1/search")
                .queryParam("key", "pk.316f1582f694cbd8f4f414bec8a1be13")
                .queryParam("q", address)
                .queryParam("format", "json");

        String response = restTemplate.getForObject(builder.encode().build().toUri(), String.class);

        String longitude = "";
        String latitude = "";

        Pattern pattern = Pattern.compile("\"lon\":\"([\\d.]+)\"");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()) longitude = matcher.group(1);

        pattern = Pattern.compile("\"lat\":\"([\\d.]+)\"");
        matcher = pattern.matcher(response);
        if(matcher.find()) latitude = matcher.group(1);

        return new Double[]{Double.parseDouble(longitude), Double.parseDouble(latitude)};
    }
}

package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        String sensorName = "Sensor1";

        registerSensor(sensorName);

        Random random = new Random();
        double maxTemperature = 45.0;
        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            sendMeasurements(random.nextDouble() * maxTemperature,
                    random.nextBoolean(), sensorName);
        }
        getMeasurements();
    }

    private static void registerSensor(String sensorName) {
        String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequest(url, jsonData);
        System.out.println("Сенсор " + sensorName + " успешно зарегистрирован!");
    }

    private static void sendMeasurements(double value, boolean raining, String sensorName) {
        String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));

        makePostRequest(url, jsonData);
        System.out.println("Измерения успешно отправлены!");
    }

    private static void getMeasurements() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/measurements";

        String response = restTemplate.getForObject(url, String.class);

        System.out.println(response);
        System.out.println("Измерения успешно приняты!");
    }

    private static void makePostRequest(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonData, httpHeaders);

        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR!");
            System.out.println(e.getMessage());
        }
    }
}

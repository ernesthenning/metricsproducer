package com.example.metricsproducer.service;

import com.example.metricsproducer.model.Metric;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final RestTemplate restTemplate;
    private final String urlMetrics = "http://localhost:8080/actuator/metrics";
    private final List<String> metrics = List.of("disk.free", "disk.total");

    public List<Metric> getAllMetrics() throws JsonProcessingException {
        List<Metric> result = new ArrayList<>();
        for(String metric : metrics) {
            String json = restTemplate.getForObject(urlMetrics + "/" + metric, String.class);
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.readTree(json).get("value").asText();
            result.add(new Metric(metric, value));
        }
        return result;
    }
}

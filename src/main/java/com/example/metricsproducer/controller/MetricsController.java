package com.example.metricsproducer.controller;

import com.example.metricsproducer.model.Metric;
import com.example.metricsproducer.service.MetricsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Slf4j
public class MetricsController {

    private final KafkaTemplate<String, Metric> kafkaTemplate;
    private final MetricsService service;

    @PostMapping
    public void sendMetrics() throws JsonProcessingException {
        List<Metric> metrics = service.getAllMetrics();
        metrics.forEach(metric -> kafkaTemplate.send("metrics-topic", metric)
                .thenAccept(result -> {
                    final var sentMetric = result.getProducerRecord().value();
                    log.info("Metric {}:{} is successfully sent to Kafka", sentMetric.tag(), sentMetric.value());
                }));
    }
}

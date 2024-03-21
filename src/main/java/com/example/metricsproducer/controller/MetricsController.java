package com.example.metricsproducer.controller;

import com.example.metricsproducer.model.Metric;
import com.example.metricsproducer.service.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/metrics")
@RequiredArgsConstructor
@Slf4j
public class MetricsController {

    private final MetricsService service;

    @PostMapping
    public void sendMetrics()  {
        service.sendMetrics();
    }
}

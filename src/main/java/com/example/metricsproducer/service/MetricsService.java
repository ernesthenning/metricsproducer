package com.example.metricsproducer.service;

import com.example.metricsproducer.model.Metric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final KafkaTemplate<String, Metric> kafkaTemplate;
    private final MetricsEndpoint metricsEndpoint;
    private final List<String> metrics = List.of("disk.free", "disk.total");

    @Scheduled(cron = "@hourly")
    public void sendMetrics() {
        List<Metric> data = new ArrayList<>();
        for(String metric : metrics) {
            data.add(new Metric(metric, metricsEndpoint.metric(metric, new ArrayList<>()).getMeasurements().get(0).getValue()));
        }
        for(Metric metric : data) {
            sendMetric(metric);
        }
    }

    private void sendMetric(Metric metric) {
        kafkaTemplate.send("metrics-topic", metric).whenComplete((result, e) -> {
            if(e != null) {
                throw new IllegalStateException();
            }
            final var sentResult = result.getProducerRecord().value();
            log.info("Metric {}:{} sent to Kafka", sentResult.tag(), sentResult.value());
        });
    }
}

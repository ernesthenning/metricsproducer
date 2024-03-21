# Поставщик метрик
Данный сервис получает метрики от эндпоинта Spring Actuator и отправляет их в Kafka.

### Запуск сервиса
- Запустите Kafka по адресу localhost:9092
- Запустите приложение командами:
   - cd metricsproducer
   - mvn spring-boot:run
- Альтернативно можно запустить приложение через IDE путем запуска файла MetricsproducerrApplication.java
### Описание API
#### Post
- /metrics - запрашивает текущие метрики и отправляет в Kafka


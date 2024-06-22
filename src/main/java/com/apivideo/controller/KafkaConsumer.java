package com.apivideo.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "apivideo")
    public void consumeMessage(ConsumerRecord<String, String> record) {
        log.info("Received message: Key={}, Value={}", record.key(), record.value());
        // 处理接收到的消息
    }
}

package dev.nnmod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "drone-simulator");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        try (KafkaConsumer<String, String> commandConsumer = new KafkaConsumer<>(consumerProps);
             KafkaProducer<String, String> telemetryProducer = new KafkaProducer<>(producerProps)) {

            commandConsumer.subscribe(List.of("drone-commands"));

            while (true) {
                ConsumerRecords<String, String> records =
                        commandConsumer.poll(Duration.ofMillis(500));

                for (ConsumerRecord<String, String> record : records) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    StartCommand cmd = objectMapper.readValue(record.value(), StartCommand.class);
                    startSimulationThread(cmd, telemetryProducer);
                }
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startSimulationThread(StartCommand cmd,
                                              KafkaProducer<String, String> telemetryProducer) {

        DroneSimulator.TelemetrySender sender = (missionId, json) -> {
            telemetryProducer.send(
                    new ProducerRecord<>("drone-telemetry", missionId, json)
            );
        };

        DroneSimulator simulator = new DroneSimulator(cmd, sender);

        new Thread(simulator::run, "sim-" + cmd.missionId).start();
    }
}
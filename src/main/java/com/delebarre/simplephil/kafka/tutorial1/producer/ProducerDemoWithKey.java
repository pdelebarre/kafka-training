package com.delebarre.simplephil.kafka.tutorial1.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithKey {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithKey.class);
        String bootstrapServers = "127.0.0.1:9092";

        // create Producer properties
        Properties properties = new Properties();
        //properties.setProperty("bootstrap.servers", bootstrapServers);
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);



        for(int i=0;i<10;i++) {
            String key = "id_"+Integer.toString(i);
            String value = "Hello world "+Integer.toString(i);

            // create a producer record
             ProducerRecord<String,String> record = new ProducerRecord<String, String>("first_topic",key,value);
                logger.info("Key: " + key);

            // send data - synchronous - BAD !
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //execute every time a record is successfully sent or exception is thrown
                    if (e == null) {
                        logger.info("Received new metadata\n" +
                                "Topic:" + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("error while producing", e);
                    }
                }
            }).get();
        }

        // flush data
        producer.flush();

        // flush and close producer
        producer.close();

    }
}

package com.example.kafka.springbootkafka.resource;

import com.example.kafka.springbootkafka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka")
public class UserResource {
    @Autowired
    KafkaTemplate<String, User> kafkaTemplate;
    @Autowired
    KafkaTemplate<String, Integer> strKafkaTemplate;

    final static String TOPIC = "message";
    final static String TOPIC_USER = "user";

    @GetMapping("/publishmessage/{name}")
    public String publishMessage(@PathVariable("name") final String name) {
//        strKafkaTemplate.send(TOPIC, "Hi " + name);
        strKafkaTemplate.send(TOPIC, new Integer(2));
        return "Published Successfully";
    }

    @GetMapping("/publish/{name}")
    public String publish(@PathVariable("name") final String name) {
        kafkaTemplate.send(TOPIC_USER, new User(name, "Technology", 12000L));
        return "JSON message Published Successfully";
    }

    @KafkaListener(topics = TOPIC, groupId = "0")
    public void consumeMessage(String message) {
        System.out.println("Consumed message :" + message);
    }

    @KafkaListener(topics = TOPIC_USER, groupId = "0", containerFactory = "userKafkaListenerFactory")
    public void consumeJson(User user) {
        System.out.println("Consumed Json Object :" + user.toString());
    }
}

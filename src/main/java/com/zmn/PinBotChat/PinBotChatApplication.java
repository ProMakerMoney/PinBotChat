package com.zmn.PinBotChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PinBotChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinBotChatApplication.class, args);
	}

}

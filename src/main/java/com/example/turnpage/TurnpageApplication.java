package com.example.turnpage;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication(/*exclude = SecurityAutoConfiguration.class*/)
public class TurnpageApplication {
	public static void main(String[] args) {
		SpringApplication.run(TurnpageApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println("타임존 설정 후 현재 시각: " + LocalDate.now());
	}
}

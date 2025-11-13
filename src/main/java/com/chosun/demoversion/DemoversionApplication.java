package com.chosun.demoversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // @CreatedDate를 사용하기 위해 어노테이션 추가
@SpringBootApplication
public class DemoversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoversionApplication.class, args);
	}

}

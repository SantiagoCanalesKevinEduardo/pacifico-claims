package com.pacifico.claims;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "file:./.env")
class ClaimsServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}


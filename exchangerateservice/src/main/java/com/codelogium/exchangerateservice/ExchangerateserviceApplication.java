package com.codelogium.exchangerateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.codelogium.exchangerateservice.mapper.CmcErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class ExchangerateserviceApplication {

	public static void main(String[] args) {
		String errorJson = "{ \"status\": { \"timestamp\": \"2025-02-03T15:26:51.192Z\", \"error_code\": 400, \"error_message\": \"Invalid value for \\\"convert\\\": \\\"FCFA\\\"\","
				+
				" \"elapsed\": 0, \"credit_count\": 0, \"notice\": null } }";

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // Enables Instant, LocalDate, etc.

		try {
			CmcErrorResponse cmcErrorResponse = objectMapper.readValue(errorJson, CmcErrorResponse.class);
			System.out.println(cmcErrorResponse);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpringApplication.run(ExchangerateserviceApplication.class, args);
	}

}

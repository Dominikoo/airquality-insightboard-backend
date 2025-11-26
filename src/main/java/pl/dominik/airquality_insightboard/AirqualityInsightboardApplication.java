package pl.dominik.airquality_insightboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "pl.dominik.airquality_insightboard")
public class AirqualityInsightboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirqualityInsightboardApplication.class, args);
	}

}

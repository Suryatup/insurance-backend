package com.example.insurance;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceApplication.class, args);
	}


	@Configuration
	@EnableMongoRepositories
	public class MongoConfig extends AbstractMongoClientConfiguration {

		@Value("${spring.data.mongodb.uri}")
		private String mongoUri;

		@Override
		protected String getDatabaseName() {
			return "gatewaycloudstorage_db_user";
		}

		@Override
		public MongoClient mongoClient() {
			return MongoClients.create(mongoUri);
		}
	}

}

/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smoketest.data.mongo;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SampleMongoApplication {

	@Bean
	public CommandLineRunner exampleRunner(CustomerRepository repository) {
		return (args) -> {
			repository.deleteAll();

			// save a couple of customers
			repository.save(new Customer("Alice", "Smith"));
			repository.save(new Customer("Bob", "Smith"));

			// fetch all customers
			System.out.println("Customers found with findAll():");
			System.out.println("-------------------------------");
			for (Customer customer : repository.findAll()) {
				System.out.println(customer);
			}
			System.out.println();

			// fetch an individual customer
			System.out.println("Customer found with findByFirstName('Alice'):");
			System.out.println("--------------------------------");
			System.out.println(repository.findByFirstName("Alice"));

			System.out.println("Customers found with findByLastName('Smith'):");
			System.out.println("--------------------------------");
			for (Customer customer : repository.findByLastName("Smith")) {
				System.out.println(customer);
			}
		};
	}

	@Bean
	public MongoClientSettingsBuilderCustomizer customizer() {
		return (builder) -> builder.applyToConnectionPoolSettings(
				(connectionPool) -> connectionPool.maxConnectionIdleTime(5, TimeUnit.MINUTES));
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleMongoApplication.class, args);
	}

}

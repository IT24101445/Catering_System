package com.example.catering_system;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.catering_system.delivery.repository.DeliverySupervisorRepository;
import com.example.catering_system.delivery.service.DeliverySupervisorService;

@SpringBootApplication(scanBasePackages = {"com.example.catering_system"})
public class
CateringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CateringSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner seedDefaultSupervisor(DeliverySupervisorRepository repo, DeliverySupervisorService service) {
		return args -> {
			String defaultEmail = "supervisor@example.com";
			String defaultName = "Admin Supervisor";
			String defaultPassword = "supervisor123";
			if (!repo.existsByEmail(defaultEmail)) {
				service.create(defaultEmail, defaultName, defaultPassword);
			}
		};
	}

}

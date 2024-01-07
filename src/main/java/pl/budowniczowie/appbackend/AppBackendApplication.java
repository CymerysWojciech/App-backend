package pl.budowniczowie.appbackend;

import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.budowniczowie.appbackend.upload.FileStorageService;

@SpringBootApplication
public class AppBackendApplication  {

	public static void main(String[] args) {
		SpringApplication.run(AppBackendApplication.class, args);
	}

}

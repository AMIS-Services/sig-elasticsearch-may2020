package nl.amis.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class RecipesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipesServiceApplication.class, args);
	}

}

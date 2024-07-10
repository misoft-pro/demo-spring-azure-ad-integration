package pro.misoft.demospringazuread;

import org.springframework.boot.SpringApplication;

public class TestDemoSpringAzureAdApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoSpringAzureAdApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

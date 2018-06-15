package io.block16.assetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"io.block16"})
public class AssetApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AssetApiApplication.class, args);
	}
}

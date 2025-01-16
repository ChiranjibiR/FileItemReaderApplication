package com.cloudops.FileItemReader;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileItemReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileItemReaderApplication.class, args);
	}

}

package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import com.example.demo.dao.PublicationRepository;
import com.example.demo.entities.Publication;

@SpringBootApplication
@EnableDiscoveryClient
public class PublicationService implements CommandLineRunner {

	@Autowired
	PublicationRepository publicationRepository;
	
	
	@Autowired
	RepositoryRestConfiguration configuration;

	public static void main(String[] args) {
		SpringApplication.run(PublicationService.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		configuration.exposeIdsFor(Publication.class);
	}
	

}

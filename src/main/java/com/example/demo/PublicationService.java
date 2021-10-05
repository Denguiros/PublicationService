package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.dao.PublicationRepository;
import com.example.demo.entities.Publication;

@SpringBootApplication
public class PublicationService implements CommandLineRunner {

	@Autowired
	PublicationRepository publicationRepository;

	public static void main(String[] args) {
		SpringApplication.run(PublicationService.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Publication publication = new Publication();
		publicationRepository.save(publication);
	}
	

}

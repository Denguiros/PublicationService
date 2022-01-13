package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.Publication;

public interface IPublicationService {
	// Crud sur les Publications
	Publication addPublication(Publication m);
	void deletePublication(Long id);
	Publication updatePublication(Publication p);
	Publication findPublication(Long id);
	List<Publication> findAll();
}

package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PublicationRepository;
import com.example.demo.entities.Publication;


@Service
public class PublicationImpl implements IPublicationService {

	@Autowired
	PublicationRepository PublicationRepository;

	@Override
	public Publication addPublication(Publication m) {
		PublicationRepository.save(m);
		return m;
	}

	@Override
	public void deletePublication(Long id) {
		PublicationRepository.deleteById(id);
	}

	@Override
	public Publication updatePublication(Publication m) {
		return PublicationRepository.saveAndFlush(m);
	}

	@Override
	public Publication findPublication(Long id) {
		return (Publication) PublicationRepository.findById(id).get();
	}

	@Override
	public List<Publication> findAll() {
		return PublicationRepository.findAll();
	}





}
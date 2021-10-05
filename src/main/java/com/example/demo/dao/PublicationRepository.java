package com.example.demo.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
	List<Publication> findByDate(Date date);
	List<Publication> findByType(String type);
	Publication findBySourcePDF(String sourcePDF);
	Publication findByTitre(String titre);
	Publication findByLien(String lien);
}

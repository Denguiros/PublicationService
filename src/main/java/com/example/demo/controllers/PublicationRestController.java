package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Publication;
import com.example.demo.service.IPublicationService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT }, allowedHeaders = "*")
public class PublicationRestController {
	@Autowired
	IPublicationService publicationService;

	@RequestMapping(value = "/publications", method = RequestMethod.GET)
	public List<Publication> findPublications() {

		List<Publication> Publications = publicationService.findAll();
		return Publications;
	}

	@GetMapping(value = "/publication/{id}")
	public Publication findOnePublicationById(@PathVariable Long id) {
		Publication Publication = publicationService.findPublication(id);
		return Publication;
	}

	@PostMapping(value = "/nouvellePublication")
	public Publication addPublication(@RequestParam(name = "publication") String publication,
			@RequestParam(name = "pdf", required = true) MultipartFile pdf,@RequestParam(name = "photo", required = true) MultipartFile photo) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		Publication pub = null;
		try {
			pub = objectMapper.readValue(publication, Publication.class);
			if (pdf != null) {
				String path = writeFile(pub.getTitre()+"/pdf", pdf);
				String encodedPath = "";
				try {
					encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				pub.setSourcePDF(encodedPath);
			}
			if (photo != null) {
				String path = writeFile(pub.getTitre()+"/photo", photo);
				String encodedPath = "";
				try {
					encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				pub.setPhoto(encodedPath);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return publicationService.addPublication(pub);
	}

	@DeleteMapping(value = "/deletePublication/{id}")
	public void deletePublication(@PathVariable Long id) {
		publicationService.deletePublication(id);
	}

	@GetMapping(value = "/get-file")
	@ResponseBody
	public FileSystemResource getFile(@RequestParam(name = "path") String path) {
		String decodedPath = URLDecoder.decode(path);
		System.out.println(decodedPath);
		File f = new File(decodedPath);
		return new FileSystemResource(f.getAbsolutePath());
	}

	@PutMapping(value = "/editPublication/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public Publication updatePublication(@PathVariable Long id, @RequestParam(name = "publication") String publication,
			@RequestParam(name = "pdf", required = false) MultipartFile pdf,@RequestParam(name = "photo", required = false) MultipartFile photo) {
		Publication Publication = publicationService.findPublication(id);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		Publication pub = null;
		try {
			pub = objectMapper.readValue(publication, Publication.class);
			if (pdf != null) {
				String path = writeFile(pub.getTitre()+"/pdf", pdf);
				String encodedPath = "";
				try {
					encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				pub.setSourcePDF(encodedPath);
			}
			if (photo != null) {
				String path = writeFile(pub.getTitre()+"/photo", photo);
				String encodedPath = "";
				try {
					encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				pub.setPhoto(encodedPath);
			}
			if (pub.getSourcePDF() == "" || pub.getSourcePDF() == null) {
				pub.setSourcePDF(Publication.getSourcePDF());
			}
			if (pub.getPhoto() == "" || pub.getPhoto() == null) {
				pub.setPhoto(Publication.getPhoto());
			}

			pub.setId(id);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return publicationService.updatePublication(pub);
	}

	public String writeFile(String name, MultipartFile file) {

		String uploadsDir = "uploads/" + name + "/";
		File f = new File(uploadsDir);
		String absolutePath = f.getAbsolutePath();
		if (!f.exists()) {
			System.out.println("Creating new directory");
			f.mkdirs();
		}
		String orgName = file.getOriginalFilename();
		String filePath = absolutePath + "/" + orgName;
		File dest = new File(filePath);
		try {
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uploadsDir + "/" + orgName;
	}

}

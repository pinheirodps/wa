package com.warwickanalytics.challenge.resource;

import com.warwickanalytics.challenge.dto.CsvDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.warwickanalytics.challenge.storage.StorageImpl;

import java.util.List;

@RestController
@RequestMapping("/upload")
@CrossOrigin("*")
public class CSVResource {
	
	@Autowired
	private StorageImpl storage;
	
	@PostMapping
	public List<CsvDTO> upload(@RequestParam MultipartFile csv) {
		return storage.uploadAndListCSV(csv);
	}

}

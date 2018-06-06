package com.warwickanalytics.challenge.storage;

import com.warwickanalytics.challenge.dto.CsvDTO;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.io.FileUtils.readFileToString;


@Component
public class StorageImpl implements Storage {
	
	@Value("${csv.storage.root}")
	private String root;
	
	@Value("${csv.storage.directory-csvs}")
	private String directoryCSV;
	private static final Logger LOGGER = Logger.getLogger(StorageImpl.class.getName());

	@Override
	public List<CsvDTO> uploadAndListCSV(MultipartFile file) {
		return this.saveFile(file, this.directoryCSV);
	}

	private List<CsvDTO> saveFile(MultipartFile file, String directory) {

		Path directoryPath = Paths.get(this.root, directory);
		Path filePath = directoryPath.resolve(file.getOriginalFilename());

		List<CsvDTO> csvDTOS;
		try {
			Files.createDirectories(directoryPath);
			file.transferTo(filePath.toFile());
			csvDTOS = loadCSV(CsvDTO.class, file.getOriginalFilename());

			csvDTOS.stream().filter((f)->f.getDecision().equals("1"));

			csvDTOS.removeIf((f) -> f.getDecision().equals("0"));

			csvDTOS.forEach(System.out::println);

		} catch (IOException e) {
			throw new RuntimeException("Failed to save file!", e);
		}
		return csvDTOS;
	}

	public <T> List<T> loadCSV(Class<T> type, String fileName) {
		try {
			CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
			CsvMapper mapper = new CsvMapper();
			File file = new File(this.root+directoryCSV+"/"+fileName);
			String content = readFileToString(file);
			FileUtils.writeStringToFile(file, content.toLowerCase());
			MappingIterator<T> readValues =
					mapper.reader(type).with(bootstrapSchema).readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,"Error occurred while loading object list from file " + fileName, e);
			return Collections.emptyList();
		}
	}





}




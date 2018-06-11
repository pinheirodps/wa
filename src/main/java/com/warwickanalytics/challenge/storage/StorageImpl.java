package com.warwickanalytics.challenge.storage;

import com.warwickanalytics.challenge.dto.CsvDTO;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        List<CsvDTO> newCsvDTOSList = new ArrayList<>();
		try {
			Files.createDirectories(directoryPath);
			file.transferTo(filePath.toFile());
			csvDTOS = loadCSV(CsvDTO.class, file.getOriginalFilename());

            Comparator<CsvDTO> comparator = Comparator.comparing(CsvDTO::getVar1)
                    .thenComparing(CsvDTO::getVar2)
                    .thenComparing(CsvDTO::getVar3)
                    .thenComparing(CsvDTO::getVar4)
                    .thenComparing(CsvDTO::getVar5);


            CsvDTO minObject = csvDTOS.stream().filter(line -> line.getDecision().equals("1")).min(comparator).get();
            CsvDTO  maxObject = csvDTOS.stream().filter(line -> line.getDecision().equals("1")).max(comparator).get();
			newCsvDTOSList.add(minObject);
			newCsvDTOSList.add(maxObject);


            for (CsvDTO dto : csvDTOS){
            	if(dto.getDecision().equals("0") &&
						dto.getVar1()!=null &&  dto.getVar1() >= minObject.getVar1() && dto.getVar1() <= maxObject.getVar1()){
					newCsvDTOSList.add(dto);
				}else if(dto.getDecision().equals("0") &&
						dto.getVar2()!=null &&  dto.getVar2() >= minObject.getVar2() && dto.getVar2() <= maxObject.getVar2()){
					newCsvDTOSList.add(dto);
				}else if(dto.getDecision().equals("0") &&
						dto.getVar3()!=null &&  dto.getVar3() >= minObject.getVar3() && dto.getVar3() <= maxObject.getVar3()){
					newCsvDTOSList.add(dto);
				}else if(dto.getDecision().equals("0") &&
						dto.getVar4()!=null &&  dto.getVar4() >= minObject.getVar4() && dto.getVar4() <= maxObject.getVar4()){
					newCsvDTOSList.add(dto);
				}else if(dto.getDecision().equals("0") && dto.getVar5()!=null &&  dto.getVar5() >= minObject.getVar5() && dto.getVar5() <= maxObject.getVar5()){
					newCsvDTOSList.add(dto);
				}
			}

			Comparator<CsvDTO> newComparator = Comparator.comparing(CsvDTO::getId);

			newCsvDTOSList.sort(newComparator);
			System.out.println("################");

			newCsvDTOSList.forEach(System.out::println);


		} catch (IOException e) {
			throw new RuntimeException("Failed to save file!", e);
		}
		return newCsvDTOSList;
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




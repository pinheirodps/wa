package com.warwickanalytics.challenge.storage;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.warwickanalytics.challenge.dto.CsvDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.io.FileUtils.readFileToString;


@Component
public class StorageImpl implements Storage {
    //specific directory to save uploaded file
    @Value("${csv.storage.root}")
    private String root;
    //directory name where the file is uploaded
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

        List<CsvDTO> csvDTOList;
        List<CsvDTO> newCsvDTOSList = new ArrayList<>();
        try {
            Files.createDirectories(directoryPath);
            file.transferTo(filePath.toFile());
            csvDTOList = loadCSV(CsvDTO.class, file.getOriginalFilename());
            //conditions to export
            conditionsExportCsv(csvDTOList, newCsvDTOSList);


        } catch (IOException e) {
            throw new RuntimeException("Failed to save file!", e);
        }
        return newCsvDTOSList;
    }

    private void conditionsExportCsv(List<CsvDTO> csvDTOList, List<CsvDTO> newCsvDTOSList) {
        Comparator<CsvDTO> comparator = Comparator.comparing(CsvDTO::getVar1)
                .thenComparing(CsvDTO::getVar2)
                .thenComparing(CsvDTO::getVar3)
                .thenComparing(CsvDTO::getVar4)
                .thenComparing(CsvDTO::getVar5);

        //Getting FMIN and FMAX.
        CsvDTO fmin = csvDTOList.stream().filter(line -> line.getDecision().equals("1")).min(comparator).get();
        CsvDTO fmax = csvDTOList.stream().filter(line -> line.getDecision().equals("1")).max(comparator).get();
        newCsvDTOSList.add(fmin);
        newCsvDTOSList.add(fmax);

        //Have a Decision of 0
        for (CsvDTO dto : csvDTOList) {
            if (dto.getDecision().equals("0") &&
                    dto.getVar1() != null && dto.getVar1() >= fmin.getVar1() && dto.getVar1() <= fmax.getVar1()) {
                newCsvDTOSList.add(dto);
            } else if (dto.getDecision().equals("0") &&
                    dto.getVar2() != null && dto.getVar2() >= fmin.getVar2() && dto.getVar2() <= fmax.getVar2()) {
                newCsvDTOSList.add(dto);
            } else if (dto.getDecision().equals("0") &&
                    dto.getVar3() != null && dto.getVar3() >= fmin.getVar3() && dto.getVar3() <= fmax.getVar3()) {
                newCsvDTOSList.add(dto);
            } else if (dto.getDecision().equals("0") &&
                    dto.getVar4() != null && dto.getVar4() >= fmin.getVar4() && dto.getVar4() <= fmax.getVar4()) {
                newCsvDTOSList.add(dto);
            } else if (dto.getDecision().equals("0") && dto.getVar5() != null && dto.getVar5() >= fmin.getVar5() && dto.getVar5() <= fmax.getVar5()) {
                newCsvDTOSList.add(dto);
            }
        }

        Comparator<CsvDTO> newComparator = Comparator.comparing(CsvDTO::getId);

        newCsvDTOSList.sort(newComparator);
        System.out.println("################");
        newCsvDTOSList.forEach(System.out::println);
    }

    public <T> List<T> loadCSV(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new File(this.root + directoryCSV + "/" + fileName);
            String content = readFileToString(file);
            FileUtils.writeStringToFile(file, content.toLowerCase());
            MappingIterator<T> readValues =
                    mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }


}




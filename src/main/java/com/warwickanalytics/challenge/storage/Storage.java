package com.warwickanalytics.challenge.storage;

import com.warwickanalytics.challenge.dto.CsvDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Storage {

    List<CsvDTO> uploadAndListCSV(MultipartFile file);
}

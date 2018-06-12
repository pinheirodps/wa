package com.warwickanalytics.challenge;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.warwickanalytics.challenge.dto.CsvDTO;
import com.warwickanalytics.challenge.resource.CSVResource;
import com.warwickanalytics.challenge.storage.StorageImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UploadApiApplicationTests {


	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wContext;

	@Autowired
	private CSVResource csvResource;

	MockMultipartFile firstFile;


	//specific directory to save uploaded file
	@Value("${csv.storage.root}")
	private String root;
	//directory name where the file is uploaded
	@Value("${csv.storage.directory-csvs}")
	private String directoryCSV;
	private static final Logger LOGGER = Logger.getLogger(StorageImpl.class.getName());

	@Before
	public void setup()throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
				.alwaysDo(print())
				.build();
		File file = new File(this.root + directoryCSV + "/" + "exampleC_input.csv");
		InputStream targetStream = new FileInputStream(file);
		firstFile = new MockMultipartFile("csv","exampleC_input.csv", "application/vnd.ms-excel",targetStream);
	}



	@Test
	public void contextLoads() {
	}

	@Test
	public void testUploadFileResultOk() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(firstFile)
				.param("csv", "exampleC_input.csv")
				.characterEncoding("UTF-8"))
				.andExpect(status().isOk());
	}

}

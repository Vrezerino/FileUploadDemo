package filemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assume.assumeThat;
//import static org.hamcrest.CoreMatchers.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileManagerTest {
	private final String API_URI = "/files";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getFilesReturnsSite() throws Exception {
		MvcResult res = mockMvc.perform(get(API_URI))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("files"))
				.andReturn();

		assertTrue(res.getResponse().getContentAsString().contains("File manager"));
	}

	@Test
	public void filenameVisibleAfterPost() throws Exception {
		String randomName = UUID.randomUUID().toString().substring(0, 6);
		MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, "burgerz/mmm", "yay".getBytes());

		mockMvc.perform(fileUpload(API_URI).file(multipartFile)) // fileUpload() = deprecated, being replaced.
				.andExpect(redirectedUrl(API_URI));

		MvcResult res = mockMvc.perform(get(API_URI))
				.andExpect(status().isOk()).andReturn();

		assertTrue(res.getResponse().getContentAsString().contains(randomName));
	}

	@Test
	public void downloadedFileAvailableAndCorrect() throws Exception {
		String randomContent = UUID.randomUUID().toString().substring(0, 6);
		String randomName = UUID.randomUUID().toString().substring(0, 6);

		MockMultipartFile multipartFile = new MockMultipartFile("file", randomName,
				MediaType.IMAGE_GIF_VALUE, randomContent.getBytes());

		mockMvc.perform(fileUpload(API_URI).file(multipartFile))
				.andExpect(redirectedUrl(API_URI));

		MvcResult res = mockMvc.perform(get(API_URI))
				.andExpect(status().isOk()).andReturn();

		Long filedId = (long) 6; //

		res = mockMvc.perform(get(API_URI + "/" + filedId))
				.andExpect(status().is2xxSuccessful()).andReturn();

		assertEquals(MediaType.IMAGE_GIF_VALUE, res.getResponse().getContentType());
		assertEquals(randomContent, new String(res.getResponse().getContentAsByteArray()));
	}

	// In progress: making more tests
}

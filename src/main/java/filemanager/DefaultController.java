package filemanager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class DefaultController {

	@Autowired
	private FileObjectService fileObjectService;

	@GetMapping("/")
	public String redirect() {
		return "redirect:/files";
	}

	@GetMapping("/files")
	public String files(Model model) {
		model.addAttribute("files", this.fileObjectService.findAll());
		return "files";
	}

	@PostMapping("/files")
	public String save(@RequestParam("file") MultipartFile file) throws IOException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
		String postDate = simpleDateFormat.format(new Date());
		
		FileObject fo = new FileObject();
		fo.setDate(postDate);
		fo.setName(file.getOriginalFilename());
		fo.setContentType(file.getContentType());
		fo.setContentLength(file.getSize());
		fo.setContent(file.getBytes());

		fileObjectService.save(fo);

		return "redirect:/files";
	}

	@GetMapping(value = "/files/{id}", produces = {
			MediaType.IMAGE_GIF_VALUE,
			MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE
	})
	public @ResponseBody byte[] viewFile(@PathVariable Long id) {
		FileObject fo = fileObjectService.findById(id);
		if (fo == null) throw new ResponseStatusException(
			HttpStatus.NOT_FOUND,
			String.format("Image with id %d not found", id));

		return fo.getContent();
	}

	@DeleteMapping("/files/{id}")
	public String deleteFile(@PathVariable Long id) {
		fileObjectService.deleteById(id);
		return "redirect:/files";
	}
}

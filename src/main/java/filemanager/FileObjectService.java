package filemanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FileObjectService {

	@Autowired
	private FileObjectRepository fileObjectRepo;

	@Cacheable("files")
	public FileObject findById(Long id) {
		FileObject fo = fileObjectRepo.findById(id).orElse(null);
		return fo;
	}

	//@Cacheable("allfiles")
	public List<FileObject> findAll() {
		return fileObjectRepo.findAll();
	}

	public void save(FileObject fo) {
		fileObjectRepo.save(fo);
	}

	public void deleteById(Long id) {
		fileObjectRepo.deleteById(id);
	}
	
}

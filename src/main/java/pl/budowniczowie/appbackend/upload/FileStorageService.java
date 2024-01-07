package pl.budowniczowie.appbackend.upload;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    Path root = null;
    public void save(MultipartFile file);
    public void setRoot(Path root);
    public Resource load(String filename);
    public void deleteAll();
    public Stream<Path> loadAll();
}

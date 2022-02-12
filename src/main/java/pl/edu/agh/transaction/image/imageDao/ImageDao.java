package pl.edu.agh.transaction.image.imageDao;

import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.transaction.image.imageModels.Image;

import java.io.IOException;
import java.util.List;

public interface ImageDao {
    void insert(String imageType, MultipartFile file) throws IOException;
    void update(String imageType, String imageName);
    void remove(String imageName);
    Image getImage(String imageName) throws IOException;
    List<String> getImageNames();
    boolean isImageNameTaken(String imageName);
}

package pl.edu.agh.transaction.image;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path="api/v1")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<String> addImage(@RequestParam("imageType") String imageType, @RequestParam("image") MultipartFile image) {
        return imageService.addImage(imageType, image);
    }

    @GetMapping(value = "image/{title}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable String title) {
        return imageService.getImage(title);
    }

    @GetMapping(value = "image_titles")
    public ResponseEntity<List<String>> getImagesTitle() {
        return imageService.getImagesTitle();
    }
}

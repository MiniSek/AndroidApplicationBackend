package pl.edu.agh.transaction.image;

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

    @PostMapping(value = "image")
    public ResponseEntity<String> addImage(@RequestParam("imageType") String imageType, @RequestParam("image") MultipartFile image) {
        return imageService.addImage(imageType, image);
    }

    @PostMapping(value = "image_type")
    public ResponseEntity<String> changeImageType(@RequestParam("imageType") String imageType, @RequestParam("image") String image) {
        return imageService.changeImageType(imageType, image);
    }

    @PostMapping(value = "image_remove")
    public ResponseEntity<String> removeImage(@RequestParam("image") String image) {
        return imageService.removeImage(image);
    }

    @GetMapping(value = "image/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable String name) {
        return imageService.getImage(name);
    }

    @GetMapping(value = "image_names")
    public ResponseEntity<List<String>> getImageNames() {
        return imageService.getImageNames();
    }
}

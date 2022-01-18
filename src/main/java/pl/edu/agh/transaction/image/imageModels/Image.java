package pl.edu.agh.transaction.image.imageModels;

import org.springframework.core.io.ByteArrayResource;
import pl.edu.agh.transaction.image.imageModels.imageType.ImageType;

public class Image {
    private final String imageName;
    private final long imageSize;
    private final String contentType;
    private final ImageType imageType;
    private final ByteArrayResource content;

    public Image(String imageName, long imageSize, String contentType, ImageType imageType, ByteArrayResource content) {
        this.imageName = imageName;
        this.imageSize = imageSize;
        this.contentType = contentType;
        this.imageType = imageType;
        this.content = content;
    }

    public String getImageName() {
        return imageName;
    }

    public long getImageSize() {
        return imageSize;
    }

    public String getContentType() {
        return contentType;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public ByteArrayResource getContent() {
        return content;
    }
}

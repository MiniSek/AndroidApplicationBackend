package pl.edu.agh.transaction.image.imageModels.imageType;

public enum ImageType {
    PREMIUM_TYPE("premium_type"),
    NON_PREMIUM_TYPE("non_premium_type");

    private final String imageType;

    ImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }
}

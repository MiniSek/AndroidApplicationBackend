package pl.edu.agh.transaction.Utils.Model.clientRole;

import com.google.common.collect.Sets;
import pl.edu.agh.transaction.Utils.Model.imageType.ImageType;

import java.util.Set;

import static pl.edu.agh.transaction.Utils.Model.imageType.ImageType.*;

public enum ClientRole {
    NON_PREMIUM(Sets.newHashSet(NON_PREMIUM_TYPE)),
    PREMIUM(Sets.newHashSet(NON_PREMIUM_TYPE, PREMIUM_TYPE));

    private final Set<ImageType> imageTypes;

    ClientRole(Set<ImageType> imageTypes) {
        this.imageTypes = imageTypes;
    }

    public Set<ImageType> getImageTypes() {
        return imageTypes;
    }
}

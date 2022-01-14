package pl.edu.agh.transaction.DataAccessLayer.imageDao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.poi.util.IOUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.transaction.Utils.ServerException.IllegalDatabaseState;
import pl.edu.agh.transaction.Utils.ServerException.ObjectNotFoundException;
import pl.edu.agh.transaction.Utils.Model.Image;
import pl.edu.agh.transaction.Utils.Model.imageType.ImageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageDaoMongoDB implements ImageDao {
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    @Autowired
    public ImageDaoMongoDB(GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }


    @Override
    public void insert(String imageType, MultipartFile file) throws IOException {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("imageName", file.getOriginalFilename());
        dbObject.put("contentType", file.getContentType());
        dbObject.put("size", file.getSize());
        dbObject.put("imageType", imageType);

        gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), dbObject);
    }

    @Override
    public Image getImage(String imageName) throws IllegalDatabaseState, ObjectNotFoundException, IOException {
        int numberOfImagesWithImageName = 0;
        for(String name : getImageNames())
            if(name.equals(imageName))
                numberOfImagesWithImageName++;

        if(numberOfImagesWithImageName > 1)
            throw new IllegalDatabaseState(
                    String.format("There is %d images with the same name %s", numberOfImagesWithImageName, imageName));
        else if(numberOfImagesWithImageName == 0)
            throw new ObjectNotFoundException(String.format("There is no such image with name %s", imageName));

        Query query = new Query();
        query.addCriteria(Criteria.where("filename").is(imageName));
        GridFSFile image = gridFsTemplate.findOne(query);
        if(image == null)
            throw new ObjectNotFoundException(String.format("There is no such image with name %s", imageName));

        Document metadata = image.getMetadata();
        if(metadata == null)
            throw new ObjectNotFoundException("Metadata was not loaded properly");
        return new Image((String)metadata.get("imageName"), (Long)metadata.get("size"),
                (String)metadata.get("contentType"), ImageType.valueOf((String)metadata.get("imageType")),
                new ByteArrayResource(IOUtils.toByteArray(gridFsOperations.getResource(image).getInputStream())));
    }

    @Override
    public List<String> getImageNames() {
        List<String> imageNames = new ArrayList<>();
        gridFsTemplate.find(new Query()).forEach(image -> imageNames.add(image.getFilename()));
        return imageNames;
    }

    @Override
    public boolean isImageNameTaken(String imageName) {
        return getImageNames().contains(imageName);
    }
}

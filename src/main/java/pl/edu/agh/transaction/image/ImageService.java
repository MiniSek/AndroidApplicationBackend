package pl.edu.agh.transaction.image;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.client.clientModels.roles.ClientRole;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.image.imageType.ImageType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ImageService {
    private final ClientDaoServiceLayer clientDaoServiceLayer;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    @Autowired
    public ImageService(ClientDaoServiceLayer clientDaoServiceLayer, GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {
        this.clientDaoServiceLayer = clientDaoServiceLayer;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    public ResponseEntity<String> addImage(String imageType, MultipartFile file) {
        if(gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("filename").is(file.getOriginalFilename()))) != null)
            return new ResponseEntity<>("There is already file with such name in database", HttpStatus.BAD_REQUEST);

        DBObject dbObject = new BasicDBObject();
        dbObject.put("fileName", file.getOriginalFilename());
        dbObject.put("contentType", file.getContentType());
        dbObject.put("size", file.getSize());
        dbObject.put("imageType", imageType);
        try {
            gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), dbObject);
            return new ResponseEntity<>("File saved successfully in database", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server internal error - during saving file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<String>> getImagesTitle() {
        List<String> imagesTitle = new ArrayList<>();
        gridFsTemplate.find(new Query()).forEach(image -> imagesTitle.add(image.getFilename()));

        return new ResponseEntity<>(imagesTitle, HttpStatus.OK);
    }

    public ResponseEntity<Resource> getImage(String title) {
        String email = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Client client;
        try {
            client = clientDaoServiceLayer.getClientByEmail(email);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Set<GrantedAuthority> clientRoles = client.getRoles();
        Query query = new Query();
        query.addCriteria(Criteria.where("filename").is(title));
        GridFSFindIterable images = gridFsTemplate.find(query);

        AtomicInteger imageWithTitleNumber = new AtomicInteger();
        images.forEach(image -> imageWithTitleNumber.getAndIncrement());

        if(imageWithTitleNumber.intValue() == 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else if(imageWithTitleNumber.intValue() > 1)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        GridFSFile image = images.first();
        for(GrantedAuthority role : clientRoles) {
            if(ClientRole.valueOf(role.getAuthority()).getImageTypes().contains(ImageType.valueOf((String)image.getMetadata().get("imageType")))) {
                try {
                    ByteArrayResource inputStream = new ByteArrayResource(IOUtils.toByteArray(gridFsOperations.getResource(image).getInputStream()));
                    return ResponseEntity.status(HttpStatus.OK).body(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
}

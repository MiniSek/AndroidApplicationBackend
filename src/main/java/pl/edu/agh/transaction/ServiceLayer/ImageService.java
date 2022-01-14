package pl.edu.agh.transaction.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.Utils.Model.clientRole.ClientRole;
import pl.edu.agh.transaction.Utils.ServerException.IllegalDatabaseState;
import pl.edu.agh.transaction.Utils.ServerException.ObjectNotFoundException;
import pl.edu.agh.transaction.DataAccessLayer.imageDao.ImageDao;
import pl.edu.agh.transaction.Utils.Model.Image;

import java.io.*;
import java.util.List;

@Service
public class ImageService {
    private final ClientDaoServiceLayer clientDaoServiceLayer;
    private final ImageDao imageDao;

    @Autowired
    public ImageService(ClientDaoServiceLayer clientDaoServiceLayer, ImageDao imageDao) {
        this.clientDaoServiceLayer = clientDaoServiceLayer;
        this.imageDao = imageDao;
    }

    public ResponseEntity<String> addImage(String imageType, MultipartFile file) {
        if(imageDao.isImageNameTaken(file.getOriginalFilename()))
            return new ResponseEntity<>("Image name taken", HttpStatus.BAD_REQUEST);

        try {
            imageDao.insert(imageType, file);
            return new ResponseEntity<>("Image saved successfully in database", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server internal error - during saving image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<String>> getImageNames() {
        return new ResponseEntity<>(imageDao.getImageNames(), HttpStatus.OK);
    }

    public ResponseEntity<Resource> getImage(String imageName) {
        String email = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Client client;
        try {
            client = clientDaoServiceLayer.getClientByEmail(email);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Image image;
        try {
            image = imageDao.getImage(imageName);
        } catch(IOException | IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        for(GrantedAuthority role : client.getRoles()) {
            if(ClientRole.valueOf(role.getAuthority()).getImageTypes().contains(image.getImageType()))
                return new ResponseEntity<>(image.getContent(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
}
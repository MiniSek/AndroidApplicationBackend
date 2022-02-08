package pl.edu.agh.transaction.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientModels.roles.ClientRole;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.image.imageDao.ImageDao;
import pl.edu.agh.transaction.image.imageModels.Image;

import java.io.*;
import java.util.List;

@Service
public class ImageService {
    private final ClientDao clientDao;
    private final ImageDao imageDao;

    @Autowired
    public ImageService(ClientDao clientDao, ImageDao imageDao) {
        this.clientDao = clientDao;
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
            client = clientDao.getClientByEmail(email);
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

package alessandro.backale.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, Long userId) throws IOException {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "users/" + userId + "/profile_pictures",
                    "overwrite", true,
                    "resource_type", "auto"
            );
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return uploadResult.get("url").toString(); // Restituisce l'URL dell'immagine
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento dell'immagine", e);
        }
    }
}

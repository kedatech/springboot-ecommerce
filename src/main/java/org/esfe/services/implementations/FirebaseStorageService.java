package org.esfe.services.implementations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final String bucketName = System.getenv("FIREBASE_STORAGE_BUCKET");

    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();
            return uploadFile(multipartFile, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    private String uploadFile(MultipartFile file, String fileName) throws IOException {
        String privateKey = System.getenv("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");

        // Crear un mapa con los datos de las variables de entorno
        Map<String, Object> credentialsMap = new HashMap<>();
        credentialsMap.put("type", "service_account");
        credentialsMap.put("project_id", System.getenv("FIREBASE_PROJECT_ID"));
        credentialsMap.put("private_key_id", System.getenv("FIREBASE_PRIVATE_KEY_ID"));
        credentialsMap.put("private_key", privateKey);
        credentialsMap.put("client_email", System.getenv("FIREBASE_CLIENT_EMAIL"));
        credentialsMap.put("client_id", System.getenv("FIREBASE_CLIENT_ID"));
        credentialsMap.put("auth_uri", System.getenv("FIREBASE_AUTH_URI"));
        credentialsMap.put("token_uri", System.getenv("FIREBASE_TOKEN_URI"));
        credentialsMap.put("auth_provider_x509_cert_url", System.getenv("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
        credentialsMap.put("client_x509_cert_url", System.getenv("FIREBASE_CLIENT_X509_CERT_URL"));

        // Convertir el mapa en una cadena JSON
        String jsonCredentials = new com.google.gson.Gson().toJson(credentialsMap);

        // Convertir la cadena JSON en un flujo de entrada
        ByteArrayInputStream credentialsStream = new ByteArrayInputStream(jsonCredentials.getBytes(StandardCharsets.UTF_8));

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        storage.create(blobInfo, file.getBytes());

        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, fileName);
    }
}

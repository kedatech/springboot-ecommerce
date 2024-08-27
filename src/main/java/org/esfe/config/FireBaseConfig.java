package org.esfe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FireBaseConfig {


    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("{\n" +
                        "  \"type\": \"service_account\",\n" +
                        "  \"project_id\": \"ecommerce-b9bd5\",\n" +
                        "  \"private_key_id\": \"6fe25bd44fbf065437ce85035e9a343f36ad17e3\",\n" +
                        "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC9wAgPVUNFwOPW\\nMDOPWPnkBkAi/ziLdM9yfHmr87KceulctR/3n2Tt6//EWge66t0tDoYdMRvhQaGc\\nM+zpV1sN6jLr1kP35NoKfH5IRb436DbdxYZAXo2BHM5pxJv9mwcPlo9Ix06VkLT6\\nCPPd2LYgnsSwheP8xPfDf9mCmnHWUg089DkQs8yL05ZdKSt4qTQqvYlTRFeZwPkb\\nq/MgXnTWxW2kUBdfho4KM/gwqw7GoJQUZOUkSRDaPlF86BdC7xyS1MBO3pumdi5J\\nzSyovSIwZjbSur0ygr/Shp8FpxwJUmbvXKum0CNY6qHVJsmXWcWYh3QpGXXAWVjB\\nuCYPTB1rAgMBAAECggEASLhaPmoaVJbGmpUxhWv/pRyiIud2J7gWMtWymaxVVPQk\\nEFQYEsIDH8204W7xjHirWDO7QV/LRApvodoinxDMBCqv5kNhHBWYXLF2gkM8UdfM\\ngzTKrUuvZn6AOvfPIqPx0k8MzcMYgmiJe1PIBksiDaFbVx2RERP5snKCjTOckhqK\\nDwMRKfAf0jmyEumLW5o4cpITDF4JRMLyQMcSHaXL9WeKjSWd22qVLKgoTNiVSiYU\\nsLdsx/bg2g9JQNMUIHk9wXjcyA7d0zdIaUrHE499xJrMSqQp0KTP8SewiGuNFOCv\\nzco1bbjDRpwH5MdgDT9OtajlPuXTcj+G/vcC5wBk9QKBgQDkTXq1XijcHJC72Cl7\\n6yUJ1fY7olwCAAVE9lwxKsoM5zMPW9Z3vS+9i1Gl/6laCR1NmLSQWGkKOwQEdTt9\\nj5+t7/7aJb/ybqBYWnszue5riCIyuUoEdxxhlSz9VXTBxy/LhR6ojRMW+zPoguUZ\\nuGHKcJ0APSidp8AnvF0T0p17pwKBgQDUxTR/zHrOklFuUJfCnxj4ZScORM9jB/0J\\nmO/l869O2ulbThL8KpvAIQ8Gm+BRr+rVMg/kHUK3dmkTgejWrTI1Os9Fd2gpC+op\\n0DNYYyTeDOKp0zL7IMIHRw1S0UvFzNeb2oCY9izEsuOu2behmvXsMZPG2Eiowue7\\ngF52K1d4nQKBgF476Nlx6zWniMowWDcxARHFQiStSIL+1S347mbQ/aNDzxyY0LaN\\n3WXEWshE9fcW6gR8cpNeGsVBRNA+P3b9UHCwLiaHDGqZ5Q9zEYVxSosSHuOG/ZlI\\nPOu+clqC6nWNrRm5ccUqreV/fJaNaNkr9RllUDioBdrFQhhktJqZBa3jAoGAASY3\\njGEsYKVFr1TcHP8DAYR6W4dfn2dRpJWg5hsU3VYtYILhc18/jCAhlqWM5qQOnqww\\njcjirV8EXkibW12AIe15ZSSp+V7JGwS24klWz6Q+LTCo29ZndeaaWdIrmJvspjjd\\n7PWXdFCLDky6wzK1FVuHMtHEQrUaz1Z4LCc7x7kCgYBJxSPEmPoVPNvpOi0snq1e\\nUqH05ugmzQw8RzTOj/q/CKq0PXIeLSguVPtYkQyVy95IHQDp0NAadyjXgeCAOf/M\\nCOcTewnj4cZYBJqu6hAfGuMLBnOnvZGJjCGgNGngDJ0LaEVy7pUkSzxDEhbel/c5\\n8N12xRRh9M6XzVJSw/RlRQ==\\n-----END PRIVATE KEY-----\\n\",\n" +
                        "  \"client_email\": \"firebase-adminsdk-ee8u0@ecommerce-b9bd5.iam.gserviceaccount.com\",\n" +
                        "  \"client_id\": \"100293050946872887812\",\n" +
                        "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                        "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                        "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                        "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ee8u0%40ecommerce-b9bd5.iam.gserviceaccount.com\",\n" +
                        "  \"universe_domain\": \"googleapis.com\"\n" +
                        "}\n");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("gs://ecommerce-b9bd5.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}

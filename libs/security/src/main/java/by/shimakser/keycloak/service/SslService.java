package by.shimakser.keycloak.service;

import by.shimakser.dto.SslRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class SslService {

    public void getCsrByRequest(SslRequest sslRequest) {
        String subject = String.format("/CN=%s/", sslRequest.getCommon());

        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "init-client-ssl.sh");
        Map<String, String> env = pb.environment();
        env.put("SUBJECT", subject);
        pb.directory(new File("scripts/init-ssl/"));
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package by.shimakser.security.service;

import by.shimakser.dto.SslRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
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
            log.info("Generated CSR by request");
        } catch (IOException e) {
            log.error("IOException from generation CSR", e);
            e.printStackTrace();
        }
    }
}

package by.shimakser.ssl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ssl")
public class SslController {

    @Autowired
    private SslService sslService;

    @PostMapping
    public SslRequest getCsr(@RequestBody SslRequest sslRequest) {
        sslService.getCsrByRequest(sslRequest);
        return sslRequest;
    }

    @GetMapping
    public String sslTest() {
        return "Hello, world!";
    }
}

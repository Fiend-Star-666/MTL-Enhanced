package com.astoria.mtldataconvert;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MtlDataConvertApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtlDataConvertApplication.class, args);
    }

}

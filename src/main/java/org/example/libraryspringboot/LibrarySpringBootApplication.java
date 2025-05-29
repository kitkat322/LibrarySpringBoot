package org.example.libraryspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class LibrarySpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibrarySpringBootApplication.class, args);


//        System.out.println("JVM TimeZone: " + ZoneId.systemDefault());
//
//        setDefaultTimeZone();
    }

//    public static void setDefaultTimeZone() {
//        System.out.println("void setDefaultTimeZone works");
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//    }
}

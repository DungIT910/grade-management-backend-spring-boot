package com.boolfly.GradeManagementRestful;

import io.hypersistence.tsid.TSID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GradeManagementRestfulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradeManagementRestfulApplication.class, args);
//        for (int i = 0; i < 3; i++) {
//            System.out.println(TSID.fast().toLong());
//        }
//        System.out.println(TSID.from(598065895196735099L).toString());
//        System.out.println(TSID.from(598016180592006050L).toString());
//        System.out.println(TSID.from(598016180617171875L).toString());
        System.out.println(TSID.from(598016180617171876L).toString());
    }
}

/*0GK61RD9QMDKV
0GK4MHGZ1YMX2
0GK4MHGZSYMX3*/


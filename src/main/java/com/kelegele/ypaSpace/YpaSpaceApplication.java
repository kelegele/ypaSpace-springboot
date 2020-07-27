package com.kelegele.ypaSpace;

import org.apache.log4j.BasicConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kelegele.ypaSpace.mapper")
public class YpaSpaceApplication {

	public static void main(String[] args) {
        BasicConfigurator.configure(); //自动快速地使用缺省Log4j环境
		SpringApplication.run(YpaSpaceApplication.class, args);
	}

}

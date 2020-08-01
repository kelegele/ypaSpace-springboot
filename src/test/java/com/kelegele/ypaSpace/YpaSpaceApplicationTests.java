package com.kelegele.ypaSpace;

import com.kelegele.ypaSpace.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YpaSpaceApplicationTests {

    @Autowired
    private FileService fileService;

	@Test
	void contextLoads() {

        try {
//            fileService.getFile("/test/DSTTest.txt","token");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

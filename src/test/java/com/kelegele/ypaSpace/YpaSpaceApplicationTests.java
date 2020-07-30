package com.kelegele.ypaSpace;

import com.kelegele.ypaSpace.service.FileService;
import com.kelegele.ypaSpace.service.HdfsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YpaSpaceApplicationTests {

    @Autowired
    private FileService fileService;

	@Test
	void contextLoads() {

        byte [] bytes = null;
        try {
            bytes = HdfsService.openFileToBytes("/test/DSTTest.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

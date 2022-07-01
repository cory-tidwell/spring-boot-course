package com.coffeecat.springbootcourse;

import com.coffeecat.springbootcourse.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.File;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class FileServiceTest {

    @Autowired
    FileService fileService;

    //File-Upload-Directory:
    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @Test
    public void testGetExtension() throws Exception {
        //.getDeclaredMethod(MethodName, TypeOfMethodParamters)
        Method method = FileService.class.getDeclaredMethod("getFileExtension", String.class);
        method.setAccessible(true);

        //.invoke(FileServiceObject, Argument)
        //return value = return value of getFileExtension cast to Object: cast back to String!
        String testResult = (String)method.invoke(fileService, "test.png");

        //assert(expected-Value, testResult, "Message")
        assertEquals("png", testResult, "Should be png");
        assertEquals("doc", (String)method.invoke(fileService, "s.doc"),"Should be doc");
        assertEquals("jpeg", (String)method.invoke(fileService, "file.jpeg"),"Should be jpeg");
        assertNull((String)method.invoke(fileService, "xyz"),"Should be null");
    }

    @Test
    public void testIsExtensionValid() throws Exception {
        Method method = FileService.class.getDeclaredMethod("isExtensionValid",String.class);
        method.setAccessible(true);

        Boolean testResult = (Boolean)method.invoke(fileService, "png");

        assertTrue(testResult, "png should be a valid.");
        assertTrue((Boolean)method.invoke(fileService, "jpg"), "jpg should be valid.");
        assertTrue((Boolean)method.invoke(fileService, "jpeg"), "jpeg should be valid.");
        assertTrue((Boolean)method.invoke(fileService, "gif"), "gif should be valid.");
        assertTrue((Boolean)method.invoke(fileService, "GIF"), "GIF should be valid.");
        assertFalse((Boolean)method.invoke(fileService, "doc"), "doc should be invalid.");
        assertFalse((Boolean)method.invoke(fileService, "gi"), "gi should be invalid.");
        assertFalse((Boolean)method.invoke(fileService, "jpg2"), "jpg2 should be invalid.");
        assertFalse((Boolean)method.invoke(fileService, ""), "Null should be invalid.");
    }

    @Test
    public void testCreateSubdirectory() throws Exception {
        Method method = FileService.class.getDeclaredMethod("createSubdirectory", String.class, String.class);
        method.setAccessible(true);

        //create amount of Directories & check if exist:
        for(int i = 0; i < 100; i++) {
            File createdDirectory = (File)method.invoke(fileService, fileUploadDirectory, "photo");

            //check if Directory exists:
            assertTrue(createdDirectory.exists(), "Directory should exist" + createdDirectory.getAbsolutePath());
        }
    }
}

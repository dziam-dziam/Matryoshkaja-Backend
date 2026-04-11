package com.matryoshkaja.demo.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    private StorageService storageService;

    @BeforeEach()
    void setUp(){
        storageService = new StorageService();
    }

    @Test
    void shouldGenerateKey(){
        //given
        String originalFileName ="my file.png";

        //when
        String key = storageService.generateKey(originalFileName);

        //then
        assertNotNull(key);
        assertTrue(key.contains("my_file.png"));
        assertTrue(key.matches("^[a-f0-9\\-]+_my_file\\.png$"));
    }

    @Test
    void shouldUseDefaultFileNameWhenOriginalFileNameIsNull(){
        //given
        String originalFileName = null;

        //when
        String key = storageService.generateKey(originalFileName);

        //then
        assertNotNull(key);
        assertTrue(key.contains("fileName.png"));
        assertTrue(key.matches("^[a-f0-9\\-]+_fileName\\.png$"));
    }

    @Test
    void shouldGenerateUniqueKeys(){
        //given
        String originalFileName ="my file.png";

        //when
        String keyOne = storageService.generateKey(originalFileName);
        String keyTwo = storageService.generateKey(originalFileName);

        //then
        assertNotEquals(keyOne,keyTwo);
    }
    @Test
    void shouldReturnUrlAfterUpload() {
        //given
        String key = "test.png";

        //when
        String url = storageService.upload(null, key);

        //then
        assertEquals("https://fake-storage.com/" + key, url);
    }

}
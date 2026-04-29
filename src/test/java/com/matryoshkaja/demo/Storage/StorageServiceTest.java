package com.matryoshkaja.demo.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private StorageService storageService;

    @BeforeEach()
    void setUp(){
        storageService = new StorageService(s3Client);
        ReflectionTestUtils.setField(storageService,"bucket","test-bucket");
        ReflectionTestUtils.setField(storageService,"baseUrl", "https://test-url.com");
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
    void shouldReturnUrlAfterUpload() throws Exception {
        // given
        String key = "test.png";

        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        // when
        String url = storageService.upload(file, key);

        // then
        assertEquals("https://test-url.com/" + key, url);
        verify(s3Client, times(1)).putObject((PutObjectRequest) any(), (RequestBody) any());
    }

    @Test
    void shouldDeleteObject() {
        // given
        String key = "test.png";

        // when
        storageService.delete(key);

        // then
        verify(s3Client, times(1)).deleteObject((DeleteObjectRequest) any());
    }

}
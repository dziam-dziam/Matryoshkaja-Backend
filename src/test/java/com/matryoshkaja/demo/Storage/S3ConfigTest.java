package com.matryoshkaja.demo.Storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.*;

class S3ConfigTest {

    private S3Config s3Config;

    @BeforeEach
    void setUp() {
        s3Config = new S3Config();

        ReflectionTestUtils.setField(s3Config, "accessKey", "test-access");
        ReflectionTestUtils.setField(s3Config, "secretKey", "test-secret");
        ReflectionTestUtils.setField(s3Config, "endpoint", "https://test-endpoint.com");
    }

    @Test
    void shouldCreateS3Client() {
        // when
        S3Client client = s3Config.s3Client();

        // then
        assertNotNull(client);
    }

    @Test
    void shouldUseCorrectEndpoint() {
        // when
        S3Client client = s3Config.s3Client();

        // then
        String endpoint = client.serviceClientConfiguration().endpointOverride().toString();

        assertTrue(endpoint.contains("https://test-endpoint.com"));
    }

    @Test
    void shouldBuildClientWithCredentials() {
        // when
        S3Client client = s3Config.s3Client();

        // then
        assertNotNull(client.serviceClientConfiguration().credentialsProvider());
    }
}
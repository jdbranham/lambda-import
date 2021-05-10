package net.savantly.lambda.dbf.domain;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

abstract public class CommonResource {
    private final static String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @ConfigProperty(name = "destination.bucket")
    String bucketDestinationName;

    protected PutObjectRequest buildPutRequest(String key) {
        return PutObjectRequest.builder()
                .bucket(bucketDestinationName)
                .key(key)
                .contentType("text/csv")
                .build();
    }

    protected GetObjectRequest buildGetRequest(String bucketSourceName, String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketSourceName)
                .key(objectKey)
                .build();
    }

    protected File tempFilePath() {
        return new File(TEMP_DIR, new StringBuilder().append("s3AsyncDownloadedTemp")
                .append((new Date()).getTime()).append(UUID.randomUUID())
                .append(".").append(".tmp").toString());
    }

    protected File uploadToTemp(InputStream data) {
        File tempPath;
        try {
            tempPath = File.createTempFile("uploadS3Tmp", ".tmp");
            Files.copy(data, tempPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return tempPath;
    }
}
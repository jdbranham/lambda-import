package net.savantly.lambda.dbf.domain;

import java.io.File;
import java.lang.invoke.LambdaConversionException;
import java.nio.file.Files;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.savantly.lambda.dbf.domain.aws.S3EventNotification.S3Entity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class DbfResource extends CommonResource {

	private static Logger log = LoggerFactory.getLogger(DbfResource.class);
	@Inject
	S3Client s3;
	
    @ConfigProperty(name = "destination.keyname.find")
    String keynameFind;
    
    @ConfigProperty(name = "destination.keyname.replace")
    String keynameReplace;

	public void saveAsCsv(S3Entity s3Entity) throws Exception {
		
		String key = s3Entity.getObject().getKey();
		if (!isDbfFile(key)) {
			log.debug("not converting file without dbf extension: {}", key);
			return;
		}

		log.info("fetching from s3: {}", key);
		ResponseInputStream<GetObjectResponse> getObjectResponse = s3.getObject(buildGetRequest(s3Entity.getBucket().getName(), key));
		
		File tempDbfFile = this.uploadToTemp(getObjectResponse);
		File tempCsvFile = DbfConverter.convertFromDbfToCsv(Files.newInputStream(tempDbfFile.toPath()));

		// uploadToTemp(getObjectResponse);

		String destinationKey = changeToCsvKey(key, keynameFind, keynameReplace);

		PutObjectResponse putResponse = s3.putObject(buildPutRequest(destinationKey),
				RequestBody.fromFile(tempCsvFile));

		if (Objects.isNull(putResponse)) {
			throw new LambdaConversionException("failed to upload csv file: " + destinationKey);
		}
		return;
	}

	private static boolean isDbfFile(String dbfS3Key) {
		return Objects.nonNull(dbfS3Key) && dbfS3Key.toLowerCase().endsWith(".dbf");
	}

	protected static String changeToCsvKey(String dbfS3Key, String keynameFind, String keynameReplace) {
		return dbfS3Key.toLowerCase().replaceFirst(keynameFind, keynameReplace);
	}

}

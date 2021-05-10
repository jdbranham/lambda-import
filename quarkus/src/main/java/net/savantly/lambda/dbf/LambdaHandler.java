package net.savantly.lambda.dbf;

import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;

import net.savantly.lambda.dbf.domain.DbfResource;

public class LambdaHandler implements RequestHandler<S3Event, String> {

	private static Logger log = LoggerFactory.getLogger(LambdaHandler.class);
	@Inject
	DbfResource dbfResource;

	public String handleRequest(S3Event input, Context context) {
		input.getRecords().forEach(eventRecord -> {
			try {
				processRecord(eventRecord);
			} catch (Exception e) {
				log.error("failed to process event: {}", eventRecord.getEventName());
			}
		});
		return "success";
	}

	private void processRecord(S3EventNotificationRecord eventRecord) throws Exception {
		if (isOkToProcess(eventRecord)) {
			log.info("handling s3 object created: {}", eventRecord.getS3().getObject().getKey());
			dbfResource.saveAsCsv(eventRecord.getS3());
		} else {
			log.debug("discarding s3 event: {}, {}", eventRecord.getEventName(),
					eventRecord.getS3().getObject().getKey());
		}
	}

	private boolean isOkToProcess(S3EventNotificationRecord eventRecord) {
		return Objects.nonNull(eventRecord) && Objects.nonNull(eventRecord.getEventName())
				&& eventRecord.getEventName().startsWith("ObjectCreated");
	}

}

package net.savantly.lambda.dbf;

import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import net.savantly.lambda.dbf.domain.DbfResource;
import net.savantly.lambda.dbf.domain.S3EventNotificationExtractor;
import net.savantly.lambda.dbf.domain.aws.S3EventNotification;
import net.savantly.lambda.dbf.domain.aws.S3EventNotification.S3EventNotificationRecord;
import net.savantly.lambda.dbf.domain.aws.SnsEvent;

public class LambdaHandler implements RequestHandler<SnsEvent, String> {

	private static Logger log = LoggerFactory.getLogger(LambdaHandler.class);
	@Inject
	DbfResource dbfResource;
	@Inject
	S3EventNotificationExtractor s3Extractor;


    @Override
	public String handleRequest(SnsEvent input, Context context) {
		try {
			
			input.getRecords().forEach(r -> {
				String msg = r.getSns().getMessage();
				try {
					S3EventNotification s3Event = s3Extractor.extractNotification(msg);
					processRecord(s3Event.getRecords().stream().findFirst().orElseThrow(() -> new RuntimeException("no s3 event record found")));
				} catch (Exception e) {
					log.error("failed to process S3Event", e);
				}
			});
		} catch (Exception e) {
			log.error(String.format("failed to process event: {}"), e);
		}
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

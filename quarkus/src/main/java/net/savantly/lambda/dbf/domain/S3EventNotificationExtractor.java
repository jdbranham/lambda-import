package net.savantly.lambda.dbf.domain;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import net.savantly.lambda.dbf.domain.aws.S3EventNotification;

@ApplicationScoped
public class S3EventNotificationExtractor {

	final ObjectMapper mapper;
	
	public S3EventNotificationExtractor(ObjectMapper mapper ) {
    	mapper.registerModule(new JodaModule());
		this.mapper = mapper;
	}
	
	public S3EventNotification extractNotification(String msg) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(msg, S3EventNotification.class);
	}
	
}

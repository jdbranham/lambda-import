package net.savantly.lambda.dbf.domain.aws;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* A helper class that represents a strongly typed S3 EventNotification item sent
* to SQS, SNS, or Lambda.
*/
public class S3EventNotification {

	@JsonProperty("Records")
    private List<S3EventNotificationRecord> records;
	
	public S3EventNotification() {}

    public S3EventNotification(List<S3EventNotificationRecord> records) {
        this.records = records;
    }

    /**
     * @return the records in this notification
     */
    public List<S3EventNotificationRecord> getRecords() {
        return records;
    }


    public static class UserIdentityEntity {

        private String principalId;
        
        public UserIdentityEntity() {}

        public UserIdentityEntity(String principalId) {
            this.principalId = principalId;
        }

        public String getPrincipalId() {
            return principalId;
        }
    }

    public static class S3BucketEntity {

        private String name;
        private UserIdentityEntity ownerIdentity;
        private String arn;
        
        public S3BucketEntity() {}

        public S3BucketEntity(String name, UserIdentityEntity ownerIdentity, String arn) {
            this.name = name;
            this.ownerIdentity = ownerIdentity;
            this.arn = arn;
        }

        public String getName() {
            return name;
        }

        public UserIdentityEntity getOwnerIdentity() {
            return ownerIdentity;
        }

        public String getArn() {
            return arn;
        }
    }

    public static class S3ObjectEntity {

        private String key;
        private Long size;
        private String eTag;
        private String versionId;
        private String sequencer;

        public S3ObjectEntity() {}

        public S3ObjectEntity(String key, Long size, String eTag, String versionId, String sequencer) {
            this.key = key;
            this.size = size;
            this.eTag = eTag;
            this.versionId = versionId;
            this.sequencer = sequencer;
        }

        public String getKey() {
            return key;
        }

        /**
         * S3 URL encodes the key of the object involved in the event. This is
         * a convenience method to automatically URL decode the key.
         * @return The URL decoded object key.
         */
        public String getUrlDecodedKey() {
            return urlDecode(getKey());
        }

        private static final String DEFAULT_ENCODING = "UTF-8";

        /**
         * Decode a string for use in the path of a URL; uses URLDecoder.decode,
         * which decodes a string for use in the query portion of a URL.
         *
         * @param value The value to decode
         * @return The decoded value if parameter is not null, otherwise, null is returned.
         */
        private static String urlDecode(final String value) {
            if (value == null) {
                return null;
            }

            try {
                return URLDecoder.decode(value, DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }

        /**
         * @deprecated use {@link #getSizeAsLong()} instead.
         */
        @Deprecated
        public Integer getSize() {
            return size == null ? null : size.intValue();
        }

        public Long getSizeAsLong() {
            return size;
        }

        public String geteTag() {
            return eTag;
        }

        public String getVersionId() {
            return versionId;
        }

        public String getSequencer() {
            return sequencer;
        }
    }

    public static class S3Entity {

        private String configurationId;
        private S3BucketEntity bucket;
        private S3ObjectEntity object;
        private String s3SchemaVersion;
        
        public S3Entity() {}

        public S3Entity(String configurationId, S3BucketEntity bucket, S3ObjectEntity object, String s3SchemaVersion) {
            this.configurationId = configurationId;
            this.bucket = bucket;
            this.object = object;
            this.s3SchemaVersion = s3SchemaVersion;
        }

        public String getConfigurationId() {
            return configurationId;
        }

        public S3BucketEntity getBucket() {
            return bucket;
        }

        public S3ObjectEntity getObject() {
            return object;
        }

        public String getS3SchemaVersion() {
            return s3SchemaVersion;
        }
    }

    public static class RequestParametersEntity {

        private String sourceIPAddress;
        
        public RequestParametersEntity() {}

        public RequestParametersEntity(String sourceIPAddress) {
            this.sourceIPAddress = sourceIPAddress;
        }

        public String getSourceIPAddress() {
            return sourceIPAddress;
        }
    }

    public static class ResponseElementsEntity {

        private String xAmzId2;
        private String xAmzRequestId;
        
        public ResponseElementsEntity() {}

        public ResponseElementsEntity(String xAmzId2, String xAmzRequestId)
        {
            this.xAmzId2 = xAmzId2;
            this.xAmzRequestId = xAmzRequestId;
        }

        public String getxAmzId2() {
            return xAmzId2;
        }

        public String getxAmzRequestId() {
            return xAmzRequestId;
        }
    }

    public static class S3EventNotificationRecord {

        private String awsRegion;
        private String eventName;
        private String eventSource;
        private DateTime eventTime;
        private String eventVersion;
        private RequestParametersEntity requestParameters;
        private ResponseElementsEntity responseElements;
        private S3Entity s3;
        private UserIdentityEntity userIdentity;
        
        public S3EventNotificationRecord() {}

        public S3EventNotificationRecord(String awsRegion, String eventName, String eventSource, String eventTime,
                                         String eventVersion, RequestParametersEntity requestParameters,
                                         ResponseElementsEntity responseElements, S3Entity s3,
                                         UserIdentityEntity userIdentity) {
            this.awsRegion = awsRegion;
            this.eventName = eventName;
            this.eventSource = eventSource;

            if (eventTime != null)
            {
                this.eventTime = DateTime.parse(eventTime);
            }

            this.eventVersion = eventVersion;
            this.requestParameters = requestParameters;
            this.responseElements = responseElements;
            this.s3 = s3;
            this.userIdentity = userIdentity;
        }

        public String getAwsRegion() {
            return awsRegion;
        }

        public String getEventName() {
            return eventName;
        }

        public String getEventSource() {
            return eventSource;
        }

        public DateTime getEventTime() {
            return eventTime;
        }

        public String getEventVersion() {
            return eventVersion;
        }

        public RequestParametersEntity getRequestParameters() {
            return requestParameters;
        }

        public ResponseElementsEntity getResponseElements() {
            return responseElements;
        }

        public S3Entity getS3() {
            return s3;
        }

        public UserIdentityEntity getUserIdentity() {
            return userIdentity;
        }
    }
}

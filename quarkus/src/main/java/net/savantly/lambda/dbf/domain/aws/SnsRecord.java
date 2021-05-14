package net.savantly.lambda.dbf.domain.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsRecord {
	@JsonProperty("Sns")
	private SnsNotification sns;

	public SnsNotification getSns() {
		return sns;
	}

	public void setSns(SnsNotification sns) {
		this.sns = sns;
	}
}
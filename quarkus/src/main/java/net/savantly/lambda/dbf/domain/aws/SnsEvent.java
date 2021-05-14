package net.savantly.lambda.dbf.domain.aws;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsEvent {

	@JsonProperty(value = "Records")
	private List<SnsRecord> records = new ArrayList<>();
	
	public SnsEvent() {}

	public List<SnsRecord> getRecords() {
		return records;
	}

	public void setRecords(List<SnsRecord> records) {
		this.records = records;
	}

	
}

package net.savantly.lambda.dbf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.amazon.lambda.test.LambdaClient;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LambdaHandlerTest {

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testSimpleLambdaSuccess() throws Exception {
		LambdaClient.invoke(Void.class, fakeEvent());
	}

	public String fakeEvent() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("/payload.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}
}

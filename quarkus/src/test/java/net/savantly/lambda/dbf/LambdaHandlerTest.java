package net.savantly.lambda.dbf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.amazon.lambda.test.LambdaClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import net.savantly.lambda.dbf.domain.DbfResource;
import net.savantly.lambda.dbf.domain.aws.SnsEvent;

@QuarkusTest
public class LambdaHandlerTest {

	ObjectMapper mapper = new ObjectMapper();
	
	@InjectMock
	DbfResource dbfResource;
	
	@BeforeAll
    public static void setup() {
		DbfResource mock = Mockito.mock(DbfResource.class);
    }

	@Test
	public void testSimpleLambdaSuccess() throws Exception {
		SnsEvent event = mapper.readerFor(SnsEvent.class).readValue(fakeEvent());
		LambdaClient.invoke(Void.class, event);
	}

	public String fakeEvent() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("/payload.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}
}

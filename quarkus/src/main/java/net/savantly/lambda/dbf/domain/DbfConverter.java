package net.savantly.lambda.dbf.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFRow;
import com.linuxense.javadbf.DBFUtils;

public class DbfConverter {

	private final static Logger log = LoggerFactory.getLogger(DbfConverter.class);
	private final static String TEMP_DIR = System.getProperty("java.io.tmpdir");

	static protected File tempFilePath() {
		return new File(TEMP_DIR, new StringBuilder().append("csvTempFile").append((new Date()).getTime())
				.append(UUID.randomUUID()).append(".").append(".tmp").toString());
	}

	static File convertFromDbfToCsv(InputStream inputStream) {

		DBFReader reader = null;
		File csvFile = null;

		try {
			csvFile = tempFilePath();

			reader = new DBFReader(inputStream);

			int numberOfFields = reader.getFieldCount();
			Map<String, DBFField> tableFields = new HashMap<>();

			for (int i = 0; i < numberOfFields; i++) {
				DBFField tableField = reader.getField(i);
				String tableFieldName = tableField.getName();
				if (log.isTraceEnabled()) {
					log.trace("importing table field: {} {}", tableFieldName, tableField.getType());
				}
				tableFields.put(tableFieldName, tableField);
			}

			try (BufferedWriter writer = Files.newBufferedWriter(csvFile.toPath());

					CSVPrinter csvPrinter = new CSVPrinter(writer,
							CSVFormat.DEFAULT.withHeader(tableFields.keySet().toArray(new String[numberOfFields])));) {

				// Iterate the rows

				DBFRow row;

				while ((row = reader.nextRow()) != null) {
					Map<String, Object> mapRow = new HashMap<String, Object>();

					for (String fieldName : tableFields.keySet()) {
						Object value = row.getObject(fieldName);
						mapRow.put(fieldName.toLowerCase(), value);
					}

					csvPrinter.printRecord(mapRow.values());
				}

				// Iterated through all of the rows
				csvPrinter.flush();
			}

		} catch (Exception e) {
			log.error("error while importing", e);

		} finally {
			DBFUtils.close(reader);
		}
		return csvFile;
	}
}

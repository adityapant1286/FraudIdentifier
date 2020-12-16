package org.fraudidentifier;

import org.fraudidentifier.csvreaders.CCTransactionCsvReader;
import org.fraudidentifier.models.CCTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private CCTransactionFraudDetector fraudDetector;
    private static final String pathToCsv = "src/test/resources/testData.csv";
    private File testCsvFile;

    @Before
    public void setup() {
        this.fraudDetector = new CCTransactionFraudDetector();
        this.testCsvFile = new File(pathToCsv);
    }

    @After
    public void destroy() {
        this.fraudDetector = null;
        this.testCsvFile = null;
    }

    @Test
    public void testFileRead() throws IOException {
        List<CCTransaction> data = new CCTransactionCsvReader().read(pathToCsv);
        assertEquals(16, data.size());
    }

    @Test
    public void testArguments() {
        List<String> result = fraudDetector.execute(new String[]{"test"});

        assertEquals(1,
                    result.size());
        assertTrue(result.get(0).contains("required"));


        result = fraudDetector.execute(new String[]{"30", "sample.csv"});
        assertEquals(1,
                result.size());
        assertEquals("Invalid file path", result.get(0));


        result = fraudDetector.execute(new String[]{"30", testCsvFile.getPath(), "16/12/2020"});
        assertEquals(1,
                result.size());
        assertEquals("Invalid date format. Expected format YYYY-MM-dd", result.get(0));
    }

    @Test
    public void testFindFraudulentOnDay() {

        List<String> results = fraudDetector.execute(new String[]{
                Double.toString(30),
                testCsvFile.getPath(),
                "2020-12-16"
        });

        results.forEach(System.out::println);

        assertEquals(3, results.size());
        assertTrue(results.get(0).contains("CC0003"));
        assertTrue(results.get(1).contains("CC0004"));
        assertTrue(results.get(2).contains("CC0001"));

        results = fraudDetector.execute(new String[]{
                Double.toString(75),
                testCsvFile.getPath(),
                "2020-12-16"
        });

        results.forEach(System.out::println);
        assertEquals(2, results.size());
        assertTrue(results.get(0).contains("CC0003"));
        assertTrue(results.get(1).contains("CC0004"));

        results = fraudDetector.execute(new String[]{
                Double.toString(50),
                testCsvFile.getPath(),
                "2020-12-15"
        });

        results.forEach(System.out::println);
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("CC0001"));


        results = fraudDetector.execute(new String[]{
                Double.toString(50),
                testCsvFile.getPath(),
                "2020-12-10"
        });

        results.forEach(System.out::println);
        assertEquals(0, results.size());
    }

}

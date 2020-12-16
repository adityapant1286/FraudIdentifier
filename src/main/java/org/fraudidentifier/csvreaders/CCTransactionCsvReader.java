package org.fraudidentifier.csvreaders;

import org.fraudidentifier.models.CCTransaction;
import org.fraudidentifier.utils.AppUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * This is an implementation of Credit Card Transaction CSV data.
 * Each row will be validated by {@code valid(String[])} function and
 * valid rows will be added to collection.
 *
 * @since 1.0
 */
public class CCTransactionCsvReader implements CsvReader<List<CCTransaction>> {


    // Data indexes
    private static final int CC_NUMBER_INDEX = 0;
    private static final int TX_TIMESTAMP_INDEX = 1;
    private static final int AMOUNT_INDEX = 2;

    // Validation message with place holder
    private static final String INVALID_DATE = "(CSV Data Error): Transaction date '%s' is invalid for CardNumber '%s' having amount %s %n";

    private boolean showErrors = false;

    public CCTransactionCsvReader() {}

    public CCTransactionCsvReader(boolean showErrors) {
        this.showErrors = showErrors;
    }

    /**
     * Validates whether all fields are non-null and transaction date is valid.
     *
     * @param fieldsInRow {@link String[]} - an array of fields of a single row to be validated
     * @return true if the field values are valid, otherwise false
     */
    private boolean valid(String[] fieldsInRow) {
        if (Arrays.stream(fieldsInRow).noneMatch(String::isEmpty)) { // all non-null
            try {
                // Validate transaction date
                AppUtils.toIsoDateTime(fieldsInRow[TX_TIMESTAMP_INDEX]);

            } catch (DateTimeParseException ex) {
                if (showErrors) {
                    System.err.printf(
                            (INVALID_DATE),
                            fieldsInRow[TX_TIMESTAMP_INDEX],
                            fieldsInRow[CC_NUMBER_INDEX],
                            fieldsInRow[AMOUNT_INDEX]
                    );
                }
                return false;
            }
            return true; // valid
        }
        return false;
    }

    @Override
    public List<CCTransaction> read(String pathToCsvFile)
            throws IOException {

        final List<CCTransaction> results = new ArrayList<>();
        String line = null;
        CCTransaction ccTransaction = null;
        String[] row = null;

        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(pathToCsvFile), StandardCharsets.UTF_8)
                )
        ) {

            line = br.readLine(); // column names at first row

            while ((line = br.readLine()) != null) {

                row = line.split(COMMA_SEPARATOR);
                if (valid(row)) { // only valid data

                    ccTransaction = CCTransaction.builder()
                                            .hashedCardNumber(row[CC_NUMBER_INDEX])
                                            .transactionTimestamp(AppUtils.toIsoDateTime(row[TX_TIMESTAMP_INDEX]))
                                            .amount(Double.valueOf(row[AMOUNT_INDEX]))
                                            .build();

                    results.add(ccTransaction);
                }

            }
        }

        return results;
    }
}

package org.fraudidentifier;

import org.fraudidentifier.csvreaders.CCTransactionCsvReader;
import org.fraudidentifier.models.CCTransaction;
import org.fraudidentifier.services.CCTransactionService;
import org.fraudidentifier.services.helper.CompositeTxKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CCTransactionFraudDetector {

    public List<String> execute(String[] args) {

        List<String> validationErrors = validate(args);
        if (validationErrors.size() > 0) {
            return validationErrors;
        }

        boolean showCsvErrors = args.length == 4 && (args[3].equals("true"));

        final CCTransactionCsvReader csvReader = new CCTransactionCsvReader(showCsvErrors);

        try {
            // read file
            List<CCTransaction> ccTransactions = csvReader.read(args[1]);
            CCTransactionService service = new CCTransactionService();

            // aggregate data
            Map<CompositeTxKey, Double> aggregateTransactions = service.groupAndAggregateCCTransactions(ccTransactions);

            // find fraudulent CreditCard data
            List<String> results;
            if (args.length == 3) {

                results = service.findFraudulentCCNumbersOnDate(aggregateTransactions,
                        Double.valueOf(args[0]),
                        LocalDate.parse(args[2]));
            } else {

                results = service.findFraudulentCCNumbersOnDate(aggregateTransactions,
                        Double.valueOf(args[0]));
            }
            return results;


        } catch (IOException e) {
            return Arrays.asList("Error in reading CSV data.");
        }
    }

    private List<String> validate(String[] args) {

        if (args.length < 2) {
            return Arrays.asList("Threshold Amount and Credit Card Transaction CSV file path are required.\n" +
            "Optionally you can provide date for historical data (YYYY-MM-dd).");
        }

        if (!Files.exists(Paths.get(args[1]))) {
            return Arrays.asList("Invalid file path");
        }
        if (args.length == 3) {
            try {
                LocalDate.parse(args[2]);
            } catch (DateTimeParseException ex) {
                return Arrays.asList("Invalid date format. Expected format YYYY-MM-dd");
            }
        }

        if (args.length == 4 && (!Arrays.asList("true", "false").contains(args[3]))) {
            return Arrays.asList("Please provide either true or false");
        }
        return new ArrayList<>();
    }


}

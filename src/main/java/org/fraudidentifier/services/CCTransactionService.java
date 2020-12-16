package org.fraudidentifier.services;

import org.fraudidentifier.models.CCTransaction;
import org.fraudidentifier.services.helper.CompositeTxKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class CCTransactionService {

    private static final String FRAUDULENT_DETAILS = "CardNumber = %s | ExceededAmount = %s";

    /**
     * A list of {@link CCTransaction} will be grouped by CreditCard number and transaction date (YYYY-MM-dd).
     * The grouped transaction amount will be aggregated.
     *
     * @param transactions CreditCard transaction data
     * @return A map of {@link CompositeTxKey} and aggregated amount
     */
    public Map<CompositeTxKey, Double> groupAndAggregateCCTransactions(List<CCTransaction> transactions) {

        return transactions.stream()
                .collect(
                        groupingBy(
                                // group by CreditCard Number and Date in ISO format
                                tx -> new CompositeTxKey(tx.getHashedCardNumber(), tx.getIsoDate()),
                                // aggregate amount
                                summingDouble(CCTransaction::getAmount)
                        )
                );
    }

    /**
     * Filters the aggregated transactions by date then by threshold amount.
     * If any data matched, it will be return as output.
     *
     * @param aggregatedTransactions - a map of grouped and aggregated transactions
     * @param thresholdAmount - maximum amount allowed in a day
     * @param date - identify fraudulent CreditCard Numbers on a given date
     * @return A list of fraudulent CreditCard numbers and exceeded amount, otherwise empty list
     */
    public List<String> findFraudulentCCNumbersOnDate(final Map<CompositeTxKey, Double> aggregatedTransactions,
                                                      final Double thresholdAmount,
                                                      final LocalDate date) {

        return aggregatedTransactions.entrySet().stream()
                .filter(byDate(date)) // filter by given date
                .filter(byThresholdAmount(thresholdAmount)) // filter by amount exceeding threshold
                .map(entry ->
                        String.format(FRAUDULENT_DETAILS,
                                entry.getKey().getHashedCardNumber(),
                                entry.getValue())
                ) // map to string
                .collect(Collectors.toList());
    }

    /**
     * Filters the aggregated transactions by current date then by threshold amount.
     * If any data matched, it will be return as output.
     *
     * @param aggregatedTransactions - a map of grouped and aggregated transactions
     * @param thresholdAmount - maximum amount allowed in a day
     * @return A list of fraudulent CreditCard numbers and exceeded amount, otherwise empty list
     */
    public List<String> findFraudulentCCNumbersOnDate(final Map<CompositeTxKey, Double> aggregatedTransactions,
                                                      final Double thresholdAmount) {
        return findFraudulentCCNumbersOnDate(aggregatedTransactions,
                                        thresholdAmount,
                                        LocalDate.now());
    }

    /**
     * Builds a predicate expression to filter by given date.
     * Returns true if the date matches.
     *
     * @param date - a date to compare with other
     * @return a predicate
     */
    private Predicate<Map.Entry<CompositeTxKey, Double>> byDate(LocalDate date) {
        return entry -> entry.getKey().getIsoDate().isEqual(date);
    }

    /**
     * Builds a predicate expression to filter by given threshold amount.
     * Returns true if the other amount exceeds the threshold amount.
     *
     * @param thresholdAmount - amount filter criteria
     * @return a predicate
     */
    private Predicate<Map.Entry<CompositeTxKey, Double>> byThresholdAmount(Double thresholdAmount) {
        return entry -> entry.getValue() > thresholdAmount;
    }
}

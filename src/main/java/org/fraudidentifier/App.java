package org.fraudidentifier;

import java.util.List;

public class App {
    public static void main(String[] args) {
        CCTransactionFraudDetector fraudDetector = new CCTransactionFraudDetector();
        List<String> results = fraudDetector.execute(args);

        display(results);
    }

    private static void display(List<String> data) {
        data.forEach(System.out::println);
    }
}

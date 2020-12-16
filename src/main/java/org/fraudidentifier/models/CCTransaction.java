package org.fraudidentifier.models;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CCTransaction {

    @NonNull
    private String hashedCardNumber;
    private LocalDateTime transactionTimestamp;
    private Double amount;

    /**
     * Transforms {@link CCTransaction#getTransactionTimestamp()} to LocalDate.
     * If this is null or empty then current date.
     * YYYY-MM-dd
     *
     * @return If {@link CCTransaction#getTransactionTimestamp()} is present then
     * transform to ISO date, otherwise current date
     */
    public LocalDate getIsoDate() {
        return Optional.of(transactionTimestamp)
                        .orElse(LocalDateTime.now())
                        .toLocalDate();
    }
}

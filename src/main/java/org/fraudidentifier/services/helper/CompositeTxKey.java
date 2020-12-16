package org.fraudidentifier.services.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * This class will be used for grouping the data stream by declared fields.
 * Instance of this class will be a composite key of a map.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompositeTxKey {

    @NonNull
    private String hashedCardNumber;
    private LocalDate isoDate;
}

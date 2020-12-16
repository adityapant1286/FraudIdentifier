package org.fraudidentifier.csvreaders;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * The implementor class should provide implementation of CSV File read and return type.
 *
 * @param <T> returned by read method
 */
public interface CsvReader<T> {

    String COMMA_SEPARATOR = ",";

    /**
     * Accepts a path of CSV file from the file system and reads all data.
     * It is recommended the implementation should invoke {@code valid(String[])}
     * during read operation.
     *
     * @param pathToCsvFile - CSV file path in the file system
     * @return {@link T} the type defined in implementation
     * @throws IOException need to be handled by caller
     */
    T read(String pathToCsvFile) throws IOException;

}

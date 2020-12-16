## Fraud Identifier

This program reads Credit Card transactions from a CSV file and identify whether any credit card numbers are fraudulent as per below criteria.

### Criteria
The total transaction amount on a day is greater than given threshold value.

###### Example

Credit Card CC0001 total transaction value > 50 on a given date is 50, then CC0001 will be displayed in output.

### Valid CSV Sample Data
Only nonempty data will be considered, and the date format matches to ISO standard.

Sample test data file is present in `src/test/resources/testData.csv`

| CCNumber        | TransactionDate           | Amount  |
| ------------- |:-------------:| -----:|
| CC0001      | 2020-12-16T10:50:00 | 10.00 |
| CC0002      | 2020-12-16T10:50:00 | 15.50 |
| CC0003      | 2020-12-16T10:50:00 | 23.70 |
| CC0004      | 2020-12-16T10:50:00 | 43 |
| CC0004      | 2020-12-16T10:50:10 | 125.74 |

### Build

- Clone the repository
- Open terminal/command prompt and navigate to the FraudIdentifier directory
- Run `mvn clean install` command in terminal to build the Jar
- This will download dependencies and generate a Jar file in target folder

**Note:** On macOS the built Jar may require copying to another directory

### Test

###### Valid Input
| Param        | Type           | Position  | Required  |
| ------------- |:-------------:| :-----:| :-----:|
| Threshold Amount      | Decimal | 0 | Yes |
| Path to CSV File     | String | 1 | Yes |
| Transaction Date      | Date in ISO format (YYYY-MM-dd) | 2 | Optional (Default today will be used) |
| Show CSV errors      | Boolean | 3 | Optional (Default false) |


- Open terminal/command prompt and navigate to the directory where the Jar file has been generated in the build step
- Run following command with valid parameter
> java -jar FraudIdentifier-1.0.jar [Threshold Amount] [Path to CSV File] [Transaction Date] [Show Errors]


### Tools and Libraries
| Library        | Purpose           |
| ------------- |:-------------:|
| Lombok      | Generates setter, getter, builder for an annotated class |
| JUnit      | Unit testing |

### Known Issue
- CSV data volume is limited to allocated Heap memory. If you plan to search on a large volume it is recommended to increase the JVM heap memory.
  Reference:
  https://docs.oracle.com/cd/E21764_01/web.1111/e13814/jvm_tuning.htm#PERFM157
  
  https://stackoverflow.com/questions/6452765/how-to-increase-heap-size-of-jvm
  
### Area of Improvements
- Implementing a rule engine will allow defining business rules
- Implementing caching will improve search functionality
- Large volume can be handled efficiently using `MapDB` or similar library.
  https://github.com/jankotek/MapDB
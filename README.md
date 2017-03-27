# payment-tracker-with-scheduler
Read, parse values currency and amounts and every minute write table from it to console.

# How to run it
1.run maven command: mvn install

2.in target folder will be generated file payment-tracker-with-scheduler-1.0-SNAPSHOT.jar

3.run it by command: java -jar payment-tracker-with-scheduler-1.0-SNAPSHOT.jar

or with input data file command: java -jar payment-tracker-with-scheduler-1.0-SNAPSHOT.jar "c:\data.txt"

# Exceptions behavior
If the user enters invalid input, or in data file are invalid data, then program just write error message to console.
 
# Used currencies:
CZK: 0.039507
EUR: 1.067346
HKD: 0.128792
RMB: 0.144765

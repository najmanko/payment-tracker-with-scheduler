package cz.najman;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import static cz.najman.Utils.*;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PaymentTracker {

    private static final int SHOW_LIST_INTERVAL_IN_SECONDS = 60;
    private Map<String, BigDecimal> currencyAmounts = new ConcurrentHashMap<>();

    void runProgram(String[] args) {
        if (args.length > 0) {
            addAmountsFromInputFile(args[0]);
        }

        showAmountListEveryMinute();
        readInputFromConsoleOrQuit();
    }

    void addAmountToCurrencyAmounts(String input) {
        addAmountToCurrencyAmounts(resolveCurrencuAmountMap(input));
    }

    void printAmountsTable() {
        printCurrencyAmountsOnConsole(currencyAmounts);
    }

    void addAmountsFromInputFile(String fileName) {
        addAmountToCurrencyAmounts(readCurrencyAmountMapFromFile(fileName));
    }

    private void addAmountToCurrencyAmounts(Map<String, BigDecimal> newInput) {
        newInput.keySet().stream().forEach(key ->
                currencyAmounts.put(key, currencyAmounts.containsKey(key) ?
                        newInput.get(key).add(currencyAmounts.get(key)) : newInput.get(key)));
    }

    private void readInputFromConsoleOrQuit() {
        System.out.println("Write new input in format \"XXX 12500\" or \"quit\" for exit.");
        while (true) {
            String input = readInputOrQuit();
            addAmountToCurrencyAmounts(input);
        }
    }

    private void showAmountListEveryMinute() {
        ScheduledExecutorService scheduledPool = newScheduledThreadPool(1);
        scheduledPool.scheduleWithFixedDelay(() ->
                        printAmountsTable(), SHOW_LIST_INTERVAL_IN_SECONDS, SHOW_LIST_INTERVAL_IN_SECONDS, SECONDS);
    }
}
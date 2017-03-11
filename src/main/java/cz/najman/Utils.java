package cz.najman;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class Utils {
    private static final String CURRENCY_INPUT_REGEX = "\\s*[A-Z][A-Z][A-Z]\\s-*\\d*\\s*";
    private static final String USD_CURRENCY = "USD";
    private static final HashMap<String, BigDecimal> EXCHANGE_RATES = new HashMap<>();

    static {
        EXCHANGE_RATES.put("CZK", new BigDecimal("0.039507"));
        EXCHANGE_RATES.put("EUR", new BigDecimal("1.067346"));
        EXCHANGE_RATES.put("HKD", new BigDecimal("0.128792"));
        EXCHANGE_RATES.put("RMB", new BigDecimal("0.144765"));
    }

    static Map<String, BigDecimal> readCurrencyAmountMapFromFile(final String fileName) {
        Map<String, BigDecimal> currencyAmountFromFile = new HashMap<>();
        System.out.println("Reading from file " + fileName);
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(input -> currencyAmountFromFile.putAll(resolveCurrencuAmountMap(input)));
        } catch (IOException e) {
            System.err.println("Input file " + fileName + " is not readable! Cause of: " + e.toString());
        }
        return currencyAmountFromFile;
    }
    
    static String readInputOrQuit() {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if (input.toLowerCase().equals("quit")) {
            System.exit(0);
        }
        return input;
    }

    static String generateOutputWithUsdExchangedValue(String currency, BigDecimal amount) {
        String output = currency + " " + amount;
        if (!USD_CURRENCY.equals(currency) && EXCHANGE_RATES.containsKey(currency)) {
            BigDecimal usdAmount = EXCHANGE_RATES.get(currency).multiply(amount);
            usdAmount = usdAmount.setScale(2, RoundingMode.CEILING);
            output += " (USD " + usdAmount + ")";
        }
        return output;
    }

    static Map<String, BigDecimal> resolveCurrencuAmountMap(String input) {
        if (input.matches(CURRENCY_INPUT_REGEX)) {
            return getResolveCurrencyAmountMap(input);
        }
        System.err.println("Input row \"" + input + "\" is invalid!");
        return emptyMap();
    }

    static void printCurrencyAmountsOnConsole(Map<String, BigDecimal> currencyAmounts) {
        System.out.println("Output:");
        currencyAmounts.keySet().stream().filter(key -> currencyAmounts.get(key).signum() != 0)
                .forEach(key -> System.out.println(generateOutputWithUsdExchangedValue(key, currencyAmounts.get(key))));
    }

    private static Map<String, BigDecimal> getResolveCurrencyAmountMap(String input) {
        input = input.trim();
        String currency = input.substring(0, 3);
        BigDecimal amountValue = new BigDecimal(input.substring(4, input.length()));
        return singletonMap(currency, amountValue);
    }
}
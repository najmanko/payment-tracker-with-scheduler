package cz.najman;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PaymentTrackerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PaymentTracker paymentTracker = new PaymentTracker();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void shouldPrintCorrectCurrencyWithAmount() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAA 123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("AAA 123");
    }

    @Test
    public void shouldPrintCorrectCurrencyWithNegativeAmount() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAA -123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("AAA -123");
    }

    @Test
    public void shouldPrintCorrectCurrencyWithTooBigAmount() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAA 12345678901234567890");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("AAA 12345678901234567890");
    }

    @Test
    public void shouldPrintCorrectCurrencyWithManyCurrencies() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAA 123");
        paymentTracker.addAmountToCurrencyAmounts("BBB 456");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("AAA 123");
        assert outContent.toString().contains("BBB 456");
    }

    @Test
    public void shouldPrintCorrectCurrencyWithAddition() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAA 123");
        paymentTracker.addAmountToCurrencyAmounts("AAA -23");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("AAA 100");
    }

    @Test
    public void shouldPrintEmptyCurrencyWhenAmountIsZero() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAA 123");
        paymentTracker.addAmountToCurrencyAmounts("AAA -123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert !outContent.toString().contains("AAA");
    }

    @Test
    public void shouldPrintCorrectCurrencyWithExchangeToUSD() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("USD 123");
        paymentTracker.addAmountToCurrencyAmounts("CZK 1000");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("USD 123");
        assert outContent.toString().contains("CZK 1000 (USD 39.51)");
    }

    @Test
    public void shouldPrintCorrectCurrencyFromInputFile() {
        //when
        paymentTracker.addAmountsFromInputFile("currencyAmountsExamples.txt");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Reading from file currencyAmountsExamples.txt");
        assert outContent.toString().contains("Output:");
        assert outContent.toString().contains("AAA 123");
        assert outContent.toString().contains("VVV 890");
        assert outContent.toString().contains("EUR -1000 (USD -1067.34)");
        assert errContent.toString().contains("Input row \"asdfasdfasdfa\" is invalid!");
        assert errContent.toString().contains("Input row \"%%\" is invalid!");
        assert errContent.toString().contains("Input row \"AAA 17a\" is invalid!");
    }

    @Test
    public void shouldPrintCorrectCurrencyFromInvalidFile() {
        //when
        paymentTracker.addAmountsFromInputFile("invalidFile.txt");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Reading from file invalidFile.txt");
        assert outContent.toString().contains("Output:");
        assert errContent.toString().contains("Input file invalidFile.txt is not readable! " +
                "Cause of: java.nio.file.NoSuchFileException: invalidFile.txt");
    }

    @Test
    public void shouldPrintWrongInputValueErrorForNotCapital() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("Aaa 123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert errContent.toString().contains("Input row \"Aaa 123\" is invalid!");
    }

    @Test
    public void shouldPrintWrongInputValueErrorForWrongCharacters() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("A%A 123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert errContent.toString().contains("Input row \"A%A 123\" is invalid!");
    }

    @Test
    public void shouldPrintWrongInputValueErrorForTooLongCurrency() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AAAA 123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert errContent.toString().contains("Input row \"AAAA 123\" is invalid!");
    }

    @Test
    public void shouldPrintWrongInputValueErrorForTooSortCurrency() {
        //when
        paymentTracker.addAmountToCurrencyAmounts("AA 123");
        paymentTracker.printAmountsTable();

        //then
        assert outContent.toString().contains("Output:");
        assert errContent.toString().contains("Input row \"AA 123\" is invalid!");
    }
}
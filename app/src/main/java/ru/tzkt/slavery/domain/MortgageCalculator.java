package ru.tzkt.slavery.domain;

import ru.tzkt.slavery.utils.MathUtils;

@SuppressWarnings("NumericOverflow")
public class MortgageCalculator {

    private int currentPrice;
    private long currentTime = System.currentTimeMillis();

    private static final long MILLIS_IN_DAY = 60 * 60 * 24 * 1000;
    private static final long DAYS_IN_MONTH = 30;

    public MortgageCalculator() {

    }

    public void setApartmentPrice(int newPrice) {
        currentPrice = newPrice;
    }

    public long getEndTimeBySavings(int monthlySavings) {
        if (monthlySavings == 0) {
            return 0;
        }
        long months = currentPrice / monthlySavings;
        return currentTime + MILLIS_IN_DAY * DAYS_IN_MONTH * months;
    }

    public long getEndTimeByMortgage(int monthlySavings, float mortgagePercent) {
        if ((monthlySavings - currentPrice * mortgagePercent / 1200) < 0 || (1 + mortgagePercent / 1200) < 0 || (1 + mortgagePercent / 1200) == 1) {
            return 1;
        }

        double months = MathUtils.logMonth(monthlySavings, currentPrice, mortgagePercent);
        long monthsComplete = (long) months;
        return currentTime + MILLIS_IN_DAY * DAYS_IN_MONTH * monthsComplete;
    }
}

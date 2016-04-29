package com.github.vjgorla.refdata.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.github.vjgorla.refdata.util.DateUtils;

/**
 * 
 * @author Vijaya Gorla
 */
public class DateUtilsTest {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
    private static Date parse(String dateStr) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
    }

    private static String format(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    @Test
    public void givenADateThatIsNotMidnight_whenTrucate_thenDateSetToMidnight() throws ParseException {
        Date date = parse("2001-07-04 14:08:56.235");
        date = DateUtils.truncate(date);
        assertThat(format(date), is("2001-07-04 00:00:00.000"));
    }

    @Test
    public void givenADateThatIsMidnight_whenTrucate_thenReturnValueSameAsInput() throws ParseException {
        Date input = parse("2001-07-04 00:00:00.000");
        Date output = DateUtils.truncate(input);
        assertThat(output, is(input));
    }

    @Test
    public void givenDate1IsBeforeDate2_whenIsAfterIgnoreTime_thenReturnsFalse() throws ParseException {
        Date date1 = parse("2014-07-01 18:08:56.235");
        Date date2 = parse("2014-07-04 14:08:56.235");
        assertThat(DateUtils.isAfterIgnoreTime(date1, date2), is(false));
    }

    @Test
    public void givenDate1IsAfterDate2ButSameDay_whenIsAfterIgnoreTime_thenReturnsFalse() throws ParseException {
        Date date1 = parse("2014-07-04 18:08:56.235");
        Date date2 = parse("2014-07-04 14:08:56.235");
        assertThat(DateUtils.isAfterIgnoreTime(date1, date2), is(false));
    }

    @Test
    public void givenDate1IsBeforeDate2ButSameDay_whenIsAfterIgnoreTime_thenReturnsFalse() throws ParseException {
        Date date1 = parse("2014-07-04 09:08:56.235");
        Date date2 = parse("2014-07-04 14:08:56.235");
        assertThat(DateUtils.isAfterIgnoreTime(date1, date2), is(false));
    }

    @Test
    public void givenDate1SameAsDate2_whenIsAfterIgnoreTime_thenReturnsFalse() throws ParseException {
        Date date1 = parse("2014-07-04 14:08:56.235");
        Date date2 = parse("2014-07-04 14:08:56.235");
        assertThat(DateUtils.isAfterIgnoreTime(date1, date2), is(false));
    }

    @Test
    public void givenDate1SameAsDate2Midnight_whenIsAfterIgnoreTime_thenReturnsFalse() throws ParseException {
        Date date1 = parse("2014-07-04 00:00:00.000");
        Date date2 = parse("2014-07-04 00:00:00.000");
        assertThat(DateUtils.isAfterIgnoreTime(date1, date2), is(false));
    }

    @Test
    public void givenDate1AfterDate2_whenIsAfterIgnoreTime_thenReturnsTrue() throws ParseException {
        Date date1 = parse("2014-07-05 09:08:56.235");
        Date date2 = parse("2014-07-04 14:08:56.235");
        assertThat(DateUtils.isAfterIgnoreTime(date1, date2), is(true));
    }

    @Test
    public void givenNullFromDateNullToDateAndPastDate_whenIsBetween_thenAlwaysReturnsTrue() throws ParseException {
        Date date = parse("2014-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, null, null), is(true));
    }

    @Test
    public void givenNullFromDateNullToDateAndFutureDate_whenIsBetween_thenAlwaysReturnsTrue() throws ParseException {
        Date date = parse("2020-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, null, null), is(true));
    }

    @Test
    public void givenNullFromDateAndDateBeforeToDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2020-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, null, toDate), is(true));
    }

    @Test
    public void givenNullFromDateAndDateAfterToDate_whenIsBetween_thenReturnsFalse() throws ParseException {
        Date date = parse("2020-07-05 09:08:56.235");
        Date toDate = parse("2014-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, null, toDate), is(false));
    }

    @Test
    public void givenNullFromDateAndDateEqualsToDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2014-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, null, toDate), is(true));
    }

    @Test
    public void givenNullToDateAndDateBeforeFromDate_whenIsBetween_thenReturnsFalse() throws ParseException {
        Date date = parse("2014-07-05 09:08:56.235");
        Date fromDate = parse("2020-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, null), is(false));
    }

    @Test
    public void givenNullToDateAndDateAfterFromDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2020-07-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, null), is(true));
    }

    @Test
    public void givenNullToDateAndDateEqualsFromDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2014-07-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, null), is(true));
    }

    @Test
    public void givenDateBetweenToAndFromDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2014-09-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2015-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, toDate), is(true));
    }

    @Test
    public void givenDateBeforeFromDate_whenIsBetween_thenReturnsFalse() throws ParseException {
        Date date = parse("2014-04-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2015-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, toDate), is(false));
    }

    @Test
    public void givenDateAfterToDate_whenIsBetween_thenReturnsFalse() throws ParseException {
        Date date = parse("2015-11-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2015-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, toDate), is(false));
    }

    @Test
    public void givenDateSameAsFromDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2014-07-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2015-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, toDate), is(true));
    }

    @Test
    public void givenDateSameAsToDate_whenIsBetween_thenReturnsTrue() throws ParseException {
        Date date = parse("2015-07-05 09:08:56.235");
        Date fromDate = parse("2014-07-05 09:08:56.235");
        Date toDate = parse("2015-07-05 09:08:56.235");
        assertThat(DateUtils.isBetween(date, fromDate, toDate), is(true));
    }
}

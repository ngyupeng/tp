package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Event's duration in the event manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidDuration(String)}
 */
public class Duration {
    public static final String MESSAGE_CONSTRAINTS = "Event duration must either be in the format of "
            + "d/M/yyyy or d/M/yyyy-d/M/yyyy. E.g. \"1/10/2025\" or \"9/1/2022-10/2/2022\"."
            + "Where each date is a valid date.";
    public static final String MESSAGE_CONSTRAINTS_RANGE = "Event duration of the format d/M/yyyy-d/M/yyyy, "
            + "should not have the first date after the second date.";

    /*
     * The string must be a positive integer.
     */
    public static final String SINGLE_DATE_REGEX = "\\d{1,2}/\\d{1,2}/\\d{4}";
    public static final String DATE_RANGE_REGEX = SINGLE_DATE_REGEX + "\\s*-\\s*" + SINGLE_DATE_REGEX;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public final LocalDate startDate;
    public final LocalDate endDate;

    /**
     * Constructs an {@code Duration}.
     *
     * @param duration A valid duration.
     */
    public Duration(String duration) {
        requireNonNull(duration);
        checkArgument(isValidDuration(duration), MESSAGE_CONSTRAINTS);
        List<LocalDate> dates = getDates(duration);
        startDate = dates.get(0);
        endDate = dates.get(1);
        checkArgument(isValidDateRange(startDate, endDate), MESSAGE_CONSTRAINTS_RANGE);
    }

    /**
     * Returns true if a given string is a valid duration.
     */
    public static boolean isValidDuration(String test) {
        if (isValidDates(test)) {
            List<LocalDate> dates = getDates(test);
            LocalDate date1 = dates.get(0);
            LocalDate date2 = dates.get(1);
            return isValidDateRange(date1, date2);
        }
        return false;
    }

    /**
     * Returns true if a given string is of a valid duration format.
     */
    public static boolean isValidDates(String test) {
        try {
            if (test.matches(SINGLE_DATE_REGEX) || test.matches(DATE_RANGE_REGEX)) {
                getDates(test);
                return true;
            }
            return false;
        } catch (DateTimeParseException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns true if dates were a valid range
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    private static List<LocalDate> getDates(String input) {
        assert input.matches(SINGLE_DATE_REGEX) || input.matches(DATE_RANGE_REGEX)
            : "input must be a valid DATE REGEX";
        if (input.matches(SINGLE_DATE_REGEX)) {
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
            return Arrays.asList(date, date);
        } else {
            String[] dates = input.split("-");
            if (dates.length != 2) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
            }
            LocalDate startDate = LocalDate.parse(dates[0].trim(), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(dates[1].trim(), DATE_FORMATTER);
            return Arrays.asList(startDate, endDate);
        }
    }

    @Override
    public String toString() {
        return startDate.format(DATE_FORMATTER)
                + " - " + endDate.format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Duration)) {
            return false;
        }

        Duration duration = (Duration) other;
        return startDate.equals(duration.startDate)
                && endDate.equals(duration.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

}

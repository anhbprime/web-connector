package core.validation;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public final class ValidationConstants {
    private ValidationConstants() {}

    public static final class Patterns {
        private Patterns() {}

        public static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        public static final Pattern UUID =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");

        public static final Pattern INTEGER =
            Pattern.compile("^-?\\d+$");

        public static final Pattern ISO_UTC_Z =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?Z$");

        public static final Pattern ALNUM_USCORE =
            Pattern.compile("^[A-Za-z0-9_]+$");

        public static final Pattern PHONE_RELAXED =
            Pattern.compile("^[+]?[-0-9()\\s]{7,20}$");
    }

    public static final class Sets {
        private Sets() {}

        private static <T> Set<T> uset(@SuppressWarnings("unchecked") T... items) {
            return Collections.unmodifiableSet(new HashSet<T>(asList(items)));
        }

        public static final Set<String> ORDER_STATUS =
            uset("NEW", "PAID", "CANCELLED", "REFUNDED");

        public static final Set<String> USER_ROLE =
            uset("ADMIN", "MANAGER", "STAFF", "GUEST");

        public static final Set<String> CURRENCY3 =
            uset("USD", "EUR", "KRW", "JPY");

        public static final Set<String> YES_NO = uset("Y","N");
    }
}

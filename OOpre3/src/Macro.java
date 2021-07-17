import java.util.regex.Pattern;

public class Macro {
    //username category Macro data:
    static final String PATTERNA = "a{2,3}b{2,4}a{2,4}c{2,3}";
    static final String PATTERNB = "a{2,3}(ba){0,100000000}?(bc){2,4}";
    static final String PATTERNC = "a{2,3}(ba){0,100000000}?(bc){2,4}";
    static final String PATTERND = "^(a{0,3}b{1,1000000}?c{2,3})(.*)" +
            "([b,B]{1,2}[a,A]{1,2}[c,C]{0,3})$";
    static final String PATTERNE = "(?:a[^a]*?){1,3}?(?:b[^b]*?){2,100}?" +
            "(?:c[^c]*?){1,2}?(?:b[^b]*?){1,3}?(?:c[^c]*?){2,100}?";

    static final String PATTERNTIME = "^(([0-9]{2}[:][0-9]{2}[:][0-9]{2})" +
            "|([0-9]{2}[:][0-9]{2})|([0-9]{2}))";
    static final String PATTERNDATE = "[0-9]{2}-[0-9]{2}-[0-9]{4}";

    private static Pattern pA = Pattern.compile(PATTERNA);
    private static Pattern pB = Pattern.compile(PATTERNB);
    private static Pattern pC = Pattern.compile(PATTERNC);
    private static Pattern pD = Pattern.compile(PATTERND);
    private static Pattern pE = Pattern.compile(PATTERNE);
    private static Pattern pTime = Pattern.compile((PATTERNTIME));
    private static Pattern pDate = Pattern.compile(PATTERNDATE);

    public static Pattern getpA() {
        return pA;
    }

    public static Pattern getpB() {
        return pB;
    }

    public static Pattern getpC() {
        return pC;
    }

    public static Pattern getpD() {
        return pD;
    }

    public static Pattern getpE() {
        return pE;
    }

    public static Pattern getpTime() {
        return pTime;
    }

    public static Pattern getpDate() {
        return pDate;
    }
}

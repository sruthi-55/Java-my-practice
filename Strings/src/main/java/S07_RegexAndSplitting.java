import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

// a regular expression describes a text pattern and Pattern is its reusable compiled form
// Matcher holds mutable matching state and should not be shared between threads
// matches checks the whole input while find searches for the next matching region

// [abc] matches one listed character and [a-z] matches one character in a range
// [^abc] excludes listed characters and [a-z&&[^aeiou]] intersects lowercase letters with non-vowels
// . matches one character except line terminators unless DOTALL is enabled
// \d matches a digit, \w matches a word character and \s matches whitespace
// \D, \W and \S match the complements of \d, \w and \s
// predefined classes use ASCII-oriented defaults while UNICODE_CHARACTER_CLASS enables Unicode definitions
// \p{L} matches a Unicode letter, \p{M} matches a combining mark and \p{N} matches a Unicode number

// * means zero or more, + means one or more and ? means zero or one
// {n} means exactly n, {n,} means at least n and {n,m} means between n and m inclusive
// quantifiers apply to the preceding character, character class or group
// greedy quantifiers consume as much as possible and backtrack when the rest of the pattern requires it
// reluctant quantifiers such as *? consume as little as needed while possessive quantifiers such as *+ never give back matches

// ^ and $ anchor line boundaries under MULTILINE while \A and \z anchor the absolute input boundaries
// $ and \Z can match before a final line terminator while \z requires the absolute end
// \b matches a word boundary and \B matches a position that is not a word boundary
// | chooses alternatives, (...) captures a group and (?:...) groups without capturing
// (?<name>...) names a capture and \1 or \k<name> matches the same text again inside a pattern
// (?=...) and (?!...) assert positive and negative lookahead without consuming text
// (?<=...) and (?<!...) assert positive and negative lookbehind without consuming text

// regex backslashes must be doubled in Java strings so regex \d is written as "\\d"
// regex \b needs "\\b" because Java "\b" is a backspace and regex \s needs "\\s" because Java "\s" is a space
// (?i), (?m) and (?s) enable case-insensitive, multiline and dot-all matching respectively
// replacement strings use $1 or ${name} for captures rather than pattern backreferences

public class S07_RegexAndSplitting {
    public static void main(String[] args) {
        characterClasses();
        quantifiers();
        boundariesAndGroups();
        lookaroundsAndFlags();

        // Java string escaping and regex escaping are separate layers
        System.out.println("123".matches("\\d+"));	// true
        System.out.println("id=123".matches("\\d+"));	// false
        Pattern digits = Pattern.compile("\\d+");
        Matcher numbers = digits.matcher("id=123 qty=2");
        while (numbers.find()) {
            System.out.println(numbers.group() + "@" + numbers.start() + ":" + numbers.end());	// 123@3:6 then 2@11:12
        }

        // groups capture values and anchors restrict the accepted format
        Matcher item = Pattern.compile("^(?<name>[A-Za-z]+)-(\\d{2})$").matcher("Java-21");
        if (item.matches()) {
            System.out.println(item.group("name") + " " + item.group(2));	// Java 21
        }
        System.out.println(Pattern.compile("java", Pattern.CASE_INSENSITIVE).matcher("JAVA!").lookingAt());	// true

        // split uses regex and its limit controls trailing empty fields and the number of pieces
        System.out.println(Arrays.toString("a.b.c".split("\\.")));	// [a, b, c]
        System.out.println(Arrays.toString("a|b".split(Pattern.quote("|"))));	// [a, b]
        System.out.println(Arrays.toString("a,b,,".split(",")));	// [a, b]
        System.out.println(Arrays.toString("a,b,,".split(",", -1)));	// [a, b, , ]
        System.out.println(Arrays.toString("a,b,c".split(",", 2)));	// [a, b,c]

        // replace is literal while replaceAll and replaceFirst interpret regex patterns
        System.out.println("a.b.c".replace(".", "-"));	// a-b-c
        System.out.println("a1b22".replaceAll("\\d+", "#"));	// a#b#
        System.out.println("a1b22".replaceFirst("\\d+", "#"));	// a#b22
        System.out.println("Java-21".replaceAll("(\\w+)-(\\d+)", "$2:$1"));	// 21:Java
        System.out.println("price=?".replaceAll("\\?", Matcher.quoteReplacement("$5")));	// price=$5

        // invalid patterns fail at compilation and should not be confused with a non-match
        try {
//            Pattern.compile("[");
        } catch (PatternSyntaxException exception) {
            System.out.println(exception.getClass().getSimpleName());	// PatternSyntaxException
        }
    }

    private static void characterClasses() {
        // character classes match one character at a time and quantifiers repeat that match
        System.out.println("b".matches("[abc]"));	// true
        System.out.println("Java21".matches("[A-Za-z0-9]+"));	// true
        System.out.println("7".matches("[^0-9]"));	// false
        System.out.println("bcdf".matches("[a-z&&[^aeiou]]+"));	// true
        System.out.println("a.c".matches("a.c"));	// true
        System.out.println("abc".matches("a\\.c"));	// false

        // predefined classes and their uppercase complements cover common token rules
        System.out.println("123".matches("\\d+"));	// true
        System.out.println("Java".matches("\\D+"));	// true
        System.out.println("user_21".matches("\\w+"));	// true
        System.out.println("!?".matches("\\W+"));	// true
        System.out.println(" \t\n".matches("\\s+"));	// true
        System.out.println("Java21".matches("\\S+"));	// true
        System.out.println("é".matches("\\w"));	// false
        System.out.println("é".matches("(?U)\\w"));	// true
        System.out.println("தமிழ்".matches("[\\p{L}\\p{M}]+"));	// true
        System.out.println("٣".matches("\\p{N}"));	// true
    }

    private static void quantifiers() {
        // repetition bounds distinguish optional, required and limited occurrences
        System.out.println("".matches("a*"));	// true
        System.out.println("".matches("a+"));	// false
        System.out.println("color".matches("colou?r"));	// true
        System.out.println("2026".matches("\\d{4}"));	// true
        System.out.println("12345".matches("\\d{3,}"));	// true
        System.out.println("12345".matches("\\d{2,4}"));	// false
        System.out.println("abab".matches("(?:ab){2}"));	// true

        // greedy and reluctant searches choose different spans from the same input
        String tags = "<b>Java</b>";
        Matcher greedy = Pattern.compile("<.*>").matcher(tags);
        Matcher reluctant = Pattern.compile("<.*?>").matcher(tags);
        if (greedy.find() && reluctant.find()) {
            System.out.println(greedy.group());	// <b>Java</b>
            System.out.println(reluctant.group());	// <b>
        }
        System.out.println("aaa".matches("a*a"));	// true
        System.out.println("aaa".matches("a*+a"));	// false
        // an atomic group (?>...) also prevents backtracking into a completed group
        System.out.println("aaa".matches("(?>a*)a"));	// false
    }

    private static void boundariesAndGroups() {
        // absolute anchors reject a final newline while $ can stop before it during a search
        System.out.println(Pattern.compile("^Java$").matcher("Java\n").find());	// true
        System.out.println(Pattern.compile("\\AJava\\z").matcher("Java\n").find());	// false
        System.out.println(Pattern.compile("Java\\Z").matcher("Java\n").find());	// true
        System.out.println(Pattern.compile("\\bJava\\b").matcher("JavaScript Java").results().count());	// 1
        System.out.println(Pattern.compile("\\Bava").matcher("Java").find());	// true

        // grouping controls alternative scope and backreferences require identical captured text
        System.out.println("cat".matches("cat|dog"));	// true
        System.out.println("dogs".matches("(?:cat|dog)s?"));	// true
        System.out.println("go go".matches("(\\w+)\\s+\\1"));	// true
        System.out.println("go no".matches("(?<word>\\w+)\\s+\\k<word>"));	// false
        System.out.println("Java-21".replaceAll("(?<name>\\w+)-(?<version>\\d+)", "${version}:${name}"));	// 21:Java
    }

    private static void lookaroundsAndFlags() {
        // assertions check neighboring text without including it in the matched result
        Matcher ahead = Pattern.compile("\\d+(?= USD)").matcher("100 USD");
        Matcher behind = Pattern.compile("(?<=USD )\\d+").matcher("USD 100");
        if (ahead.find() && behind.find()) {
            System.out.println(ahead.group());	// 100
            System.out.println(behind.group());	// 100
        }
        System.out.println(Pattern.compile("Java(?!Script)").matcher("JavaScript").find());	// false
        System.out.println(Pattern.compile("(?<!un)happy").matcher("unhappy").find());	// false

        // multiline changes anchors while dot-all independently changes dot matching
        System.out.println("JAVA".matches("(?i)java"));	// true
        System.out.println(Pattern.compile("(?m)^Java$").matcher("SQL\nJava\nGit").find());	// true
        System.out.println("a\nb".matches("a.b"));	// false
        System.out.println("a\nb".matches("(?s)a.b"));	// true
        System.out.println("É".matches("(?iu)é"));	// true
        // UNICODE_CASE extends case folding while UNICODE_CHARACTER_CLASS changes predefined character classes
    }

    // reuse Pattern for repeated work and create a separate Matcher for each independent operation
    // nested ambiguous quantifiers can cause excessive backtracking so bound and validate external input
    // split is not a CSV parser because quoted fields and escaped delimiters need structured parsing
}

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;

// a code unit is one UTF-16 char while a code point can require a pair of char values
// a grapheme cluster is user-perceived text that may contain multiple code points
// encoding converts text to bytes and decoding interprets bytes using a charset

public class S08_UnicodeAndEncoding {
    public static void main(String[] args) throws CharacterCodingException {
        // supplementary characters occupy two UTF-16 positions but represent one code point
        String text = "A😀B";
        System.out.println(text.length());	// 4
        System.out.println(text.codePointCount(0, text.length()));	// 3
        System.out.println(Character.isHighSurrogate(text.charAt(1)));	// true
        System.out.println(text.codePointAt(1));	// 128512
        System.out.println(text.codePointBefore(3));	// 128512
        System.out.println(Arrays.toString(text.chars().toArray()));	// [65, 55357, 56832, 66]
        System.out.println(Arrays.toString(text.codePoints().toArray()));	// [65, 128512, 66]
        int end = text.offsetByCodePoints(1, 1);
        System.out.println(text.substring(1, end));	// 😀
        System.out.println(new String(new int[]{0x1F600}, 0, 1));	// 😀

        // visually equivalent text can differ in code points until normalized
        String composed = "é";
        String decomposed = "e\u0301";
        System.out.println(composed.equals(decomposed));	// false
        System.out.println(decomposed.codePointCount(0, decomposed.length()));	// 2
        System.out.println(composed.equals(Normalizer.normalize(decomposed, Normalizer.Form.NFC)));	// true

        // explicit charsets keep byte conversion independent of environment defaults
        byte[] bytes = composed.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(bytes));	// [-61, -87]
        System.out.println(new String(bytes, StandardCharsets.UTF_8));	// é
        System.out.println(new String(bytes, StandardCharsets.ISO_8859_1).equals(composed));	// false
        try {
            StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(new byte[]{(byte) 0xC3}));
        } catch (CharacterCodingException exception) {
            System.out.println("invalid UTF-8");	// invalid UTF-8
        }

        // case conversion can change length and depends on locale
        System.out.println("straße".toUpperCase(Locale.ROOT));	// STRASSE
        System.out.println("TITLE".toLowerCase(Locale.forLanguageTag("tr")));	// tıtle
        System.out.println("TITLE".toLowerCase(Locale.ROOT));	// title
        Collator collator = Collator.getInstance(Locale.ENGLISH);
        collator.setStrength(Collator.PRIMARY);
        System.out.println(collator.compare("resume", "résumé") == 0);	// true
    }

    // String comparison uses UTF-16 units rather than linguistic ordering and does not normalize automatically
    // code-point-safe algorithms can still split grapheme clusters such as combining marks or joined emoji
    // String byte constructors replace malformed input while a decoder can report it explicitly
}

package org.nsdl.mptstore.query;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

public class SPOQueryCompilerTest extends TestCase {

    public SPOQueryCompilerTest(String name) { super (name); }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    //
    // Parse Tests
    //

    private boolean isValidQuery(String query) {
        try {
            SPOQueryCompiler.parse(query);
            return true;
        } catch (QuerySyntaxException e) {
            return false;
        }
    }

    public void testParseValidQueries() {
        assertTrue(isValidQuery("* * *"));
        assertTrue(isValidQuery("* * 'test'"));
        assertTrue(isValidQuery("* * \"test\""));
        assertTrue(isValidQuery("* * \"Here's a quote: \\\"\""));
        assertTrue(isValidQuery("<urn:a> <urn:b> <urn:c>"));
        assertTrue(isValidQuery("<urn:a> <urn:b> \"test\""));
    }

    public void testParseInvalidQueries() {
        assertFalse(isValidQuery(""));
        assertFalse(isValidQuery("* *"));
        assertFalse(isValidQuery("* * * *"));
        assertFalse(isValidQuery("a * *"));
        assertFalse(isValidQuery("* <> *"));
        assertFalse(isValidQuery("* <urn:test> \""));
        assertFalse(isValidQuery("* <urn:test> \'"));
        assertFalse(isValidQuery("<urn:test> <urn:test> 2"));
    }

    //
    // Validation Tests
    //

    private boolean isValidToken(String token) {
        try {
            SPOQueryCompiler.validate(token);
            return true;
        } catch (QuerySyntaxException e) {
            return false;
        }
    }

    // Validation Tests: URI Tokens

    public void testValidateGoodURI() {
        assertTrue(isValidToken("<urn:test>"));
    }

    public void testValidateBadURI1() {
        assertFalse(isValidToken("<urn:test"));
    }

    public void testValidateBadURI2() {
        assertFalse(isValidToken("<urn:test test>"));
    }

    public void testValidateEmptyURI() {
        assertFalse(isValidToken("<>"));
    }

    // Validation Tests: Plain Literals

    public void testValidateSimplePlainLiteral() {
        String token = "test";
        assertTrue(isValidToken("\"" + token + "\""));
        assertTrue(isValidToken("'" + token + "'"));
    }

    public void testValidateSimplePlainLiteralWithSpace() {
        String token = "te st";
        assertTrue(isValidToken("\"" + token + "\""));
        assertTrue(isValidToken("'" + token + "'"));
    }

    public void testValidatePlainLiteralWithGoodEscaping() {
        String token = "Here is a quote: \\\", and a forward slash: \\\\";
        assertTrue(isValidToken("\"" + token + "\""));
        assertTrue(isValidToken("'" + token + "'"));
    }

    public void testValidatePlainLiteralWithoutRequiredEscaping() {
        String token = "Here is a quote: \""; // Here is a quote: "
        assertFalse(isValidToken("\"" + token + "\""));
        assertFalse(isValidToken("'" + token + "'"));
    }

    public void testValidateEmptyPlainLiteral() {
        String token = "";
        assertTrue(isValidToken("\"" + token + "\""));
        assertTrue(isValidToken("'" + token + "'"));
    }

    public void testValidatePlainLiteralMissingTerminal() {
        String token = "test";
        assertFalse(isValidToken("\"" + token));
        assertFalse(isValidToken("'" + token));
    }

    public void testValidatePlainLiteralWrongTerminal() {
        String token = "test";
        assertFalse(isValidToken("\"" + token + "'"));
        assertFalse(isValidToken("'" + token + "\""));
    }

    public void testValidatePlainLiteralBadValue() {
        String token = "test\\";
        assertFalse(isValidToken("\"" + token + "\""));
        assertFalse(isValidToken("'" + token + "'"));
    }

    // Validation Tests : Qualified Literals

    public void testValidateGoodTypedLiteral() {
        String token = "test";
        assertTrue(isValidToken("\"" + token + "\"^^urn:someType"));
        assertTrue(isValidToken("'" + token + "'^^urn:someType"));
    }

    public void testValidateBadTypedLiteral() {
        String token = "test";
        assertFalse(isValidToken("\"" + token + "\"^^not-a-uri"));
        assertFalse(isValidToken("'" + token + "'^^not-a-uri"));
    }

    public void testValidateGoodLocalizedLiteral() {
        String token = "test";
        assertTrue(isValidToken("\"" + token + "\"@en"));
        assertTrue(isValidToken("'" + token + "'@en"));
    }

    public void testValidateBadLocalizedLiteral() {
        String token = "test";
        assertFalse(isValidToken("\"" + token + "\"@"));
        assertFalse(isValidToken("'" + token + "'@"));
    }

    public void testValidateBadQualifiedLiteral() {
        String token = "test";
        assertFalse(isValidToken("\"" + token + "\"foo"));
        assertFalse(isValidToken("'" + token + "'foo"));
    }


    //
    // Normalization Tests
    //

    public void testNormalizeAsterisk() {
        assertNull(SPOQueryCompiler.normalize("*"));
    }

    public void testNormalizeResource() {
        String in = "<urn:test>";
        assertEquals(in, SPOQueryCompiler.normalize(in));
    }

    public void testNormalizeDoubleQuotedPlainLiteral() {
        String in = "\"test\"";
        assertEquals(in, SPOQueryCompiler.normalize(in));
    }

    public void testNormalizeSingleQuotedPlainLiteral() {
        String in = "'test'";
        String out = "\"test\"";
        assertEquals(out, SPOQueryCompiler.normalize(in));
    }

    public void testNormalizeSingleQuotedPlainLiteralWithEscapedDoubleQuote() {
        String in = "'Here is a quote: \\\"'"; // 'Here is a quote: \"'
        String out = "\"Here is a quote: \\\"\""; // "Here is a quote: \""
        assertEquals(out, SPOQueryCompiler.normalize(in));
    }

    public void testNormalizeSingleQuotedTypedLiteral() {
        String in = "'test'^^urn:someType";
        String out = "\"test\"^^urn:someType";
        assertEquals(out, SPOQueryCompiler.normalize(in));
    }

    public static void main(String[] args) {
        TestRunner.run(SPOQueryCompilerTest.class);
    }   

}

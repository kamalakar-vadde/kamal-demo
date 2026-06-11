package com.pdfgenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HtmlToPdfConverter
 */
class HtmlToPdfConverterTest {

    private HtmlToPdfConverter converter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        converter = new HtmlToPdfConverter();
    }

    @Test
    void testConvertSimpleHtmlStringToPdf() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String outputPath = tempDir.resolve("output.pdf").toString();

        converter.convertHtmlStringToPdf(html, outputPath);

        assertTrue(Files.exists(tempDir.resolve("output.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    void testConvertHtmlWithFormattingToPdf() throws Exception {
        String html = "<html>" +
                "<head><style>h1 { color: red; } p { font-size: 12px; }</style></head>" +
                "<body>" +
                "<h1>Formatted Title</h1>" +
                "<p>This is a paragraph with formatting.</p>" +
                "</body>" +
                "</html>";
        String outputPath = tempDir.resolve("formatted.pdf").toString();

        converter.convertHtmlStringToPdf(html, outputPath);

        assertTrue(Files.exists(tempDir.resolve("formatted.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    void testConvertHtmlWithTableToPdf() throws Exception {
        String html = "<html><body>" +
                "<table border='1'>" +
                "<tr><td>Header 1</td><td>Header 2</td></tr>" +
                "<tr><td>Data 1</td><td>Data 2</td></tr>" +
                "</table>" +
                "</body></html>";
        String outputPath = tempDir.resolve("table.pdf").toString();

        converter.convertHtmlStringToPdf(html, outputPath);

        assertTrue(Files.exists(tempDir.resolve("table.pdf")));
    }

    @Test
    void testConvertHtmlFileWithContentToPdf() throws Exception {
        String html = "<html><body><h1>File Test</h1></body></html>";
        Path htmlFile = tempDir.resolve("test.html");
        Files.write(htmlFile, html.getBytes());

        String outputPath = tempDir.resolve("from_file.pdf").toString();
        converter.convertHtmlFileToPdf(htmlFile.toString(), outputPath);

        assertTrue(Files.exists(tempDir.resolve("from_file.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    void testExtractPlaceholdersFromHtml() {
        String html = "<html><body>" +
                "<h1>{{title}}</h1>" +
                "<p>{{content}}</p>" +
                "<footer>{{author}}</footer>" +
                "</body></html>";

        String[] placeholders = converter.extractPlaceholders(html);

        assertEquals(3, placeholders.length);
        assertTrue(containsPlaceholder(placeholders, "title"));
        assertTrue(containsPlaceholder(placeholders, "content"));
        assertTrue(containsPlaceholder(placeholders, "author"));
    }

    @Test
    void testReplaceSinglePlaceholder() {
        String html = "<html><body><h1>{{title}}</h1></body></html>";
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("title", "My PDF Title");

        String result = converter.replacePlaceholders(html, placeholders);

        assertTrue(result.contains("My PDF Title"));
        assertFalse(result.contains("{{title}}"));
    }

    @Test
    void testReplaceMultiplePlaceholders() {
        String html = "<html><body>" +
                "<h1>{{title}}</h1>" +
                "<p>Author: {{author}}</p>" +
                "<p>Date: {{date}}</p>" +
                "</body></html>";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("title", "Invoice");
        placeholders.put("author", "John Doe");
        placeholders.put("date", "2024-01-15");

        String result = converter.replacePlaceholders(html, placeholders);

        assertTrue(result.contains("Invoice"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("2024-01-15"));
        assertFalse(result.contains("{{"));
    }

    @Test
    void testPlaceholdersWithWhitespace() {
        String html = "<html><body><h1>{{ title }}</h1><p>{{  content  }}</p></body></html>";
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("title", "Test Title");
        placeholders.put("content", "Test Content");

        String result = converter.replacePlaceholders(html, placeholders);

        assertTrue(result.contains("Test Title"));
        assertTrue(result.contains("Test Content"));
    }

    @Test
    void testConvertHtmlWithPlaceholdersToPdf() throws Exception {
        String html = "<html><body>" +
                "<h1>{{documentTitle}}</h1>" +
                "<p>Prepared for: {{clientName}}</p>" +
                "<p>Date: {{reportDate}}</p>" +
                "</body></html>";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("documentTitle", "Annual Report");
        placeholders.put("clientName", "Acme Corporation");
        placeholders.put("reportDate", "2024-01-15");

        String outputPath = tempDir.resolve("with_placeholders.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(html, placeholders, outputPath);

        assertTrue(Files.exists(tempDir.resolve("with_placeholders.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    void testConvertHtmlFileWithPlaceholdersToPdf() throws Exception {
        String html = "<html><body>" +
                "<h1>{{invoice_number}}</h1>" +
                "<p>{{customer_name}}</p>" +
                "<p>Total: ${{total_amount}}</p>" +
                "</body></html>";

        Path htmlFile = tempDir.resolve("invoice.html");
        Files.write(htmlFile, html.getBytes());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("invoice_number", "INV-2024-001");
        placeholders.put("customer_name", "Jane Smith");
        placeholders.put("total_amount", "1500.00");

        String outputPath = tempDir.resolve("invoice.pdf").toString();
        converter.convertHtmlFileWithPlaceholdersToPdf(htmlFile.toString(), placeholders, outputPath);

        assertTrue(Files.exists(tempDir.resolve("invoice.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    void testNullPlaceholderValue() {
        String html = "<html><body><h1>{{title}}</h1><p>{{missing}}</p></body></html>";
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("title", "Test");
        placeholders.put("missing", null);

        String result = converter.replacePlaceholders(html, placeholders);

        assertTrue(result.contains("Test"));
        assertFalse(result.contains("{{missing}}"));
    }

    @Test
    void testPlaceholderNotFound() {
        String html = "<html><body><h1>{{title}}</h1></body></html>";
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("author", "Someone");

        String result = converter.replacePlaceholders(html, placeholders);

        assertTrue(result.contains("{{title}}"));
    }

    @Test
    void testComplexHtmlWithPlaceholders() throws Exception {
        String html = "<html>" +
                "<head>" +
                "<style>" +
                "h1 { color: blue; }" +
                "table { border-collapse: collapse; }" +
                "th, td { border: 1px solid black; padding: 8px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>{{reportTitle}}</h1>" +
                "<p>Generated on {{date}}</p>" +
                "<table>" +
                "<tr><th>Item</th><th>Value</th></tr>" +
                "<tr><td>Name</td><td>{{clientName}}</td></tr>" +
                "<tr><td>Total</td><td>{{total}}</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("reportTitle", "Q1 2024 Report");
        placeholders.put("date", "2024-02-01");
        placeholders.put("clientName", "Enterprise Ltd");
        placeholders.put("total", "$50,000");

        String outputPath = tempDir.resolve("complex.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(html, placeholders, outputPath);

        assertTrue(Files.exists(tempDir.resolve("complex.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    void testEmptyPlaceholdersMap() throws Exception {
        String html = "<html><body><h1>No placeholders here</h1></body></html>";
        Map<String, String> placeholders = new HashMap<>();

        String outputPath = tempDir.resolve("no_placeholders.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(html, placeholders, outputPath);

        assertTrue(Files.exists(tempDir.resolve("no_placeholders.pdf")));
    }

    @Test
    void testSpecialCharactersInPlaceholderValues() throws Exception {
        String html = "<html><body><h1>{{title}}</h1><p>{{content}}</p></body></html>";
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("title", "Title with <special> & characters");
        placeholders.put("content", "Content with \"quotes\" and 'apostrophes'");

        String outputPath = tempDir.resolve("special_chars.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(html, placeholders, outputPath);

        assertTrue(Files.exists(tempDir.resolve("special_chars.pdf")));
    }

    @Test
    void testInvalidOutputPath() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String invalidPath = "/invalid/path/that/does/not/exist/output.pdf";

        assertThrows(IOException.class, () ->
                converter.convertHtmlStringToPdf(html, invalidPath)
        );
    }

    @Test
    void testNullHtmlContent() {
        String outputPath = tempDir.resolve("null_test.pdf").toString();

        assertThrows(Exception.class, () ->
                converter.convertHtmlStringToPdf(null, outputPath)
        );
    }

    @Test
    void testEmptyHtmlContent() throws Exception {
        String html = "";
        String outputPath = tempDir.resolve("empty.pdf").toString();

        converter.convertHtmlStringToPdf(html, outputPath);
        assertTrue(Files.exists(tempDir.resolve("empty.pdf")));
    }

    private boolean containsPlaceholder(String[] placeholders, String target) {
        for (String placeholder : placeholders) {
            if (placeholder.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
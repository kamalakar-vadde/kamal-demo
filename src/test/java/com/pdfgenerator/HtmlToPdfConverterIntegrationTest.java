package com.pdfgenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for HtmlToPdfConverter
 * Tests real-world scenarios and complex use cases
 */
@DisplayName("HtmlToPdfConverter Integration Tests")
class HtmlToPdfConverterIntegrationTest {

    private HtmlToPdfConverter converter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        converter = new HtmlToPdfConverter();
    }

    @Test
    @DisplayName("Generate invoice PDF with dynamic data")
    void testInvoicePdfGeneration() throws Exception {
        String invoiceHtml = buildInvoiceHtml();
        Path htmlFile = tempDir.resolve("invoice_template.html");
        Files.write(htmlFile, invoiceHtml.getBytes());

        Map<String, String> invoiceData = buildInvoiceData();
        String outputPath = tempDir.resolve("invoice_2024_001.pdf").toString();

        converter.convertHtmlFileWithPlaceholdersToPdf(
                htmlFile.toString(),
                invoiceData,
                outputPath
        );

        assertTrue(Files.exists(tempDir.resolve("invoice_2024_001.pdf")));
        long fileSize = new File(outputPath).length();
        assertTrue(fileSize > 1000, "PDF should have content");
    }

    @Test
    @DisplayName("Generate report PDF with multiple sections")
    void testReportPdfGeneration() throws Exception {
        String reportHtml = buildReportHtml();
        Map<String, String> reportData = buildReportData();
        String outputPath = tempDir.resolve("quarterly_report.pdf").toString();

        converter.convertHtmlWithPlaceholdersToPdf(reportHtml, reportData, outputPath);

        assertTrue(Files.exists(tempDir.resolve("quarterly_report.pdf")));
        assertTrue(new File(outputPath).length() > 0);
    }

    @Test
    @DisplayName("Generate certificate PDF with styling")
    void testCertificatePdfGeneration() throws Exception {
        String certificateHtml = buildCertificateHtml();
        Map<String, String> certData = buildCertificateData();
        String outputPath = tempDir.resolve("certificate.pdf").toString();

        converter.convertHtmlWithPlaceholdersToPdf(certificateHtml, certData, outputPath);

        assertTrue(Files.exists(tempDir.resolve("certificate.pdf")));
    }

    @Test
    @DisplayName("Generate letter with date and recipient info")
    void testLetterPdfGeneration() throws Exception {
        String letterHtml = buildLetterHtml();
        Map<String, String> letterData = buildLetterData();
        String outputPath = tempDir.resolve("letter.pdf").toString();

        converter.convertHtmlWithPlaceholdersToPdf(letterHtml, letterData, outputPath);

        assertTrue(Files.exists(tempDir.resolve("letter.pdf")));
    }

    @Test
    @DisplayName("Batch process multiple HTML files")
    void testBatchPdfGeneration() throws Exception {
        int documentCount = 3;

        for (int i = 1; i <= documentCount; i++) {
            String html = "<html><body><h1>Document {{docNumber}}</h1>" +
                    "<p>This is document number {{docNumber}}</p></body></html>";
            Map<String, String> data = new HashMap<>();
            data.put("docNumber", String.valueOf(i));

            String outputPath = tempDir.resolve("document_" + i + ".pdf").toString();
            converter.convertHtmlWithPlaceholdersToPdf(html, data, outputPath);

            assertTrue(Files.exists(tempDir.resolve("document_" + i + ".pdf")));
        }

        assertEquals(documentCount, tempDir.toFile().listFiles().length);
    }

    @Test
    @DisplayName("Extract and validate all placeholders")
    void testPlaceholderExtraction() {
        String template = "<html><body>" +
                "<h1>{{title}}</h1>" +
                "<p>Author: {{author}}</p>" +
                "<p>Date: {{date}}</p>" +
                "<p>Content: {{content}}</p>" +
                "<footer>{{footer}}</footer>" +
                "</body></html>";

        String[] placeholders = converter.extractPlaceholders(template);

        assertEquals(5, placeholders.length);
        assertTrue(arrayContains(placeholders, "title"));
        assertTrue(arrayContains(placeholders, "author"));
        assertTrue(arrayContains(placeholders, "date"));
        assertTrue(arrayContains(placeholders, "content"));
        assertTrue(arrayContains(placeholders, "footer"));
    }

    @Test
    @DisplayName("Handle special HTML entities in placeholders")
    void testSpecialHtmlEntities() throws Exception {
        String html = "<html><body>" +
                "<p>&copy; {{year}} {{company}}</p>" +
                "<p>&nbsp;{{spacing}}&nbsp;</p>" +
                "</body></html>";

        Map<String, String> data = new HashMap<>();
        data.put("year", "2024");
        data.put("company", "ACME & Associates");
        data.put("spacing", "Content");

        String outputPath = tempDir.resolve("entities.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(html, data, outputPath);

        assertTrue(Files.exists(tempDir.resolve("entities.pdf")));
    }

    @Test
    @DisplayName("Process large HTML document")
    void testLargeHtmlDocument() throws Exception {
        StringBuilder largeHtml = new StringBuilder();
        largeHtml.append("<html><body><h1>{{title}}</h1>");

        for (int i = 0; i < 100; i++) {
            largeHtml.append("<p>Paragraph {{pageNumber}} - Content ").append(i).append("</p>");
        }

        largeHtml.append("</body></html>");

        Map<String, String> data = new HashMap<>();
        data.put("title", "Large Document");
        data.put("pageNumber", "1");

        String outputPath = tempDir.resolve("large_document.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(largeHtml.toString(), data, outputPath);

        assertTrue(Files.exists(tempDir.resolve("large_document.pdf")));
        assertTrue(new File(outputPath).length() > 5000);
    }

    @Test
    @DisplayName("Handle nested HTML tags with placeholders")
    void testNestedTagsWithPlaceholders() throws Exception {
        String html = "<html><body>" +
                "<div class='container'>" +
                "<section>" +
                "<article>" +
                "<h1>{{heading}}</h1>" +
                "<p>{{description}}</p>" +
                "</article>" +
                "</section>" +
                "</div>" +
                "</body></html>";

        Map<String, String> data = new HashMap<>();
        data.put("heading", "Nested Content Test");
        data.put("description", "Testing deeply nested HTML structures");

        String outputPath = tempDir.resolve("nested.pdf").toString();
        converter.convertHtmlWithPlaceholdersToPdf(html, data, outputPath);

        assertTrue(Files.exists(tempDir.resolve("nested.pdf")));
    }

    private String buildInvoiceHtml() {
        return "<html>" +
                "<head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                "table { width: 100%; border-collapse: collapse; margin: 20px 0; }" +
                "th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                "th { background-color: #4CAF50; color: white; }" +
                ".invoice-header { text-align: center; margin-bottom: 30px; }" +
                "</style></head>" +
                "<body>" +
                "<div class='invoice-header'>" +
                "<h1>INVOICE</h1>" +
                "<p>Invoice #: {{invoiceNumber}}</p>" +
                "</div>" +
                "<p><strong>From:</strong> {{senderName}}</p>" +
                "<p><strong>To:</strong> {{recipientName}}</p>" +
                "<p><strong>Date:</strong> {{invoiceDate}}</p>" +
                "<table>" +
                "<tr><th>Description</th><th>Quantity</th><th>Unit Price</th><th>Total</th></tr>" +
                "<tr><td>{{itemDescription}}</td><td>{{itemQuantity}}</td><td>{{itemPrice}}</td><td>{{itemTotal}}</td></tr>" +
                "</table>" +
                "<p><strong>Total Amount:</strong> {{totalAmount}}</p>" +
                "<p><strong>Terms:</strong> {{paymentTerms}}</p>" +
                "</body>" +
                "</html>";
    }

    private Map<String, String> buildInvoiceData() {
        Map<String, String> data = new HashMap<>();
        data.put("invoiceNumber", "INV-2024-12345");
        data.put("senderName", "ABC Corporation");
        data.put("recipientName", "XYZ Company");
        data.put("invoiceDate", "2024-01-15");
        data.put("itemDescription", "Consulting Services");
        data.put("itemQuantity", "40");
        data.put("itemPrice", "$150.00");
        data.put("itemTotal", "$6,000.00");
        data.put("totalAmount", "$6,000.00");
        data.put("paymentTerms", "Net 30");
        return data;
    }

    private String buildReportHtml() {
        return "<html><body>" +
                "<h1>{{reportTitle}}</h1>" +
                "<p><strong>Period:</strong> {{period}}</p>" +
                "<p><strong>Prepared By:</strong> {{preparedBy}}</p>" +
                "<h2>Executive Summary</h2>" +
                "<p>{{summary}}</p>" +
                "<h2>Key Metrics</h2>" +
                "<ul>" +
                "<li>Revenue: {{revenue}}</li>" +
                "<li>Expenses: {{expenses}}</li>" +
                "<li>Profit: {{profit}}</li>" +
                "</ul>" +
                "</body></html>";
    }

    private Map<String, String> buildReportData() {
        Map<String, String> data = new HashMap<>();
        data.put("reportTitle", "Q1 2024 Financial Report");
        data.put("period", "January 1 - March 31, 2024");
        data.put("preparedBy", "Finance Department");
        data.put("summary", "Strong performance in all quarters with growth across all segments.");
        data.put("revenue", "$5,000,000");
        data.put("expenses", "$3,200,000");
        data.put("profit", "$1,800,000");
        return data;
    }

    private String buildCertificateHtml() {
        return "<html>" +
                "<head><style>" +
                "body { text-align: center; font-family: Georgia, serif; margin: 50px; }" +
                "h1 { font-size: 36px; color: #333; margin: 30px 0; }" +
                "p { font-size: 16px; margin: 15px 0; }" +
                ".border { border: 2px solid gold; padding: 40px; margin: 30px 0; }" +
                "</style></head>" +
                "<body>" +
                "<h1>Certificate of Achievement</h1>" +
                "<div class='border'>" +
                "<p>This certifies that</p>" +
                "<p><strong>{{recipientName}}</strong></p>" +
                "<p>has successfully completed</p>" +
                "<p><strong>{{courseName}}</strong></p>" +
                "<p>on {{completionDate}}</p>" +
                "<p>With Distinction</p>" +
                "</div>" +
                "<p>Signed: {{signatoryName}}</p>" +
                "</body></html>";
    }

    private Map<String, String> buildCertificateData() {
        Map<String, String> data = new HashMap<>();
        data.put("recipientName", "John Smith");
        data.put("courseName", "Advanced Java Programming");
        data.put("completionDate", "January 15, 2024");
        data.put("signatoryName", "Director of Education");
        return data;
    }

    private String buildLetterHtml() {
        return "<html><body style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                "<p>{{letterDate}}</p>" +
                "<p>{{recipientAddress}}</p>" +
                "<p>Dear {{recipientName}},</p>" +
                "<p>{{letterBody}}</p>" +
                "<p>Sincerely,<br/>{{senderName}}</p>" +
                "</body></html>";
    }

    private Map<String, String> buildLetterData() {
        Map<String, String> data = new HashMap<>();
        data.put("letterDate", "January 15, 2024");
        data.put("recipientAddress", "123 Main Street, Anytown, USA");
        data.put("recipientName", "Jane Doe");
        data.put("letterBody", "Thank you for your inquiry. We are pleased to provide the requested information.");
        data.put("senderName", "John Executive");
        return data;
    }

    private boolean arrayContains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
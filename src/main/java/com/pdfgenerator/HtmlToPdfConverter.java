package com.pdfgenerator;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main converter class for HTML to PDF conversion using iText 7.1
 * Supports XML placeholders that can be replaced with dynamic values
 */
public class HtmlToPdfConverter {

    private static final Logger logger = LoggerFactory.getLogger(HtmlToPdfConverter.class);
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{\\s*([^}]+)\\s*\\}\\}");

    /**
     * Convert HTML string to PDF file
     *
     * @param htmlContent the HTML content as string
     * @param outputPath  path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlStringToPdf(String htmlContent, String outputPath) throws IOException {
        logger.info("Starting HTML to PDF conversion. Output path: {}", outputPath);
        try (PdfWriter writer = new PdfWriter(outputPath);
             PdfDocument pdfDoc = new PdfDocument(writer)) {
            HtmlConverter.convertToPdf(htmlContent, pdfDoc);
            logger.info("PDF generated successfully at: {}", outputPath);
        } catch (Exception e) {
            logger.error("Error converting HTML to PDF", e);
            throw new IOException("Failed to convert HTML to PDF", e);
        }
    }

    /**
     * Convert HTML file to PDF
     *
     * @param htmlFilePath path to the HTML file
     * @param outputPath   path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlFileToPdf(String htmlFilePath, String outputPath) throws IOException {
        logger.info("Converting HTML file: {} to PDF: {}", htmlFilePath, outputPath);
        String htmlContent = readFile(htmlFilePath);
        convertHtmlStringToPdf(htmlContent, outputPath);
    }

    /**
     * Convert HTML with XML placeholders to PDF
     * Replaces {{placeholder}} with corresponding values from the map
     *
     * @param htmlContent  the HTML content with placeholders
     * @param placeholders map of placeholder keys and their values
     * @param outputPath   path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlWithPlaceholdersToPdf(String htmlContent, Map<String, String> placeholders,
                                                  String outputPath) throws IOException {
        logger.info("Converting HTML with {} placeholders to PDF: {}", placeholders.size(), outputPath);
        String processedHtml = replacePlaceholders(htmlContent, placeholders);
        convertHtmlStringToPdf(processedHtml, outputPath);
    }

    /**
     * Convert HTML file with XML placeholders to PDF
     *
     * @param htmlFilePath path to the HTML file with placeholders
     * @param placeholders map of placeholder keys and their values
     * @param outputPath   path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlFileWithPlaceholdersToPdf(String htmlFilePath, Map<String, String> placeholders,
                                                      String outputPath) throws IOException {
        logger.info("Converting HTML file with placeholders: {} to PDF: {}", htmlFilePath, outputPath);
        String htmlContent = readFile(htmlFilePath);
        convertHtmlWithPlaceholdersToPdf(htmlContent, placeholders, outputPath);
    }

    /**
     * Replace placeholders in HTML content
     * Supports {{placeholder_key}} format
     *
     * @param htmlContent  the HTML content with placeholders
     * @param placeholders map of placeholder keys and their values
     * @return HTML content with placeholders replaced
     */
    public String replacePlaceholders(String htmlContent, Map<String, String> placeholders) {
        logger.debug("Replacing placeholders in HTML content");
        String result = htmlContent;
        
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "\\{\\{\\s*" + Pattern.quote(entry.getKey()) + "\\s*\\}\\}";
            String replacement = Matcher.quoteReplacement(entry.getValue() != null ? entry.getValue() : "");
            result = result.replaceAll(placeholder, replacement);
        }
        
        logger.debug("Placeholder replacement complete");
        return result;
    }

    /**
     * Read file content as string
     *
     * @param filePath path to the file
     * @return file content
     * @throws IOException if file reading fails
     */
    private String readFile(String filePath) throws IOException {
        logger.debug("Reading file: {}", filePath);
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    /**
     * Get all placeholders found in HTML content
     *
     * @param htmlContent the HTML content
     * @return array of placeholder keys found
     */
    public String[] extractPlaceholders(String htmlContent) {
        logger.debug("Extracting placeholders from HTML content");
        java.util.Set<String> placeholders = new java.util.HashSet<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(htmlContent);
        
        while (matcher.find()) {
            placeholders.add(matcher.group(1).trim());
        }
        
        return placeholders.toArray(new String[0]);
    }
}
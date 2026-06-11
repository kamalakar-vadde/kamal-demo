package com.pdfgenerator;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
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
 * Full integration with iText 7.1 XML License Key for commercial features
 */
public class HtmlToPdfConverter {

    private static final Logger logger = LoggerFactory.getLogger(HtmlToPdfConverter.class);
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{\\s*([^}]+)\\s*\\}\\}");
    
    private boolean licenseKeyLoaded = false;

    /**
     * Initialize iText 7.1 XML License Key from file
     * This enables all commercial features like advanced styling, digital signatures, etc.
     *
     * @param licenseKeyPath path to the XML license key file
     * @throws IOException if file reading fails
     */
    public void initializeLicenseKey(String licenseKeyPath) throws IOException {
        try {
            logger.info("Initializing iText 7.1 License Key from file: {}", licenseKeyPath);
            byte[] licenseKeyContent = Files.readAllBytes(Paths.get(licenseKeyPath));
            LicenseKey.loadLicenseFile(licenseKeyContent);
            licenseKeyLoaded = true;
            logger.info("iText 7.1 License Key loaded successfully. Commercial features enabled.");
        } catch (IOException e) {
            logger.error("Failed to load iText License Key from file: {}", licenseKeyPath, e);
            throw new IOException("Failed to load iText License Key: " + e.getMessage(), e);
        }
    }

    /**
     * Initialize iText 7.1 XML License Key from XML string
     * Useful for loading from environment variables or configuration strings
     *
     * @param licenseKeyXml the XML license key content as string
     * @throws IOException if license key initialization fails
     */
    public void initializeLicenseKeyFromString(String licenseKeyXml) throws IOException {
        try {
            logger.info("Initializing iText 7.1 License Key from XML string");
            if (licenseKeyXml == null || licenseKeyXml.trim().isEmpty()) {
                throw new IllegalArgumentException("License key XML cannot be null or empty");
            }
            byte[] licenseKeyBytes = licenseKeyXml.getBytes(StandardCharsets.UTF_8);
            LicenseKey.loadLicenseFile(licenseKeyBytes);
            licenseKeyLoaded = true;
            logger.info("iText 7.1 License Key loaded successfully from string. Commercial features enabled.");
        } catch (Exception e) {
            logger.error("Failed to load iText License Key from string", e);
            throw new IOException("Failed to load iText License Key: " + e.getMessage(), e);
        }
    }

    /**
     * Initialize iText 7.1 XML License Key from environment variable
     * Environment variable name: ITEXT_LICENSE_KEY
     *
     * @throws IOException if environment variable not set or license key initialization fails
     */
    public void initializeLicenseKeyFromEnvironment() throws IOException {
        try {
            String licenseKeyXml = System.getenv("ITEXT_LICENSE_KEY");
            if (licenseKeyXml == null || licenseKeyXml.trim().isEmpty()) {
                throw new IllegalArgumentException("ITEXT_LICENSE_KEY environment variable not set");
            }
            initializeLicenseKeyFromString(licenseKeyXml);
            logger.info("iText 7.1 License Key loaded from ITEXT_LICENSE_KEY environment variable");
        } catch (Exception e) {
            logger.error("Failed to initialize license key from environment variable", e);
            throw new IOException("Failed to load iText License Key from environment: " + e.getMessage(), e);
        }
    }

    /**
     * Check if license key has been successfully loaded
     *
     * @return true if license key is loaded, false otherwise
     */
    public boolean isLicenseKeyLoaded() {
        return licenseKeyLoaded;
    }

    /**
     * Convert HTML string to PDF file with license key features
     *
     * @param htmlContent the HTML content as string
     * @param outputPath  path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlStringToPdf(String htmlContent, String outputPath) throws IOException {
        logger.info("Starting HTML to PDF conversion. Output path: {} | License Key Active: {}", outputPath, licenseKeyLoaded);
        try (PdfWriter writer = new PdfWriter(outputPath);
             PdfDocument pdfDoc = new PdfDocument(writer)) {
            HtmlConverter.convertToPdf(htmlContent, pdfDoc);
            logger.info("PDF generated successfully at: {} (License Key: {})", outputPath, licenseKeyLoaded ? "ENABLED" : "DISABLED");
        } catch (Exception e) {
            logger.error("Error converting HTML to PDF", e);
            throw new IOException("Failed to convert HTML to PDF", e);
        }
    }

    /**
     * Convert HTML file to PDF with license key features
     *
     * @param htmlFilePath path to the HTML file
     * @param outputPath   path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlFileToPdf(String htmlFilePath, String outputPath) throws IOException {
        logger.info("Converting HTML file: {} to PDF: {} | License Key: {}", htmlFilePath, outputPath, licenseKeyLoaded);
        String htmlContent = readFile(htmlFilePath);
        convertHtmlStringToPdf(htmlContent, outputPath);
    }

    /**
     * Convert HTML with XML placeholders to PDF
     * Replaces {{placeholder}} with corresponding values from the map
     * Full iText commercial features available if license key loaded
     *
     * @param htmlContent  the HTML content with placeholders
     * @param placeholders map of placeholder keys and their values
     * @param outputPath   path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlWithPlaceholdersToPdf(String htmlContent, Map<String, String> placeholders,
                                                  String outputPath) throws IOException {
        logger.info("Converting HTML with {} placeholders to PDF: {} | License Key: {}", 
                   placeholders.size(), outputPath, licenseKeyLoaded);
        String processedHtml = replacePlaceholders(htmlContent, placeholders);
        convertHtmlStringToPdf(processedHtml, outputPath);
    }

    /**
     * Convert HTML file with XML placeholders to PDF
     * Uses iText 7.1 commercial license key features for advanced rendering
     *
     * @param htmlFilePath path to the HTML file with placeholders
     * @param placeholders map of placeholder keys and their values
     * @param outputPath   path where the PDF will be saved
     * @throws IOException if file operations fail
     */
    public void convertHtmlFileWithPlaceholdersToPdf(String htmlFilePath, Map<String, String> placeholders,
                                                      String outputPath) throws IOException {
        logger.info("Converting HTML file with placeholders: {} to PDF: {} | License Key: {}", 
                   htmlFilePath, outputPath, licenseKeyLoaded);
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

    /**
     * Get license key status information
     *
     * @return string describing current license key status
     */
    public String getLicenseKeyStatus() {
        return licenseKeyLoaded ? "ACTIVE - iText 7.1 Commercial Features Enabled" : "INACTIVE - Using Open Source Mode";
    }
}

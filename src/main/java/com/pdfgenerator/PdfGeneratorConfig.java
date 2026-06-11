package com.pdfgenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for PDF generation settings
 */
public class PdfGeneratorConfig {

    private String pageSize;
    private String pageOrientation;
    private Map<String, String> customProperties;
    private boolean enableCompression;
    private String author;
    private String title;
    private String subject;
    private String creator;

    public PdfGeneratorConfig() {
        this.pageSize = "A4";
        this.pageOrientation = "PORTRAIT";
        this.customProperties = new HashMap<>();
        this.enableCompression = true;
        this.author = "PDF Generator";
        this.creator = "PDF Generator v1.0";
    }

    // Getters and Setters
    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageOrientation() {
        return pageOrientation;
    }

    public void setPageOrientation(String pageOrientation) {
        this.pageOrientation = pageOrientation;
    }

    public Map<String, String> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Map<String, String> customProperties) {
        this.customProperties = customProperties;
    }

    public boolean isEnableCompression() {
        return enableCompression;
    }

    public void setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
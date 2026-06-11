# PDF Generator

A Java Maven project for converting HTML to PDF using iText 7.1 with support for XML-style placeholders.

## Features

- **HTML to PDF Conversion**: Convert HTML strings and files to PDF using iText 7.1
- **XML Placeholder Support**: Use `{{placeholder_name}}` syntax in HTML templates
- **Dynamic Content**: Replace placeholders with dynamic values from a Map
- **Flexible Configuration**: Configurable PDF settings and properties
- **Comprehensive Testing**: Unit and integration tests included
- **File-based and String-based Conversion**: Support for both HTML files and strings

## Requirements

- Java 11 or higher
- Maven 3.6.0 or higher

## Dependencies

- iText 7.1.18 (core and html2pdf)
- JUnit 5 (testing)
- Mockito (testing)
- SLF4J and Logback (logging)

## Installation

Clone the repository and build with Maven:

```bash
git clone https://github.com/kamalakar-vadde/kamal-demo.git
cd kamal-demo
git checkout feature/html-to-pdf-itext
mvn clean install
```

## Quick Start

### Basic HTML to PDF Conversion

```java
HtmlToPdfConverter converter = new HtmlToPdfConverter();
String html = "<html><body><h1>Hello World</h1></body></html>";
converter.convertHtmlStringToPdf(html, "output.pdf");
```

### Convert HTML File to PDF

```java
converter.convertHtmlFileToPdf("template.html", "output.pdf");
```

### HTML with Placeholders

```java
String html = "<html><body>" +
    "<h1>{{title}}</h1>" +
    "<p>Author: {{author}}</p>" +
    "</body></html>";

Map<String, String> placeholders = new HashMap<>();
placeholders.put("title", "My Document");
placeholders.put("author", "John Doe");

converter.convertHtmlWithPlaceholdersToPdf(html, placeholders, "output.pdf");
```

### HTML File with Placeholders

```java
Map<String, String> data = new HashMap<>();
data.put("documentTitle", "Annual Report");
data.put("clientName", "Acme Corporation");
data.put("reportDate", "2024-01-15");

converter.convertHtmlFileWithPlaceholdersToPdf(
    "template.html",
    data,
    "report.pdf"
);
```

## Placeholder Syntax

Placeholders follow the XML/template syntax:

```html
{{placeholder_name}}
{{placeholder with spaces}}
{{  even_with_extra_spaces  }}
```

Key points:
- Placeholders are case-sensitive
- Whitespace around placeholder names is automatically trimmed
- Unmatched placeholders remain in the output if not provided in the map
- Null values are replaced with empty strings

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=HtmlToPdfConverterTest
mvn test -Dtest=HtmlToPdfConverterIntegrationTest
```

### Test Coverage

The project includes:
- **20+ Unit Tests**: Basic functionality, edge cases, error handling
- **8+ Integration Tests**: Real-world scenarios (invoices, reports, certificates, letters)
- **Batch Processing Tests**: Multiple document generation
- **Special Character Tests**: HTML entities and special characters
- **Large Document Tests**: Performance with large HTML content

## Project Structure

```
feature/html-to-pdf-itext/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── java/com/pdfgenerator/
│   │       ├── HtmlToPdfConverter.java
│   │       └── PdfGeneratorConfig.java
│   └── test/
│       ├── java/com/pdfgenerator/
│       │   ├── HtmlToPdfConverterTest.java
│       │   └── HtmlToPdfConverterIntegrationTest.java
│       └── resources/
│           └── sample-template.html
```

## Build and Package

### Build JAR

```bash
mvn clean package
```

### Generate JavaDoc

```bash
mvn javadoc:javadoc
```

## API Reference

### HtmlToPdfConverter

#### Methods

**`convertHtmlStringToPdf(String htmlContent, String outputPath)`**
- Converts HTML string to PDF file
- Throws: `IOException`

**`convertHtmlFileToPdf(String htmlFilePath, String outputPath)`**
- Converts HTML file to PDF
- Throws: `IOException`

**`convertHtmlWithPlaceholdersToPdf(String htmlContent, Map<String, String> placeholders, String outputPath)`**
- Converts HTML with placeholder replacement to PDF
- Throws: `IOException`

**`convertHtmlFileWithPlaceholdersToPdf(String htmlFilePath, Map<String, String> placeholders, String outputPath)`**
- Converts HTML file with placeholder replacement to PDF
- Throws: `IOException`

**`replacePlaceholders(String htmlContent, Map<String, String> placeholders)`**
- Replaces placeholders in HTML content
- Returns: HTML string with placeholders replaced

**`extractPlaceholders(String htmlContent)`**
- Extracts all placeholder names from HTML
- Returns: Array of placeholder names

## License

This project is open source.

# Example iText License Key XML Format

This directory should contain your iText 7.1 license key in XML format.

## License Key XML Structure

Place your license key XML file in a secure location and reference it in your application:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<license>
    <id>YOUR_LICENSE_ID</id>
    <expiryDate>2025-12-31</expiryDate>
    <licensee>YOUR_COMPANY_NAME</licensee>
    <!-- Additional license details -->
</license>
```

## How to Use

### Load License Key from File

```java
HtmlToPdfConverter converter = new HtmlToPdfConverter();

// Load license key before creating PDFs
converter.initializeLicenseKey("/path/to/license.xml");

// Now convert HTML to PDF
String html = "<html><body><h1>My PDF</h1></body></html>";
converter.convertHtmlStringToPdf(html, "output.pdf");
```

### Load License Key from String

```java
// For license keys stored in environment variables or config servers
String licenseKeyXml = System.getenv("ITEXT_LICENSE_KEY");
converter.initializeLicenseKeyFromString(licenseKeyXml);
```

### Check License Key Status

```java
if (converter.isLicenseKeyLoaded()) {
    System.out.println("License key is active");
}
```

## Getting a License Key

Visit [iText Getting Started](https://itextpdf.com/en/community/licensing-options) to:
- Download a free Community License (for open-source projects)
- Obtain an Evaluation License (30-day free trial)
- Purchase a Commercial License

## Security Best Practices

1. **Never commit license keys** to version control
2. **Use environment variables** for sensitive license data in production
3. **Restrict file permissions** on license key files
4. **Use configuration servers** for secure key management
5. **Rotate keys** periodically

## Obtaining Your License Key

For Community/Open-Source projects:
- Visit https://itextpdf.com/en/products/itext-7/faq
- Fill out the Community License form

For Commercial projects:
- Contact iText sales
- Download evaluation license
- Purchase appropriate license tier

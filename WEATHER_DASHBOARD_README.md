# Weather Dashboard with iText 7.1 PDF Integration

A comprehensive weather dashboard application that fetches real-time weather data from OpenWeatherMap API and generates professional PDF reports using iText 7.1 with XML license key support.

## Features

✅ **Weather API Integration** - Real-time weather data from OpenWeatherMap  
✅ **iText 7.1 License Key** - Professional PDF generation with commercial features  
✅ **XML Placeholder Support** - Dynamic content with {{placeholder}} syntax  
✅ **Single & Multi-City Reports** - Generate reports for one or multiple cities  
✅ **Professional Styling** - Gradient backgrounds, card layouts, responsive design  
✅ **Comprehensive Testing** - 20+ unit tests included  

## Quick Start

### 1. Get API Key
Register at [openweathermap.org](https://openweathermap.org/api) for free API key

### 2. Initialize Components
```java
// Initialize Weather API Client
WeatherApiClient weatherClient = new WeatherApiClient("your_api_key", "metric");

// Initialize PDF Converter with iText License Key
HtmlToPdfConverter pdfConverter = new HtmlToPdfConverter();
pdfConverter.initializeLicenseKey("/path/to/itext_license.xml");

// Create Service
WeatherDashboardService service = new WeatherDashboardService(weatherClient, pdfConverter);
```

### 3. Generate Reports
```java
// Single city report
service.generateWeatherReportPdf("London", "weather_report.pdf");

// Multiple cities
List<String> cities = Arrays.asList("London", "Paris", "Tokyo");
service.generateMultipleCitiesWeatherReportPdf(cities, "multi_report.pdf");

// Dashboard with placeholders
service.generateWeatherDashboardWithPlaceholders("Berlin", "dashboard.pdf");
```

## Project Structure

```
src/
├── main/java/com/weather/
│   ├── model/
│   │   └── WeatherData.java              (Weather information model)
│   └── service/
│       ├── WeatherApiClient.java          (OpenWeatherMap API client)
│       └── WeatherDashboardService.java   (PDF report generation)
└── test/java/com/weather/
    ├── model/
    │   └── WeatherDataTest.java
    └── service/
        ├── WeatherApiClientTest.java
        └── WeatherDashboardServiceTest.java
```

## Weather Data Models

### WeatherData
Complete weather information with:
- Temperature, feels like, min/max
- Humidity, pressure, wind speed
- Location, country, timestamp
- Weather condition and description

## Testing

```bash
# Run all tests
mvn test

# Run weather tests
mvn test -Dtest=Weather*
```

## iText 7.1 License Key

Place your XML license key and load it:

```java
// From file
pdfConverter.initializeLicenseKey("license.xml");

// From environment variable
String licenseXml = System.getenv("ITEXT_LICENSE_KEY");
pdfConverter.initializeLicenseKeyFromString(licenseXml);

// Check if loaded
if (pdfConverter.isLicenseKeyLoaded()) {
    // Use full commercial features
}
```

## API Documentation

- [OpenWeatherMap API](https://openweathermap.org/api)
- [iText 7.1 Documentation](https://itextpdf.com/en/resources/documentation/itext-7)

## Configuration

### Units
- `metric` - Celsius, m/s (default)
- `imperial` - Fahrenheit, mph

```java
WeatherApiClient client = new WeatherApiClient(apiKey, "imperial");
```

## Examples

See test cases in `src/test/java/com/weather/` for comprehensive usage examples.

## Future Enhancements

- Weather forecasts (5-day, 16-day)
- Historical weather data
- Email report delivery
- Scheduled generation
- Multiple API providers
- Weather alerts

## Support

For issues or questions, review the test cases and API documentation.

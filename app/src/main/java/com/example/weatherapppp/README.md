## Weather App
### Introduction
   WeatherApppp is a simple Android weather application developed using Kotlin and Jetpack
Compose. The goal of this project was to practice modern Android development principles,
including API usage, MVVM architecture, and offline data handling.
### Technologies Used
* Kotlin as the main programming language
* Jetpack Compose for UI
* Retrofit and Gson for networking and JSON parsing
* Open-Meteo API for weather data (no API key required)
* MVVM architecture pattern
* SharedPreferences for local caching
### Application Functionality
The application allows users to search for weather information by entering a city name. After the
search, the app displays current weather conditions, including temperature, humidity, wind speed,
and weather description. Additionally, a 3-day forecast is shown below the current weather section.
### Offline Mode
   If the device has no internet connection, the application automatically loads the last saved 
weather data from local storage. In this case, an "Offline mode" label is displayed to inform the 
user that  cached data is being used.
### Architecture
   The project follows the MVVM (Model–View–ViewModel) architecture. Networking and data
   handling are managed in the Repository layer, UI logic is handled in ViewModels, and the user
   interface is built with Jetpack Compose.
### Error Handling
   The application handles common errors such as empty input, city not found, and missing internet
   connection. User-friendly messages are displayed instead of app crashes.
### Limitations
* Temperature unit switching (Celsius/Fahrenheit) is not implemented
* Forecast is limited to 3 days
* No automatic location detection
### Conclusion
   WeatherApppp demonstrates the use of modern Android development tools and clean architecture
principles. The project successfully fulfills the core requirements of fetching, displaying, and caching
weather data, while remaining simple and easy to understand.
### AI Usage Disclosure
 ChatGPT was used as a supporting tool for explanations, debugging, and documentation
assistance.

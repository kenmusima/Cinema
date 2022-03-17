# Cinema (In Progress)
CinemaApp is an application that uses a section of Android Jetpack libraries and also Retrofit to display data from 
[TheMovieDB](https://www.themoviedb.org/documentation/api) API in terms of popular movies for booking.

### Prerequisites
To setup the project add the following secret and keys in `local.properties` file:
MovieDB API Key
```yaml
apiKey=""
```
Google Maps API Key
```yaml
MAPS_API_KEY=""
```
Mpesa API Consumer Key, Consumer Secret, Passkey,Business ShortCode
```yaml
CONSUMER_KEY=""
CONSUMER_SECRET=""
PASSKEY=""
BUSINESS_SHORT_CODE=""
```
Google Functions Callback Url
```yaml
CALLBACK_URL=""
```

To run the Project build using Android Studio or Intelli J and all the required dependencies will be downloaded and installed.

## Architecture

The project uses MVVM architecture pattern.

## Libraries 

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/) - Manage UI related data in a lifecycle conscious way and act as a channel between use cases and ui
* [ViewBinding](https://developer.android.com/topic/libraries/data-binding) - support library that allows binding of UI components in layouts to data sources,binds character details and search results to UI
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) - Android Jetpack's Navigation component helps in implementing
navigation between fragments
* [Dagger Hilt](https://developer.android.com/jetpack/androidx/releases/hilt) - For Dependency Injection.
* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=in) - Allow pagination of the Data.
* [Retrofit](https://square.github.io/retrofit/) - To access the Rest Api
* [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) - To store data in key value pairs, in this case to store boolean if the disclaimer dialog is shown or not

package ru.yandex.buggyweatherapp.repository

import ru.yandex.buggyweatherapp.model.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location>
    suspend fun getCityNameFromLocation(location: Location): Result<String?>
    fun startLocationUpdates()
    fun stopLocationUpdates()
}
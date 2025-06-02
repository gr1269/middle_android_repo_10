package ru.yandex.buggyweatherapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.ui.components.ErrorView
import ru.yandex.buggyweatherapp.ui.components.LoadingView
import ru.yandex.buggyweatherapp.ui.components.LocationSearch
import ru.yandex.buggyweatherapp.ui.components.WeatherCard
import ru.yandex.buggyweatherapp.utils.UiState
import ru.yandex.buggyweatherapp.utils.WeatherIconMapper
import ru.yandex.buggyweatherapp.viewmodel.WeatherViewModel

/**
 * Главный экран приложения, отображающий данные о погоде.
 */

/**
 * Composable-функция для отображения экрана погоды.
 * Отвечает за координацию отображения поиска и данных о погоде.
 *
 * @param modifier Модификатор для настройки внешнего вида
 * @param viewModel ViewModel для получения данных о погоде
 */
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    // Получаем состояния из ViewModel через StateFlow
    val weatherUiState by viewModel.weatherUiState.collectAsState()
    val cityName by viewModel.cityNameState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Поиск по городу
        LocationSearch(
            onCitySearch = { city ->
                viewModel.searchWeatherByCity(city)
            },
            onLocationRequest = {
                viewModel.fetchCurrentLocationWeather()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение соответствующего контента в зависимости от состояния
        when (val state = weatherUiState) {
            is UiState.Loading -> {
                LoadingView()
            }

            is UiState.Success -> {
                WeatherCard(
                    weatherData = state.data,
                    cityName = cityName
                )
            }

            is UiState.Error -> {
                ErrorView(
                    message = state.message,
                    onRetry = { viewModel.fetchCurrentLocationWeather() }
                )
            }
        }
    }
}

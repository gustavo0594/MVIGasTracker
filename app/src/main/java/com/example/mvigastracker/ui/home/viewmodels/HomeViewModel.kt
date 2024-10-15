package com.example.mvigastracker.ui.home.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.mvigastracker.domain.usecases.GetHomeDataUseCase
import com.example.mvigastracker.domain.usecases.GetHomeDataUseCaseInput
import com.example.mvigastracker.ui.base.BaseViewModel
import com.example.mvigastracker.ui.home.contracts.HomeContract
import com.example.mvigastracker.ui.home.mappers.toViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetHomeDataUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    private var yearSelected = Calendar.getInstance().get(Calendar.YEAR)

    init {
        fetchData(yearSelected, false)
    }

    override fun setInitialState() = HomeContract.State(
        homeViewState = null,
        yearSelected = yearSelected,
        isLoading = true,
        isError = false
    )

    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.Retry -> fetchData(event.yearSelected, true)
            is HomeContract.Event.OnYearSelected -> fetchData(event.yearSelected, false)
            HomeContract.Event.OnAddNewRecord -> setEffect { HomeContract.Effect.Navigation.ToAddNewRecord }
            HomeContract.Event.OnListMenuClicked -> setEffect { HomeContract.Effect.Navigation.ToRecordList }
        }
    }


    private fun fetchData(year: Int, retry: Boolean) {
        viewModelScope.launch {
            yearSelected = year
            setState {
                copy(
                    yearSelected = year,
                    isLoading = true,
                    isError = false
                )
            }
            useCase(GetHomeDataUseCaseInput(yearSelected.toString()))
                .flowOn(Dispatchers.IO)
                .onStart { delay(1000) }
                .onEach {
                    if (!retry) {
                        throw Exception()
                    }
                }
                .catch {
                    setState {
                        copy(
                            yearSelected = year,
                            isLoading = false,
                            isError = true
                        )
                    }
                }
                .collect { data ->
                    val viewState = if (data.monthlyRecordsByYear.isEmpty()) {
                        null
                    } else {
                        data.toViewState()
                    }
                    setState {
                        copy(
                            homeViewState = viewState,
                            yearSelected = year,
                            isLoading = false,
                            isError = false
                        )
                    }
                }
        }
    }
}

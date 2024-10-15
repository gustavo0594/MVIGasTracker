package com.example.mvigastracker.ui.records.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.mvigastracker.core.dateFormat
import com.example.mvigastracker.core.currencyFormat
import com.example.mvigastracker.core.distanceFormat
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.repositories.GasRecordRepository
import com.example.mvigastracker.ui.base.BaseViewModel
import com.example.mvigastracker.ui.records.contracts.RecordListContract
import com.example.mvigastracker.ui.records.contracts.RecordListContract.Event.FetchRecords
import com.example.mvigastracker.ui.records.contracts.RecordListContract.Event.OnAddNewRecord
import com.example.mvigastracker.ui.records.contracts.RecordListContract.Event.OnBackEvent
import com.example.mvigastracker.ui.records.contracts.RecordListContract.Event.OnItemClicked
import com.example.mvigastracker.ui.records.entities.UIFuelRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val gasRecordRepository: GasRecordRepository
) : BaseViewModel<RecordListContract.Event, RecordListContract.State, RecordListContract.Effect>() {

    init {
        getRecordList(false)
    }


    override fun setInitialState(): RecordListContract.State =
        RecordListContract.State(
            records = emptyList(),
            isLoading = true,
            isError = false
        )

    override fun handleEvents(event: RecordListContract.Event) {
        when (event) {
            FetchRecords -> getRecordList(false)
            RecordListContract.Event.Retry -> getRecordList(true)
            OnAddNewRecord -> setEffect { RecordListContract.Effect.Navigation.ToAddNewRecord }
            OnBackEvent -> setEffect { RecordListContract.Effect.Navigation.NavigateBack }
            is OnItemClicked -> setEffect { RecordListContract.Effect.Navigation.ToEditRecord(event.item.id) }
        }
    }

    private fun getRecordList(retry: Boolean) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            gasRecordRepository.getAll()
                .flowOn(Dispatchers.IO)
                .onStart { delay(1000) }
                .map { records -> records.toUIRecords() }
                .onEach {
                    if (!retry) {
                        throw Exception()
                    }
                }
                .catch {
                    setState {
                        copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
                .collect { items ->
                    setState {
                        copy(
                            isError = false,
                            records = items,
                            isEmpty = items.isEmpty(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun List<GasRecord>.toUIRecords(): List<UIFuelRecord> =
        this.map {
            UIFuelRecord(
                id = it.id,
                date = it.date.dateFormat(),
                distance = it.kilometers.distanceFormat(),
                totalCost = it.totalPayment.currencyFormat()
            )
        }
}
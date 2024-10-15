package com.example.mvigastracker.ui.fuelrecord.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.repositories.GasRecordRepository
import com.example.mvigastracker.ui.base.BaseViewModel
import com.example.mvigastracker.ui.fuelrecord.contracts.FuelRecordContract
import com.example.mvigastracker.ui.fuelrecord.contracts.UIField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FuelRecordViewModel @Inject constructor(
    private val gasRecordRepository: GasRecordRepository,
) : BaseViewModel<FuelRecordContract.Event, FuelRecordContract.State, FuelRecordContract.Effect>() {

    override fun setInitialState() = FuelRecordContract.State()


    override fun handleEvents(event: FuelRecordContract.Event) {
        when (event) {
            is FuelRecordContract.Event.FetchRecord -> fetchRecord(event.recordId)
            is FuelRecordContract.Event.OnValueUpdated -> onValueUpdated(event)
            FuelRecordContract.Event.OnSaveAction -> saveRecord()
        }
    }

    private fun onValueUpdated(onValueUpdated: FuelRecordContract.Event.OnValueUpdated) {
        when (onValueUpdated.field) {
            UIField.Date -> {
                setState { copy(date = onValueUpdated.value) }
            }

            UIField.Payment -> {
                setState { copy(totalAmount = onValueUpdated.value) }
            }

            UIField.Distance -> {
                setState { copy(kilometers = onValueUpdated.value) }
            }
        }
    }

    private fun fetchRecord(recordId: Int) {
        if (recordId < 1) {
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gasRecordRepository.getById(recordId)
            }?.let {
                setState {
                    copy(
                        id = it.id,
                        date = it.date,
                        kilometers = it.kilometers.toString(),
                        totalAmount = it.totalPayment.toString()
                    )
                }
            }
        }
    }


    private fun saveRecord() {
        viewModelScope.launch {
            val fuelRecord = viewState.value.let {
                GasRecord(
                    id = it.id,
                    date = it.date,
                    kilometers = it.kilometers.toInt(),
                    totalPayment = it.totalAmount.toInt()
                )
            }
            if (fuelRecord.id != 0) {
                gasRecordRepository.updateRecord(fuelRecord)
            } else {
                gasRecordRepository.insertNewRecord(fuelRecord)
            }
            setEffect { FuelRecordContract.Effect.Navigation.NavigateBack }
        }
    }


}
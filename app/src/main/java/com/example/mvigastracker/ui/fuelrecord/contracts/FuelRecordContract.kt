package com.example.mvigastracker.ui.fuelrecord.contracts

import com.example.mvigastracker.ui.base.ViewEvent
import com.example.mvigastracker.ui.base.ViewSideEffect
import com.example.mvigastracker.ui.base.ViewState

class FuelRecordContract {

    sealed class Event : ViewEvent {
        data class FetchRecord(val recordId: Int) : Event()
        data class OnValueUpdated(val value: String, val field: UIField) : Event()
        data object OnSaveAction : Event()
    }

    data class State(
        val id: Int = 0,
        val date: String = "",
        val kilometers: String = "",
        val isKmFieldValid: Boolean = true,
        val totalAmount: String = "",
        val isAmountValid: Boolean = true,
        val isSaveButtonEnabled: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data object NavigateBack : Navigation()
        }
    }


}

enum class UIField {
    Date, Payment, Distance
}
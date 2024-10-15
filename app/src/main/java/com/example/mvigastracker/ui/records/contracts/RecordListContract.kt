package com.example.mvigastracker.ui.records.contracts

import com.example.mvigastracker.ui.base.ViewEvent
import com.example.mvigastracker.ui.base.ViewSideEffect
import com.example.mvigastracker.ui.base.ViewState
import com.example.mvigastracker.ui.records.entities.UIFuelRecord

class RecordListContract {

    sealed class Event : ViewEvent {
        data object Retry : Event()
        data object FetchRecords : Event()
        data object OnBackEvent : Event()
        data object OnAddNewRecord : Event()
        data class OnItemClicked(val item: UIFuelRecord) : Event()
    }

    data class State(
        val records: List<UIFuelRecord> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isEmpty: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data object NavigateBack : Navigation()
            data object ToAddNewRecord : Navigation()
            data class ToEditRecord(val recordId: Int) : Navigation()
        }
    }
}
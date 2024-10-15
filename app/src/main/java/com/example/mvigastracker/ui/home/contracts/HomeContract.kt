package com.example.mvigastracker.ui.home.contracts

import com.example.mvigastracker.ui.base.ViewEvent
import com.example.mvigastracker.ui.base.ViewSideEffect
import com.example.mvigastracker.ui.base.ViewState
import com.example.mvigastracker.ui.home.entities.HomeViewState

class HomeContract {

    sealed class Event : ViewEvent {
        data class Retry(val yearSelected: Int) : Event()
        data class OnYearSelected(val yearSelected: Int) : Event()
        data object OnAddNewRecord : Event()
        data object OnListMenuClicked : Event()
    }

    data class State(
        val homeViewState: HomeViewState?,
        val yearSelected: Int,
        val isLoading: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data object ToAddNewRecord : Navigation()
            data object ToRecordList : Navigation()
        }
    }
}
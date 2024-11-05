package com.example.mvigastracker.ui.records.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.mvigastracker.core.dateFormat
import com.example.mvigastracker.core.currencyFormat
import com.example.mvigastracker.core.distanceFormat
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.repositories.GasRecordRepository
import com.example.mvigastracker.ui.base.BaseViewModel
import com.example.mvigastracker.ui.home.contracts.HomeContract
import com.example.mvigastracker.ui.records.contracts.RecordListContract
import com.example.mvigastracker.ui.records.contracts.RecordListContract.Event.FetchRecords
import com.example.mvigastracker.ui.records.contracts.RecordListContract.Event.Retry
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

//step5 implement ViewModel





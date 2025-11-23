package com.souvik.timelock.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvik.timelock.data.repository.AppLimitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetLimitViewModel @Inject constructor(
    private val repo: AppLimitsRepository
) : ViewModel() {

    private val _minutes = MutableStateFlow("")
    val minutes: StateFlow<String> = _minutes

    fun onMinutesChange(value: String) {
        _minutes.value = value
    }

    fun saveLimit(packageName: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            val min = _minutes.value.toLongOrNull() ?: return@launch
            repo.setLimit(packageName, min)
            onSaved()
        }
    }
}

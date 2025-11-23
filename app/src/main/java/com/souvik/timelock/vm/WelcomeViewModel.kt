package com.souvik.timelock.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvik.timelock.data.repository.AppLimitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repo: AppLimitsRepository
) : ViewModel() {

    private val _onboarded = MutableStateFlow(false)
    val onboarded: StateFlow<Boolean> = _onboarded

    init {
        viewModelScope.launch {
            _onboarded.value = repo.onboardedFlow().first()
        }
    }

    fun setOnboarded() {
        viewModelScope.launch {
            repo.markOnboarded()
            _onboarded.value = true
        }
    }
}

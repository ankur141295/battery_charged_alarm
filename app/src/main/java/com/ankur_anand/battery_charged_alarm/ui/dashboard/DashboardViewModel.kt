package com.ankur_anand.battery_charged_alarm.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankur_anand.battery_charged_alarm.utils.pref_datastore.PrefDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val prefDataStore: PrefDataStore
) : ViewModel() {

    private val _isPercentageTextFieldError = mutableStateOf(false)
    val isPercentageTextFieldError: State<Boolean> = _isPercentageTextFieldError

    private val _percentageText = mutableStateOf(TextFieldValue(""))
    val percentageText: State<TextFieldValue> = _percentageText

    private val _showSuccessToast = mutableStateOf(false)
    val showSuccessToast: State<Boolean> = _showSuccessToast

    init {
        getPercentageFromPrefDataStore()
    }

    private fun getPercentageFromPrefDataStore() {
        viewModelScope.launch {
            val savedPercentage = prefDataStore.getSavedPercentage()
            _percentageText.value = TextFieldValue(savedPercentage)
        }
    }

    private fun setPercentage(textFieldValue: TextFieldValue) {
        _percentageText.value = textFieldValue.copy(
            text = textFieldValue.text.trim()
        )
    }

    fun savePercentage() {
        if (isValidText(_percentageText.value)) {
            viewModelScope.launch {
                prefDataStore.savePercentage(_percentageText.value.text.trim())
            }
            resetToastValue(true)
            setPercentageTextFieldError(error = false)
        } else {
            setPercentageTextFieldError(error = true)
        }
    }

    private fun setPercentageTextFieldError(error: Boolean) {
        _isPercentageTextFieldError.value = error
    }

    fun setTextAndValidate(textFieldValue: TextFieldValue) {
        setPercentage(textFieldValue)
        setPercentageTextFieldError(error = !isValidText(textFieldValue = textFieldValue))
    }

    private fun isValidText(textFieldValue: TextFieldValue): Boolean {
        val text = textFieldValue.text

        return (text.isNotBlank() && text.isDigitsOnly() && text.toInt() > 0 && text.toInt() <= 100)
    }

    fun resetToastValue(value: Boolean) {
        _showSuccessToast.value = value
    }
}
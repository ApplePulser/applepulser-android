package com.shinythinking.applepulser_android.presentation.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shinythinking.applepulser_android.domain.model.PlayerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val json: Json
) : ViewModel(), ContainerHost<RecordContract.State, RecordContract.SideEffect> {
    private val resultJson: String = checkNotNull(savedStateHandle["resultJson"])

    override val container = container<RecordContract.State, RecordContract.SideEffect>(
        RecordContract.State()
    ) {
        parseData()
    }

    private fun parseData() = intent {
        try {
            val results = json.decodeFromString<List<PlayerResult>>(resultJson)
            val sortedResults = results.sortedBy { it.rank }

            reduce { state.copy(results = sortedResults) }
        } catch (e: Exception) {
        }
    }

    fun onIntent(intent: RecordContract.Intent) = when (intent) {
        is RecordContract.Intent.OnHomeClicked -> handleHomeClicked()
    }

    private fun handleHomeClicked() = intent {
        postSideEffect(RecordContract.SideEffect.NavigateToHome)
    }
}
package com.shinythinking.applepulser_android.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.shinythinking.applepulser_android.domain.model.PlayerResult
import com.shinythinking.applepulser_android.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GameResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val json: Json
) : ViewModel(), ContainerHost<GameResultContract.State, GameResultContract.SideEffect> {
    private val args = savedStateHandle.toRoute<Route.GameResult>()
    private val resultJson: String = args.resultJson
    private val myPlayerId: String = args.playerId
    private val roomId: String = args.roomId

    override val container = container<GameResultContract.State, GameResultContract.SideEffect>(
        GameResultContract.State(myPlayerId = myPlayerId)
    ) {
        parseResults()
    }

    private fun parseResults() = intent {
        try {
            val results = json.decodeFromString<List<PlayerResult>>(resultJson)

            val sortedResults = results.sortedBy { it.rank }

            reduce {
                state.copy(rankings = sortedResults)
            }
        } catch (e: Exception) {
        }
    }

    fun onIntent(intent: GameResultContract.Intent) = when (intent) {
        is GameResultContract.Intent.OnRecordClicked -> handleRecord()
        is GameResultContract.Intent.OnFinishClicked -> handleFinish()
    }

    private fun handleRecord() = intent {
        postSideEffect(
            GameResultContract.SideEffect.NavigateToRecord(
                playerId = myPlayerId,
                roomId = roomId,
                resultJson = resultJson
            )
        )
    }

    private fun handleFinish() = intent {
        postSideEffect(GameResultContract.SideEffect.NavigateToHome)
    }
}
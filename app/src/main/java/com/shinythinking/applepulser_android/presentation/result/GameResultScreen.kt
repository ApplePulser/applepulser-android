package com.shinythinking.applepulser_android.presentation.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerResult
import com.shinythinking.applepulser_android.domain.model.PlayerType.GREEN
import com.shinythinking.applepulser_android.domain.model.PlayerType.RED
import com.shinythinking.applepulser_android.domain.model.PlayerType.WHITE
import com.shinythinking.applepulser_android.domain.model.PlayerType.YELLOW
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GameResultScreen(
    onNavigateToRecord: (String, String, String) -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameResultViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    BackHandler {
        viewModel.onIntent(GameResultContract.Intent.OnFinishClicked)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GameResultContract.SideEffect.NavigateToRecord -> {
                onNavigateToRecord(
                    sideEffect.playerId,
                    sideEffect.roomId,
                    sideEffect.resultJson
                )
            }

            is GameResultContract.SideEffect.NavigateToHome -> {
                onNavigateToHome()
            }
        }
    }

    GameResultContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun GameResultContent(
    state: GameResultContract.State,
    onIntent: (GameResultContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    APBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Result",
                fontSize = 60.sp,
                fontWeight = FontWeight.Thin,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(40.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(state.rankings) { result ->
                    RankItem(
                        rank = result.rank,
                        nickname = result.name,
                        isMe = result.playerId == state.myPlayerId
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onIntent(GameResultContract.Intent.OnRecordClicked) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D4037).copy(alpha = 0.8f)
                    )
                ) {
                    Text(
                        text = "Record",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Button(
                    onClick = { onIntent(GameResultContract.Intent.OnFinishClicked) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3E2723)
                    )
                ) {
                    Text(
                        text = "Finish",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun RankItem(
    rank: Int,
    nickname: String,
    isMe: Boolean
) {
    val containerColor = if (isMe) Color(0xFF3E2723) else Color.Transparent
    val borderColor = if (isMe) Color.Transparent else MaterialTheme.colorScheme.onPrimary
    val contentColor = MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(containerColor, RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = contentColor
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .background(containerColor, RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nickname,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun GameResultScreenPreview() {
    val player1 = Player(
        id = "p1",
        name = "홍사인",
        colorType = RED
    )

    val player2 = Player(
        id = "p2",
        name = "한예준",
        colorType = YELLOW
    )

    val player3 = Player(
        id = "p3",
        name = "신바다",
        colorType = GREEN
    )

    val player4 = Player(
        id = "p4",
        name = "김나경",
        colorType = WHITE
    )

    val dummyRankings = listOf(
        PlayerResult(
            rank = 1,
            averageBpm = 145.0,
            maxBpm = 170,
            minBpm = 90,
            playerId = "1",
            name = "홍사인"
        ),
        PlayerResult(
            rank = 2,
            averageBpm = 145.0,
            maxBpm = 170,
            minBpm = 90,
            playerId = "1",
            name = "홍사인"
        ),
        PlayerResult(
            rank = 3,
            averageBpm = 145.0,
            maxBpm = 170,
            minBpm = 90,
            playerId = "1",
            name = "홍사인"
        ),
    )

    ApplepulserTheme {
        GameResultContent(
            state = GameResultContract.State(
                rankings = dummyRankings,
                myPlayerId = "p3"
            ),
            onIntent = {}
        )
    }
}
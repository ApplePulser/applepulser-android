package com.shinythinking.applepulser_android.presentation.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerResult
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.PlayerType
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APLongButton
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.Locale

@Composable
fun RecordScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    BackHandler {
        viewModel.onIntent(RecordContract.Intent.OnHomeClicked)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RecordContract.SideEffect.NavigateToHome -> onNavigateToHome()
        }
    }

    RecordContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun RecordContent(
    state: RecordContract.State,
    onIntent: (RecordContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    APBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detailed Analysis",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.results) { result ->
                    PlayerRecordCard(result = result)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            APLongButton(
                onClick = { onIntent(RecordContract.Intent.OnHomeClicked) },
                text = "Go Home",
            )
        }
    }
}

@Composable
fun PlayerRecordCard(
    result: PlayerResult
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f) // 반투명 검정 배경
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${result.rank}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = result.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
//            Divider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
//                    StatItem(
//                        icon = Icons.Default.Favorite,
//                        label = "Duration",
//                        value = formatSeconds(result.workoutTime),
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    StatItem(
                        icon = Icons.Default.Favorite,
                        label = "Avg BPM",
                        value = "${result.averageBpm} bpm",
                        tint = Color(0xFFFF5252)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    StatItem(
                        icon = Icons.Default.Build,
                        label = "Peak / Min",
                        value = "${result.maxBpm} / ${result.minBpm}",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format(Locale.US, "%02d:%02d", m, s)
}

fun getPlayerColor(type: PlayerType): Color {
    return when (type) {
        PlayerType.RED -> Color(0xFFFF5252)
        PlayerType.YELLOW -> Color(0xFFFFD740)
        PlayerType.GREEN -> Color(0xFF69F0AE)
        PlayerType.WHITE -> Color(0xFFE0E0E0)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun RecordScreenPreview() {
    val player1 = Player(
        id = "p1",
        name = "홍사인",
        colorType = PlayerType.RED,
        status = PlayerStatus.FINISHED
    )

    val player2 = Player(
        id = "p2",
        name = "한예준",
        colorType = PlayerType.YELLOW,
        status = PlayerStatus.FINISHED
    )

    val player3 = Player(
        id = "p3",
        name = "신바다",
        colorType = PlayerType.GREEN,
        status = PlayerStatus.FINISHED
    )

    val dummyResults = listOf(
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
        RecordContent(
            state = RecordContract.State(
                results = dummyResults
            ),
            onIntent = {}
        )
    }
}
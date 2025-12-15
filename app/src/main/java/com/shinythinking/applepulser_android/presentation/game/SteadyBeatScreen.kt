package com.shinythinking.applepulser_android.presentation.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.domain.model.GameStatus
import com.shinythinking.applepulser_android.domain.model.Limit
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.PlayerType
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APShortButton
import com.shinythinking.applepulser_android.presentation.base.component.AppleCharacter
import com.shinythinking.applepulser_android.presentation.base.component.ErrorMessage
import com.shinythinking.applepulser_android.presentation.base.component.LoadingIndicator
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.math.roundToInt

@Composable
fun SteadyBeatScreen(
    onNavigateToResult: (String, String, String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SteadyBeatViewmodel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SteadyBeatContract.SideEffect.NavigateToResult -> {
                onNavigateToResult(
                    sideEffect.roomId,
                    sideEffect.myPlayerId,
                    sideEffect.resultJson
                )
            }
            is SteadyBeatContract.SideEffect.NavigateBack -> onNavigateBack()

            is SteadyBeatContract.SideEffect.ShowToast -> {
            }

            is SteadyBeatContract.SideEffect.ShowQuitDialog -> {
            }
        }
    }

    SteadyBeatContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun SteadyBeatContent(
    state: SteadyBeatContract.State,
    onIntent: (SteadyBeatContract.Intent) -> Unit
) {
    APBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is SteadyBeatContract.State.Connecting -> {
                    ConnectingContent()
                }

                is SteadyBeatContract.State.Playing -> {
                    PlayingContent(
                        state = state,
                        onIntent = onIntent
                    )
                }

                is SteadyBeatContract.State.Error -> {
                    ErrorContent(
                        message = state.message,
                        canRetry = state.canRetry,
                        onRetry = { onIntent(SteadyBeatContract.Intent.RetryConnection) },
                    )
                }

                SteadyBeatContract.State.Finished -> {
                    LoadingIndicator(message = "Good Job!")
                }
            }
        }
    }
}

@Composable
private fun ConnectingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(message = "Connecting to game...")
    }
}

@Composable
private fun PlayingContent(
    state: SteadyBeatContract.State.Playing,
    onIntent: (SteadyBeatContract.Intent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        CurrentStatusSection(
            heartRate = state.currentHeartRate,
            currentRank = state.currentRank,
            deviation = state.deviationFromTarget,
            elapsedTime = state.elapsedTimeFormatted,
            totalTime = state.totalTimeFormatted
        )

        Spacer(modifier = Modifier.height(24.dp))

        GameBox(
            players = state.players,
            limit = state.gameStatus.limit,
            modifier = Modifier.weight(1f),
        )
    }

    if (state.isPaused) {
        PauseOverlay(
            onResume = { onIntent(SteadyBeatContract.Intent.ResumeClicked) },
            onQuit = { onIntent(SteadyBeatContract.Intent.QuitClicked) }
        )
    }
}

@Composable
private fun CurrentStatusSection(
    heartRate: Int,
    currentRank: Int,
    deviation: Int,
    elapsedTime: String,
    totalTime: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Current Status",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = heartRate.toString(),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoCard(
                title = "Current Rank",
                value = currentRank.toString(),
                modifier = Modifier.weight(1f)
            )

            InfoCard(
                title = "Deviation",
                value = if (deviation >= 0) "+$deviation" else deviation.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$elapsedTime / $totalTime",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun GameBox(
    players: List<Player>,
    limit: Limit,
    modifier: Modifier = Modifier,
) {
    val gameBox = ImageBitmap.imageResource(R.drawable.ic_game_box)
    val infiniteTransition = rememberInfiniteTransition(label = "gameBox")
    val offsetXRatio by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetX"
    )
    BoxWithConstraints(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {
        val width = maxWidth
        val height = maxHeight
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp + 16.dp
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .requiredWidth(screenWidthDp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val imageWidth = gameBox.width.toFloat()
            val imageHeight = gameBox.height.toFloat()

            val scale = canvasWidth / imageWidth
            val scrollOffset = imageWidth * offsetXRatio
            val scaleFactor = canvasWidth / imageWidth
            val scaleFactorHeight = canvasHeight / imageHeight
            val scaledWidth = imageWidth * scaleFactor
            val scaledHeight = imageHeight * scaleFactorHeight
            val dstSize = IntSize(scaledWidth.roundToInt(), scaledHeight.roundToInt())

            val offsetY = (canvasHeight - scaledHeight) / 2
            val overlapFix = 3f

            val imgW = gameBox.width
            val imgH = gameBox.height

            val trimHorizontal = 4
            val srcOffset = IntOffset(trimHorizontal, 0)
            val srcSize = IntSize(imgW - (trimHorizontal * 2), imgH)

            translate(left = -scrollOffset, top = offsetY) {
                drawImage(
                    image = gameBox,
                    srcOffset = srcOffset,
                    srcSize = srcSize,
                    dstSize = dstSize
                )
            }
            translate(left = -scrollOffset + scaledWidth - overlapFix, top = offsetY) {
                drawImage(
                    image = gameBox,
                    srcOffset = srcOffset,
                    srcSize = srcSize,
                    dstSize = dstSize
                )
            }
        }
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(players) { player ->
                PlayerTrack(
                    player = player,
                    limit = limit
                )
            }

        }
    }
}

@Composable
private fun PlayerTrack(
    player: Player,
    limit: Limit,
    modifier: Modifier = Modifier
) {
    val totalDif = limit.max - limit.min
    val playerDif = (player.bpm ?: 120) - limit.min
    val playerPercentage = when (playerDif.toFloat() / totalDif.toFloat()) {
        in 0.01f..0.99f -> playerDif.toFloat() / totalDif.toFloat()
        in 0.99f..Float.MAX_VALUE -> 0.99f
        else -> 0.01f
    }
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(Modifier.weight(1 - playerPercentage))
        AppleCharacter(
            player = player,
            showHeartRate = true,
        )
        Spacer(Modifier.weight(playerPercentage))
    }
}

@Composable
private fun PauseOverlay(
    onResume: () -> Unit,
    onQuit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "PAUSED",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            APShortButton(
                text = "Resume",
                onClick = onResume,
                modifier = Modifier.width(200.dp)
            )

            APShortButton(
                text = "Quit",
                onClick = onQuit,
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit,
) {
    ApplepulserTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ErrorMessage(
                message = message,
                onRetry = if (canRetry) onRetry else null
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SteadyBeatConnectingPreview() {
    SteadyBeatContent(
        state = SteadyBeatContract.State.Connecting,
        onIntent = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SteadyBeatPlayingPreview() {
    val mockState = SteadyBeatContract.State.Playing(
        gameStatus = GameStatus(
            currentHeartRate = 145,
            currentRank = 3,
            deviationFromTarget = 5,
            elapsedTime = 120,
            totalTime = 300,
            players = listOf(
                Player(
                    id = "1",
                    name = "홍사인",
                    status = PlayerStatus.PLAYING,
                    isHost = true,
                    colorType = PlayerType.YELLOW,
                    bpm = 145
                ),
                Player(
                    id = "2",
                    name = "한예준",
                    status = PlayerStatus.PLAYING,
                    isHost = true,
                    colorType = PlayerType.RED,
                    bpm = 160
                ),
                Player(
                    id = "3",
                    name = "신바다",
                    status = PlayerStatus.PLAYING,
                    isHost = true,
                    colorType = PlayerType.WHITE,
                    bpm = 130
                ),
                Player(
                    id = "4",
                    name = "김나경",
                    status = PlayerStatus.PLAYING,
                    isHost = true,
                    colorType = PlayerType.GREEN,
                    bpm = 120
                ),
            ),
            limit = Limit(120, 160)
        )
    )

    SteadyBeatContent(
        state = mockState,
        onIntent = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SteadyBeatPausedPreview() {
    val mockState = SteadyBeatContract.State.Playing(
        gameStatus = GameStatus(
            currentHeartRate = 145,
            currentRank = 3,
            deviationFromTarget = 5,
            elapsedTime = 120,
            totalTime = 300,
            players = listOf(
                Player(
                    id = "1",
                    name = "홍사인",
                    status = PlayerStatus.PLAYING,
                    isHost = true,
                    colorType = PlayerType.YELLOW,
                    bpm = 145
                ),
                Player(
                    id = "2",
                    name = "한예준",
                    status = PlayerStatus.PLAYING,
                    isHost = true,
                    colorType = PlayerType.RED,
                    bpm = 120
                ),
            ),
            limit = Limit(120, 140)
        ),
        isPaused = true
    )

    ApplepulserTheme {
        SteadyBeatContent(
            state = mockState,
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SteadyBeatErrorPreview() {
    SteadyBeatContent(
        state = SteadyBeatContract.State.Error("Connection lost"),
        onIntent = {}
    )
}

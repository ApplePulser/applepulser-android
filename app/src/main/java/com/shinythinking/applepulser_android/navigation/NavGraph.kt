package com.shinythinking.applepulser_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shinythinking.applepulser_android.presentation.client.clientRoom.ClientRoomScreen
import com.shinythinking.applepulser_android.presentation.client.enterByCode.CodeInputScreen
import com.shinythinking.applepulser_android.presentation.game.SteadyBeatScreen
import com.shinythinking.applepulser_android.presentation.home.HomeScreen
import com.shinythinking.applepulser_android.presentation.host.gameSetting.GameSettingScreen
import com.shinythinking.applepulser_android.presentation.host.hostRoom.HostRoomScreen
import com.shinythinking.applepulser_android.presentation.launch.LaunchRoomScreen
import com.shinythinking.applepulser_android.presentation.record.RecordScreen
import com.shinythinking.applepulser_android.presentation.result.GameResultScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
    ) {
        composable<Route.Home> {
            HomeScreen(
                onNavigateToLaunchRoom = {
                    navController.navigate(Route.LaunchRoom)
                },
                onNavigateToEnterRoom = {
                    navController.navigate(Route.EnterRoomByCode)
                }
            )
        }

        composable<Route.LaunchRoom> {
            LaunchRoomScreen(
                onNavigateToHostRoom = { playerId, roomId ->
                    navController.navigate(
                        Route.HostRoom(
                            playerId = playerId,
                            roomId = roomId
                        )
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.HostRoom> {
            HostRoomScreen(
                onNavigateToGameSetting = { playerId, roomId ->
                    navController.navigate(
                        Route.GameSettings(
                            playerId = playerId,
                            roomId = roomId
                        )
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.GameSettings> {
            GameSettingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToGamePlay = { playerId, roomId, settingJson, deviceAddress ->
                    navController.navigate(
                        Route.SteadyBeatGame(
                            playerId = playerId,
                            roomId = roomId,
                            deviceAddress = deviceAddress, // todo,
                            settingJson = settingJson
                        )
                    )
                },
            )
        }

        composable<Route.EnterRoomByCode> {
            CodeInputScreen(
                onNavigateToRoom = { roomId, playerId ->
                    navController.navigate(Route.ClientRoom(roomId = roomId, playerId = playerId))
                }
            )
        }

        composable<Route.ClientRoom> {
            ClientRoomScreen(
                onNavigateToGame = { roomId, playerId, gameSettingJson ->
                    navController.navigate(
                        Route.SteadyBeatGame(
                            playerId = playerId,
                            roomId = roomId,
                            deviceAddress = "", // todo
                            settingJson = gameSettingJson
                        )
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.SteadyBeatGame> {
            SteadyBeatScreen(
                onNavigateToResult = { roomId, playerId, resultJson ->
                    navController.navigate(
                        Route.GameResult(
                            roomId = roomId,
                            playerId = playerId,
                            resultJson = resultJson
                        )
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.GameResult> {
            GameResultScreen(
                onNavigateToRecord = { playerId, roomId, resultJson ->
                    navController.navigate(
                        Route.Record(
                            playerId = playerId,
                            roomId = roomId,
                            resultJson = resultJson
                        )
                    )
                },
                onNavigateToHome = {
                    navController.navigate(Route.Home)
                }
            )
        }

        composable<Route.Record> {
            RecordScreen(
                onNavigateToHome = {
                    navController.navigate(Route.Home) {
                        navController.navigateAndClearBackStack(Route.Home)
                    }
                }
            )
        }
    }
}
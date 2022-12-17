package com.pvsb.composechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.pvsb.composechatapp.presentation.chat.ChatScreen
import com.pvsb.composechatapp.presentation.username.UserNameScreen
import com.pvsb.composechatapp.ui.theme.ComposeChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "username_screen"
            ) {
                composable("username_screen") {
                    UserNameScreen(onNavigate = navController::navigate)
                }

                composable(
                    "chat_screen/{username}",
                    arguments = listOf(navArgument(name = "username") {
                        type = NavType.StringType
                        nullable = true
                    })
                ) {
                    val userName = it.arguments?.getString("username") ?: ""

                    ChatScreen(userName = userName)
                }

            }
        }
    }
}

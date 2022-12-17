package com.pvsb.composechatapp.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.pvsb.composechatapp.presentation.username.UserNameViewModel
import com.pvsb.composechatapp.ui.theme.Shapes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    userName: String,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }
    }

    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.initChat()

                }
                Lifecycle.Event.ON_STOP -> {
                    viewModel.disconnect()
                }
            }

        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.chatState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(state.messages) { message ->
                val isOwnMessage = message.userName == userName

                Box(
                    contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .width(200.dp)
                            .drawBehind {
                                val cornerRadius = 10.dp.toPx()
                                val triangleHeight = 20.dp.toPx()
                                val triangleWidth = 25.dp.toPx()

                                val trianglePth = Path().apply {
                                    if (isOwnMessage) {
                                        moveTo(size.width, size.height - cornerRadius)
                                        lineTo(size.width, size.height + triangleHeight)
                                        lineTo(size.width - triangleWidth, size.height - cornerRadius)
                                        close()
                                    } else {
                                        moveTo(0f, size.height - cornerRadius)
                                        lineTo(0f, size.height + triangleHeight)
                                        lineTo(triangleWidth, size.height - cornerRadius)
                                        close()
                                    }
                                }

                                drawPath(trianglePth, if (isOwnMessage) Color.Green else Color.DarkGray)
                            }
                            .background(
                                color = if (isOwnMessage) Color.Green else Color.DarkGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(text = userName, fontWeight = FontWeight.Bold)

                        Text(text = message.text, color = Color.White)

                        Text(text = message.time, color = Color.White, modifier = Modifier.align(Alignment.End))
                    }


                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(value = viewModel.messageText.value, onValueChange = viewModel::onMessageChanged, placeholder = {
                Text(text = "Enter a message", modifier = Modifier.weight(1f))
            })

            IconButton(onClick = viewModel::sendMessage) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}
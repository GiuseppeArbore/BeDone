package it.polito.BeeDone.profile.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.utils.CreateImage

@Composable
fun UserChatListPane(
    userChat: SnapshotStateList<UserChat>,
    userChatPane: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    if(userChat.size == 0) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No chat to display"
            )
        }
    }
    else {
        LazyColumn {
            items(userChat) { c ->
                val sender: User
                val receiver: User
                if (c.user1 == loggedUser) {
                    sender = c.user1
                    receiver = c.user2
                } else {
                    sender = c.user2
                    receiver = c.user1
                }
                Row(
                    modifier = Modifier
                        .clickable { userChatPane(receiver.userNickname) }
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CreateImage(
                            photo = receiver.userImage,
                            name = "${receiver.userFirstName} ${receiver.userLastName}",
                            size = if (screenWidth < screenHeight) 50 else 60
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(if (screenWidth < screenHeight) 6f else 10f)
                    ) {
                        Text(
                            text = receiver.userNickname,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "${c.messages.get(c.messages.lastIndex).date} - ${
                                c.messages.get(
                                    c.messages.lastIndex
                                ).time
                            }", color = Color.Gray
                        )

                        Text(
                            text = if (loggedUser == c.messages.get(c.messages.lastIndex).sender) {
                                "You: "
                            } else {
                                ""
                            } + c.messages.get(c.messages.lastIndex).message,
                            fontSize = 18.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                HorizontalDivider(color = Color.LightGray)
            }
        }
    }
}
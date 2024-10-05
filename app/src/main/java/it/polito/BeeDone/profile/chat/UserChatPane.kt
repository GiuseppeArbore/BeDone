package it.polito.BeeDone.profile.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.utils.CreateImage
import it.polito.BeeDone.utils.CreateTextFieldNoError
import it.polito.BeeDone.utils.lightBlue
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@Composable
fun UserChatPane(
    selectedUser: User,
    userMessageValue: String,
    setUserMessage: (String) -> Unit,
    showUserInformationPane: (String) -> Unit
) {
    //Check if a chat between the two users (loggedProfile and selectedProfile) already exists. If so, we take the details of this chat. Otherwise, we create a new instance of the class UserChat
    val c: UserChat
    if(loggedUser.userChat.map { it.user1 }.contains(selectedUser)) {
        c = loggedUser.userChat.find { it.user1 == selectedUser }!!
    }
    else {
        if(loggedUser.userChat.map { it.user2 }.contains(selectedUser)) {
            c = loggedUser.userChat.find { it.user2 == selectedUser }!!
        }
        else {
            c = UserChat(loggedUser, selectedUser, mutableStateListOf())
            loggedUser.userChat.add(c)
            selectedUser.userChat.add(c)
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val context = LocalContext.current

    Scaffold (
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            Color(
                                250, 250, 250
                            )
                        )   //Same color as the background
                        .fillMaxWidth()
                        .padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CreateTextFieldNoError(
                        value = userMessageValue,
                        setValue = setUserMessage,
                        label = "Write your message here.",
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth(0.85f),
                        maxLines = 3
                    )
                    IconButton(onClick = {
                        if (userMessageValue.isNotBlank()) {            //If the message is blank, nothing happens
                            c.messages.add(Message(userMessageValue, SimpleDateFormat("dd/MM/yyyy").format(Date()), SimpleDateFormat("hh:mm").format(Date()), loggedUser))
                            setUserMessage("")                          //Reset the message
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, "Send")
                    }
                }
            }
        }
    ) {innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding).padding(top = 16.dp)
        ) {

            items(c.messages) { m ->
                val linkPattern = Regex("(https?://[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?)")

                val annotatedString = buildAnnotatedString {
                    var lastIndex = 0
                    val matches = (linkPattern.findAll(m.message))

                    for (match in matches) {
                        append(m.message.substring(startIndex = lastIndex, endIndex = match.range.first))
                        val annotationTag = if (match.value.startsWith("@")) "USER_TAG" else "URL"

                        if (annotationTag == "URL") {
                            pushStringAnnotation(tag = annotationTag, annotation = match.value)
                            withStyle(style = SpanStyle(color = lightBlue, textDecoration = TextDecoration.Underline)) {
                                append(match.value)
                            }
                            pop()
                        } else {
                            append(match.value)
                        }
                        lastIndex = match.range.last + 1
                    }
                    append(m.message.substring(lastIndex))
                }

                //Show messages
                if (m.sender == loggedUser) {                                                 //Sent message: align it to the right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .widthIn(0.dp, (screenWidth * 0.7).dp)
                                .border(
                                    1.dp, Color.Gray, RoundedCornerShape(
                                        topStart = 47f,
                                        topEnd = 0f,
                                        bottomStart = 47f,
                                        bottomEnd = 47f
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                        ) {
                            ClickableText(
                                text = annotatedString,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp, textAlign = TextAlign.Left),
                                onClick = { offset ->
                                    annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                        .firstOrNull()?.let { annotation ->
                                            val urlIntent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(annotation.item)
                                            )
                                            context.startActivity(urlIntent)
                                        }
                                }
                            )
                            Text(
                                text = "${m.date} - ${m.time}" ,
                                modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                                textAlign = TextAlign.End,
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickable(onClick = {
                                    showUserInformationPane(loggedUser.userNickname)
                                })
                        ) {
                            CreateImage(
                                photo = loggedUser.userImage,
                                name = "${loggedUser.userFirstName} ${loggedUser.userLastName}",
                                size = 30
                            )
                        }
                    }
                } else {                                                                          //Received message: align it to the left
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier
                                .clickable(onClick = {
                                    showUserInformationPane(selectedUser.userNickname)
                                }),
                            horizontalAlignment = Alignment.End
                        ) {
                            CreateImage(
                                photo = selectedUser.userImage,
                                name = "${selectedUser.userFirstName} ${selectedUser.userLastName}",
                                size = 30
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .widthIn(0.dp, (screenWidth * 0.7).dp)
                                .background(
                                    Color(230, 230, 230), RoundedCornerShape(
                                        topStart = 0f,
                                        topEnd = 47f,
                                        bottomStart = 47f,
                                        bottomEnd = 47f
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                        ) {
                            ClickableText(
                                text = annotatedString,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp, textAlign = TextAlign.Left),
                                onClick = { offset ->
                                    annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                        .firstOrNull()?.let { annotation ->
                                            val urlIntent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(annotation.item)
                                            )
                                            context.startActivity(urlIntent)
                                        }
                                }
                            )
                            Text(
                                text = "${m.date} - ${m.time}" ,
                                modifier = Modifier.padding(top = 3.dp),
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
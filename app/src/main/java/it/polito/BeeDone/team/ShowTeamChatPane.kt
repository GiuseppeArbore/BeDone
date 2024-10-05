package it.polito.BeeDone.team

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.BeeDone.profile.chat.Message
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.utils.CreateImage
import it.polito.BeeDone.utils.CreateTextFieldNoError
import it.polito.BeeDone.utils.lightBlue

@Composable
fun ShowTeamChatPane(
    selectedTeam: Team,
    chat: MutableList<Message>,
    setChat: (String, Team) -> Unit,
    messageValue: String,
    setMessage: (String) -> Unit,
    showUserInformationPane: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(250, 250, 250))
                        .fillMaxWidth()
                ) {       //When we want to tag another user with @, we show a list of the team users to select from
                    if (messageValue.isNotEmpty() && messageValue.last() == '@') {
                        ShowTeamUserList(
                            messageValue = messageValue,
                            setMessage = setMessage,
                            selectedTeam = selectedTeam
                        )
                    }
                }

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
                        value = messageValue,
                        setValue = setMessage,
                        label = "Write your message here.",
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth(0.85f),
                        maxLines = 3
                    )
                    IconButton(onClick = {
                        if (messageValue.isNotBlank()) {            //If the message is blank, nothing happens
                            setChat(messageValue, selectedTeam)
                            setMessage("")                          //Reset the message
                            selectedTeam.teamUsers.forEach{ //When a user sends a message, I indicate that the other members of the team have a message to read
                                if(it.user != loggedUser) {
                                    for(i in 0..<it.user.userTeams.size) {
                                        if(it.user.userTeams[i].first == selectedTeam) {
                                            it.user.userTeams[i] = Pair(selectedTeam, true)
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, "Send")
                    }
                }
            }
        }
    ) {innerPadding ->
        for(i in 0..<loggedUser.userTeams.size) {           //When the user enters in the TeamChat, set messages as read
            if(loggedUser.userTeams[i].first == selectedTeam) {
                loggedUser.userTeams[i] = Pair(selectedTeam, false)
                break
            }
        }

        LazyColumn(
            Modifier.padding(innerPadding).padding(top = 10.dp)
        ) {

            items(chat) { m ->
                val linkPattern = Regex("(https?://[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?)")
                val userTagPattern = Regex("@\\S+")

                val annotatedString = buildAnnotatedString {
                    var lastIndex = 0
                    val matches = (linkPattern.findAll(m.message) + userTagPattern.findAll(m.message)).sortedBy { it.range.first }

                    for (match in matches) {
                        append(m.message.substring(startIndex = lastIndex, endIndex = match.range.first))
                        val annotationTag = if (match.value.startsWith("@")) "USER_TAG" else "URL"
                        val nicknames = selectedTeam.teamUsers.map { it.user }.map { it.userNickname }
                        val isExistingNickname = match.value.startsWith("@") && nicknames.contains(match.value.substring(0))

                        if (isExistingNickname || annotationTag == "URL") {
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
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
                                    annotatedString.getStringAnnotations(tag = "USER_TAG", start = offset, end = offset)
                                        .firstOrNull()?.let { annotation ->
                                            showUserInformationPane(annotation.item)
                                        }

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
                                modifier = Modifier
                                    .padding(top = 3.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.End,
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .widthIn(0.dp, 320.dp)
                                .clickable(onClick = {
                                    showUserInformationPane(loggedUser.userNickname)
                                }),
                                    horizontalAlignment = Alignment.Start

                        ) {
                            CreateImage(
                                photo = loggedUser.userImage,
                                name = "${loggedUser.userFirstName} ${loggedUser.userLastName}",
                                size = 30
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))

                    }
                } else {                                                                          //Received message: align it to the left
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier
                                .clickable(onClick = {
                                    showUserInformationPane(m.sender.userNickname)
                                }),
                            horizontalAlignment = Alignment.End
                        ) {
                            CreateImage(
                                photo = m.sender.userImage,
                                name = "${m.sender.userFirstName} ${m.sender.userLastName}",
                                size = 30
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            horizontalAlignment = Alignment.Start,
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
                            Text(
                                text = m.sender.userNickname,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.clickable(onClick = {
                                    showUserInformationPane(m.sender.userNickname)
                                })
                            )

                            ClickableText(
                                text = annotatedString,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp, textAlign = TextAlign.Left),
                                onClick = { offset ->
                                    annotatedString.getStringAnnotations(tag = "USER_TAG", start = offset, end = offset)
                                        .firstOrNull()?.let { annotation ->
                                            showUserInformationPane(annotation.item)
                                        }

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

@Composable
fun ShowTeamUserList (
    selectedTeam: Team,
    messageValue: String,
    setMessage: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 5.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
    ) {
        for (u in selectedTeam.teamUsers) {
            if(u.user != loggedUser) {
                Row (
                    Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CreateImage(
                        photo = u.user.userImage,
                        name = "${u.user.userFirstName} ${u.user.userLastName}",
                        size = 30
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = u.user.userNickname,
                        modifier = Modifier
                            .clickable {
                                setMessage("${messageValue}${u.user.userNickname.removePrefix("@")}")
                            }
                            .fillMaxWidth()
                        //.border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
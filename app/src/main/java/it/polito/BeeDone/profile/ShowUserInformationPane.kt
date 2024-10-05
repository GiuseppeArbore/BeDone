package it.polito.BeeDone.profile

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.polito.BeeDone.task.TaskStatus
import it.polito.BeeDone.team.Team
import it.polito.BeeDone.utils.CreateKPI
import it.polito.BeeDone.utils.CreateLastRowText
import it.polito.BeeDone.utils.CreateImage
import it.polito.BeeDone.utils.CreateRowText
import it.polito.BeeDone.utils.ShowCommonTeams
import it.polito.BeeDone.utils.lightBlue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.P)                         //Denotes that the annotated element should only be called on the given API level or higher. Needed for the DatePicker and the Profile Image
@Composable
fun ShowUserInformationPane(
    selectedUser: User,
    photo: Uri?,
    firstName: String,
    lastName: String,
    nickname: String,
    mail: String,
    location: String,
    description: String,
    birthDate: String,
    status: String,
    userChatPane: (String) -> Unit,
    userChatListPane: () -> Unit,
    showTeamDetailsPane: (String) -> Unit
) {
    BoxWithConstraints {
        val maxH = this.maxHeight
        if (this.maxHeight > this.maxWidth) {               //True if the screen is in portrait mode
            //VERTICAL
            Box {
                Column(
                    modifier = if(selectedUser != loggedUser) {
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, bottom = 70.dp)     //start and end padding is needed in order to leave 16dp from left and right borders. The chat button is 70.dp in size, so I set the same padding so that it won't overlap the profile information
                        }
                        else {
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                            .padding(horizontal = 16.dp)                            //Padding is needed in order to leave 16dp from left and right borders
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //Profile Picture
                    Row(
                        modifier = Modifier
                            .height(maxH / 3)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CreateImage(photo, "$firstName $lastName", 170)
                    }

                    Column(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                            .padding(end = 4.dp)
                    )
                    {
                        Spacer(modifier = Modifier.height(10.dp))
                        //Full Name
                        CreateRowText(
                            contentDescription = "Full Name",
                            text = "$firstName $lastName"
                        )

                        //Nickname
                        CreateRowText(contentDescription = "Nickname", text = nickname)

                        //Mail
                        CreateRowText(contentDescription = "Mail", text = mail)

                        //Location
                        CreateRowText(contentDescription = "Location", text = location)

                        //Description
                        CreateRowText(contentDescription = "Description", text = description)

                        //Birth Date
                        CreateRowText(contentDescription = "Birth Date", text = birthDate)

                        //Status
                        CreateLastRowText(contentDescription = "Status", text = status)
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if(loggedUser != selectedUser){
                        val commonTeams: MutableList<Pair<Team, Boolean>> = mutableListOf()
                        for(t in loggedUser.userTeams){
                            if(selectedUser.userTeams.contains(t)){
                                commonTeams.add(t)
                            }
                        }
                        if (commonTeams.isEmpty()){
                            Text("No Teams in common")
                        }else{
                            ShowCommonTeams(commonTeams, showTeamDetailsPane)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    if (selectedUser.taskList.size > 0) {
                        Column(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                                .padding(horizontal = 10.dp)
                        )
                        {
                            Spacer(modifier = Modifier.height(16.dp))
                            //KPI
                            CreateKPI(
                                selectedUser.taskList.filter { t -> (t.taskStatus == TaskStatus.Completed || t.taskStatus == TaskStatus.ExpiredCompleted) }.size,
                                selectedUser.taskList.size,
                                selectedUser.taskList.filter { t -> t.taskStatus == TaskStatus.ExpiredCompleted }.size,
                                selectedUser.taskList.filter { t ->
                                    (t.taskStatus == TaskStatus.Pending || t.taskStatus == TaskStatus.InProgress || t.taskStatus == TaskStatus.ExpiredNotCompleted) && LocalDate.parse(
                                        t.taskDeadline,
                                        DateTimeFormatter.ofPattern("dd/MM/uuuu")
                                    ) < LocalDate.now()
                                }.size
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        Text(text = "The user has never received a task to do.")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if(selectedUser != loggedUser) {
                    FloatingActionButton(
                        onClick = {
                            userChatPane(selectedUser.userNickname)
                        },
                        shape = CircleShape,
                        containerColor = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .size(70.dp)
                            .align(Alignment.BottomEnd)
                            .border(2.dp, lightBlue, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MailOutline,
                            contentDescription = "Open chat",
                            Modifier.size(30.dp)
                        )
                    }
                }
                else {
                    FloatingActionButton(
                        onClick = {
                            userChatListPane()
                        },
                        shape = CircleShape,
                        containerColor = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .size(70.dp)
                            .align(Alignment.BottomEnd)
                            .border(2.dp, lightBlue, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MailOutline,
                            contentDescription = "Open chat list",
                            Modifier.size(30.dp)
                        )
                    }
                }
            }
        } else {
            //HORIZONTAL
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    //Profile Picture
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CreateImage(photo, "$firstName $lastName", 170)
                    }

                    Column(
                        modifier = if(selectedUser != loggedUser) {
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .weight(2f)
                                .padding(bottom = 70.dp)            //The chat button is 70.dp in size, so I set the same padding so that it won't stay over the profile information
                            }
                            else{                                   //In this case the chat button is not shown, so no need for padding
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .weight(2f)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                        )
                        {
                            //Full Name
                            CreateRowText(
                                contentDescription = "Full Name",
                                text = "$firstName $lastName"
                            )

                            //Nickname
                            CreateRowText(contentDescription = "Nickname", text = nickname)

                            //Mail
                            CreateRowText(contentDescription = "Mail", text = mail)

                            //Location
                            CreateRowText(contentDescription = "Location", text = location)

                            //Description
                            CreateRowText(contentDescription = "Description", text = description)

                            //Birth Date
                            CreateRowText(contentDescription = "Birth Date", text = birthDate)

                            //Status
                            CreateLastRowText(contentDescription = "Status", text = status)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if(loggedUser != selectedUser){
                            val commonTeams: MutableList<Pair<Team, Boolean>> = mutableListOf()
                            for(t in loggedUser.userTeams){
                                if(selectedUser.userTeams.contains(t)){
                                    commonTeams.add(t)
                                }
                            }
                            ShowCommonTeams(commonTeams, showTeamDetailsPane)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (selectedUser.taskList.size > 0) {
                            Column(
                                modifier = Modifier
                                    .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                                    .padding(horizontal = 10.dp)
                            )
                            {
                                Spacer(modifier = Modifier.height(16.dp))
                                //KPI
                                CreateKPI(
                                    selectedUser.taskList.filter { t -> (t.taskStatus == TaskStatus.Completed || t.taskStatus == TaskStatus.ExpiredCompleted) }.size,
                                    selectedUser.taskList.size,
                                    selectedUser.taskList.filter { t -> t.taskStatus == TaskStatus.ExpiredCompleted }.size,
                                    selectedUser.taskList.filter { t ->
                                        (t.taskStatus == TaskStatus.Pending || t.taskStatus == TaskStatus.InProgress || t.taskStatus == TaskStatus.ExpiredNotCompleted) && LocalDate.parse(
                                            t.taskDeadline,
                                            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                                        ) < LocalDate.now()
                                    }.size
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else {
                            Text(text = "The user has never received a task to do.")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            }
        }

        if(selectedUser != loggedUser) {
            FloatingActionButton(                   //Chat button
                onClick = {
                    userChatPane(selectedUser.userNickname)
                },
                shape = CircleShape,
                containerColor = Color.White,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(70.dp)
                    .align(Alignment.BottomEnd)
                    .border(2.dp, lightBlue, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.MailOutline,
                    contentDescription = "Open chat",
                    Modifier.size(30.dp)
                )
            }
        }
        else {
            FloatingActionButton(
                onClick = {
                    userChatListPane()
                },
                shape = CircleShape,
                containerColor = Color.White,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(70.dp)
                    .align(Alignment.BottomEnd)
                    .border(2.dp, lightBlue, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.MailOutline,
                    contentDescription = "Open chat list",
                    Modifier.size(30.dp)
                )
            }
        }
    }
}
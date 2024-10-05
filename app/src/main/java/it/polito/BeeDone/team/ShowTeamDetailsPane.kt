package it.polito.BeeDone.team

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.task.Task
import it.polito.BeeDone.team.Role.Admin
import it.polito.BeeDone.teamViewModel
import it.polito.BeeDone.utils.CreateChatNotificationCircle
import it.polito.BeeDone.utils.CreateClickableCreatorText
import it.polito.BeeDone.utils.CreateImage
import it.polito.BeeDone.utils.CreateRowText
import it.polito.BeeDone.utils.CreateTeamUsersSection
import it.polito.BeeDone.utils.lightBlue

@Composable
fun ShowTeamDetailsMenu(
    teamName: String,
    editTeamPane: (String) -> Unit,
    shareTeamPane: () -> Unit,
    feedbackPerformancePane: (String) -> Unit,
    showTeamChatPane: (String) -> Unit,
    teamListPane: () -> Unit,
    selectedTeam: Team,
    allTasks: SnapshotStateList<Task>
) {
    var menuVisible by remember { mutableStateOf(false) }

    //popup to leave team
    var showLeaveTeamPopUp by remember { mutableStateOf(false) }
    //popup to delete team
    var showDeleteTeamPopUp by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { menuVisible = !menuVisible }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "team menu",
                modifier = Modifier.size(30.dp)
            )

            if(loggedUser.userTeams.filter { it.first == selectedTeam }[0].second)
                CreateChatNotificationCircle(5, 5)
        }

        DropdownMenu(
            expanded = menuVisible,
            properties = PopupProperties(focusable = true),
            onDismissRequest = { menuVisible = false },
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Options",
                        color = Color.Black,
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp, 1.dp)
                            .width(205.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Left
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            menuVisible = false
                            editTeamPane(selectedTeam.teamId)
                        },
                        modifier = Modifier
                            .padding(10.dp, 1.dp)
                            .height(40.dp)
                            .width(205.dp),
                        shape = ButtonDefaults.shape,
                        enabled = selectedTeam.getRoleTeamUser(loggedUser) == Admin,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Edit Team",
                                textAlign = TextAlign.Center,
                                color =
                                if(selectedTeam.getRoleTeamUser(loggedUser) == Admin)
                                    Color.Black
                                else
                                    Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            showDeleteTeamPopUp = !showDeleteTeamPopUp
                        },
                        modifier = Modifier
                            .padding(10.dp, 1.dp)
                            .height(40.dp)
                            .width(205.dp),
                        shape = ButtonDefaults.shape,
                        enabled =  if(loggedUser == selectedTeam.teamCreator) true else false,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Delete Team",
                                textAlign = TextAlign.Center,
                                color = if(loggedUser == selectedTeam.teamCreator) Color.Black else Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            showLeaveTeamPopUp = !showLeaveTeamPopUp
                        },
                        modifier = Modifier
                            .padding(10.dp, 1.dp)
                            .height(40.dp)
                            .width(205.dp),
                        shape = ButtonDefaults.shape,
                        enabled =  if(loggedUser != selectedTeam.teamCreator) true else false,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Leave Team",
                                textAlign = TextAlign.Center,
                                color = if(loggedUser != selectedTeam.teamCreator) Color.Black else Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            menuVisible = false
                            shareTeamPane()
                        },
                        modifier = Modifier
                            .padding(10.dp, 1.dp)
                            .height(40.dp)
                            .width(205.dp),
                        shape = ButtonDefaults.shape,
                        enabled = selectedTeam.getRoleTeamUser(loggedUser) == Admin,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Share Team",
                                textAlign = TextAlign.Center,
                                color = if(selectedTeam.getRoleTeamUser(loggedUser) == Admin)
                                    Color.Black
                                else
                                    Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            menuVisible = false
                            feedbackPerformancePane(selectedTeam.teamId)
                        },
                        modifier = Modifier
                            .padding(10.dp, 1.dp)
                            .height(40.dp)
                            .width(205.dp),
                        shape = ButtonDefaults.shape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Feedback & Performance",
                                textAlign = TextAlign.Left,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            menuVisible = false
                            showTeamChatPane(selectedTeam.teamId)
                        },
                        modifier = Modifier
                            .padding(10.dp, 1.dp)
                            .height(40.dp)
                            .width(205.dp),
                        shape = ButtonDefaults.shape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Team Chat",
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                            if(loggedUser.userTeams.filter { it.first == selectedTeam }[0].second) {
                                CreateChatNotificationCircle(15, 0)
                            }
                        }
                    }
                }

                if(showLeaveTeamPopUp) {
                    Column {
                        val dialogWidth = 400.dp / (1.3F)
                        val dialogHeight = 150.dp

                        if (showLeaveTeamPopUp) {
                            Dialog(onDismissRequest = {
                                showLeaveTeamPopUp = false
                            }) {
                                Card(
                                    Modifier
                                        .size(dialogWidth, dialogHeight),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(Color.White)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        Spacer(modifier = Modifier.height(15.dp))

                                        Text(
                                            text = "Are you sure you want to leave '$teamName' team?",
                                            color = Color.Black,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(10.dp, 1.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontFamily = FontFamily.SansSerif,
                                            textAlign = TextAlign.Left
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        FloatingActionButton(
                                            onClick = {
                                                selectedTeam.deleteUser(loggedUser)
                                                teamListPane()
                                                showLeaveTeamPopUp = false
                                            },
                                            containerColor = Color.White,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier
                                                .border(2.dp, Color.Red, RoundedCornerShape(20.dp))
                                                .height(35.dp)
                                                .width(120.dp)
                                        ) {
                                            Text(text = "Yes", color = Color.Black)
                                        }

                                        Spacer(modifier = Modifier.height(18.dp))

                                        FloatingActionButton(
                                            onClick = {
                                                showLeaveTeamPopUp = false
                                            },
                                            containerColor = Color.White,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier
                                                .border(2.dp, lightBlue, RoundedCornerShape(20.dp))
                                                .height(35.dp)
                                                .width(120.dp)

                                        ) {
                                            Text(text = "No", color = Color.Black)
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                if(showDeleteTeamPopUp) {
                    Column {
                        val dialogWidth = 420.dp / (1.3F)
                        val dialogHeight = 150.dp

                        if (showDeleteTeamPopUp) {
                            Dialog(onDismissRequest = {
                                showDeleteTeamPopUp = false
                            }) {
                                Card(
                                    Modifier
                                        .size(dialogWidth, dialogHeight),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(Color.White)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        Spacer(modifier = Modifier.height(15.dp))

                                        Text(
                                            text = "Are you sure you want to delete '$teamName' team?",
                                            color = Color.Black,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(10.dp, 1.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontFamily = FontFamily.SansSerif,
                                            textAlign = TextAlign.Left
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        FloatingActionButton(
                                            onClick = {
                                                for (u in selectedTeam.teamUsers) {
                                                    for(t in selectedTeam.teamTasks) {
                                                        allTasks.remove(t)
                                                        u.user.taskList.remove(t)
                                                    }
                                                    u.user.deleteTeam(selectedTeam)
                                                }
                                                selectedTeam.teamCreator.deleteTeam(selectedTeam)
                                                teamViewModel.allTeams.remove(selectedTeam)
                                                showDeleteTeamPopUp = false
                                                teamListPane()
                                            },
                                            containerColor = Color.White,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier
                                                .border(2.dp, Color.Red, RoundedCornerShape(20.dp))
                                                .height(35.dp)
                                                .width(120.dp)
                                        ) {
                                            Text(text = "Yes", color = Color.Black)
                                        }

                                        Spacer(modifier = Modifier.height(18.dp))

                                        FloatingActionButton(
                                            onClick = {
                                                showDeleteTeamPopUp = false
                                            },
                                            containerColor = Color.White,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier
                                                .border(2.dp, lightBlue, RoundedCornerShape(20.dp))
                                                .height(35.dp)
                                                .width(120.dp)

                                        ) {
                                            Text(text = "No", color = Color.Black)
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)                         //Denotes that the annotated element should only be called on the given API level or higher. Needed for the Profile Image
@Composable
fun ShowTeamDetailsPane(
    team: Team,
    teamTaskListPane: (String) -> Unit,
    userChatPane: (String) -> Unit,
    showUserInformationPane: (String) -> Unit,
    teamListPane: () -> Unit
) {
    BoxWithConstraints {
        val maxH = this.maxHeight
        if (this.maxHeight > this.maxWidth) {               //True if the screen is in portrait mode
            //VERTICAL
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 50.dp),     //Padding is needed in order to leave 16dp from left and right borders
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //Team image
                Row(
                    modifier = Modifier
                        .height(maxH / 3)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CreateImage(team.teamImage, team.teamName, 170)
                }

                Column(
                    modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                        .padding(end = 4.dp)
                )
                {
                    Spacer(modifier = Modifier.height(10.dp))
                    //Team Name
                    CreateRowText(contentDescription = "Team name", text = team.teamName)

                    //Description
                    CreateRowText(contentDescription = "Description", text = team.teamDescription)

                    //Category
                    CreateRowText(contentDescription = "Category", text = team.teamCategory)

                    //Creation Date
                    CreateRowText(contentDescription = "Creation date", text = team.teamCreationDate)

                    //Team Creator
                    CreateClickableCreatorText(contentDescription = "Creator", creator = team.teamCreator, showUserInformationPane = showUserInformationPane)

                    //Team members
                    CreateTeamUsersSection(
                        team = team,
                        userChatPane = userChatPane,
                        showUserInformationPane = showUserInformationPane,
                        teamListPane = teamListPane
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(60.dp))

            //button to view team's tasks
            FloatingActionButton(
                onClick = { teamTaskListPane(team.teamId) },
                shape = RoundedCornerShape(30.dp),
                containerColor = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    // .size(70.dp)
                    .align(Alignment.BottomEnd)
                    .offset((-10).dp, (-10).dp)
                    .border(2.dp, lightBlue, RoundedCornerShape(30.dp))
            ) {
                Text(
                    text = "Go to team tasks",
                    color = Color.Black,
                    modifier = Modifier.padding(10.dp)

                )
            }
        } else {
            //HORIZONTAL
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                //Profile Picture
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(3.dp))
                    CreateImage(team.teamImage, team.teamName, 160)

                    //button to view team's tasks
                    FloatingActionButton(
                        onClick = { teamTaskListPane(team.teamId) },
                        shape = RoundedCornerShape(20.dp),
                        containerColor = Color.White,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .border(2.dp, lightBlue, RoundedCornerShape(20.dp))
                    ) {
                        Text(
                            text = "Go to team tasks",
                            color = Color.Black,
                            modifier = Modifier.padding(10.dp)

                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                    )
                    {
                        //Team Name
                        CreateRowText(contentDescription = "Team name", text = team.teamName)

                        //Description
                        CreateRowText(contentDescription = "Description", text = team.teamDescription)

                        //Category
                        CreateRowText(contentDescription = "Category", text = team.teamCategory)

                        //Creation Date
                        CreateRowText(contentDescription = "Creation date", text = team.teamCategory)

                        //Team Creator
                        CreateRowText(contentDescription = "Creator", text = team.teamCreator.userNickname)

                        //Team members
                        CreateTeamUsersSection(
                            team = team,
                            userChatPane = userChatPane,
                            showUserInformationPane = showUserInformationPane,
                            teamListPane = teamListPane
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    }
}
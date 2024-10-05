package it.polito.BeeDone.utils

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.task.Task
import it.polito.BeeDone.task.history.Event
import it.polito.BeeDone.team.Role
import it.polito.BeeDone.team.Team
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Slice(val value: Float, val color: Color, val text: String)

var timeValue by mutableStateOf("")
    private set

//variable for image description
var timeError by mutableStateOf("")
    private set

/**
Creates StackedBar chart that shows number of completed tasks/number of tasks not completed.
Used for the KPIs
 */
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun StackedBar(done: Int, total: Int) {
    val slices = listOf(
        Slice(value = done.toFloat(), color = Color(0XFF9EB25D), text = done.toString()),
        Slice(
            value = (total - done).toFloat(),
            color = Color(0XFFd3d3d3),
            text = (total - done).toString()
        )
    )

    Row(
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
    ) {
        if (total > 0) {
            slices.forEach {

                if (it.value > 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(it.value),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(it.color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = it.text, color = Color.White)
                        }
                    }
                }

            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "no subtasks", color = Color.White)
                }
            }

        }
    }
}

/**
Creates a Text element that displays information
 */
@Composable
fun CreateRowText(contentDescription: String, text: String) {
    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(20.dp, 0.dp)
        ) {
            Text(
                text = contentDescription,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Column(
            modifier = Modifier.weight(0.55f)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

    }
    Spacer(modifier = Modifier.height(6.dp))
    HorizontalDivider(Modifier.padding(20.dp, 0.dp), thickness = Dp.Hairline, color = Color.Gray)
}

/**
Same as function above, but there is no HorizontalDivider under the Text element
 */
@Composable
fun CreateLastRowText(contentDescription: String, text: String) {
    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(20.dp, 0.dp)
        ) {
            Text(
                text = contentDescription,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Column(
            modifier = Modifier.weight(0.55f)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

    }
    Spacer(modifier = Modifier.height(6.dp))
}

/**
 * Creates a Text that displays a list of users. Each of these users can be clicked
 */
@Composable
fun CreateClickableUserText(
    contentDescription: String,
    taskUsers: MutableList<User>,
    showUserInformationPane: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(20.dp, 0.dp)
        ) {
            Text(
                text = contentDescription,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp
            )
        }
        Column(
            modifier = Modifier.weight(0.55f)
        ) {
            for (u in taskUsers) {
                Row(
                    modifier = Modifier.clickable {
                        showUserInformationPane(u.userNickname)
                    }
                ) {

                    CreateImage(
                        photo = u.userImage,
                        name = "${u.userFirstName} ${u.userLastName}",
                        size = 25
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (loggedUser != u) u.userNickname else "@You",
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp,
                        color = lightBlue
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

    }
    HorizontalDivider(Modifier.padding(20.dp, 0.dp), thickness = Dp.Hairline, color = Color.Gray)
}

@Composable
fun CreateClickableCreatorText(
    contentDescription: String,
    creator: User,
    showUserInformationPane: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(20.dp, 0.dp)
        ) {
            Text(
                text = contentDescription,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp
            )
        }
        Column(
            modifier = Modifier.weight(0.55f)
        ) {

            Row(
                modifier = Modifier.clickable {
                    showUserInformationPane(creator.userNickname)
                }
            ) {

                CreateImage(
                    photo = creator.userImage,
                    name = "${creator.userFirstName} ${creator.userLastName}",
                    size = 25
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = if (loggedUser != creator) creator.userNickname else "@You",
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = lightBlue
                )

                Spacer(modifier = Modifier.height(30.dp))
            }

        }

    }
    HorizontalDivider(Modifier.padding(20.dp, 0.dp), thickness = Dp.Hairline, color = Color.Gray)
}

/**
 * Creates a Text that displays the team name. It can be clicked
 */
@Composable
fun CreateClickableTeamText(
    contentDescription: String,
    taskTeam: Team,
    showTeamDetailsPane: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(20.dp, 0.dp)
        ) {
            Text(
                text = contentDescription,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp
            )
        }
        Column(
            modifier = Modifier.weight(0.55f)
        ) {
            Row(
                modifier = Modifier.clickable {
                    showTeamDetailsPane(taskTeam.teamId)
                }
            ) {

                CreateImage(
                    photo = taskTeam.teamImage,
                    name = taskTeam.teamName,
                    size = 25
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = taskTeam.teamName,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = lightBlue
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }

    }
    HorizontalDivider(Modifier.padding(20.dp, 0.dp), thickness = Dp.Hairline, color = Color.Gray)
}


/**
Manages the KPIs. Shows a StackedBarChart and two boxes with information about the tasks completed by the user
 */
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CreateKPI(
    taskDone: Int,
    totalTask: Int,
    taskCompletedExpired: Int,
    taskExpiredNotCompleted: Int
) {
    Text(
        text = "Tasks completed/Tasks not completed",
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(4.dp))
    StackedBar(taskDone, totalTask)
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .border(
                    2.dp, Color.LightGray, shape = RoundedCornerShape(15.dp)
                )
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(2.dp, 4.dp)
        ) {
            Row {
                Text(
                    text = "Task completed after expiration",
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp, 1.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    minLines = 2
                )
            }
            Row {
                Text(
                    text = taskCompletedExpired.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp, 1.dp),
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.05f))
        Column(
            modifier = Modifier
                .weight(1f)
                .border(
                    2.dp, Color.LightGray, shape = RoundedCornerShape(15.dp)
                )
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(2.dp, 4.dp)
        ) {
            Row {
                Text(
                    text = "Tasks expired and not yet completed",
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp, 2.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    minLines = 2
                )
            }
            Row {
                Text(
                    text = taskExpiredNotCompleted.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp, 0.dp),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
Manages the profile/group image. See Camera.kt for details
 */
@Composable
fun CreateImage(photo: Uri?, name: String, size: Int) {
    if (photo != null && !Uri.EMPTY.equals(photo)) {
        SetImage(photo, size)
    } else {
        NoImage(name, size)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskRow(task: Task, showTaskDetailsPane: (Int) -> Unit) {
    val currentDate = LocalDate.now()
    Row(
        Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .padding(10.dp)
            .clickable(onClick = {
                showTaskDetailsPane(task.taskId)
            })
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = task.taskTeam.teamName, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.size(10.dp))

        Column(Modifier.weight(3f)) {
            Text(
                text = task.taskTitle, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.size(10.dp))


        var mod = Modifier.width(IntrinsicSize.Max)
        val deadline = LocalDate.parse(task.taskDeadline, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        if (deadline.isBefore(currentDate)) {
            mod = Modifier
                .width(IntrinsicSize.Max)
                .background(Color(0xFFEB7856), RoundedCornerShape(10.dp))
                .padding(5.dp, 1.dp)
        } else if (deadline.isBefore(currentDate.plusDays(5))) {
            mod = Modifier
                .width(IntrinsicSize.Max)
                .background(Color(0xFFF5CA5E), RoundedCornerShape(10.dp))
                .padding(5.dp, 1.dp)
        }

        Column(modifier = mod) {
            Text(
                text = task.taskDeadline, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
    Spacer(Modifier.size(10.dp))
}

@Composable
fun TeamBox(
    team: Pair<Team, Boolean>,
    showTeamDetailsPane: (String) -> Unit,
    acceptInvitationPane: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .border(1.dp, lightBlue, RoundedCornerShape(15.dp))
            .clickable {
                if (team.first.teamUsers.any { it.user == loggedUser && it.role == Role.Invited }) {
                    acceptInvitationPane(team.first.teamId)
                } else {
                    showTeamDetailsPane(team.first.teamId)
                }
            }
    ) {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {
                CreateImage(team.first.teamImage, team.first.teamName, 150)
                Text(text = team.first.teamName, maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
            }

            if (team.second) {
                CreateChatNotificationCircle(150, 5)
            }
        }
    }
}

@Composable
fun EventRow(event: Event, showUserInformationPane: (String) -> Unit) {
    Text(text = event.date)

    val (expanded, setExpanded) = remember {
        mutableStateOf(false)
    }

    val b = if (expanded) "\u25b2" else "\u25bc"

    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .padding(10.dp)
    ) {
        Row {
            Column(Modifier.weight(1f)) {
                Text(
                    text = event.title, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.size(10.dp))

            Column(
                Modifier
                    .weight(1f)
                    .clickable(onClick = {
                        showUserInformationPane(event.user.userNickname)
                    })
            ) {
                Text(
                    text = event.user.userNickname,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = lightBlue,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(Modifier.size(10.dp))

            Column(Modifier.width(IntrinsicSize.Max)) {
                Text(
                    text = event.taskStatus.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.size(10.dp))

            Column(Modifier.width(IntrinsicSize.Max)) {
                Text(
                    text = event.taskDoneSubtasks + "/" + event.taskTotalSubtasks,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


        }

        if (event.taskChanges.isNotEmpty()) {
            Spacer(modifier = Modifier.size(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "List of changes:")

                OutlinedButton(
                    onClick = { setExpanded(!expanded) },
                    colors = ButtonColors(Color.White, lightBlue, Color.Black, Color.Black),
                    modifier = Modifier.size(70.dp, 30.dp)
                ) {
                    Text(text = b)
                }
            }
        }
        if (expanded) {

            for (e in event.taskChanges) {
                if (event.taskChanges.size > 1) {
                    Text(text = "- " + e)
                } else {
                    Text(text = e)
                }
            }
        }
    }
    Spacer(Modifier.size(10.dp))
}

@Composable
fun CreateTeamUsersSection(
    team: Team,
    userChatPane: (String) -> Unit,
    showUserInformationPane: (String) -> Unit,
    teamListPane: () -> Unit
) {
    val (expandedIndex, setExpandedIndex) = remember {                                  //Indicates the index of the user for which the dropdown should be expanded
        mutableIntStateOf(-2)
    }

    val (showRemovePopup, setShowRemovePopup) = remember {
        mutableStateOf(false)
    }

    val (showUpdateTimePopup, setShowUpdateTimePopup) = remember {
        mutableStateOf(false)
    }

    val (refreshPane, setRefreshPane) = remember {
        mutableStateOf(false)
    }

    var indexToRemove by remember {
        mutableIntStateOf(-1)
    }

    var indexToUpdateTime by remember {
        mutableIntStateOf(-1)
    }

    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

        Text(
            text = "Team Members",
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            fontSize = 17.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
    Spacer(modifier = Modifier.height(6.dp))

    for (i in 0..<team.teamUsers.size) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
        ) {
            ShowTeamMemberInfo(
                team.teamUsers[i].user,
                addSpacesToSentence(team.teamUsers[i].role.toString()),
                i,
                showUserInformationPane,
                expandedIndex,
                setExpandedIndex
            )

            if (expandedIndex == i) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Weekly hours for this team: ",
                            color = Color.Black,
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                    }
                    Column(
                        modifier = Modifier.weight(0.55f)
                    ) {
                        Text(
                            text = team.teamUsers[i].timePartecipation.toString(),
                            maxLines = 1
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (team.teamUsers[i].user != loggedUser) {
                        //Send Message
                        TextButton(
                            onClick = {
                                userChatPane(team.teamUsers[i].user.userNickname)
                            },
                            modifier = Modifier
                                .background(Color(176, 196, 222), RoundedCornerShape(10.dp))
                                .weight(1f)
                        ) {
                            Text(
                                text = "Send Message",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                minLines = 2,
                                maxLines = 2
                            )
                        }

                        if ((team.getRoleTeamUser(loggedUser) == Role.Admin || loggedUser == team.teamCreator) && team.teamUsers[i].user != team.teamCreator) {  //The Remove button is not shown under the profile of the creator of the team (the team creator cannot be removed). Also, it is only shown to the Admins and to the Creator of the team
                            Spacer(modifier = Modifier.weight(0.05f))

                            //Edit role
                            TextButton(
                                onClick = {
                                    if (team.teamUsers[i].role == Role.Participant) {
                                        team.teamUsers[i].role = Role.Admin
                                        setRefreshPane(!refreshPane)                                                                    //Whenever I need to refresh the content of the page, I update this MutableState variable (the content of the page cannot be automatically updated when changing the role, since it is not a Mutable State
                                    } else {
                                        team.teamUsers[i].role = Role.Participant
                                        setRefreshPane(!refreshPane)
                                    }
                                },
                                modifier = Modifier
                                    .background(Color(176, 196, 222), RoundedCornerShape(10.dp))
                                    .weight(1f)
                            ) {
                                Text(
                                    //The button to change a user's role is displayed accordingly to the role of the related user
                                    text = if (team.teamUsers[i].role == Role.Participant) "Promote to admin" else {
                                        if (team.teamUsers[i].role == Role.Admin) "Demote to participant" else "Promote to participant"
                                    },
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    minLines = 2,
                                    maxLines = 2
                                )
                            }

                            Spacer(modifier = Modifier.weight(0.05f))

                            //Remove
                            TextButton(
                                onClick = {
                                    indexToRemove = i
                                    setShowRemovePopup(!showRemovePopup)
                                },
                                modifier = Modifier
                                    .background(Color(176, 196, 222), RoundedCornerShape(10.dp))
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Remove",
                                    modifier = Modifier.padding(top = 10.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    minLines = 2,
                                    maxLines = 2
                                )
                            }
                        }
                    } else {                                          //Show different options for the member that corresponds to the logged profile
                        if (team.teamUsers[i].role == Role.Admin && team.teamUsers[i].user != team.teamCreator) {
                            //Edit role
                            TextButton(
                                onClick = {
                                    team.teamUsers[i].role = Role.Participant
                                    setRefreshPane(!refreshPane)
                                },
                                modifier = Modifier
                                    .background(Color(176, 196, 222), RoundedCornerShape(10.dp))
                                    .weight(1f)
                            ) {
                                Text(
                                    //The button to change a user's role is displayed accordingly to the role of the related user
                                    text = "Demote to participant",
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    minLines = 2,
                                    maxLines = 2
                                )
                            }

                            Spacer(modifier = Modifier.weight(0.05f))
                        }

                        //Leave team
                        if (team.teamUsers[i].user != team.teamCreator) {
                            TextButton(
                                onClick = {
                                    indexToRemove = i
                                    setShowRemovePopup(!showRemovePopup)
                                },
                                modifier = Modifier
                                    .background(Color(176, 196, 222), RoundedCornerShape(10.dp))
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Leave team",
                                    /*modifier =
                                    //resolves a graphic bug
                                    if (team.getRoleTeamUser(loggedUser) != Role.Participant)
                                        Modifier.padding()
                                    else
                                        Modifier.padding(top = 10.dp),*/
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    minLines = 2,
                                    maxLines = 2
                                )
                            }

                            Spacer(modifier = Modifier.weight(0.05f))
                        }

                        //Set time
                        TextButton(
                            onClick = {
                                indexToUpdateTime = i
                                setShowUpdateTimePopup(!showUpdateTimePopup)
                            },
                            modifier = Modifier
                                .background(Color(176, 196, 222), RoundedCornerShape(10.dp))
                                .weight(1f)
                        ) {
                            Text(
                                text = "Update time",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                minLines = 2,
                                maxLines = 2,
                                style = TextStyle()
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }

    /**
     * Shows a pop-up to confirm whether to remove an user from a team or not
     */
    if (showRemovePopup) {
        Column {
            val dialogWidth = 400.dp / (1.3F)
            val dialogHeight = 450.dp / 2

            Dialog(onDismissRequest = {
                setShowRemovePopup(false)
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
                        Button(
                            onClick = {
                                if(team.teamUsers[indexToRemove].user == loggedUser) {
                                    team.deleteUser(team.teamUsers[indexToRemove].user)
                                    setShowRemovePopup(false)
                                    teamListPane()
                                }
                                else {
                                    team.deleteUser(team.teamUsers[indexToRemove].user)
                                    setShowRemovePopup(false)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.border(2.dp, Color.Red, RoundedCornerShape(30.dp))
                        ) {
                            Text(
                                text = if (team.teamUsers[indexToRemove].user != loggedUser) "Remove ${team.teamUsers[indexToRemove].user.userNickname} from team" else "Leave Team",
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                indexToRemove = -1
                                setShowRemovePopup(false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.border(2.dp, lightBlue, RoundedCornerShape(30.dp))
                        ) {
                            Text(text = "Cancel", color = Color.Black)
                        }
                    }
                }
            }
        }
    }

    if (showUpdateTimePopup) {
        Column {
            val dialogWidth = 400.dp / (1.3F)
            val dialogHeight = 550.dp / 2

            Dialog(onDismissRequest = {
                setShowUpdateTimePopup(false)
                timeValue = ""
                timeError = ""
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

                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Change your time partecipation for the team",
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp, 1.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Left
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = timeValue,
                            onValueChange = {
                                timeValue = it
                                timeError = "" // Reset error when user changes the input
                            },
                            label = { Text("Hours per week") },
                            isError = timeError.isNotBlank(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = myShape,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = lightBlue,
                                focusedLabelColor = lightBlue,
                                focusedTextColor = Color.DarkGray
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (timeError.isNotBlank()) {
                            Text(
                                timeError,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Right,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (timeValue.isNotEmpty()) {
                                    if (timeValue.matches(Regex("^\\d+\$"))) {

                                        team.setTimeTeamUser(
                                            team.teamUsers[indexToUpdateTime].user,
                                            timeValue.toInt()
                                        )

                                        timeValue = "" // Reset text field
                                        timeError = ""
                                        setShowUpdateTimePopup(false)
                                    } else {
                                        timeError =
                                            "Please enter only integers and positive numbers"
                                    }
                                } else {
                                    timeError = "The time field cannot be empty"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .border(2.dp, lightBlue, RoundedCornerShape(30.dp))
                                .fillMaxWidth(0.8f)
                        ) {
                            Text(
                                text = "Change",
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                indexToUpdateTime = -1
                                timeValue = ""
                                timeError = ""
                                setShowUpdateTimePopup(false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.border(2.dp, lightBlue, RoundedCornerShape(30.dp))
                        ) {
                            Text(text = "Cancel", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTeamMemberInfo(
    teamMember: User,
    memberRole: String,
    i: Int,
    showUserInformationPane: (String) -> Unit,
    expandedIndex: Int,
    setExpandedIndex: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 8.dp)
                .clickable(onClick = {
                    showUserInformationPane(teamMember.userNickname)
                })
        ) {
            CreateImage(
                photo = teamMember.userImage,
                name = "${teamMember.userFirstName} ${teamMember.userLastName}",
                size = 25
            )
        }

        Column(
            modifier = Modifier
                .weight(6.3f)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = if (teamMember != loggedUser) teamMember.userNickname else "@You",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(onClick = {
                        showUserInformationPane(teamMember.userNickname)
                    }),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .weight(3.2f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = memberRole,
                color = Color.Gray,
            )
        }

        FloatingActionButton(
            onClick = {
                if (expandedIndex != i) {
                    setExpandedIndex(i)
                } else {
                    setExpandedIndex(-2)
                }
            },
            containerColor = Color.DarkGray,
            shape = CircleShape,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(14.dp), //padding first so I move it away from the edge and then reduce the size
        ) {
            if (expandedIndex == i) {
                Icon(Icons.Default.KeyboardArrowUp, "Hide user options", tint = Color.White)
            } else {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    "View user options",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ShowCommonTeams(
    teams: MutableList<Pair<Team, Boolean>>,
    showTeamDetailsPane: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
            .padding(end = 4.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Teams in common",
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (teams.isEmpty()) {
            Text(
                text = "No teams in common.", fontSize = 16.sp, fontFamily = FontFamily.Monospace,
                modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center
            )
        } else {
            for (i in teams.indices step 3) {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .width(70.dp)
                            .padding(start = 20.dp)
                            .clickable {
                                showTeamDetailsPane(teams[i].first.teamId)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CreateImage(teams[i].first.teamImage, teams[i].first.teamName, 70)
                        Text(
                            text = teams[i].first.teamName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 7.dp, bottom = 2.dp)
                        )
                    }

                    // Check if there is a second team in the current pair
                    if (i + 1 < teams.size) {
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .width(70.dp)
                                .padding(start = 25.dp)
                                .clickable {
                                    showTeamDetailsPane(teams[i + 1].first.teamId)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CreateImage(
                                teams[i + 1].first.teamImage, teams[i + 1].first.teamName, 70
                            )
                            Text(
                                text = teams[i + 1].first.teamName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 7.dp, bottom = 2.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(0.5f))
                    }

                    // Check if there is a third team in the current group
                    if (i + 2 < teams.size) {
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .width(70.dp)
                                .padding(start = 30.dp)
                                .clickable {
                                    showTeamDetailsPane(teams[i + 2].first.teamId)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CreateImage(
                                teams[i + 2].first.teamImage,
                                teams[i + 2].first.teamName,
                                70
                            )
                            Text(
                                text = teams[i + 2].first.teamName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 7.dp, bottom = 2.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(0.5f))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateChatNotificationCircle(
    xOffset: Int = 0,
    yOffset: Int = 0
) {
    Text(
        modifier = Modifier
            .offset(xOffset.dp, yOffset.dp)
            .drawBehind {
                drawCircle(
                    color = Color(220, 73, 58),
                    radius = 16f
                )
            },
        text = ""
    )
}
package it.polito.BeeDone

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import it.polito.BeeDone.profile.ConfirmMailPane
import it.polito.BeeDone.profile.EditUserInformationPane
import it.polito.BeeDone.profile.HomePane
import it.polito.BeeDone.profile.PasswordPane
import it.polito.BeeDone.profile.ShowUserInformationPane
import it.polito.BeeDone.profile.UserViewModel
import it.polito.BeeDone.profile.chat.UserChatListPane
import it.polito.BeeDone.profile.chat.UserChatPane
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.profile.tmpProfiles
import it.polito.BeeDone.task.CreateTaskPane
import it.polito.BeeDone.task.EditTaskPane
import it.polito.BeeDone.task.PersonalTaskListPane
import it.polito.BeeDone.task.ShowTaskAttachmentsPane
import it.polito.BeeDone.task.ShowTaskDetailsMenu
import it.polito.BeeDone.task.ShowTaskDetailsPane
import it.polito.BeeDone.task.Task
import it.polito.BeeDone.task.TaskMenu
import it.polito.BeeDone.task.TaskViewModel
import it.polito.BeeDone.task.history.TaskHistoryPane
import it.polito.BeeDone.task.timer.ShowTimer
import it.polito.BeeDone.task.timer.exitTimerPane
import it.polito.BeeDone.team.CreateTeamPane
import it.polito.BeeDone.team.EditTeamPane
import it.polito.BeeDone.team.FeedbackPerformancePane
import it.polito.BeeDone.team.InviteUser
import it.polito.BeeDone.team.InvitedPane
import it.polito.BeeDone.team.ShareTeamPane
import it.polito.BeeDone.team.ShowTeamChatPane
import it.polito.BeeDone.team.ShowTeamDetailsMenu
import it.polito.BeeDone.team.ShowTeamDetailsPane
import it.polito.BeeDone.team.Team
import it.polito.BeeDone.team.TeamListMenu
import it.polito.BeeDone.team.TeamListPane
import it.polito.BeeDone.team.TeamTaskListPane
import it.polito.BeeDone.team.TeamTaskMenu
import it.polito.BeeDone.team.TeamViewModel
import it.polito.BeeDone.ui.theme.Lab04Theme
import it.polito.BeeDone.utils.CreateChatNotificationCircle
import it.polito.BeeDone.utils.lightBlue
import it.polito.BeeDone.utils.questions_answers.Question
import it.polito.BeeDone.utils.questions_answers.ShowAnswers
import it.polito.BeeDone.utils.questions_answers.ShowQuestions

val userViewModel = UserViewModel()
val taskViewModel = TaskViewModel()
val teamViewModel = TeamViewModel()

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            val permission = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permission, 112)
        }

        setContent {
            Lab04Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

        for(u in tmpProfiles) {
            if(!userViewModel.allUsers.contains(u)) {
                userViewModel.allUsers.add(u)
            }
        }
    }
}


class Actions(
    val navController: NavHostController
) {
    val editUserInformationPane: () -> Unit = {
        navController.navigate("EditUserInformationPane")
    }

    val editPasswordPane: () -> Unit = {
        navController.navigate("EditPasswordPane")
    }

    val showUserInformationPane: (String) -> Unit = { userNickname ->
        navController.navigate("ShowUserInformationPane/${userNickname}")
    }

    val confirmMailPane: () -> Unit = {
        navController.navigate("ConfirmMailPane")
    }

    val personalTaskListPane: () -> Unit = {
        navController.navigate("PersonalTaskListPane")
    }

    val createTaskPane: () -> Unit = {
        navController.navigate("CreateTaskPane")
    }

    val createTaskPaneFromTeam: (String) -> Unit = { teamId ->
        navController.navigate("CreateTaskPane/${teamId}")
    }

    val showTaskDetailsPane: (Int) -> Unit = { taskId ->
        navController.navigate("ShowTaskDetailsPane/${taskId}")
    }

    val editTaskPane: () -> Unit = {
        navController.navigate("EditTaskPane")
    }

    val showTaskQuestionsPane: (Int) -> Unit = { taskId ->
        navController.navigate("ShowTaskQuestionsPane/${taskId}")
    }

    val showTaskAnswersPane: (Int) -> Unit = { questionId ->
        navController.navigate("ShowTaskAnswersPane/${questionId}")
    }

    val showTaskAttachmentsPane: () -> Unit = {
        navController.navigate("ShowTaskAttachmentsPane")
    }

    val historyPane: () -> Unit = {
        navController.navigate("HistoryPane")
    }

    val taskTimerPane: () -> Unit = {
        navController.navigate("TaskTimerPane")
    }

    val teamTaskListPane: (String) -> Unit = { teamId ->
        navController.navigate("TeamTaskListPane/${teamId}")
    }

    val createTeamPane: () -> Unit = {
        navController.navigate("CreateTeamPane")
    }

    val teamListPane: () -> Unit = {
        navController.navigate("TeamListPane")
    }

    val showTeamDetailsPane: (String) -> Unit = { teamId ->
        navController.navigate("ShowTeamDetailsPane/${teamId}")
    }

    val shareTeamPane: () -> Unit = {
        navController.navigate("ShareTeamPane")
    }

    val editTeamPane: (String) -> Unit = { teamId ->
        navController.navigate("EditTeamPane/${teamId}")
    }

    val feedbackPerformancePane: (String) -> Unit = { teamId ->
        navController.navigate("FeedbackPerformancePane/${teamId}")
    }

    val showTeamChatPane: (String) -> Unit = { teamId ->
        navController.navigate("ShowTeamChatPane/${teamId}")
    }

    val userChatPane: (String) -> Unit = { userNickname ->
        navController.navigate("UserChatPane/${userNickname}")
    }

    val userChatListPane: () -> Unit = {
        navController.navigate("UserChatListPane")
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    val navigateBackShowDetails: () -> Unit = {
        navController.popBackStack()
        navController.navigate("HomePane")
    }

    val homePane: () -> Unit = {
        navController.navigate("HomePane")
    }

    val invitePane: (String) -> Unit = {teamId ->
        navController.navigate("/invite/${teamId}")
    }

    val acceptInvitationPane: (String) -> Unit = {teamId ->
        navController.navigate("AcceptInvitationPane/${teamId}")
    }
}

@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {              //At the moment, the main screen is the profile page
    var selectedUser = loggedUser

    var selectedTask: Task? = null
    var selectedQuestion: Question? = null
    var selectedTeam: Team? = null

    val navController = rememberNavController()
    val actions = Actions(navController)

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = currentDestination?.route

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        topBar = {
            TopAppBar(title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = lightBlue),
                actions = {
                    when (currentScreen) {
                        "EditUserInformationPane" -> {

                            IconButton(onClick = {
                                userViewModel.exitWithoutUserInformationUpdate(
                                    actions.navigateBack
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Profile", color = Color.Black, fontSize = 24.sp)

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = { userViewModel.validate(showUserInformationPane = actions.showUserInformationPane, navigateBack = actions.navigateBack) }) {
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Save"
                                )
                            }
                        }

                        "EditPasswordPane" -> {
                            IconButton(onClick = {
                                userViewModel.exitWithoutPasswordUpdate(actions.navigateBack)
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }

                            Text(text = "Profile", color = Color.Black, fontSize = 24.sp)

                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { userViewModel.validatePassword(actions.navigateBack) }) {
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Save"
                                )
                            }
                        }

                        "ShowUserInformationPane/{userNickname}" -> {
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "Profile", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            if (selectedUser == loggedUser) {                      //We show the option to edit the profile only if the selectedProfile corresponds to the loggedProfile
                                IconButton(onClick = {
                                    actions.editUserInformationPane()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                }
                            }
                        }

                        "ConfirmMailPane" -> {
                            IconButton(onClick = {
                                userViewModel.exitWithoutCodeUpdate(actions.navigateBack)
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Profile", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { userViewModel.validateConfirmationCode(actions.navigateBack) }) {
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Save"
                                )
                            }
                        }

                        "PersonalTaskListPane" -> {
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "My tasks", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))

                            TaskMenu(
                                taskViewModel.showingTasks,
                                taskViewModel.allTasks,
                                selectedUser.userTeams
                            )
                        }

                        "CreateTaskPane" -> {
                            IconButton(onClick = {
                                taskViewModel.noUpdateTaskInformation()
                                actions.navigateBack()
                            }) //back to my task list
                            {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Create task", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                taskViewModel.validateTaskInformation(
                                    showTaskDetailsPane = actions.showTaskDetailsPane,
                                    navigateBack = actions.navigateBack,
                                    task = null
                                )
                            }) {             //Saves the information and returns to the ShowTaskDetailsPane
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Create"
                                )
                            }
                        }

                        "CreateTaskPane/{teamId}" -> {
                            IconButton(onClick = {
                                taskViewModel.noUpdateTaskInformation()
                                actions.navigateBack()
                            }) //back to my task list
                            {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Create task", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                actions.navigateBack
                                taskViewModel.validateTaskInformation(
                                    showTaskDetailsPane = actions.showTaskDetailsPane,
                                    navigateBack = actions.navigateBack,
                                    task = null
                                )
                            }) {             //Saves the information and returns to ShowTaskDetailsPane
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Create"
                                )
                            }
                        }

                        "ShowTaskDetailsPane/{taskId}" -> {
                            IconButton(onClick = {
                                taskViewModel.showingTasks =
                                    taskViewModel.allTasks.toMutableStateList()
                                actions.navigateBack()    //back to my task list
                            })

                            {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = selectedTask!!.taskTitle,
                                color = Color.Black,
                                fontSize = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(0.8f)                  //Limiting the maximum width of the Text, so that the menu icon can be shown
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            ShowTaskDetailsMenu(
                                actions.showTaskQuestionsPane,
                                actions.showTaskAttachmentsPane,
                                actions.historyPane,
                                selectedTask!!
                            )

                        }

                        "EditTaskPane" -> {
                            IconButton(onClick = {
                                taskViewModel.noUpdateTaskInformation()
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Edit Task", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                taskViewModel.validateTaskInformation(
                                    navigateBack = actions.navigateBack, task = selectedTask
                                )
                            }) {             //Saves the information and returns to the ShowTaskDetailsPane
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Save"
                                )
                            }
                        }

                        "TeamTaskListPane/{teamId}" -> {

                            IconButton(onClick = { actions.navigateBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }

                            Text(
                                text = "${selectedTeam!!.teamName} tasks",
                                color = Color.Black,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            TeamTaskMenu(
                                taskViewModel.showingTasks, selectedTeam!!.teamTasks
                            )
                        }

                        "TeamListPane" -> {

                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "My Teams", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))

                            TeamListMenu(
                                loggedUser.userTeams, teamViewModel.showingTeams
                            )

                        }

                        "ShowTaskQuestionsPane/{taskId}" -> {
                            IconButton(onClick = {
                                taskViewModel.setTaskQuestion("")
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Q&A", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "ShowTaskAnswersPane/{questionId}" -> {
                            IconButton(onClick = {
                                taskViewModel.setTaskAnswer("")
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Q&A", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "ShowTaskAttachmentsPane" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Attachments", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "HistoryPane" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "History", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "TaskTimerPane" -> {
                            IconButton(onClick = {
                                exitTimerPane(
                                    taskViewModel::setTaskTimerTitle, taskViewModel::setTaskTimer
                                )
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = selectedTask!!.taskTitle,
                                color = Color.Black,
                                fontSize = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "ShowTeamDetailsPane/{teamId}" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            })

                            {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            if (selectedTeam != null) {

                                Text(
                                    text = selectedTeam!!.teamName,
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.fillMaxWidth(0.8f)                  //Limiting the maximum width of the Text, so that the menu icon can be shown
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))

                            if (selectedTeam != null) {

                                ShowTeamDetailsMenu(
                                    selectedTeam!!.teamName,
                                    actions.editTeamPane,
                                    actions.shareTeamPane,
                                    actions.feedbackPerformancePane,
                                    actions.showTeamChatPane,
                                    actions.teamListPane,
                                    selectedTeam!!,
                                    taskViewModel.allTasks
                                )
                            }
                        }

                        "CreateTeamPane" -> {
                            IconButton(onClick = {
                                teamViewModel.noUpdateTeamInformation()
                                actions.navigateBack()
                            }) //back to my task list
                            {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Create team", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                teamViewModel.validateTeamInformation(
                                    navController, actions.navigateBack, null
                                )
                            }) {             //Saves the information and returns to the ShowTeamDetailsPane
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Create"
                                )
                            }
                        }


                        "EditTeamPane/{teamId}" -> {
                            IconButton(onClick = {
                                teamViewModel.noUpdateTeamInformation()
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Edit Team", color = Color.Black, fontSize = 24.sp)

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                teamViewModel.validateTeamInformation(
                                    navController, actions.navigateBack, selectedTeam
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = "Save"
                                )
                            }
                        }

                        "ShareTeamPane" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Share Team", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "FeedbackPerformancePane/{teamId}" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = "Feedback & Performance",
                                color = Color.Black,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "ShowTeamChatPane/{teamId}" -> {
                            IconButton(onClick = {
                                teamViewModel.setTeamMessage("")
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Chat", color = Color.Black, fontSize = 24.sp)
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "UserChatPane/{userNickname}" -> {
                            IconButton(onClick = {
                                userViewModel.setUserMessage("")
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            TextButton(onClick = {
                                actions.showUserInformationPane(selectedUser.userNickname)
                            }) {
                                Text(
                                    text = selectedUser.userNickname,
                                    color = Color.Black,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "UserChatListPane" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = "Chat",
                                color = Color.Black,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "HomePane" -> {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Temporary home page", color = Color.Black, fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        "AcceptInvitationPane/{teamId}" -> {
                            IconButton(onClick = {
                                actions.navigateBack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = "Accept invitation",
                                color = Color.Black,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            )
        },
        //This scaffold is needed for the BottomBar. Inside of the scaffold content, there is the application interface
        bottomBar = {
            val navigationBarItemColors = NavigationBarItemColors(
                selectedTextColor = Color(45, 86, 156),
                selectedIconColor = Color(45, 86, 156),
                selectedIndicatorColor = Color(132, 174, 250),
                unselectedIconColor = Color.DarkGray,
                unselectedTextColor = Color.DarkGray,
                disabledIconColor = Color.DarkGray,
                disabledTextColor = Color.DarkGray
            )

            NavigationBar(
                tonalElevation = 0.dp,
                containerColor = lightBlue, //same color as the top bar
                modifier = Modifier.height(60.dp),
            ) {
                NavigationBarItem(colors = navigationBarItemColors, onClick = {
                    actions.homePane()
                }, icon = {
                    Icon(
                        Icons.Outlined.Home, "Home", Modifier.size(25.dp)
                    )
                }, label = { Text("Home") }, selected = false
                )

                NavigationBarItem(colors = navigationBarItemColors,
                    onClick = {
                        actions.teamListPane()
                    },
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_notselected_teams),
                            "Teams",
                            Modifier.size(25.dp)
                        )

                        if (loggedUser.userTeams.map { it.second }.contains(true)) {
                            CreateChatNotificationCircle(30, -5)
                        }
                    },
                    label = { Text("Teams") },
                    selected = (currentScreen == "TeamTaskListPane/{teamId}" || currentScreen == "ShowTeamDetailsPane/{teamId}" || currentScreen == "TeamListPane" || currentScreen == "ShareTeamPane" || currentScreen == "EditTeamPane/{teamId}" || currentScreen == "ShowTeamChatPane/{teamId}" || currentScreen == "FeedbackPerformancePane/{teamId}" || currentScreen == "AcceptInvitationPane/{teamId}")
                )

                NavigationBarItem(colors = navigationBarItemColors,
                    onClick = {
                        taskViewModel.showingTasks = taskViewModel.allTasks.toMutableStateList()
                        actions.personalTaskListPane()
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_notselected_tasks),
                            contentDescription = "Tasks",
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    label = { Text("Tasks") },
                    selected = (currentScreen == "PersonalTaskListPane" || currentScreen == "CreateTaskPane" || currentScreen == "CreateTaskPane/{teamId}" || currentScreen == "ShowTaskDetailsPane/{taskId}" || currentScreen == "EditTaskPane" || currentScreen == "ShowTaskAttachmentsPane" || currentScreen == "HistoryPane" || currentScreen == "ShowTaskAnswersPane/{questionId}" || currentScreen == "ShowTaskQuestionsPane/{taskId}" || currentScreen == "TaskTimerPane")
                )

                NavigationBarItem(colors = navigationBarItemColors,

                    /* when the user clicks on the profile icon in the bottom bar
                       he returns to the profile page saving the changes he has made */
                    onClick = {
                        actions.showUserInformationPane(loggedUser.userNickname)
                    },
                    icon = {
                        Icon(
                            Icons.Filled.AccountCircle, "Profile", Modifier.size(25.dp)
                        )
                    },
                    label = { Text("Profile") },
                    selected = (currentScreen == "ShowUserInformationPane/{userNickname}" || currentScreen == "EditPasswordPane" || currentScreen == "EditUserInformationPane" || currentScreen == "ConfirmMailPane" || currentScreen == "UserChatPane/{userNickname}" || currentScreen == "UserChatListPane")
                )
            }
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "TeamListPane",

                ) {

                composable("EditUserInformationPane") {
                    EditUserInformationPane(
                        userViewModel.photoValue,
                        userViewModel::setPhoto,
                        userViewModel.firstNameValue,
                        userViewModel.firstNameError,
                        userViewModel::setFirstName,
                        userViewModel.lastNameValue,
                        userViewModel.lastNameError,
                        userViewModel::setLastName,
                        userViewModel.nicknameValue,
                        userViewModel.nicknameError,
                        userViewModel::setNickname,
                        userViewModel.mailValue,
                        userViewModel.mailError,
                        userViewModel::setMail,
                        userViewModel::openConfirmMailPane,
                        userViewModel.locationValue,
                        userViewModel.locationError,
                        userViewModel::setLocation,
                        userViewModel.descriptionValue,
                        userViewModel::setDescription,
                        userViewModel.birthDateValue,
                        userViewModel.birthDateError,
                        userViewModel::setBirthDate,
                        userViewModel.statusValue,
                        userViewModel::setStatus,
                        userViewModel::changingPassword,
                        actions.confirmMailPane,
                        actions.editPasswordPane
                    )
                }

                composable("EditPasswordPane") {
                    PasswordPane(
                        userViewModel.passwordValue,
                        userViewModel.passwordConfirmation,
                        userViewModel.passwordError,
                        userViewModel::setPassword,
                        userViewModel::setConfirmationPassword
                    )
                }

                composable(
                    "ShowUserInformationPane/{userNickname}",
                    arguments = listOf(navArgument("userNickname") { type = NavType.StringType })
                ) { entry ->
                    val parameterNickname = entry.arguments?.getString("userNickname")
                    if(userViewModel.allUsers.map{it.userNickname}.contains(parameterNickname)) {
                        selectedUser = userViewModel.allUsers.find {
                            it.userNickname == parameterNickname
                        }!!
                        userViewModel.setProfileInformation(selectedUser)
                        userViewModel.setOldProfileInformation()
                        ShowUserInformationPane(
                            selectedUser,
                            userViewModel.photoValue,
                            userViewModel.firstNameValue,
                            userViewModel.lastNameValue,
                            userViewModel.nicknameValue,
                            userViewModel.mailValue,
                            userViewModel.locationValue,
                            userViewModel.descriptionValue,
                            userViewModel.birthDateValue,
                            userViewModel.statusValue,
                            actions.userChatPane,
                            actions.userChatListPane,
                            actions.showTeamDetailsPane
                        )
                    }
                }

                composable("ConfirmMailPane") {
                    ConfirmMailPane(
                        userViewModel.confirmationCodeValue,
                        userViewModel.confirmationCodeError,
                        userViewModel::setConfirmationCode
                    )
                }

                composable("PersonalTaskListPane") {
                    PersonalTaskListPane(
                        taskViewModel.showingTasks,
                        taskViewModel::clearTaskInformation,
                        actions.createTaskPane,
                        actions.showTaskDetailsPane
                    )
                }

                composable("CreateTaskPane") {
                    CreateTaskPane(
                        taskTitleValue = taskViewModel.taskTitleValue,
                        taskTitleError = taskViewModel.taskTitleError,
                        setTaskTitle = taskViewModel::setTaskTitle,
                        taskDescriptionValue = taskViewModel.taskDescriptionValue,
                        setTaskDescription = taskViewModel::setTaskDescription,
                        taskDeadlineValue = taskViewModel.taskDeadlineValue,
                        taskDeadlineError = taskViewModel.taskDeadlineError,
                        setTaskDeadline = taskViewModel::setTaskDeadline,
                        taskTagValue = taskViewModel.taskTagValue,
                        setTaskTag = taskViewModel::setTaskTag,
                        taskCategoryValue = taskViewModel.taskCategoryValue,
                        taskCategoryError = taskViewModel.taskCategoryError,
                        setTaskCategory = taskViewModel::setTaskCategory,
                        taskUsersValue = taskViewModel.taskUsersValue,
                        setTaskUsers = taskViewModel::setTaskUsers,
                        deleteTaskUsers = taskViewModel::deleteTaskUsers,
                        taskRepeatValue = taskViewModel.taskRepeatValue,
                        setTaskRepeat = taskViewModel::setTaskRepeat,
                        taskTeamValue = taskViewModel.taskTeamValue,
                        taskTeamError = taskViewModel.taskTeamError,
                        setTaskTeam = taskViewModel::setTaskTeam,
                        selectedTeam = null,
                        actions.createTaskPaneFromTeam
                    )
                }

                composable(
                    "CreateTaskPane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam =
                        teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }
                    CreateTaskPane(
                        taskTitleValue = taskViewModel.taskTitleValue,
                        taskTitleError = taskViewModel.taskTitleError,
                        setTaskTitle = taskViewModel::setTaskTitle,
                        taskDescriptionValue = taskViewModel.taskDescriptionValue,
                        setTaskDescription = taskViewModel::setTaskDescription,
                        taskDeadlineValue = taskViewModel.taskDeadlineValue,
                        taskDeadlineError = taskViewModel.taskDeadlineError,
                        setTaskDeadline = taskViewModel::setTaskDeadline,
                        taskTagValue = taskViewModel.taskTagValue,
                        setTaskTag = taskViewModel::setTaskTag,
                        taskCategoryValue = taskViewModel.taskCategoryValue,
                        taskCategoryError = taskViewModel.taskCategoryError,
                        setTaskCategory = taskViewModel::setTaskCategory,
                        taskUsersValue = taskViewModel.taskUsersValue,
                        setTaskUsers = taskViewModel::setTaskUsers,
                        deleteTaskUsers = taskViewModel::deleteTaskUsers,
                        taskRepeatValue = taskViewModel.taskRepeatValue,
                        setTaskRepeat = taskViewModel::setTaskRepeat,
                        taskTeamValue = taskViewModel.taskTeamValue,
                        taskTeamError = taskViewModel.taskTeamError,
                        setTaskTeam = taskViewModel::setTaskTeam,
                        selectedTeam = selectedTeam,
                        actions.createTaskPaneFromTeam
                    )
                }

                composable(
                    "ShowTaskDetailsPane/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                ) { entry ->
                    selectedTask =
                        taskViewModel.allTasks.find { it.taskId == entry.arguments?.getInt("taskId") }
                    if (selectedTask != null) {
                        taskViewModel.setTaskInformation(selectedTask!!)
                        ShowTaskDetailsPane(
                            selectedTask = selectedTask!!,
                            taskViewModel::addTaskEventToHistory, //for subtask hystory
                            taskViewModel::setTaskAsCompleted,
                            taskViewModel.taskStatusValue,
                            taskViewModel.taskSubtasksValue,
                            taskViewModel::addTaskSubtasks,
                            actions.editTaskPane,
                            actions.taskTimerPane,
                            actions.showTeamDetailsPane,
                            actions.showUserInformationPane
                        )
                    }
                }

                composable("EditTaskPane") {
                    EditTaskPane(
                        taskTitleValue = taskViewModel.taskTitleValue,
                        taskTitleError = taskViewModel.taskTitleError,
                        setTaskTitle = taskViewModel::setTaskTitle,
                        taskDescriptionValue = taskViewModel.taskDescriptionValue,
                        setTaskDescription = taskViewModel::setTaskDescription,
                        taskDeadlineValue = taskViewModel.taskDeadlineValue,
                        taskDeadlineError = taskViewModel.taskDeadlineError,
                        setTaskDeadline = taskViewModel::setTaskDeadline,
                        taskTagValue = taskViewModel.taskTagValue,
                        setTaskTag = taskViewModel::setTaskTag,
                        taskCategoryValue = taskViewModel.taskCategoryValue,
                        taskCategoryError = taskViewModel.taskCategoryError,
                        setTaskCategory = taskViewModel::setTaskCategory,
                        taskUsersValue = taskViewModel.taskUsersValue,
                        setTaskUsers = taskViewModel::setTaskUsers,
                        deleteTaskUsers = taskViewModel::deleteTaskUsers,
                        taskRepeatValue = taskViewModel.taskRepeatValue,
                        setTaskRepeat = taskViewModel::setTaskRepeat,
                        taskTeamValue = taskViewModel.taskTeamValue
                    )
                }

                composable(
                    "ShowTaskQuestionsPane/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                ) { entry ->
                    selectedTask =
                        taskViewModel.allTasks.find { it.taskId == entry.arguments?.getInt("taskId") }
                    if (selectedTask != null) {
                        ShowQuestions(
                            selectedTask = selectedTask!!,
                            questions = taskViewModel.taskQuestionsValue,
                            setQuestions = taskViewModel::setTaskQuestions,
                            questionValue = taskViewModel.taskQuestionValue,
                            setQuestion = taskViewModel::setTaskQuestion,
                            showUserInformationPane = actions.showUserInformationPane,
                            navigate = actions.showTaskAnswersPane
                        )
                    }
                }

                composable(
                    "ShowTaskAnswersPane/{questionId}",
                    arguments = listOf(navArgument("questionId") { type = NavType.IntType })
                ) { entry ->
                    selectedQuestion = taskViewModel.taskQuestionsValue.find {
                        it.questionId == entry.arguments?.getInt("questionId")
                    }
                    if (selectedQuestion != null) {
                        ShowAnswers(
                            answers = taskViewModel.taskAnswersValue,
                            setAnswers = taskViewModel::setTaskAnswers,
                            assignAnswers = taskViewModel::assignTaskAnswers,
                            answerValue = taskViewModel.taskAnswerValue,
                            setAnswer = taskViewModel::setTaskAnswer,
                            selectedQuestion = selectedQuestion!!,
                            showUserInformationPane = actions.showUserInformationPane
                        )
                    }
                }

                composable("ShowTaskAttachmentsPane") {
                    ShowTaskAttachmentsPane(
                        taskViewModel.taskLinkValue,
                        taskViewModel::setTaskLink,
                        taskViewModel.taskMediaListValue,
                        taskViewModel::setTaskMediaList,
                        taskViewModel.taskLinkListValue,
                        taskViewModel::setTaskLinkList,
                        taskViewModel.taskDocumentListValue,
                        taskViewModel::setTaskDocumentList
                    )
                }

                composable("HistoryPane") {
                    TaskHistoryPane(
                        history = taskViewModel.taskHistoryValue,
                        showUserInformationPane = actions.showUserInformationPane
                    )
                }

                composable("TaskTimerPane") {
                    if(selectedTask != null) {
                        ShowTimer(
                            task = selectedTask!!,
                            taskTimerTitleValue = taskViewModel.taskTimerTitleValue,
                            setTaskTimerTitle = taskViewModel::setTaskTimerTitle,
                            taskTimerValue = taskViewModel.taskTimerValue,
                            setTaskTimer = taskViewModel::setTaskTimer,
                            taskTimerHistory = taskViewModel.taskTimerHistory,
                            addTaskTimerHistory = taskViewModel::addTaskTimerHistory,
                            showUserInformationPane = actions.showUserInformationPane
                        )
                    }
                }

                composable(
                    "TeamTaskListPane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam =
                        teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }
                    if (selectedTeam != null) {
                        taskViewModel.showingTasks = selectedTeam!!.teamTasks.toMutableStateList()
                        TeamTaskListPane(
                            taskViewModel.showingTasks,
                            taskViewModel::clearTaskInformation,
                            actions.createTaskPaneFromTeam,
                            actions.showTaskDetailsPane,
                            selectedTeam!!
                        )
                    }
                }

                composable("CreateTeamPane") {
                    CreateTeamPane(
                        teamViewModel.teamNameValue,
                        teamViewModel.teamNameError,
                        teamViewModel::setTeamName,
                        teamViewModel.teamDescriptionValue,
                        teamViewModel::setTeamDescription,
                        teamViewModel.teamCategoryValue,
                        teamViewModel.teamCategoryError,
                        teamViewModel::setTeamCategory,
                        teamViewModel.teamImageValue,
                        teamViewModel::setTeamImage
                    )
                }

                composable("TeamListPane") {
                    teamViewModel.showingTeams = loggedUser.userTeams.toMutableStateList()
                    TeamListPane(
                        teamViewModel.showingTeams,
                        loggedUser.userTeams,
                        teamViewModel::clearTeamInformation,
                        actions.createTeamPane,
                        actions.showTeamDetailsPane,
                        actions.acceptInvitationPane
                    )
                }

                composable(
                    "ShowTeamDetailsPane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam =
                        teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }
                    if (selectedTeam != null) {
                        teamViewModel.setTeamInformation(selectedTeam!!)
                        ShowTeamDetailsPane(
                            team = selectedTeam!!,
                            teamTaskListPane = actions.teamTaskListPane,
                            userChatPane = actions.userChatPane,
                            showUserInformationPane = actions.showUserInformationPane,
                            actions.teamListPane
                        )
                    }
                }

                composable("ShareTeamPane") {
                    if (selectedTeam != null) {

                        ShareTeamPane(userViewModel.allUsers, selectedTeam!!)

                    }
                }

                composable(
                    "EditTeamPane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam =
                        teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }
                    if (selectedTeam != null) {
                        EditTeamPane(
                            teamPhotoValue = teamViewModel.teamImageValue,
                            setTeamPhoto = teamViewModel::setTeamImage,
                            teamNameValue = teamViewModel.teamNameValue,
                            teamNameError = teamViewModel.teamNameError,
                            setTeamName = teamViewModel::setTeamName,
                            teamDescriptionValue = teamViewModel.teamDescriptionValue,
                            setTeamDescription = teamViewModel::setTeamDescription,
                            teamCategoryValue = teamViewModel.teamCategoryValue,
                            teamCategoryError = teamViewModel.teamCategoryError,
                            setTeamCategory = teamViewModel::setTeamCategory
                        )
                    }
                }

                composable(
                    "FeedbackPerformancePane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam =
                        teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }
                    if (selectedTeam != null) {
                        FeedbackPerformancePane(teamViewModel.userSelected, selectedTeam!!)
                    }
                }

                composable(
                    "ShowTeamChatPane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam =
                        teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }
                    if (selectedTeam != null) {
                        ShowTeamChatPane(
                            selectedTeam = selectedTeam!!,
                            chat = teamViewModel.teamChatValue,
                            setChat = teamViewModel::setTeamChat,
                            messageValue = teamViewModel.teamMessageValue,
                            setMessage = teamViewModel::setTeamMessage,
                            showUserInformationPane = actions.showUserInformationPane
                        )
                    }
                }

                composable(
                    "UserChatPane/{userNickname}",
                    arguments = listOf(navArgument("userNickname") { type = NavType.StringType })
                ) { entry ->
                    selectedUser = userViewModel.allUsers.find {
                        it.userNickname == entry.arguments?.getString("userNickname")
                    }!!
                    UserChatPane(
                        selectedUser,
                        userViewModel.userMessageValue,
                        userViewModel::setUserMessage,
                        actions.showUserInformationPane
                    )
                }

                composable(
                    "UserChatListPane"
                ) {
                    UserChatListPane(
                        userChat = loggedUser.userChat,
                        userChatPane = actions.userChatPane
                    )
                }

                composable("HomePane") {
                    HomePane(
                        userViewModel.allUsers, actions.showUserInformationPane
                    )
                }

                val uri = "https://www.beedone.com"

                composable(
                    route = "/invite/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType }),
                    deepLinks = listOf(NavDeepLink("$uri/invite/{teamId}"))
                ) { navBackStackEntry ->
                    val teamId = navBackStackEntry.arguments?.getString("teamId")

                    if (teamId != null) {
                        InviteUser(
                            teamViewModel.allTeams,
                            teamId,
                            loggedUser,
                            actions.showTeamDetailsPane,
                            actions.teamListPane,
                            actions.acceptInvitationPane
                        )
                    }
                }

                composable(
                    route = "AcceptInvitationPane/{teamId}",
                    arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                ) { entry ->
                    selectedTeam = teamViewModel.allTeams.find { it.teamId == entry.arguments?.getString("teamId") }

                    InvitedPane(
                        actions.showTeamDetailsPane,
                        actions.navigateBack,
                        actions.teamListPane,
                        selectedTeam!!
                    )
                }
            }
        }
    }
}
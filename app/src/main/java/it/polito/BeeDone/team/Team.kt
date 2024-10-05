package it.polito.BeeDone.team

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import it.polito.BeeDone.profile.User
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import it.polito.BeeDone.profile.chat.Message
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.task.Task
import it.polito.BeeDone.task.TaskStatus
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID

class Team(
    var teamId: String,
    var teamName: String,
    var teamUsers: MutableList<TeamMember>,
    var teamTasks: MutableList<Task>,
    var teamDescription: String,
    var teamCategory: String,
    var teamCreationDate: String,
    var teamImage: Uri?,
    var teamCreator: User,
    var teamChat: SnapshotStateList<Message>
) {
    constructor() : this(
        UUID.randomUUID().toString(),       //We use UUID to generate the IDs of the teams. It generates a random ID of 128 bits
        "",
        mutableStateListOf(),
        mutableListOf(),
        "",
        "",
        "",
        null,
        loggedUser,
        mutableStateListOf()
    )

    public fun addUser(user: TeamMember){
        teamUsers.add(user)
    }

    public fun addUsers(users: MutableList<TeamMember>){
        teamUsers = users.toMutableStateList()
    }

    /*
    * Search in teamUsers for a team member whose user is the same as the Profile passed
    * and whose role is Participant or Admin.
    * If it finds such a member, it removes it from the teamUsers list.
    * Moreover it removes the team from the list of teams the user belongs to.
    */
    public fun deleteUser(user: User){
        val memberToRemove = teamUsers.find { it.user == user }
        memberToRemove?.let {

            teamUsers.remove(it) //remove the user from the team member list
            it.user.deleteTeam(this) //remove the team from the user's team list

            if(it.user.taskList.isNotEmpty()) {
                for (t in it.user.taskList) {

                    //if I am the only user working on it, the task goes pending
                    if (t.taskUsers.contains(it.user) && (t.taskStatus != TaskStatus.Completed || t.taskStatus != TaskStatus.ExpiredCompleted
                                || t.taskStatus != TaskStatus.ExpiredNotCompleted)
                    ) {
                        if (t.taskUsers.size == 1) {
                            t.taskStatus = TaskStatus.Pending
                        }
                        t.taskUsers.remove(it.user)
                        it.user.taskList.remove(t)
                    }
                }
            }
        }
    }

    //set time
    fun setTimeTeamUser(user: User, i: Int){
        val member = teamUsers.find { it.user == user}
        member?.let {
            it.timePartecipation = i
        }
    }

    /* get role
    Check if the user is part of this Team.
     If it is, I get his role, otherwise I assume it has role = participant
    */
    fun getRoleTeamUser(user: User): Role? {
        val member = teamUsers.find { it.user == user}
        member?.let {
            return it.role
        }
        return Role.Participant
    }
}

@SuppressLint("MutableCollectionMutableState")
class TeamViewModel : ViewModel() {
    var allTeams = mutableStateListOf<Team>()
    var showingTeams = mutableStateListOf<Pair<Team, Boolean>>()

    fun addTeam(newTeam: Team) {
        allTeams.add(newTeam)
    }

    //Team Name
    var teamNameValue by mutableStateOf("")
        private set
    var teamNameError by mutableStateOf("")
        private set

    fun setTeamName(n: String) {
        teamNameValue = n
    }

    private fun checkName() {
        if (teamNameValue == "") {
            teamNameError = "Name cannot be empty"
        } else {
            teamNameError = ""
        }
    }

    //Team Image
    var teamImageValue: Uri? by mutableStateOf(null)
        private set

    fun setTeamImage(i: Uri?) {
        teamImageValue = i
    }

    //Team Description
    var teamDescriptionValue by mutableStateOf("")
        private set

    fun setTeamDescription(n: String) {
        teamDescriptionValue = n
    }

    //Team Category
    var teamCategoryValue by mutableStateOf("")
        private set
    var teamCategoryError by mutableStateOf("")
        private set

    fun setTeamCategory(n: String) {
        teamCategoryValue = n
    }

    private fun checkCategory() {
        if (teamCategoryValue == "") {
            teamCategoryError = "Category cannot be empty"
        } else {
            teamCategoryError = ""
        }
    }

    //Team Image
    var userSelected: User? by mutableStateOf(null)
        private set

    fun setTeUserSelected(i: User?) {
        userSelected = i
    }

    //Team Chat
    //Single message written by the user
    var teamMessageValue by mutableStateOf("")
        private set

    fun setTeamMessage(n: String) {
        teamMessageValue = n
    }

    //List of Messages sent to the team
    var teamChatValue by mutableStateOf(
        mutableListOf<Message>()
    )
        private set

    @SuppressLint("SimpleDateFormat")
    fun setTeamChat(n: String, selectedTeam: Team) {
        val q = Message(
            n,
            SimpleDateFormat("dd/MM/yyyy").format(Date()),
            SimpleDateFormat("hh:mm").format(Date()),
            loggedUser
        )
        teamChatValue.add(q)
        selectedTeam.teamChat = teamChatValue.toMutableStateList()   //Updates selectedTeam and, consequently, the list of teams
    }

    @SuppressLint("SimpleDateFormat")
    fun assignTeamChat(n: MutableList<Message>) {
        teamChatValue = n
    }



    private var oldTeamNameValue: String = teamNameValue
    private var oldTeamPhotoValue: Uri? = teamImageValue
    private var oldTeamCategoryValue: String = teamCategoryValue
    private var oldTeamDescriptionValue: String = teamDescriptionValue

    /* information validation */
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun validateTeamInformation(
        navController: NavHostController,
        navigateBack: () -> Unit,
        team: Team?
    ) {
        var selectedTeam: Team
        if(team != null) {
            selectedTeam = team
        }
        else {
            selectedTeam = Team()
        }

        checkName()
        checkCategory()

        if (teamNameError.isBlank() && teamCategoryError.isBlank()) {
            if (navController.currentBackStackEntry?.destination?.route == "CreateTeamPane") {
                var teamId = UUID.randomUUID().toString()
                while(allTeams.map { it.teamId }.contains(teamId)) {        //By using UUID, there is a tiny little chance that it generates two identical IDs. So, we check that the generated ID is unique
                    teamId = UUID.randomUUID().toString()
                }
                val tmpTeam = Team(
                    teamId,
                    teamNameValue,
                    mutableListOf<TeamMember>(),
                    mutableListOf<Task>(),
                    teamDescriptionValue,
                    teamCategoryValue,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")),
                    teamImageValue,
                    loggedUser,
                    mutableStateListOf()
                )
                tmpTeam.addUser(TeamMember(loggedUser, role = Role.Admin, 0))
                loggedUser.userTeams.add(Pair(tmpTeam, false))
                selectedTeam=tmpTeam
                addTeam(tmpTeam)
                setTeamInformation(selectedTeam)

            } else { //you are coming from EditTeamPane
                //Saves edit to a Team.
                selectedTeam.teamImage = teamImageValue
                selectedTeam.teamName = teamNameValue
                selectedTeam.teamDescription = teamDescriptionValue
                selectedTeam.teamCategory = teamCategoryValue
            }


            //you have to set old=new
            oldTeamPhotoValue = teamImageValue
            oldTeamNameValue = teamNameValue
            oldTeamDescriptionValue = teamDescriptionValue
            oldTeamCategoryValue = teamCategoryValue

            navigateBack()
        }
    }

    //function set the old values, ignoring the changes
    fun noUpdateTeamInformation() {
        //set old values
        teamImageValue = oldTeamPhotoValue
        teamNameValue = oldTeamNameValue
        teamDescriptionValue = oldTeamDescriptionValue
        teamCategoryValue = oldTeamCategoryValue

        //clear old error values
        teamNameError = ""
        teamCategoryError = ""
    }

    //function clear all values of team
    fun clearTeamInformation() {
        //reset infos
        teamImageValue = null
        teamNameValue = ""
        teamDescriptionValue = ""
        teamCategoryValue = ""


        //reset errors
        teamNameError = ""
        teamCategoryError = ""
    }

    fun setTeamInformation(selectedTeam: Team) {
        //set new values
        setTeamImage(selectedTeam.teamImage)
        setTeamName(selectedTeam.teamName)
        setTeamDescription(selectedTeam.teamDescription)
        setTeamCategory(selectedTeam.teamCategory)
        assignTeamChat(selectedTeam.teamChat.toMutableList())

        //set old=new
        oldTeamPhotoValue = teamImageValue
        oldTeamNameValue = teamNameValue
        oldTeamDescriptionValue = teamDescriptionValue
        oldTeamCategoryValue = teamCategoryValue
    }
}

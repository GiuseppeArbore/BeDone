package it.polito.BeeDone.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import it.polito.BeeDone.profile.chat.UserChat
import it.polito.BeeDone.task.Task
import it.polito.BeeDone.team.Team

class User(
    var userImage: Uri?,
    var userFirstName: String,
    var userLastName: String,
    var userNickname: String,
    var userMail: String,
    var userLocation: String,
    var userDescription: String,
    var userBirthDate: String,
    var userStatus: String,
    var userTeams: SnapshotStateList<Pair<Team, Boolean>>,          //For every team the user is in, save if he has any messages to read (true) or not (false)
    var taskList: MutableList<Task>,
    var userChat: SnapshotStateList<UserChat>
) {
    constructor() : this(
        null, "", "",
        "", "", "", "",
        "", "", mutableStateListOf(), mutableListOf<Task>(),
        mutableStateListOf()
    )

    public fun deleteTeam(team: Team){
        if(userTeams.map { it.first }.contains(team)) {
            val teamToRemove: Pair<Team, Boolean> = userTeams.find { it.first == team }!!
            userTeams.remove(teamToRemove)
        }
    }
}

/**
 * Since we don't have yet an actual team creation, we created 4 teams manually for
 * test purpose. We also added 3 users to test all the functionalities.
 * The logged user is by default john smith
 */


/** Temporary profile*/
var loggedUser: User = User(
    null,
    "John",
    "Smith",
    "@John.Smith",
    "John.Smith@gmail.com",
    "Torino",
    "I love Turin and robots!",
    "11/10/1999",
    "working",
    mutableStateListOf(),
    mutableListOf(),
    mutableStateListOf()
)

var user2: User = User(
    null,
    "Mario",
    "Rossi",
    "@mario.rossi",
    "Mario.rossi@gmail.com",
    "Torino",
    "I love Turin and robots!",
    "2/10/1999",
    "sleeping",
    mutableStateListOf(),
    mutableListOf(),
    mutableStateListOf()
)

var user3: User = User(
    null,
    "Paul",
    "Gates",
    "@Paul.gates",
    "Paul.gates@gmail.com",
    "Torino",
    "I love Turin and robots!",
    "3/10/1999",
    "nothing to say",
    mutableStateListOf(),
    mutableListOf(),
    mutableStateListOf()
)

var tmpProfiles = mutableListOf(loggedUser, user2, user3)

class UserViewModel() : ViewModel() {

    var allUsers = mutableStateListOf<User>()

    fun addUser(u: User){
        allUsers.add(u)
    }

    //PROFILE PICTURE
    var photoValue: Uri? by mutableStateOf(null)
        private set

    fun setPhoto(i: Uri?) {
        photoValue = i
    }

    //FIRST NAME
    var firstNameValue by mutableStateOf("")
        private set
    var firstNameError by mutableStateOf("")
        private set

    fun setFirstName(n: String) {
        firstNameValue =
            n.replaceFirstChar { it.uppercase() } //returns the string with only the initial capital letter
    }

    private fun checkFirstName() {
        if (firstNameValue.isBlank()) {
            firstNameError = "First name cannot be blank"
        } else {
            firstNameError = ""
        }
    }

    //LAST NAME
    var lastNameValue by mutableStateOf("")
        private set
    var lastNameError by mutableStateOf("")
        private set

    fun setLastName(n: String) {
        lastNameValue = n.replaceFirstChar { it.uppercase() }
    }

    private fun checkLastName() {
        if (lastNameValue.isBlank()) {
            lastNameError = "Last name cannot be blank"
        } else {
            lastNameError = ""
        }
    }

    //NICKNAME
    var nicknameValue by mutableStateOf("")
        private set
    var nicknameError by mutableStateOf("")
        private set

    fun setNickname(n: String) {
        nicknameValue = n
    }

    private fun checkNickname() {
        if (nicknameValue.isBlank()) {
            nicknameError = "Nickname cannot be blank"
        } else if (nicknameValue.contains(' ')) {
            nicknameError = "Nickname cannot contain spaces"
        } else if(!nicknameValue.startsWith('@')) {
            nicknameError = "Nickname must start with @"
        } else if(nicknameValue.filter { it == '@' }.length != 1) {
            nicknameError = "Nickname can contain only one @"
        } else {
            nicknameError = ""
        }
    }

    //MAIL
    var mailValue by mutableStateOf("")
        private set
    var mailError by mutableStateOf("")
        private set

    fun setMail(m: String) {
        mailValue = m
    }

    private fun checkMail() {
        if (mailValue.isBlank()) {
            mailError = "Email cannot be blank"
        } else if (!mailValue.contains('@')) {
            mailError = "Invalid email address"
        } else {
            mailError = ""
        }
    }

    var confirmationCodeValue by mutableStateOf("")
        private set
    var confirmationCodeError by mutableStateOf("")
        private set

    fun setConfirmationCode(c: String) {
        confirmationCodeValue = c
    }

    fun openConfirmMailPane(confirmMailPane: () -> Unit) {
        checkMail()
        if (mailError.isBlank()) {
            confirmMailPane()
        }
    }

    fun checkConfirmationCode() {
        if (confirmationCodeValue.isBlank()) {
            confirmationCodeError = "Confirmation code cannot be blank"
        } else {
            if (confirmationCodeValue.length != 4) {
                confirmationCodeError = "Confirmation code must have 4 characters"
            } else {
                confirmationCodeError = ""
            }
        }
    }

    fun validateConfirmationCode(editUserInformationPane: () -> Unit) {
        checkConfirmationCode()
        if (confirmationCodeError.isBlank()) {
            oldMailValue = mailValue
            confirmationCodeValue = ""
            editUserInformationPane()
        }
    }

    //LOCATION
    var locationValue by mutableStateOf("")
        private set
    var locationError by mutableStateOf("")
        private set

    fun setLocation(l: String) {
        locationValue = l.replaceFirstChar { it.uppercase() }
    }

    private fun checkLocation() {
        if (locationValue.isBlank()) {
            locationError = "Location cannot be blank"
        } else {
            locationError = ""
        }
    }

    //DESCRIPTION
    var descriptionValue by mutableStateOf("")
        private set

    fun setDescription(d: String) {
        descriptionValue = d
    }

    //BIRTH DATE
    var birthDateValue by mutableStateOf("")
        private set
    var birthDateError by mutableStateOf("")
        private set

    fun setBirthDate(b: String) {
        birthDateValue = b
    }

    private fun checkBirthDate() {
        if (birthDateValue.isBlank()) {
            birthDateError = "Birth date cannot be blank"
        } else {
            birthDateError = ""
        }
    }

    //STATUS
    var statusValue by mutableStateOf("")
        private set

    fun setStatus(s: String) {
        statusValue = s
    }

    //PASSWORD
    var passwordValue by mutableStateOf("")
        private set
    var passwordConfirmation by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    fun setPassword(p: String) {
        passwordValue = p
    }

    fun setConfirmationPassword(p: String) {
        passwordConfirmation = p
    }

    fun changingPassword(editPasswordPane: () -> Unit) {
        editPasswordPane()  // switch to editing state
    }

    fun validatePassword(editUserInformationPane: () -> Unit) {
        checkPassword()
        if (passwordError.isBlank()) {
            /*
            I make sure that when I return to the password page the fields are
            always empty (and do not contain the previously entered value)
            */
            passwordValue = ""
            passwordConfirmation = ""
            editUserInformationPane()
        }
    }

    private fun checkPassword() {
        if (passwordValue.isBlank()) {
            passwordError = "Password cannot be blank"
        } else if (passwordValue.length < 8) {
            passwordError = "Password should be at least 8 character long"

        } else if (passwordValue != passwordConfirmation) {
            passwordError = "Password should match"
        } else {
            passwordError = ""
        }
    }

    //MESSAGE
    var userMessageValue by mutableStateOf("")
        private set
    fun setUserMessage(n: String) {
        userMessageValue = n
    }

    private var oldPhotoValue: Uri? = photoValue
    private lateinit var oldFirstNameValue: String
    private lateinit var oldLastNameValue: String
    private lateinit var oldNicknameValue: String
    private lateinit var oldMailValue: String
    private lateinit var oldLocationValue: String
    private lateinit var oldDescriptionValue: String
    private lateinit var oldBirthDateValue: String
    private lateinit var oldStatusValue: String

    fun validate(showUserInformationPane: (String) -> Unit, navigateBack: (() -> Unit)? = null) {

        checkFirstName()
        checkLastName()
        checkMail()
        checkNickname()
        checkBirthDate()
        checkLocation()

        //the user has changed the email and it needs to be revalidated
        if (mailValue != oldMailValue) {
            mailError = "Confirm mail"
        }

        if (lastNameError.isBlank() && firstNameError.isBlank() && mailError.isBlank() && nicknameError.isBlank() && birthDateError.isBlank() && locationError.isBlank()) {
            oldPhotoValue = photoValue
            oldFirstNameValue = firstNameValue
            oldLastNameValue = lastNameValue
            oldNicknameValue = nicknameValue
            oldMailValue = mailValue
            oldLocationValue = locationValue
            oldDescriptionValue = descriptionValue
            oldBirthDateValue = birthDateValue
            oldStatusValue = statusValue

            //Updates the information of the currently logged user
            loggedUser.userImage = photoValue
            loggedUser.userFirstName = firstNameValue
            loggedUser.userLastName = lastNameValue
            loggedUser.userNickname = nicknameValue
            loggedUser.userMail = mailValue
            loggedUser.userLocation = locationValue
            loggedUser.userDescription = descriptionValue
            loggedUser.userBirthDate = birthDateValue
            loggedUser.userStatus = statusValue

            if(navigateBack != null) {
                navigateBack()
            }
            showUserInformationPane(loggedUser.userNickname)

        }
    }

    //on a graphic level it is < in the top bar
    fun exitWithoutUserInformationUpdate(navigateBack: () -> Unit) {
        revertUserInformationChanges()

        navigateBack()

    }

    fun revertUserInformationChanges() {
        setPhoto(oldPhotoValue)
        setFirstName(oldFirstNameValue)
        setLastName(oldLastNameValue)
        setNickname(oldNicknameValue)
        setMail(oldMailValue)
        setLocation(oldLocationValue)
        setDescription(oldDescriptionValue)
        setBirthDate(oldBirthDateValue)
        setStatus(oldStatusValue)

        //Deletes all the error messages
        firstNameError = ""
        lastNameError = ""
        nicknameError = ""
        mailError = ""
        locationError = ""
        birthDateError = ""
    }

    fun exitWithoutCodeUpdate(editUserInformationPane: () -> Unit) {
        setConfirmationCode("")
        confirmationCodeError = ""
        editUserInformationPane()
    }

    fun exitWithoutPasswordUpdate(editUserInformationPane: () -> Unit) {
        setPassword("")
        passwordError = ""
        setConfirmationPassword("")
        editUserInformationPane()
    }

    fun setProfileInformation(selectedUser: User) {
        setFirstName(selectedUser.userFirstName)
        setLastName(selectedUser.userLastName)
        setMail(selectedUser.userMail)
        setBirthDate(selectedUser.userBirthDate)
        setDescription(selectedUser.userDescription)
        setLocation(selectedUser.userLocation)
        setNickname(selectedUser.userNickname)
        setPhoto(selectedUser.userImage)
        setStatus(selectedUser.userStatus)
    }

    fun setOldProfileInformation() {
        oldPhotoValue = photoValue
        oldFirstNameValue = firstNameValue
        oldLastNameValue = lastNameValue
        oldNicknameValue = nicknameValue
        oldMailValue = mailValue
        oldLocationValue = locationValue
        oldDescriptionValue = descriptionValue
        oldBirthDateValue = birthDateValue
        oldStatusValue = statusValue
    }
}
package it.polito.BeeDone.team

import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import it.polito.BeeDone.R
import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.utils.CreateTextFieldError
import it.polito.BeeDone.utils.lightBlue

fun getQrCodeBitmap(link: String): Bitmap {
    val size = 512 //pixels
    val hints = hashMapOf<EncodeHintType, Int>().also {
        it[EncodeHintType.MARGIN] = 1
    } // Make the QR code buffer border narrower
    val bits = QRCodeWriter().encode(link, BarcodeFormat.QR_CODE, size, size, hints)
    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(x, y, if (bits[x, y]) lightBlue.toArgb() else Color.White.toArgb())
            }
        }
    }
}

@Composable
fun ShareTeamPane(allUsers: MutableList<User>, selectedTeam: Team) {

    var userInvited by remember { mutableStateOf("") }
    var userInvitedError by remember { mutableStateOf("") }
    var userInvitedCorrectly by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    val inviteLink = "https://www.beedone.com/invite/${selectedTeam.teamId}"

    fun setUserInvited(newUserInvited: String) {
        userInvited = newUserInvited
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.size(250.dp)
        ) {
            AsyncImage(
                model = getQrCodeBitmap(inviteLink),
                contentDescription = "Invitation QR",
                modifier = Modifier.fillMaxSize(1f)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(IntrinsicSize.Max)
            ) {
                Text(
                    text = inviteLink,
                    modifier = Modifier

                        .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                        .padding(8.dp),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            Column {
                IconButton(onClick = { clipboardManager.setText(AnnotatedString(inviteLink)) }) {
                    Icon(
                        painter = painterResource(R.drawable.copy_icon), contentDescription = "Copy"
                    )
                }
            }

        }

        CreateTextFieldError(
            value = userInvited,
            error = userInvitedError,
            setValue = { newUser -> setUserInvited(newUser) },
            keyboardType = KeyboardType.Text,
            label = "New User",
            placeholder = "@username"
        )

        Text(text = userInvitedCorrectly, color = Color(36, 133, 62))

        FloatingActionButton(shape = RoundedCornerShape(25.dp),
            containerColor = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border( 2.dp, lightBlue, RoundedCornerShape(25.dp) ),
            onClick = {
                //user not found
                if (!allUsers.map(User::userNickname).contains(userInvited)) {
                    userInvitedError = "User Doest Exist"
                    userInvitedCorrectly = ""
                }
                //user already invited
                else if (selectedTeam.teamUsers.map(TeamMember::user).map(User::userNickname)
                        .contains(userInvited)
                ) {
                    userInvitedError = "User already in the group!"
                    userInvitedCorrectly = ""
                }
                //user invited correctly
                else {
                    val userToAdd = allUsers.find { user: User -> user.userNickname == userInvited }
                    if (userToAdd != null) {
                        selectedTeam.teamUsers.add(TeamMember(userToAdd, Role.Invited, 0))
                        userToAdd.userTeams.add(Pair(selectedTeam, false))
                    }
                    userInvitedError=""
                    userInvitedCorrectly = "User invited correctly!"
                }
            }) {
            Text(text = "Invite User")
        }
    }
}

@Composable
fun InviteUser(
    allTeams: MutableList<Team>,
    teamId: String,
    userToAdd: User,
    showTeamDetailsPane: (String) -> Unit,
    teamListPane: () -> Unit,
    acceptInvitationPane: (String) -> Unit,
) {

    val teamToInvite = allTeams.find { it.teamId == teamId }

    if (teamToInvite != null) {

        //not in the team
        if (!teamToInvite.teamUsers.map(TeamMember::user).contains(loggedUser)) {
            teamToInvite.teamUsers.add(TeamMember(userToAdd, Role.Invited, 0))
            loggedUser.userTeams.add(Pair(teamToInvite, false))
            acceptInvitationPane(teamId)
        }
        //already invited in the team
        else if (teamToInvite.teamUsers.any { it.user == loggedUser && it.role == Role.Invited }) {
            acceptInvitationPane(teamId)
        }
        //already joined the team
        else {
            showTeamDetailsPane(teamToInvite.teamId)
        }
    }
    //Wrong id -> just show the team list
    else {
        teamListPane()
    }
}

@Composable
fun InvitedPane(
    showTeamDetailsPane: (String) -> Unit,
    navigateBack: () -> Unit,
    teamListPane: () -> Unit,
    selectedTeam: Team
) {

    var hours by remember { mutableIntStateOf(0) }
    var hoursError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                textAlign = TextAlign.Center,
                text = "YOU HAVE BEEN INVITED TO JOIN \"${selectedTeam.teamName}\"",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                fontSize = 16.sp,
                text = "If you want to join the team, enter the number of hours per week you want to dedicate to this team and click ACCEPT"
            )
            Spacer(modifier = Modifier.height(20.dp))


            CreateTextFieldError(
                value = hours.toString(),
                error = hoursError,
                setValue = { newVal ->
                    hours = if (newVal == "") {
                        "0".toInt()
                    } else {
                        newVal.toInt()
                    }

                },
                label = "Number of hours",
                keyboardType = KeyboardType.Number,
            )


            FloatingActionButton(shape = RoundedCornerShape(25.dp),
                containerColor = Color.White,
                modifier = Modifier.border(2.dp, lightBlue, RoundedCornerShape(25.dp)),
                onClick = {
                    if (hours < 0) {
                        hoursError = "Hours cannot be less than 0"
                    } else {

                        selectedTeam.deleteUser(loggedUser)
                        selectedTeam.addUser(TeamMember(loggedUser, Role.Participant, hours))
                        loggedUser.userTeams.add(Pair(selectedTeam, false))
                        navigateBack()
                        showTeamDetailsPane(selectedTeam.teamId)
                    }

                }) {
                Text(
                    modifier = Modifier.padding(25.dp, 0.dp), text = "ACCEPT"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            FloatingActionButton(shape = RoundedCornerShape(25.dp),
                containerColor = Color.White,
                modifier = Modifier.border(2.dp, lightBlue, RoundedCornerShape(25.dp)),
                onClick = {
                    selectedTeam.deleteUser(loggedUser)
                    teamListPane()
                }) {
                Text(
                    modifier = Modifier.padding(25.dp, 0.dp), text = "REJECT"
                )
            }
        }
    }
}

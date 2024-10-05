package it.polito.BeeDone.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.polito.BeeDone.utils.CreateErrorText
import it.polito.BeeDone.utils.addSpacesToSentence
import it.polito.BeeDone.utils.lightBlue
import it.polito.BeeDone.utils.myShape

@Composable
fun HomePane(
    allUser: MutableList<User>,
    showUserInformationPane: (String) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "This is a temporary home page for using the" +
                " application in the absence of authentication and a database",
            modifier = Modifier.fillMaxWidth(0.8f))
        Spacer(modifier = Modifier.size(20.dp))
        CreateDropdownLoggedProfile(loggedUser, allUser, showUserInformationPane)
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateDropdownLoggedProfile(
    value: User?,
    users: List<User>,
    showUserInformationPane: (String) -> Unit

) {
    var isExpanded by remember {            //Used to decide wether the DropDown is expanded or not
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it}
    ) {
        OutlinedTextField(
            value = addSpacesToSentence(value!!.userNickname),
            isError = false,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "User *") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            shape = myShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lightBlue,
                focusedLabelColor = lightBlue,
                focusedTextColor = Color.DarkGray
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false},
            modifier = Modifier.background(Color.White)
        ) {
            for (t in users){
                DropdownMenuItem(
                    text = { Text(t.userNickname) },
                    onClick = {
                        loggedUser =t
                        isExpanded = false
                        showUserInformationPane(loggedUser.userNickname)
                    },
                )
            }
        }
    }
    CreateErrorText(error = "")
    Spacer(modifier = Modifier.height(16.dp))
}
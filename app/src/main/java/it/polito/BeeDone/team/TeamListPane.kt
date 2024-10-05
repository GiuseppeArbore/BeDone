package it.polito.BeeDone.team

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.polito.BeeDone.R
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.utils.TeamBox
import it.polito.BeeDone.utils.lightBlue
import me.saket.cascade.CascadeDropdownMenu

@SuppressLint("UnrememberedMutableState")
@Composable
fun TeamListPane(
    showingTeams: SnapshotStateList<Pair<Team, Boolean>>,
    allTeams: MutableList<Pair<Team, Boolean>>,
    clearTeamInformation: () -> Unit,
    createTeamPane: () -> Unit,
    showTeamDetailsPane: (String) -> Unit,
    acceptInvitationPane: (String) -> Unit
) {

    var state by remember { mutableIntStateOf(0) }

    //show only teams in which the user is a participant
    if(state==0)
        showingTeams.removeAll  { team -> team.first.teamUsers.any { it.user== loggedUser && it.role==Role.Invited } }


    Column {
        TabRow(selectedTabIndex = state) {

            Tab(
                text = { Text(text = "My teams") },
                selected = state == 0,
                onClick = {
                    state = 0

                    //show only teams in which the user is a partecipant
                    showingTeams.removeAll { true }
                    showingTeams.addAll( allTeams.filter { it.first.teamUsers.map(TeamMember::user).contains(loggedUser) })
                    showingTeams.removeAll  { team -> team.first.teamUsers.any { it.user== loggedUser && it.role==Role.Invited } }
                }
            )
            Tab(
                text = { Text(text = "Pending invitation") },
                selected = state == 1,
                onClick = {
                    state = 1

                    //show only teams in which the user is invited
                    showingTeams.removeAll { true }
                    showingTeams.addAll( allTeams.filter { team -> team.first.teamUsers.any{it.user== loggedUser && it.role==Role.Invited} })
                }
            )
        }

        Box(
            Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxHeight(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.Top
            ) {
                items(showingTeams) { team ->
                    TeamBox(team, showTeamDetailsPane, acceptInvitationPane)
                }
            }

            //create a new team
            FloatingActionButton(
                onClick = {
                    clearTeamInformation()
                    createTeamPane()
                },
                shape = CircleShape,
                containerColor = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .size(70.dp)
                    .align(Alignment.BottomEnd)
                    .offset(5.dp, 0.dp)
                    .border(2.dp, lightBlue, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create New Team",
                    Modifier.size(30.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun TeamListMenu(
    showingTeams: MutableList<Pair<Team, Boolean>>,
    allTeams: SnapshotStateList<Pair<Team, Boolean>>
) {
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    var filterText by mutableStateOf("")

    Row(horizontalArrangement = Arrangement.End) {

        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            showSortMenu = !showSortMenu
        }) {
            Icon(
                painter = painterResource(R.drawable.sorting),
                contentDescription = "Sorting",
                modifier = Modifier.size(25.dp)
            )
        }

        IconButton(onClick = {
            showFilterMenu = !showFilterMenu
        }) {
            Icon(
                painter = painterResource(R.drawable.filter),
                contentDescription = "Filter",
                modifier = Modifier.size(25.dp)
            )
        }

        //SORT MENU
        CascadeDropdownMenu(modifier = Modifier
            .border(1.dp, lightBlue, RoundedCornerShape(20.dp))
            .background(Color.White),
            expanded = showSortMenu,
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = { showSortMenu = false }) {

            //sort by Deadline
            DropdownMenuItem(modifier = Modifier.background(Color.White), childrenHeader = {
                DropdownMenuHeader(Modifier.background(Color.White)) {
                    Text(text = "Sort by Team Name")
                }
            }, text = { Text(text = "Sort by Team Name") }, children = {

                //Sort By Ascending deadline
                androidx.compose.material3.DropdownMenuItem(modifier = Modifier
                    .background(Color.White)
                    .border(Dp.Hairline, lightBlue),
                    text = { Text(text = "Ascending") },
                    onClick = {
                        showingTeams.sortBy { it.first.teamName }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ascending),
                            contentDescription = "Ascending",
                            modifier = Modifier.size(22.dp)
                        )
                    })
                //Sort By Descending deadline
                androidx.compose.material3.DropdownMenuItem(
                    modifier = Modifier.background(Color.White),
                    text = { Text(text = "Descending") },
                    onClick = { showingTeams.sortByDescending { it.first.teamName } },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.descending),
                            contentDescription = "Descending",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                )
            })

            //sort by Creation date
            DropdownMenuItem(modifier = Modifier
                .background(Color.White)
                .border(Dp.Hairline, lightBlue),
                childrenHeader = {
                    DropdownMenuHeader(Modifier.background(Color.White)) {
                        Text(text = "Sort by Creation Date")
                    }
                },
                text = { Text(text = "Sort by Creation Date") },
                children = {
                    //Sort By Ascending Creation date
                    androidx.compose.material3.DropdownMenuItem(modifier = Modifier
                        .background(Color.White)
                        .border(Dp.Hairline, lightBlue),
                        text = { Text(text = "Ascending") },
                        onClick = { showingTeams.sortBy { it.first.teamCreationDate } },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ascending),
                                contentDescription = "Ascending",
                                modifier = Modifier.size(22.dp)
                            )
                        })
                    androidx.compose.material3.DropdownMenuItem(modifier = Modifier.background(
                        Color.White
                    ),
                        text = { Text(text = "Descending") },
                        onClick = { showingTeams.sortByDescending { it.first.teamCreationDate } },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.descending),
                                contentDescription = "Descending",
                                modifier = Modifier.size(20.dp)
                            )
                        })
                }
            )
        }

        //FILTER MENU
        CascadeDropdownMenu(
            modifier = Modifier
                .border(1.dp, lightBlue, RoundedCornerShape(20.dp))
                .background(Color.White),
            expanded = showFilterMenu,
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = {
                showFilterMenu = false
                filterText = ""
            },
        ) {

            DropdownMenuItem(modifier = Modifier
                .background(Color.White)
                .border(Dp.Hairline, lightBlue),
                childrenHeader = {
                    DropdownMenuHeader(Modifier.background(Color.White)) {
                        filterText = ""
                        Text(text = "Filter by Team Name")
                    }
                },
                text = { Text(text = "Filter by Team Name") },
                children = {
                    androidx.compose.material3.DropdownMenuItem(
                        modifier = Modifier.background(Color.White),
                        text = {
                            Column {
                                OutlinedTextField(
                                    placeholder = { Text(text = "Insert Team Name") },
                                    value = filterText,
                                    onValueChange = { newText -> filterText = newText },
                                    maxLines = 1,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = lightBlue,
                                        focusedLabelColor = lightBlue,
                                        focusedTextColor = Color.DarkGray
                                    ),
                                )

                                FloatingActionButton(
                                    onClick = { showingTeams.removeAll { it.first.teamName != filterText } },
                                    containerColor = Color.White,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp, 10.dp)
                                        .border(2.dp, lightBlue, RoundedCornerShape(20.dp))
                                        .height(30.dp)
                                ) {
                                    Text(text = "Apply Filter")
                                }
                            }
                        },
                        onClick = { showingTeams.removeAll { it.first.teamName != filterText } },
                        interactionSource = MutableInteractionSource()
                    )
                })

            //Filter by Category
            DropdownMenuItem(modifier = Modifier
                .background(Color.White)
                .border(Dp.Hairline, lightBlue),
                childrenHeader = {
                    DropdownMenuHeader(Modifier.background(Color.White)) {
                        filterText = ""
                        Text(text = "Filter by Category")
                    }
                },
                text = { Text(text = "Filter by Category") },
                children = {
                    androidx.compose.material3.DropdownMenuItem(
                        modifier = Modifier.background(Color.White),
                        text = {
                            Column {
                                OutlinedTextField(
                                    placeholder = { Text(text = "Insert Category") },
                                    value = filterText,
                                    onValueChange = { newText -> filterText = newText },
                                    maxLines = 1,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = lightBlue,
                                        focusedLabelColor = lightBlue,
                                        focusedTextColor = Color.DarkGray
                                    ),
                                )

                                FloatingActionButton(
                                    onClick = { showingTeams.removeAll { it.first.teamCategory != filterText } },
                                    containerColor = Color.White,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp, 10.dp)
                                        .border(2.dp, lightBlue, RoundedCornerShape(20.dp))
                                        .height(30.dp)
                                ) {
                                    Text(text = "Apply Filter")
                                }
                            }
                        },
                        onClick = { showingTeams.removeAll { it.first.teamCategory != filterText } },
                        interactionSource = MutableInteractionSource()
                    )
                })

            //Remove all filters
            androidx.compose.material3.DropdownMenuItem(modifier = Modifier
                .background(Color.White)
                .border(Dp.Hairline, lightBlue),
                text = { Text("Remove Filters") },
                onClick = {
                    showingTeams.removeAll { true }
                    showingTeams.addAll(allTeams)
                }
            )
        }
    }
}

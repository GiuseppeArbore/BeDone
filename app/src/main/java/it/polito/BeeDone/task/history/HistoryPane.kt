package it.polito.BeeDone.task.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.polito.BeeDone.profile.User
import it.polito.BeeDone.utils.EventRow

@Composable
fun TaskHistoryPane(
    history: List<Event>,
    showUserInformationPane: (String) -> Unit
) {

    Box(Modifier.padding(horizontal = 15.dp, vertical = 5.dp)) {
        LazyColumn(
            Modifier
                .fillMaxHeight(), verticalArrangement = Arrangement.Top
        ) {
            items(history) { event ->
                EventRow(event = event, showUserInformationPane)
                HorizontalDivider(Modifier.padding(15.dp, 5.dp), thickness = Dp.Hairline, color = Color.Gray)
            }
        }

    }
}

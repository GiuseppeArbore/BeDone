package it.polito.BeeDone.task.history

import it.polito.BeeDone.profile.User
import it.polito.BeeDone.task.TaskStatus

class Event(
    var title: String,
    var date: String,
    var taskStatus: TaskStatus,
    var user: User,
    var taskDoneSubtasks: String,
    var taskTotalSubtasks: String,
    var taskChanges: List<String>
) {
    constructor() : this("", "", TaskStatus.Pending, User(), "", "", mutableListOf())
}
package it.polito.BeeDone.task.timer

import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser

class TaskTimer (
    var ticks: Int,
    var date: String,
    var title: String,
    var user: User
) {
    constructor() : this(0, "", "", loggedUser)
}
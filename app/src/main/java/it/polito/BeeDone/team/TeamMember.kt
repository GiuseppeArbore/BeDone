package it.polito.BeeDone.team

import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser

enum class Role {
    Invited,                    //User invited to the team and not yet joined
    Participant,                //User joined the team and has a regular role
    Admin                       //User is an admin of the team (only another admin or the creator of the team can promote a user to admin)
}

class TeamMember(
    var user: User,
    var role: Role,
    var timePartecipation: Int
) {
    constructor(): this(loggedUser, Role.Invited, 0)
}
package it.polito.BeeDone.utils.questions_answers

import it.polito.BeeDone.profile.User

class Question (
    var questionId: Int,
    var text: String,
    var date: String,
    var user: User,
    var answers: MutableList<Answer>
) {
    constructor() : this(0, "", "", User(), mutableListOf())
}
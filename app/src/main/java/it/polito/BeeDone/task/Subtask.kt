package it.polito.BeeDone.task

class Subtask(
    var subtaskTitle: String, var subtaskState: State
) {
    constructor() : this("", State.NotCompleted)
}

enum class State {
    NotCompleted, Completed
}

fun setState(sub: Subtask?) {
    sub?.subtaskState = State.Completed
}
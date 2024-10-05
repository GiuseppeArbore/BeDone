package it.polito.BeeDone.task

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import it.polito.BeeDone.profile.User
import it.polito.BeeDone.profile.loggedUser
import it.polito.BeeDone.task.history.Event
import it.polito.BeeDone.utils.questions_answers.Answer
import it.polito.BeeDone.utils.questions_answers.Question
import it.polito.BeeDone.task.timer.TaskTimer
import it.polito.BeeDone.team.Team
import it.polito.BeeDone.utils.addSpacesToSentence
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

enum class Repeat {
    NoRepeat, OncePerWeek, OnceEveryTwoWeeks, OncePerMonth
}

enum class TaskStatus{
    Completed, ExpiredNotCompleted, ExpiredCompleted, InProgress, Pending
}

private val taskCount: AtomicInteger = AtomicInteger(1)

class Task(
    var taskId: Int,
    var taskTitle: String,
    var taskDescription: String,
    var taskDeadline: String,
    var taskTag: String,
    var taskCategory: String,
    var taskUsers: MutableList<User>,
    var taskRepeat: Repeat,
    var taskSubtasks: SnapshotStateList<Subtask>,
    var taskTeam: Team,
    var taskHistory: MutableList<Event>,
    var taskQuestions: MutableList<Question>,
    var taskStatus: TaskStatus,
    var taskTimerHistory: MutableList<TaskTimer>,
    var taskMediaList: SnapshotStateList<Media>,
    var taskLinkList: SnapshotStateList<String>,
    var taskDocumentList: SnapshotStateList<Document>,
    var taskCreator: User,
    var taskCreationDate: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(
        taskId = taskCount.incrementAndGet(),
        taskTitle = "",
        taskDescription = "",
        taskDeadline = "",
        taskTag = "",
        taskCategory = "",
        taskUsers = mutableListOf(),
        taskRepeat = Repeat.NoRepeat,
        taskSubtasks = mutableStateListOf(),
        taskTeam = Team(),
        taskHistory = mutableListOf(),
        taskQuestions = mutableListOf(),
        taskStatus = TaskStatus.Pending,
        taskTimerHistory = mutableListOf(),
        taskMediaList = mutableStateListOf(),
        taskLinkList = mutableStateListOf(),
        taskDocumentList = mutableStateListOf(),
        loggedUser,
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    )
}

//this is the task that is currently shown

@SuppressLint("MutableCollectionMutableState")
class TaskViewModel : ViewModel() {

    var allTasks = mutableStateListOf<Task>()
    var showingTasks = mutableStateListOf<Task>()

    fun addTask(newTask: Task) {
        allTasks.add(newTask)
    }

    //Task Title
    var taskTitleValue by mutableStateOf("")
        private set
    var taskTitleError by mutableStateOf("")
        private set

    fun setTaskTitle(n: String) {
        taskTitleValue =
            n.replaceFirstChar { it.uppercase() }   //returns the string with only the initial capital letter
    }

    private fun checkTitle() {
        if (taskTitleValue == "") {
            taskTitleError = "Title cannot be empty"
        } else {
            taskTitleError = ""
        }
    }

    //Task Description
    var taskDescriptionValue by mutableStateOf("")
        private set

    fun setTaskDescription(n: String) {
        taskDescriptionValue = n
    }

    //Task Deadline
    var taskDeadlineValue by mutableStateOf("")
        private set
    var taskDeadlineError by mutableStateOf("")
        private set

    fun setTaskDeadline(n: String) {
        taskDeadlineValue = n
    }

    private fun checkDeadline() {
        if (taskDeadlineValue == "") {
            taskDeadlineError = "Deadline cannot be empty"
        } else {
            taskDeadlineError = ""
        }
    }

    //Task Tag
    var taskTagValue by mutableStateOf("")
        private set

    fun setTaskTag(n: String) {
        taskTagValue = n
    }

    //Task Category
    var taskCategoryValue by mutableStateOf("")
        private set
    var taskCategoryError by mutableStateOf("")
        private set

    fun setTaskCategory(n: String) {
        taskCategoryValue = n
    }

    private fun checkCategory() {
        if (taskCategoryValue == "") {
            taskCategoryError = "Category cannot be empty"
        } else {
            taskCategoryError = ""
        }
    }

    //Task Users
    var taskUsersValue by mutableStateOf(       //List of Users assigned to a certain task
        mutableListOf<User>()
    )
        private set

    fun setTaskUsers(n: User) {
        taskUsersValue.add(n)
    }

    fun deleteTaskUsers(n: User) {
        taskUsersValue.remove(n)
    }

    fun assignTaskUsers(n: MutableList<User>) {
        taskUsersValue = n
    }

    //Task Repeat
    var taskRepeatValue by mutableStateOf(Repeat.NoRepeat)
        private set

    fun setTaskRepeat(n: Repeat) {
        taskRepeatValue = n
    }

    //Task subtasks
    //Variable used to insert a new subtask in the list of the subtasks
    var taskSubtaskValue by mutableStateOf("")
        private set

    /*   fun setTaskSubtask(n: String) {
           taskSubtaskValue = n
       } */

    var taskSubtasksValue by mutableStateOf(
        mutableStateListOf<Subtask>()
    )
        private set

    /*   fun setTaskSubtasks(n: String) {
           taskSubtasksValue.add(n)
       } */
    fun assignTaskSubtasks(n: SnapshotStateList<Subtask>) {
        taskSubtasksValue = n
    }

    fun addTaskSubtasks(n: Subtask) {
        taskSubtasksValue.add(n)
    }

    //Task Team
    var taskTeamValue by mutableStateOf(Team())
        private set
    var taskTeamError by mutableStateOf("")
        private set

    fun setTaskTeam(n: Team) {
        taskTeamValue = n
    }

    fun checkTaskTeam() {
        if(taskTeamValue.teamName == "") {
            taskTeamError = "Team cannot be blank"
        }
        else {
            taskTeamError = ""
        }
    }

    //attachments of a task
    var taskMediaListValue by mutableStateOf(
        mutableStateListOf<Media>()
    )
        private set

    var taskLinkListValue by mutableStateOf(
        mutableStateListOf<String>()
    )
        private set

    var taskDocumentListValue by mutableStateOf(
        mutableStateListOf<Document>()
    )
        private set

    var taskLinkValue by mutableStateOf("")
        private set

    fun setTaskMediaList(n: Media) {
        taskMediaListValue.add(n)
    }
    fun assignTaskMediaList(n: SnapshotStateList<Media>) {
        taskMediaListValue = n
    }

    fun setTaskLink(n: String) {
        taskLinkValue = n
    }

    fun setTaskLinkList(n: String) {
        taskLinkListValue.add(n)
    }
    fun assignTaskLinkList(n: SnapshotStateList<String>) {
        taskLinkListValue = n
    }


    fun setTaskDocumentList(n: Document) {
        taskDocumentListValue.add(n)
    }
    fun assignTaskDocumentList(n: SnapshotStateList<Document>) {
        taskDocumentListValue = n
    }

    //Task Questions
    //Single instance of Task Question
    var taskQuestionValue by mutableStateOf("")       //Text of a new question
        private set

    fun setTaskQuestion(n: String) {
        taskQuestionValue = n
    }

    //List of Task Questions
    var taskQuestionsValue by mutableStateOf(               //List of all questions related to a certain task
        mutableListOf<Question>()
    )
        private set

    @SuppressLint("SimpleDateFormat")
    fun setTaskQuestions(n: String, selectedTask: Task) {
        val q = Question(
            if(taskQuestionsValue.size == 0) 1 else taskQuestionsValue.maxOf { it.questionId }+1,
            n,
            SimpleDateFormat("dd/MM/yyyy").format(Date()),
            loggedUser,
            mutableListOf<Answer>()
        )                                                   //When we will have a working login, the Profile will be the one of the currently logged in user
        taskQuestionsValue.add(q)
        selectedTask.taskQuestions = taskQuestionsValue
    }

    fun assignTaskQuestions(n: MutableList<Question>) {
        taskQuestionsValue = n
    }

    //Task Answers
    //Single instance of Task Answer
    var taskAnswerValue by mutableStateOf("")           //Text of a new answer
        private set

    fun setTaskAnswer(n: String) {
        taskAnswerValue = n
    }

    //List of Task Answers
    var taskAnswersValue by mutableStateOf(                   //List of all answers related to a certain question
        mutableListOf<Answer>()
    )
        private set

    @SuppressLint("SimpleDateFormat")
    fun setTaskAnswers(n: String, selectedQuestion: Question) {
        val a = Answer(
            n,
            SimpleDateFormat("dd/MM/yyyy").format(Date()),
            loggedUser
        )
        taskAnswersValue.add(a)
        selectedQuestion.answers = taskAnswersValue
    }

    fun assignTaskAnswers(n: MutableList<Answer>) {
        taskAnswersValue = n
    }

    //Task History
    //Variable used to insert a new user in the list of the users

    var taskHistoryValue by mutableStateOf(
        mutableListOf<Event>()
    )
        private set

    fun addTaskEventToHistory(n: Event) {
        taskHistoryValue.add(n)
    }

    fun assignTaskHistory(n: MutableList<Event>) {
        taskHistoryValue = n
    }

    var taskStatusValue by mutableStateOf(TaskStatus.Pending)

    fun setTaskStatus(n: TaskStatus) {
        taskStatusValue = n
    }

    var taskTimerTitleValue by mutableStateOf("")
        private set
    fun setTaskTimerTitle(n: String) {
        taskTimerTitleValue = n
    }

    var taskTimerValue by mutableStateOf("0:00:00")
        private set
    fun setTaskTimer(n: String) {
        taskTimerValue = n
    }

    var taskTimerHistory by mutableStateOf<MutableList<TaskTimer>>(mutableListOf())
        private set
    @SuppressLint("SimpleDateFormat")
    fun addTaskTimerHistory(n: Int) {
        taskTimerHistory.add(
            0,
            TaskTimer(
                ticks = n,
                date = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                title = taskTimerTitleValue,
                user = loggedUser
            )
        )
    }
    fun assignTaskTimerHistory(n: MutableList<TaskTimer>) {
        taskTimerHistory = n
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setTaskAsCompleted() {
        if(LocalDate.parse(taskDeadlineValue, DateTimeFormatter.ofPattern("dd/MM/uuuu")) < LocalDate.now()){
            taskStatusValue = TaskStatus.ExpiredCompleted
        }else{
            taskStatusValue = TaskStatus.Completed

        }
        addTaskEventToHistory(
            Event(
                "Task Completed",
                SimpleDateFormat("dd/MM/yyyy").format(Date()),
                taskStatusValue,
                loggedUser,
                taskSubtasksValue.size.toString(),
                taskSubtasksValue.size.toString(),
                mutableListOf()
            )
        )
    }

    private var oldTaskTitleValue: String = taskTitleValue
    private var oldTaskCategoryValue: String = taskCategoryValue
    private var oldTaskDeadlineValue: String = taskDeadlineValue
    private var oldTaskDescriptionValue: String = taskDescriptionValue
    private var oldTaskTagValue: String = taskTagValue
    private var oldTaskUsersValue: MutableList<User> = taskUsersValue.toMutableList()
    private var oldTaskRepeatValue: Repeat = taskRepeatValue
    private var oldTaskSubtasksValue: SnapshotStateList<Subtask> = taskSubtasksValue.toMutableStateList()
    private var oldTaskSubtaskValue: String = taskSubtaskValue
    private var oldTaskTeamValue: Team = taskTeamValue
    private var oldTaskHistoryValue: MutableList<Event> = taskHistoryValue.toMutableList()
    private var oldTaskStatusValue: TaskStatus = taskStatusValue

    /* information validation */
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun validateTaskInformation(
        showTaskDetailsPane: ((Int) -> Unit)? = null,               //When I am creating a task, I want to navigate to showTaskDetailsPane
        navigateBack: (() -> Unit)? = null,                         //When I am editing a task, I want to navigate back
        task: Task?
    ) {
        var selectedTask: Task
        if(task != null) {
            selectedTask = task
        }
        else {
            selectedTask = Task()
        }

        checkTitle()
        checkDeadline()
        checkCategory()
        checkTaskTeam()

        if (taskTitleError.isBlank() && taskDeadlineError.isBlank() && taskCategoryError.isBlank() && taskTeamError.isBlank()) {

            if (showTaskDetailsPane != null) {          //If showTaskDetailsPane is not null, I am creating a new tsk. Otherwise, I am editing a task, so navigateBack isn't null
                if (taskUsersValue.size>0){
                    taskStatusValue=TaskStatus.InProgress
                }else{
                    taskStatusValue=TaskStatus.Pending
                }
                val tmpTask = Task(
                    taskCount.incrementAndGet(),
                    taskTitleValue,
                    taskDescriptionValue,
                    taskDeadlineValue,
                    taskTagValue,
                    taskCategoryValue,
                    taskUsersValue.toMutableList(),
                    taskRepeatValue,
                    taskSubtasksValue.toMutableStateList(),
                    taskTeamValue,
                    taskHistoryValue,
                    mutableListOf<Question>(),
                    taskStatusValue,
                    mutableListOf(),
                    mutableStateListOf(),
                    mutableStateListOf(),
                    mutableStateListOf(),
                    loggedUser,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))
                )

                addTask(tmpTask)

                val str : MutableList<String> = mutableListOf()
                val taskChangesTmp: String = "Title: $taskTitleValue \n"+
                        "Description: $taskDescriptionValue \n" +
                        "Deadline: $taskDeadlineValue \n" +
                        "Category: $taskCategoryValue \n" +
                        "Repeat: $taskRepeatValue \n" +
                        "Team: ${taskTeamValue.teamName} \n" +
                        "Users: " +
                        taskUsersValue.map { u-> u.userNickname }.toSet().joinToString()

                str.add(0, taskChangesTmp)

                /*add creation event*/
                addTaskEventToHistory(
                    Event(
                        "Task created", SimpleDateFormat("dd/MM/yyyy").format(Date()), taskStatusValue,
                        loggedUser, "0", taskSubtasksValue.size.toString(), str
                    )
                )

                selectedTask = tmpTask

                for (u in selectedTask.taskUsers){
                    u.taskList.add(selectedTask)
                }

                taskTeamValue.teamTasks.add(selectedTask)
                setTaskInformation(selectedTask)
                navigateBack!!()
                navigateBack!!()
                updateOldTaskInformation()
                showTaskDetailsPane(selectedTask.taskId)
            } else {
                //Saves edit to a Task.

                selectedTask.taskTitle = taskTitleValue
                selectedTask.taskDescription = taskDescriptionValue
                selectedTask.taskDeadline = taskDeadlineValue
                selectedTask.taskTag = taskTagValue
                selectedTask.taskCategory = taskCategoryValue
                selectedTask.taskUsers = taskUsersValue.toMutableList()
                selectedTask.taskRepeat = taskRepeatValue
                selectedTask.taskSubtasks = taskSubtasksValue
                selectedTask.taskTeam = taskTeamValue
                selectedTask.taskHistory = taskHistoryValue
                selectedTask.taskStatus = taskStatusValue

                if (taskUsersValue.size>0){         //Update task status according to the number of users
                    selectedTask.taskStatus=TaskStatus.InProgress
                }else{
                    selectedTask.taskStatus=TaskStatus.Pending
                }

                //For every element, I check what was changed, so that I can add it to history
                val taskChangesTmp: MutableList<String> = mutableListOf<String>()
                if (taskTitleValue != oldTaskTitleValue) {
                    taskChangesTmp.add(
                        0, "Changed Title from \"$oldTaskTitleValue to $taskTitleValue\""
                    )
                }
                if (taskDescriptionValue != oldTaskDescriptionValue) {
                    taskChangesTmp.add(
                        0,
                        "Changed Description from \"$oldTaskDescriptionValue\" to \"$taskDescriptionValue\""
                    )
                }
                if (taskTagValue != oldTaskTagValue) {
                    taskChangesTmp.add(
                        0, "Changed Tag from \"$oldTaskTagValue\" to \"$taskTagValue\""
                    )
                }
                if (taskCategoryValue != oldTaskCategoryValue) {
                    taskChangesTmp.add(
                        0,
                        "Changed Category from \"$oldTaskCategoryValue\" to \"$taskCategoryValue\""
                    )
                }
                if (taskUsersValue != oldTaskUsersValue) {
                    var stringUserChanges=""
                    if ((taskUsersValue.map(User::userNickname) subtract oldTaskUsersValue.map(
                            User::userNickname
                        )).isNotEmpty()
                    ){
                        stringUserChanges += "Added users: \n"
                        stringUserChanges += (taskUsersValue.map(User::userNickname) subtract oldTaskUsersValue.map(
                            User::userNickname
                        ).toSet()).joinToString()
                        stringUserChanges+="\n"
                    }

                    if((oldTaskUsersValue.map(User::userNickname) subtract taskUsersValue.map(
                            User::userNickname
                        )).isNotEmpty()){
                        stringUserChanges += "Removed users: \n"
                        stringUserChanges += (oldTaskUsersValue.map(User::userNickname) subtract taskUsersValue.map(
                            User::userNickname
                        ).toSet()).joinToString()
                    }

                    taskChangesTmp.add(
                        0,
                        stringUserChanges
                    )           //Subtract one list from the other to obtain only the users added (taskUsersValue - oldTaskUsersValue) and only the users removedc (oldTaskUsersValue - taskUsersValue)
                }
                if (taskRepeatValue != oldTaskRepeatValue) {
                    taskChangesTmp.add(
                        0,
                        "Changed Repetition from \"${addSpacesToSentence(oldTaskRepeatValue.toString())}\" to \"${
                            addSpacesToSentence(taskRepeatValue.toString())
                        }\""
                    )
                }
                //No check on Subtasks and Team

                addTaskEventToHistory(
                    if(taskUsersValue.size>0) {
                        setTaskStatus(TaskStatus.InProgress)
                        Event(
                            "Task edited",
                            SimpleDateFormat("dd/MM/yyyy").format(Date()),
                            TaskStatus.InProgress,
                            loggedUser,
                            selectedTask.taskSubtasks.filter { s -> s.subtaskState == State.Completed }.size.toString(),
                            taskSubtasksValue.size.toString(),
                            taskChangesTmp
                        )
                    }else{
                        setTaskStatus(TaskStatus.Pending)
                        Event(
                            "Task edited",
                            SimpleDateFormat("dd/MM/yyyy").format(Date()),
                            TaskStatus.Pending,
                            loggedUser,
                            selectedTask.taskSubtasks.filter { s -> s.subtaskState == State.Completed }.size.toString(),
                            taskSubtasksValue.size.toString(),
                            taskChangesTmp
                        )
                    }
                )

                for (u in taskUsersValue) {
                    if(!oldTaskUsersValue.contains(u)) {
                        u.taskList.add(selectedTask)
                    }
                }

                for (u in oldTaskUsersValue) {
                    if(!taskUsersValue.contains(u)) {
                        u.taskList.remove(selectedTask)
                    }
                }

                updateOldTaskInformation()
                navigateBack!!()
            }
        }
    }

    fun updateOldTaskInformation() {
        oldTaskTitleValue = taskTitleValue
        oldTaskRepeatValue = taskRepeatValue
        oldTaskCategoryValue = taskCategoryValue
        oldTaskDeadlineValue = taskDeadlineValue
        oldTaskTagValue = taskTagValue
        oldTaskDescriptionValue = taskDescriptionValue
        oldTaskUsersValue = taskUsersValue.toMutableList()
        oldTaskSubtasksValue = taskSubtasksValue.toMutableStateList()
        oldTaskSubtaskValue = taskSubtaskValue
        oldTaskTeamValue = taskTeamValue
        oldTaskHistoryValue = taskHistoryValue.toMutableList()
        oldTaskStatusValue = taskStatusValue
    }

    //function set the old values, ignoring the changes
    fun noUpdateTaskInformation() {
        //set old values
        taskTitleValue = oldTaskTitleValue
        taskRepeatValue = oldTaskRepeatValue
        taskCategoryValue = oldTaskCategoryValue
        taskDeadlineValue = oldTaskDeadlineValue
        taskTagValue = oldTaskTagValue
        taskDescriptionValue = oldTaskDescriptionValue
        taskUsersValue = oldTaskUsersValue
        taskSubtasksValue = oldTaskSubtasksValue
        taskSubtaskValue = oldTaskSubtaskValue
        taskTeamValue = oldTaskTeamValue
        taskHistoryValue = oldTaskHistoryValue
        taskStatusValue = oldTaskStatusValue

        taskTitleError = ""
        taskDeadlineError = ""
        taskCategoryError = ""
        taskTeamError = ""
    }

    //function clear all values of task
    fun clearTaskInformation() {
        //reset infos
        taskTitleValue = ""
        taskRepeatValue = Repeat.NoRepeat
        taskCategoryValue = ""
        taskDeadlineValue = ""
        taskTagValue = ""
        taskDescriptionValue = ""
        taskUsersValue = mutableListOf<User>()
        taskSubtasksValue = mutableStateListOf<Subtask>()
        taskSubtaskValue = ""
        taskTeamValue = Team()
        taskQuestionsValue = mutableListOf<Question>()
        taskQuestionValue = ""
        taskHistoryValue = mutableListOf<Event>()
        taskTimerHistory = mutableListOf()


        taskTitleError = ""
        taskDeadlineError = ""
        taskCategoryError = ""
        taskTeamError = ""
    }

    fun setTaskInformation(selectedTask: Task) {
        setTaskTitle(selectedTask.taskTitle)
        setTaskDescription(selectedTask.taskDescription)
        setTaskDeadline(selectedTask.taskDeadline)
        setTaskTag(selectedTask.taskTag)
        setTaskCategory(selectedTask.taskCategory)
        assignTaskUsers(selectedTask.taskUsers.toMutableList())
        setTaskRepeat(selectedTask.taskRepeat)
        assignTaskSubtasks(selectedTask.taskSubtasks.toMutableStateList())
        setTaskTeam(selectedTask.taskTeam)
        assignTaskQuestions(selectedTask.taskQuestions.toMutableList())
        assignTaskHistory(selectedTask.taskHistory)
        setTaskStatus(selectedTask.taskStatus)
        assignTaskTimerHistory(selectedTask.taskTimerHistory.toMutableList())
        assignTaskMediaList(selectedTask.taskMediaList)
        assignTaskLinkList(selectedTask.taskLinkList)
        assignTaskDocumentList(selectedTask.taskDocumentList)
        setTaskTimerTitle("")
        setTaskTimer("0:00:00")

        oldTaskTitleValue = taskTitleValue
        oldTaskRepeatValue = taskRepeatValue
        oldTaskCategoryValue = taskCategoryValue
        oldTaskDeadlineValue = taskDeadlineValue
        oldTaskTagValue = taskTagValue
        oldTaskDescriptionValue = taskDescriptionValue
        oldTaskUsersValue = taskUsersValue.toMutableList()
        oldTaskSubtasksValue = taskSubtasksValue.toMutableStateList()
        oldTaskSubtaskValue = taskSubtaskValue
        oldTaskTeamValue = taskTeamValue
        oldTaskHistoryValue = taskHistoryValue.toMutableList()
        oldTaskStatusValue = taskStatusValue
    }
}
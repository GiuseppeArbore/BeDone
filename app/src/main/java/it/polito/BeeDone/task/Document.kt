package it.polito.BeeDone.task

import android.net.Uri

class Document(
    var document: Uri, var date: String
) {
    constructor() : this(Uri.EMPTY, "")
}
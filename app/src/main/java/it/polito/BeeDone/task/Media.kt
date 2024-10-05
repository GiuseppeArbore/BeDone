package it.polito.BeeDone.task

import android.net.Uri

class Media(
    var image: Uri, var mediaDescription: String, var date: String
) {
    constructor() : this(Uri.EMPTY,"", "")
}

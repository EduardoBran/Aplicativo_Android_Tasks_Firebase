package com.luizeduardobrandao.tasksfirebasehilt.data.model

import com.luizeduardobrandao.tasksfirebasehilt.helper.Status

data class Task(
    // id do Firebase espera uma String
    var id: String = "",
    var description: String = "",
    var status: Status = Status.TODO
)
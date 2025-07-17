package com.luizeduardobrandao.tasksfirebasehilt.data.model

import android.os.Parcelable
import com.luizeduardobrandao.tasksfirebasehilt.helper.Status
import kotlinx.parcelize.Parcelize

@Parcelize                                 // Gera automaticamente os métodos de Parcelable
data class Task(
    var id: String = "",                   // ID no Firebase (String)
    var description: String = "",          // Descrição da tarefa
    var status: Status = Status.TODO       // Status atual (T0DO, DOING, DONE)
) : Parcelable                             // Permite passagem via SafeArgs
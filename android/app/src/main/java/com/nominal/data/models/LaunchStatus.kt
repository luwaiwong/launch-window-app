package com.nominal.data.models

enum class LaunchStatus(val id: Int, val displayName: String) {
    GO(1, "Go"),
    TBD(2, "TBD"),
    SUCCESS(3, "Success"),
    FAILURE(4, "Failure"),
    ON_HOLD(5, "On Hold"),
    IN_FLIGHT(6, "In Flight"),
    PARTIAL_FAILURE(7, "Partial Failure"),
    TBC(8, "TBC");

    companion object {
        fun fromId(id: Int): LaunchStatus {
            return entries.find { it.id == id } ?: TBD
        }
    }
}

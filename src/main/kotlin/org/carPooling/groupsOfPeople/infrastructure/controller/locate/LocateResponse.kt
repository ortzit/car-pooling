package org.carPooling.groupsOfPeople.infrastructure.controller.locate

import kotlinx.serialization.Serializable
import org.carPooling.groupsOfPeople.application.locate.LocateGroupOfPeopleResult

@Serializable
data class LocateResponse(
    val id: Int,
    val seats: Short
) {
    companion object {
        fun fromResult(result: LocateGroupOfPeopleResult): LocateResponse =
            LocateResponse(
                id = result.id,
                seats = result.seats
            )
    }
}
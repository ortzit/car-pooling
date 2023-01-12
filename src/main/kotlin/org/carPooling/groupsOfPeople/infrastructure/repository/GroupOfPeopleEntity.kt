package org.carPooling.groupsOfPeople.infrastructure.repository

import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId

data class GroupOfPeopleEntity(val id: Int, val people: Short) {
    fun toDomain(): GroupOfPeople =
        GroupOfPeople(
            id = GroupOfPeopleId(this.id),
            people = this.people
        )

    companion object {
        fun fromDomain(domain: GroupOfPeople): GroupOfPeopleEntity =
            GroupOfPeopleEntity(
                id = domain.id.value,
                people = domain.people
            )
    }
}

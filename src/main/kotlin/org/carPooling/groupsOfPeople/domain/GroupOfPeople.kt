package org.carPooling.groupsOfPeople.domain

data class GroupOfPeopleId(val value: Int)

data class GroupOfPeople(val id: GroupOfPeopleId, val people: Short)
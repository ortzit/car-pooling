package org.carPooling.groupsOfPeople.domain

import org.carPooling.cars.domain.CarId
import org.carPooling.shared.domain.CarPoolingRepository

interface GroupOfPeopleRepository : CarPoolingRepository {
    fun get(groupOfPeopleId: GroupOfPeopleId): GroupOfPeople?

    fun add(groupOfPeople: GroupOfPeople)

    fun getCarId(groupOfPeopleId: GroupOfPeopleId): CarId?

    fun setCarId(groupOfPeopleId: GroupOfPeopleId, targetCarId: CarId)

    fun remove(id: GroupOfPeopleId)

    fun findGroupOfPeopleWithUnassignedCar(availableSeats: Short): GroupOfPeople?
}
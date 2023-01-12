package org.carPooling.cars.domain

import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CarService @Autowired constructor(
    private val carRepository: CarRepository,
    private val groupOfPeopleRepository: GroupOfPeopleRepository
) {
    fun load(cars: Collection<Car>) {
        groupOfPeopleRepository.clear()
        carRepository.load(cars)
    }
}
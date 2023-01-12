package org.carPooling.shared.domain

import org.carPooling.cars.domain.CarId
import org.carPooling.cars.domain.CarRepository
import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CarAssignmentService @Autowired constructor(
    private val carRepository: CarRepository,
    private val groupOfPeopleRepository: GroupOfPeopleRepository
){
    fun assignCar(groupOfPeople: GroupOfPeople, carId: CarId) {
        carRepository.decreaseAvailableSeats(carId, groupOfPeople.people)
        groupOfPeopleRepository.setCarId(
            groupOfPeopleId = groupOfPeople.id,
            targetCarId = carId
        )
    }
}
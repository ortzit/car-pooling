package org.carPooling.groupsOfPeople.domain.add

import org.carPooling.cars.domain.CarRepository
import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.carPooling.shared.domain.CarAssignmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddGroupOfPeopleService @Autowired constructor(
    private val groupOfPeopleRepository: GroupOfPeopleRepository,
    private val carRepository: CarRepository,
    private val carAssignmentService: CarAssignmentService,
) {
    fun addAndAssignCarIfPossible(groupOfPeople: GroupOfPeople) {
        groupOfPeopleRepository.add(groupOfPeople)
        assignCarWithEnoughAvailableSeats(groupOfPeople)
    }

    private fun assignCarWithEnoughAvailableSeats(groupOfPeople: GroupOfPeople) {
        carRepository.findCarByGroupSize(groupOfPeople.people)
            ?.let { availableCar ->
                carAssignmentService.assignCar(
                    groupOfPeople = groupOfPeople,
                    carId = availableCar.id,
                )
            }
    }
}
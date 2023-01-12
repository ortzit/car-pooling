package org.carPooling.groupsOfPeople.domain.dropOff

import org.carPooling.cars.domain.CarId
import org.carPooling.cars.domain.CarRepository
import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.carPooling.shared.domain.CarAssignmentService
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DropOffGroupOfPeopleService @Autowired constructor(
    private val groupOfPeopleRepository: GroupOfPeopleRepository,
    private val carRepository: CarRepository,
    private val carAssignmentService: CarAssignmentService,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun dropOff(groupOfPeopleId: GroupOfPeopleId): CarId? {
        val groupOfPeople = groupOfPeopleRepository.get(groupOfPeopleId)
        return if (groupOfPeople != null) {
            deleteGroupOfPeopleData(groupOfPeople)
        } else {
            logger.info("Tried to drop off not existing group [${groupOfPeopleId.value}]")
            throw GroupOfPeopleNotFoundException("[${groupOfPeopleId.value}] group not found")
        }
    }

    private fun deleteGroupOfPeopleData(groupOfPeople: GroupOfPeople): CarId? =
        groupOfPeopleRepository.getCarId(groupOfPeople.id)?.let { groupCarId ->
            carRepository.increaseAvailableSeats(
                carId = groupCarId,
                value = groupOfPeople.people
            )
            groupCarId
        }.also {
            groupOfPeopleRepository.remove(groupOfPeople.id)
        }

    fun tryToFillFreeSeats(carId: CarId) {
        generateSequence {
            carRepository.get(carId)
                ?.let { groupOfPeopleRepository.findGroupOfPeopleWithUnassignedCar(it.availableSeats) }
        }
            .takeWhile { true }
            .forEach { targetGroupOfPeople ->
                carAssignmentService.assignCar(
                    groupOfPeople = targetGroupOfPeople,
                    carId = carId,
                )
            }
    }
}
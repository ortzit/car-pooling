package org.carPooling.groupsOfPeople.domain.locate

import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.CarRepository
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocateGroupOfPeopleService @Autowired constructor(
    private val groupOfPeopleRepository: GroupOfPeopleRepository,
    private val carRepository: CarRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun getAssignedCar(groupOfPeopleId: GroupOfPeopleId): Car? {
        val groupOfPeople = groupOfPeopleRepository.get(groupOfPeopleId)
        return if (groupOfPeople != null) {
            groupOfPeopleRepository.getCarId(groupOfPeople.id)?.let { carId ->
                carRepository.get(carId)!!
            }
        } else {
            logger.info("Searched assigned car of not existing groupOfPeopleId [${groupOfPeopleId.value}]")
            throw GroupOfPeopleNotFoundException("[${groupOfPeopleId.value}] group not found")
        }
    }
}
package org.carPooling.shared.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.carPooling.cars.domain.CarRepository
import org.carPooling.cars.domain.anyCarId
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeople
import org.junit.jupiter.api.Test

class CarAssignmentServiceTest {
    private val carRepository: CarRepository = mock()
    private val groupOfPeopleRepository: GroupOfPeopleRepository = mock()
    private val service: CarAssignmentService = CarAssignmentService(
        carRepository = carRepository,
        groupOfPeopleRepository = groupOfPeopleRepository,
    )

    @Test
    fun `should decrease car's available seats and set the car id to the group of people when assigning a car`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        val carId = anyCarId()

        // when
        service.assignCar(
            groupOfPeople = groupOfPeople,
            carId = carId,
        )

        // then
        verify(carRepository).decreaseAvailableSeats(
            carId = carId,
            value = groupOfPeople.people,
        )
        verify(groupOfPeopleRepository).setCarId(
            groupOfPeopleId = groupOfPeople.id,
            targetCarId = carId,
        )
    }
}
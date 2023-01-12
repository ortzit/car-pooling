package org.carPooling.cars.domain

import com.nhaarman.mockitokotlin2.mock
import org.carPooling.anyNotEmptyListOf
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class CarServiceTest {
    private val carRepository: CarRepository = mock()
    private val groupOfPeopleRepository: GroupOfPeopleRepository = mock()
    private val service: CarService = CarService(
        carRepository = carRepository,
        groupOfPeopleRepository = groupOfPeopleRepository
    )

    @Test
    fun `should clear group of people repository and call car repository load`() {
        // given
        val cars = anyNotEmptyListOf { anyCar() }

        // when
        service.load(cars)

        // then
        Mockito.verify(groupOfPeopleRepository).clear()
        Mockito.verify(carRepository).load(cars)
    }
}
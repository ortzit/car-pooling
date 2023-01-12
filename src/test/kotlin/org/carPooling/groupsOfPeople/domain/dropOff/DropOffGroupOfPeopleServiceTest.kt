package org.carPooling.groupsOfPeople.domain.dropOff

import com.nhaarman.mockitokotlin2.*
import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.CarRepository
import org.carPooling.cars.domain.anyCar
import org.carPooling.cars.domain.anyCarId
import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeople
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleId
import org.carPooling.shared.domain.CarAssignmentService
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DropOffGroupOfPeopleServiceTest {
    private val groupOfPeopleRepository: GroupOfPeopleRepository = mock()
    private val carRepository: CarRepository = mock()
    private val carAssignmentService: CarAssignmentService = mock()
    private val service: DropOffGroupOfPeopleService = DropOffGroupOfPeopleService(
        groupOfPeopleRepository = groupOfPeopleRepository,
        carRepository = carRepository,
        carAssignmentService = carAssignmentService,
    )

    @Test
    fun `should throw exception when trying to drop off not existing group of people`() {
        // given
        whenever(groupOfPeopleRepository.get(any())).thenReturn(null)

        // expect
        assertThrows<GroupOfPeopleNotFoundException> {
            service.dropOff(anyGroupOfPeopleId())
        }
    }

    @Test
    fun `should remove group of people when dropping off a group of people with no car assigned`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        whenever(groupOfPeopleRepository.get(groupOfPeople.id)).thenReturn(groupOfPeople)
        whenever(groupOfPeopleRepository.getCarId(groupOfPeople.id)).thenReturn(null)

        // when
        val result = service.dropOff(groupOfPeople.id)

        // then
        verify(groupOfPeopleRepository).remove(groupOfPeople.id)
        assertNull(result)
    }

    @Test
    fun `should remove group of people and assigned car freeing occupied seats when dropping off a group of people with assigned car`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        val carId = anyCarId()
        whenever(groupOfPeopleRepository.get(groupOfPeople.id)).thenReturn(groupOfPeople)
        whenever(groupOfPeopleRepository.getCarId(groupOfPeople.id)).thenReturn(carId)

        // when
        val result = service.dropOff(groupOfPeople.id)

        // then
        verify(carRepository).increaseAvailableSeats(carId = carId, value = groupOfPeople.people)
        verify(groupOfPeopleRepository).remove(groupOfPeople.id)
        assertEquals(carId, result)
    }

    @Test
    fun `should not assign a car when the car does not exist`() {
        // given
        val carId = anyCarId()
        whenever(carRepository.get(carId)).thenReturn(null)

        // when
        service.tryToFillFreeSeats(carId)

        // then
        verify(groupOfPeopleRepository, never()).findGroupOfPeopleWithUnassignedCar(any())
        verify(carAssignmentService, never()).assignCar(any(), any())
    }

    @Test
    fun `should not assign a car when the car exist but there is no group matching available seats`() {
        // given
        val car = anyCar()
        whenever(carRepository.get(car.id)).thenReturn(car)
        whenever(groupOfPeopleRepository.findGroupOfPeopleWithUnassignedCar(car.availableSeats)).thenReturn(null)

        // when
        service.tryToFillFreeSeats(car.id)

        // then
        verify(carAssignmentService, never()).assignCar(any(), any())
    }

    @Test
    fun `should assign a car when the car exist and there is a group matching available seats`() {
        // given
        val car = anyCar()
        val groupOfPeople = anyGroupOfPeople()
        whenever(carRepository.get(car.id)).thenReturn(car)
        returnGroupOfPeopleWithUnassignedCarFirstTime(
            car = car,
            groupOfPeople = groupOfPeople,
        )

        // when
        service.tryToFillFreeSeats(car.id)

        // then
        verify(carAssignmentService).assignCar(
            groupOfPeople = groupOfPeople,
            carId = car.id,
        )
    }

    private fun returnGroupOfPeopleWithUnassignedCarFirstTime(car: Car, groupOfPeople: GroupOfPeople) {
        var count = 0
        whenever(groupOfPeopleRepository.findGroupOfPeopleWithUnassignedCar(car.availableSeats)).thenAnswer {
            count.takeIf { it == 0 }?.let { groupOfPeople }.also { count++ }
        }
    }
}
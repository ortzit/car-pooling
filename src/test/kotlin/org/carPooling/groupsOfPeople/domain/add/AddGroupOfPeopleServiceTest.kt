package org.carPooling.groupsOfPeople.domain.add

import com.nhaarman.mockitokotlin2.*
import org.carPooling.cars.domain.CarRepository
import org.carPooling.cars.domain.anyCar
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeople
import org.carPooling.shared.domain.CarAssignmentService
import org.junit.jupiter.api.Test

class AddGroupOfPeopleServiceTest {
    private val carRepository: CarRepository = mock()
    private val groupOfPeopleRepository: GroupOfPeopleRepository = mock()
    private val carAssignmentService: CarAssignmentService = mock()
    private val service: AddGroupOfPeopleService = AddGroupOfPeopleService(
        carRepository = carRepository,
        groupOfPeopleRepository = groupOfPeopleRepository,
        carAssignmentService = carAssignmentService,
    )

    @Test
    fun `should save group of people but not assign a car when there is no suitable car for group of people`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        whenever(carRepository.findCarByGroupSize(groupOfPeople.people)).thenReturn(null)

        // when
        service.addAndAssignCarIfPossible(groupOfPeople)

        // then
        verify(groupOfPeopleRepository).add(groupOfPeople)
        verify(carAssignmentService, never()).assignCar(any(), any())
    }

    @Test
    fun `should save group of people and assign a car when there is a suitable car for group of people`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        val availableCar = anyCar()
        whenever(carRepository.findCarByGroupSize(groupOfPeople.people)).thenReturn(availableCar)

        // when
        service.addAndAssignCarIfPossible(groupOfPeople)

        // then
        verify(groupOfPeopleRepository).add(groupOfPeople)
        verify(carAssignmentService).assignCar(
            groupOfPeople = groupOfPeople,
            carId = availableCar.id,
        )
    }
}
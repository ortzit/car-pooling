package org.carPooling.groupsOfPeople.infrastructure.repository

import org.carPooling.cars.domain.anyCarId
import org.carPooling.groupsOfPeople.domain.*
import org.carPooling.groupsOfPeople.domain.add.GroupOfPeopleAlreadyExistsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InMemoryGroupOfPeopleRepositoryTest {
    private val repository: InMemoryGroupOfPeopleRepository = InMemoryGroupOfPeopleRepository()

    @BeforeEach
    fun clear() {
        repository.clear()
    }

    @Test
    fun `should get added group of people`() {
        // given
        val groupOfPeople = anyGroupOfPeople()

        // when
        repository.add(groupOfPeople)
        val result = repository.get(groupOfPeople.id)

        // then
        assertEquals(groupOfPeople, result)
    }

    @Test
    fun `should return null when getting not existent group of people`() {
        // given
        val groupOfPeople = anyGroupOfPeople()

        // when
        val result = repository.get(groupOfPeople.id)

        // then
        assertNull(result)
    }

    @Test
    fun `should throw error when trying to add two elements with the same id`() {
        // given
        val groupOfPeople1 = anyGroupOfPeople()
        val groupOfPeople2 = anyGroupOfPeople().copy(id = groupOfPeople1.id)

        // expect
        repository.add(groupOfPeople1)
        assertThrows<GroupOfPeopleAlreadyExistsException> {
            repository.add(groupOfPeople2)
        }
    }

    @Test
    fun `should return null when setting car id to a not existing group of people`() {
        // given
        val groupOfPeopleId = anyGroupOfPeopleId()
        val carId = anyCarId()

        // when
        repository.setCarId(groupOfPeopleId = groupOfPeopleId, targetCarId = carId)
        val result = repository.getCarId(groupOfPeopleId)

        // then
        assertNull(result)
    }

    @Test
    fun `should get added car id assignment`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        val carId = anyCarId()

        // when
        repository.add(groupOfPeople)
        repository.setCarId(groupOfPeopleId = groupOfPeople.id, targetCarId = carId)
        val result = repository.getCarId(groupOfPeople.id)

        // then
        assertEquals(carId, result)
    }

    @Test
    fun `should return null when getting not existent car assignment`() {
        // given
        val groupOfPeopleId = anyGroupOfPeopleId()

        // when
        val result = repository.getCarId(groupOfPeopleId)

        // then
        assertNull(result)
    }

    @Test
    fun `should return null when getting group of people after removal`() {
        // given
        val groupOfPeople = anyGroupOfPeople()

        // when
        repository.add(groupOfPeople)
        val resultBeforeRemoval = repository.get(groupOfPeople.id)
        repository.remove(groupOfPeople.id)
        val result = repository.get(groupOfPeople.id)

        // then
        assertEquals(groupOfPeople, resultBeforeRemoval)
        assertNull(result)
    }

    @Test
    fun `should return null car id when cleared all saved data`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        val carId = anyCarId()

        // when
        repository.add(groupOfPeople)
        repository.setCarId(groupOfPeopleId = groupOfPeople.id, targetCarId = carId)
        repository.clear()
        val result = repository.getCarId(groupOfPeople.id)

        // then
        assertNull(result)
    }

    @Test
    fun `should return group of people with no assigned car when there are enough seats available`() {
        // given
        val groupOfPeople = anyGroupOfPeople()
        val availableSeats = groupOfPeople.people

        // when
        repository.add(groupOfPeople)
        val result = repository.findGroupOfPeopleWithUnassignedCar(availableSeats)

        // then
        assertEquals(groupOfPeople, result)
    }

    @Test
    fun `should return no group of people with no assigned car when there are not enough seats available`() {
        // given
        val groupOfPeople = anyGroupOfPeopleWithAtLeastTwoPeople()
        val availableSeats = groupOfPeople.people.dec()

        // when
        repository.add(groupOfPeople)
        val result = repository.findGroupOfPeopleWithUnassignedCar(availableSeats)

        // then
        assertNull(result)
    }

    @Test
    fun `should return null when finding group of people with no assigned car after removal`() {
        // given
        val groupOfPeople = anyGroupOfPeople()

        // when
        repository.add(groupOfPeople)
        val resultBeforeRemoval = repository.findGroupOfPeopleWithUnassignedCar(groupOfPeople.people)
        repository.remove(groupOfPeople.id)
        val result = repository.findGroupOfPeopleWithUnassignedCar(groupOfPeople.people)

        // then
        assertEquals(groupOfPeople, resultBeforeRemoval)
        assertNull(result)
    }

    private fun anyGroupOfPeopleWithAtLeastTwoPeople(): GroupOfPeople =
        anyGroupOfPeople().copy(people = anyPeople(minValue = 2))
}
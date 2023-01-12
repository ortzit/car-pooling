package org.carPooling.groupsOfPeople.infrastructure.repository

import org.carPooling.cars.domain.CarId
import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.add.GroupOfPeopleAlreadyExistsException
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class InMemoryGroupOfPeopleRepository : GroupOfPeopleRepository {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val groupsOfPeopleById: HashMap<Int, GroupOfPeopleEntity> = hashMapOf()
    private val groupsOfPeopleQueue: LinkedHashSet<GroupOfPeopleEntity> = linkedSetOf()
    private val carIdByGroupOfPeopleId: HashMap<Int, Int> = hashMapOf()

    override fun get(groupOfPeopleId: GroupOfPeopleId): GroupOfPeople? =
        groupsOfPeopleById[groupOfPeopleId.value]?.toDomain()

    override fun add(groupOfPeople: GroupOfPeople) {
        GroupOfPeopleEntity.fromDomain(groupOfPeople).let { groupOfPeopleEntity ->
            if (groupsOfPeopleById.containsKey(groupOfPeopleEntity.id)) {
                logger.info("Tried to add already existing group of people [${groupOfPeople.id}]")
                throw GroupOfPeopleAlreadyExistsException(
                    "[${groupOfPeopleEntity.id}] has already been added previously"
                )
            }
            groupsOfPeopleById[groupOfPeopleEntity.id] = groupOfPeopleEntity
            groupsOfPeopleQueue.add(groupOfPeopleEntity)
        }
    }

    override fun getCarId(groupOfPeopleId: GroupOfPeopleId): CarId? =
        carIdByGroupOfPeopleId[groupOfPeopleId.value]?.let { carIdValue -> CarId(carIdValue) }

    override fun setCarId(groupOfPeopleId: GroupOfPeopleId, targetCarId: CarId) {
        groupsOfPeopleById[groupOfPeopleId.value]?.let { groupOfPeopleEntity ->
            groupsOfPeopleQueue.remove(groupOfPeopleEntity)
            carIdByGroupOfPeopleId[groupOfPeopleId.value] = targetCarId.value
        }
    }

    override fun remove(id: GroupOfPeopleId) {
        val groupOfPeopleEntity = groupsOfPeopleById[id.value]!!
        groupsOfPeopleById.remove(groupOfPeopleEntity.id)
        groupsOfPeopleQueue.remove(groupOfPeopleEntity)
        carIdByGroupOfPeopleId.remove(id.value)
    }

    override fun findGroupOfPeopleWithUnassignedCar(availableSeats: Short): GroupOfPeople? =
        groupsOfPeopleQueue
            .firstOrNull { it.notExceedsAvailableSeats(availableSeats) }
            ?.toDomain()

    override fun clear() {
        groupsOfPeopleById.clear()
        groupsOfPeopleQueue.clear()
        carIdByGroupOfPeopleId.clear()
    }

    private fun GroupOfPeopleEntity.notExceedsAvailableSeats(maxGroupSize: Short): Boolean =
        this.people <= maxGroupSize
}
package org.carPooling.cars.infrastructure.repository

import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.CarId
import org.carPooling.cars.domain.CarRepository
import org.springframework.stereotype.Repository

@Repository
class InMemoryCarRepository : CarRepository {
    private val carsById: HashMap<Int, CarEntity> = hashMapOf()
    private val carIdsByFreeSeats: HashMap<Short, HashSet<Int>> = hashMapOf()

    override fun load(cars: Collection<Car>) {
        CarEntity.fromDomain(cars).let { carEntities ->
            clear()
            this.carsById.putAll(
                carEntities.associateBy { it.id }
            )
            carEntities.forEach { carEntity ->
                addToLoadedCarIdsByFreeSeats(carEntity)
            }
        }
    }

    override fun get(carId: CarId): Car? = carsById[carId.value]?.toDomain()

    override fun findCarByGroupSize(people: Short): Car? =
        getMostSuitableAvailableSeats(people)
            ?.let { targetAvailableSeats ->
                carIdsByFreeSeats[targetAvailableSeats]!!
                    .let { foundCarIds ->
                        carsById[foundCarIds.first()]!!.toDomain()
                    }
            }

    override fun decreaseAvailableSeats(carId: CarId, value: Short) {
        val carEntity = getCarEntity(carId)
        removeFromLoadedCarIdsByFreeSeats(carEntity)
        carEntity.decreaseAvailableSeats(value)
        addToLoadedCarIdsByFreeSeats(carEntity)
    }

    override fun increaseAvailableSeats(carId: CarId, value: Short) {
        val carEntity = getCarEntity(carId)
        removeFromLoadedCarIdsByFreeSeats(carEntity)
        carEntity.increaseAvailableSeats(value)
        addToLoadedCarIdsByFreeSeats(carEntity)
    }

    override fun clear() {
        carIdsByFreeSeats.clear()
        carsById.clear()
    }

    private fun addToLoadedCarIdsByFreeSeats(carEntity: CarEntity) {
        this.carIdsByFreeSeats.computeIfPresent(carEntity.availableSeats) { _, savedCarIds ->
            savedCarIds.add(carEntity.id)
            savedCarIds
        }
        this.carIdsByFreeSeats.computeIfAbsent(carEntity.availableSeats) {
            hashSetOf(carEntity.id)
        }
    }

    private fun getMostSuitableAvailableSeats(minSeats: Short): Short? =
        carIdsByFreeSeats.keys
            .sorted()
            .firstOrNull { it >= minSeats }
            ?.takeIf { carIdsByFreeSeats[it]!!.isNotEmpty() }

    private fun getCarEntity(carId: CarId): CarEntity = carsById[carId.value]!!

    private fun removeFromLoadedCarIdsByFreeSeats(carEntity: CarEntity) {
        val carIds = carIdsByFreeSeats[carEntity.availableSeats]!!
        carIds.remove(carEntity.id)
    }
}
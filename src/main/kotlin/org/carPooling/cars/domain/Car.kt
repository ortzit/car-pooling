package org.carPooling.cars.domain

data class CarId(val value: Int)

data class Car(val id: CarId, val seats: Short, val availableSeats: Short)
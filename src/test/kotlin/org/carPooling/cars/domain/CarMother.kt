package org.carPooling.cars.domain

import org.carPooling.anyPositiveInt
import org.carPooling.anyPositiveShort

fun anyCar(): Car =
    anySeats()
        .let { seats ->
            Car(
                id = anyCarId(),
                seats = seats,
                availableSeats = anySeats(seats)
            )
        }

fun anyCarId(): CarId = CarId(anyCardIdValue())

fun anyCardIdValue(): Int = anyPositiveInt()

fun anySeats(maxValue: Short = MAX_CAR_SEATS): Short =
    anyPositiveShort(
        minValue = MIN_CAR_SEATS,
        maxValue = maxValue,
    )

private const val MIN_CAR_SEATS: Short = 4
private const val MAX_CAR_SEATS: Short = 6
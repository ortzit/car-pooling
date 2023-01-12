package org.carPooling.cars.domain

import org.carPooling.anyString

fun anyDuplicatedCarIdException(): DuplicatedCarIdException = DuplicatedCarIdException(anyString())
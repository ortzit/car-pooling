package org.carPooling.cars.infrastructure.controller.cars

import org.carPooling.cars.domain.DuplicatedCarIdException
import org.carPooling.cars.domain.InvalidNumberOfSeatsException
import org.carPooling.shared.infrastructure.ResponseEntityMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

interface CarsResponseEntityMapper : ResponseEntityMapper

@Service
class CarsOkResponseMapper : CarsResponseEntityMapper {
    override fun initialize(response: Any?): Boolean = response is Unit

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.OK).body("")
}

@Service
class CarsDuplicatedCarIdExceptionMapper : CarsResponseEntityMapper {
    private lateinit var response: DuplicatedCarIdException

    override fun initialize(response: Any?): Boolean =
        if (response is DuplicatedCarIdException) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
}

@Service
class CarsInvalidNumberOfSeatsExceptionMapper : CarsResponseEntityMapper {
    private lateinit var response: InvalidNumberOfSeatsException

    override fun initialize(response: Any?): Boolean =
        if (response is InvalidNumberOfSeatsException) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
}
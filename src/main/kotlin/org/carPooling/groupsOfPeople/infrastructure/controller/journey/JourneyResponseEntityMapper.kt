package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import org.carPooling.groupsOfPeople.domain.add.GroupOfPeopleAlreadyExistsException
import org.carPooling.groupsOfPeople.domain.add.InvalidGroupOfPeopleSizeException
import org.carPooling.shared.infrastructure.ResponseEntityMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

interface JourneyResponseEntityMapper : ResponseEntityMapper

@Service
internal class JourneyOkResponseMapper : JourneyResponseEntityMapper {
    override fun initialize(response: Any?): Boolean = response is Unit

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.OK).body("")
}

@Service
class JourneyGroupOfPeopleAlreadyExistsExceptionMapper : JourneyResponseEntityMapper {
    private lateinit var response: GroupOfPeopleAlreadyExistsException

    override fun initialize(response: Any?): Boolean =
        if (response is GroupOfPeopleAlreadyExistsException) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.CONFLICT).body("")
}

@Service
class JourneyInvalidGroupOfPeopleSizeExceptionMapper : JourneyResponseEntityMapper {
    private lateinit var response: InvalidGroupOfPeopleSizeException

    override fun initialize(response: Any?): Boolean =
        if (response is InvalidGroupOfPeopleSizeException) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
}

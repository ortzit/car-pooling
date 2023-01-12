package org.carPooling.groupsOfPeople.infrastructure.controller.dropOff

import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.carPooling.shared.infrastructure.ResponseEntityMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

interface DropOffResponseEntityMapper : ResponseEntityMapper

@Service
internal class DropOffOkResponseMapper : DropOffResponseEntityMapper {
    override fun initialize(response: Any?): Boolean = response is Unit

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.OK).body("")
}

@Service
class DropOffGroupOfPeopleNotFoundExceptionMapper : DropOffResponseEntityMapper {
    private lateinit var response: GroupOfPeopleNotFoundException

    override fun initialize(response: Any?): Boolean =
        if (response is GroupOfPeopleNotFoundException) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("")
}
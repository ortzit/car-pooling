package org.carPooling.groupsOfPeople.infrastructure.controller.locate

import org.carPooling.groupsOfPeople.application.locate.LocateGroupOfPeopleResult
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.carPooling.shared.infrastructure.ResponseEntityMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

interface LocateResponseEntityMapper : ResponseEntityMapper

@Service
internal class LocateNoContentResponseMapper : LocateResponseEntityMapper {
    override fun initialize(response: Any?): Boolean = response == null

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NO_CONTENT).body("")
}

@Service
internal class LocateOkResponseMapper : LocateResponseEntityMapper {
    private lateinit var response: LocateGroupOfPeopleResult

    override fun initialize(response: Any?): Boolean =
        if (response is LocateGroupOfPeopleResult) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<LocateResponse> =
        LocateResponse.fromResult(response).let { locateResponse ->
            ResponseEntity.status(HttpStatus.OK).body(locateResponse)
        }
}

@Service
internal class LocateGroupOfPeopleNotFoundExceptionMapper : LocateResponseEntityMapper {
    private lateinit var response: GroupOfPeopleNotFoundException

    override fun initialize(response: Any?): Boolean =
        if (response is GroupOfPeopleNotFoundException) {
            this.response = response
            true
        } else {
            false
        }

    override fun toResponseEntity(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("")
}
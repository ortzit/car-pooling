package org.carPooling.groupsOfPeople.infrastructure.controller.dropOff

import org.carPooling.groupsOfPeople.application.dropOff.DropOffGroupOfPeopleCommand
import org.carPooling.groupsOfPeople.application.dropOff.DropOffGroupOfPeopleUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DropOffController @Autowired constructor(
    private val dropOffGroupOfPeopleUseCase: DropOffGroupOfPeopleUseCase,
    private val responseEntityFactory: DropOffResponseEntityFactory,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping(
        path = [DROPOFF_PATH],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    fun dropOff(@RequestParam(name = DROPOFF_ID_PARAM) id: Int): ResponseEntity<*> {
        logger.debug("Request received in [$DROPOFF_PATH] - $DROPOFF_ID_PARAM: [$id]")
        return DropOffGroupOfPeopleCommand(id)
            .let { command ->
                try {
                    dropOffGroupOfPeopleUseCase.run(command)
                } catch (ex: Exception) {
                    ex
                }.let { response ->
                    responseEntityFactory.build(response)
                }
            }
    }

    companion object {
        private const val DROPOFF_PATH = "/dropoff"
        private const val DROPOFF_ID_PARAM = "ID"
    }
}
package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import org.carPooling.groupsOfPeople.application.add.AddGroupOfPeopleUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JourneyController @Autowired constructor(
    private val addGroupOfPeopleUseCase: AddGroupOfPeopleUseCase,
    private val exceptionResponseEntityFactory: JourneyResponseEntityFactory,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping(
        path = [JOURNEY_PATH],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun journey(@RequestBody request: JourneyHttpRequest): ResponseEntity<*> {
        logger.debug("Request received in [$JOURNEY_PATH] - request: [$request]")
        return request.toCommand()
            .let { command ->
                try {
                    addGroupOfPeopleUseCase.run(command)
                } catch (ex: Exception) {
                    ex
                }.let { response ->
                    exceptionResponseEntityFactory.build(response)
                }
            }
    }

    companion object {
        private const val JOURNEY_PATH = "/journey"
    }
}
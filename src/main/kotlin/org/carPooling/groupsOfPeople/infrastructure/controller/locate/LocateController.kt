package org.carPooling.groupsOfPeople.infrastructure.controller.locate

import org.carPooling.groupsOfPeople.application.locate.LocateGroupOfPeopleQuery
import org.carPooling.groupsOfPeople.application.locate.LocateGroupOfPeopleUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LocateController @Autowired constructor(
    private val locateGroupOfPeopleUseCase: LocateGroupOfPeopleUseCase,
    private val responseEntityFactory: LocateResponseEntityFactory,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping(
        path = [LOCATE_PATH],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun locate(@RequestParam(name = LOCATE_ID_PARAM) id: Int): ResponseEntity<*> {
        logger.debug("Request received in [$LOCATE_PATH] - $LOCATE_ID_PARAM: [$id]")
        return LocateGroupOfPeopleQuery(id).let { query ->
            try {
                locateGroupOfPeopleUseCase.query(query)
            } catch (ex: Exception) {
                ex
            }.let { response ->
                responseEntityFactory.build(response)
            }
        }
    }

    companion object {
        private const val LOCATE_PATH = "/locate"
        private const val LOCATE_ID_PARAM = "ID"
    }
}
package org.carPooling.cars.infrastructure.controller.cars

import org.carPooling.cars.application.loadCars.LoadCarsUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CarsController @Autowired constructor(
    private val loadCarsUseCase: LoadCarsUseCase,
    private val responseEntityFactory: CarsResponseEntityFactory,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PutMapping(
        path = [CARS_PATH],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun loadCars(@RequestBody request: Collection<CarHttpRequest>): ResponseEntity<*> {
        logger.info("Request received in [$CARS_PATH] - size: [${request.size}]")
        return request.toCommand()
            .let { command ->
                try {
                    loadCarsUseCase.run(command)
                } catch (ex: Exception) {
                    ex
                }.let { response ->
                    responseEntityFactory.build(response)
                }
            }
    }

    companion object {
        private const val CARS_PATH = "/cars"
    }
}
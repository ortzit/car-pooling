package org.carPooling.cars.application.loadCars

import org.carPooling.cars.domain.CarService
import org.carPooling.cars.domain.CarValidator
import org.carPooling.shared.application.CommandUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoadCarsUseCase @Autowired constructor(
    private val validator: CarValidator,
    private val carService: CarService,
) : CommandUseCase<LoadCarsCommand> {
    override fun run(command: LoadCarsCommand) =
        command.toDomain()
            .let { cars ->
                validator.check(cars)
                carService.load(cars)
            }
}
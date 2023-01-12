package org.carPooling.cars.infrastructure.controller.cars

import org.carPooling.shared.infrastructure.ResponseEntityFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CarsResponseEntityFactory @Autowired constructor(
    responseEntityMappers: List<CarsResponseEntityMapper>
) : ResponseEntityFactory(responseEntityMappers)
package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import org.carPooling.shared.infrastructure.ResponseEntityFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JourneyResponseEntityFactory @Autowired constructor(
    responseEntityMappers: List<JourneyResponseEntityMapper>
) : ResponseEntityFactory(responseEntityMappers)
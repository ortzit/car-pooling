package org.carPooling.groupsOfPeople.infrastructure.controller.dropOff

import org.carPooling.shared.infrastructure.ResponseEntityFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DropOffResponseEntityFactory @Autowired constructor(
    responseEntityMappers: List<DropOffResponseEntityMapper>
) : ResponseEntityFactory(responseEntityMappers)
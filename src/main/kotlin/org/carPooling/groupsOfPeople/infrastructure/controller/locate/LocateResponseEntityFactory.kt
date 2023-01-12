package org.carPooling.groupsOfPeople.infrastructure.controller.locate

import org.carPooling.shared.infrastructure.ResponseEntityFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocateResponseEntityFactory @Autowired constructor(
    responseEntityMappers: List<LocateResponseEntityMapper>
) : ResponseEntityFactory(responseEntityMappers)
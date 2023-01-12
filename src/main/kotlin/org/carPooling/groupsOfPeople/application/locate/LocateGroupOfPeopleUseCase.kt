package org.carPooling.groupsOfPeople.application.locate

import org.carPooling.groupsOfPeople.domain.locate.LocateGroupOfPeopleService
import org.carPooling.shared.application.QueryUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocateGroupOfPeopleUseCase @Autowired constructor(
    private val service: LocateGroupOfPeopleService
) : QueryUseCase<LocateGroupOfPeopleQuery, LocateGroupOfPeopleResult?> {
    override fun query(query: LocateGroupOfPeopleQuery): LocateGroupOfPeopleResult? =
        query.toDomain().let { groupOfPeopleId ->
            service.getAssignedCar(groupOfPeopleId)?.let { assignedCar ->
                LocateGroupOfPeopleResult.fromDomain(assignedCar)
            }
        }
}
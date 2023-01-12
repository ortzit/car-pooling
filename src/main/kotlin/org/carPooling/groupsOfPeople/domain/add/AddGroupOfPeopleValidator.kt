package org.carPooling.groupsOfPeople.domain.add

import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AddGroupOfPeopleValidator{
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun check(groupOfPeople: GroupOfPeople) {
        groupOfPeople.people
            .takeIf { it < MIN_GROUP_SIZE || it > MAX_GROUP_SIZE }
            ?.let { people ->
                logger.info("Tried to add group of people [${groupOfPeople.id}] with invalid group size [$people]")
                throw InvalidGroupOfPeopleSizeException(groupOfPeople.invalidSizeMessage())
            }
    }

    private fun GroupOfPeople.invalidSizeMessage(): String =
        "[${this.id}] group size [${this.people}] should be between $MIN_GROUP_SIZE and $MAX_GROUP_SIZE"

    companion object {
        private const val MIN_GROUP_SIZE: Short = 1
        private const val MAX_GROUP_SIZE: Short = 6
    }
}
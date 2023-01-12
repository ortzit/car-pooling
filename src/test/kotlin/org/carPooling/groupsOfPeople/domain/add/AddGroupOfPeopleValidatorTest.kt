package org.carPooling.groupsOfPeople.domain.add

import org.carPooling.groupsOfPeople.domain.anyGroupOfPeople
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddGroupOfPeopleValidatorTest {
    private val validator: AddGroupOfPeopleValidator = AddGroupOfPeopleValidator()

    @Test
    fun `should throw no exception when checking valid group`() {
        // given
        val groupOfPeople = anyGroupOfPeople()

        // expect
        validator.check(groupOfPeople)
    }

    @Test
    fun `should throw exception when checking group of people size below minimum`() {
        // given
        val groupOfPeople = anyGroupOfPeople().copy(people = MIN_GROUP_SIZE.dec())

        // expect
        assertThrows<InvalidGroupOfPeopleSizeException> {
            validator.check(groupOfPeople)
        }
    }

    @Test
    fun `should throw exception when checking group of people size above maximum`() {
        // given
        val groupOfPeople = anyGroupOfPeople().copy(people = MAX_GROUP_SIZE.inc())

        // expect
        assertThrows<InvalidGroupOfPeopleSizeException> {
            validator.check(groupOfPeople)
        }
    }

    companion object {
        private const val MIN_GROUP_SIZE: Short = 1
        private const val MAX_GROUP_SIZE: Short = 6
    }
}
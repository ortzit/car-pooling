package org.carPooling.shared.domain

import org.carPooling.anyString

fun anyGroupOfPeopleNotFoundException(): GroupOfPeopleNotFoundException =
    GroupOfPeopleNotFoundException(anyString())
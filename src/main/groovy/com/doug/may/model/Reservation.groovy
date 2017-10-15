package com.doug.may.model

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Reservation {
    long campsiteId
    Date startDate
    Date endDate
}

package com.doug.may.model

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class GapRule {
    // The size of a prohibited gap (in days) between campsite reservations
    int gapSize
}

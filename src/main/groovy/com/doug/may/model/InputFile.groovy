package com.doug.may.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

class InputFile {
    Search search

    // This annotation keeps the set of campsites in the same order as they appear in the JSON input file
    @JsonDeserialize(as=LinkedHashSet.class)
    Set<Campsite> campsites

    Set<GapRule> gapRules
    List<Reservation> reservations
}

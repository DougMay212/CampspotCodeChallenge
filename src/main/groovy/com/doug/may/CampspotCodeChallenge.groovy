package com.doug.may

import com.doug.may.model.Campsite
import com.doug.may.model.InputFile
import com.doug.may.service.CampsiteReservationService
import com.fasterxml.jackson.databind.ObjectMapper

import java.text.SimpleDateFormat

class CampspotCodeChallenge {
    public static final SimpleDateFormat EXPECTED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")

    private static CampsiteReservationService campsiteReservationService = new CampsiteReservationService()

    public static void main(String[] args) {
        if (!args || args.length > 1) {
            println "ERROR: you must provide a single input file as this program's only argument " +
                    "(Eg. 'src/test/res/test-case.json')."
            return
        }

        String filePath = args[0]
        File inputFile = new File(filePath)
        if (!inputFile.exists()) {
            println "ERROR: could not find file '${filePath}'."
            return
        }

        ObjectMapper mapper = new ObjectMapper()
        mapper.setDateFormat(EXPECTED_DATE_FORMAT)
        InputFile input = mapper.readValue(inputFile, InputFile.class)

        println "Searching for campsite availability between " +
                "${EXPECTED_DATE_FORMAT.format(input.search.startDate)} and " +
                "${EXPECTED_DATE_FORMAT.format(input.search.endDate)}..."
        println "  ... Searching ${input.campsites?.size()} campsites."
        println "  ... with ${input.reservations?.size()} existing reservations."
        println "  ... and applying ${input.gapRules?.size()} gap rules."
        println ""

        campsiteReservationService.persistCampsitesAndExistingReservations(input)

        println "Available Campsites:"
        List<Campsite> availableCampsites = campsiteReservationService.findAllAvailableCampsites(input.search, input.gapRules)
        availableCampsites?.each { println "  ${it.name}" }
        if (!availableCampsites) println "  None"
    }
}

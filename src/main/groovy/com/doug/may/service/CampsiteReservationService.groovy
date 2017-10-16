package com.doug.may.service

import com.doug.may.model.Campsite
import com.doug.may.model.GapRule
import com.doug.may.model.InputFile
import com.doug.may.model.Search

class CampsiteReservationService {

    private campsiteDao = new MockCampsiteDao()
    private reservationDao = new MockReservationDao()

    public void persistCampsitesAndExistingReservations(InputFile inputFile) {
        campsiteDao.persistCampsites(inputFile.campsites)
        reservationDao.persistReservations(inputFile.reservations)
    }

    public List<Campsite> findAllAvailableCampsites(Search search, Set<GapRule> gapRules) {
        List<Campsite> availableCampsites = []
        List<Date> forbiddenStartDates = []
        List<Date> forbiddenEndDates = []
        gapRules?.each {
            forbiddenEndDates.add(computeDateShiftedByNDays(search.startDate, -1 * it.gapSize))
            forbiddenStartDates.add(computeDateShiftedByNDays(search.endDate, it.gapSize))
        }
        campsiteDao.retrieveAllCampsites()?.each { campsite ->
            if (!reservationDao.existReservationsByCampsiteThatOverlapWith(campsite.id, search) &&
                    !reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(campsite.id, forbiddenStartDates, forbiddenEndDates)) {
                availableCampsites.add(campsite)
            }
        }
        return availableCampsites
    }

    private Date computeDateShiftedByNDays(Date original, int offsetInDays) {
        Calendar calendar = new GregorianCalendar()
        calendar.setTime(original)
        calendar.add(Calendar.DATE, offsetInDays)
        return calendar.getTime()
    }
}

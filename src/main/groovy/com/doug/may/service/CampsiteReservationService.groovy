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
        //TODO: Actually implement this
        return []
    }
}

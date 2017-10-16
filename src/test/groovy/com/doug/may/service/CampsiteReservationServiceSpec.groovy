package com.doug.may.service

import com.doug.may.model.Campsite
import com.doug.may.model.InputFile
import com.doug.may.util.TestUtil
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class CampsiteReservationServiceSpec {

    private CampsiteReservationService campsiteReservationService

    @Before
    public void setUp() {
        campsiteReservationService = new CampsiteReservationService()
    }

    @Test
    public void test_computeDateShiftedByNDays() {
        Date janFirstAtMidnight = TestUtil.date('1/1/2017 00:00')
        Date june28thAroundNoon = TestUtil.date('6/28/2017 11:47')

        assertEquals(TestUtil.date('1/4/2017 00:00'), campsiteReservationService.computeDateShiftedByNDays(janFirstAtMidnight, 3))
        assertEquals(TestUtil.date('12/25/2016 00:00'), campsiteReservationService.computeDateShiftedByNDays(janFirstAtMidnight, -7))
        assertEquals(TestUtil.date('7/28/2017 11:47'), campsiteReservationService.computeDateShiftedByNDays(june28thAroundNoon, 30))
        assertEquals(TestUtil.date('5/31/2017 11:47'), campsiteReservationService.computeDateShiftedByNDays(june28thAroundNoon, -28))
    }

    @Test
    public void test_findAllAvailableCampsites_forDefaultTestCase() {
        InputFile input = TestUtil.readFile('test-case')
        campsiteReservationService.persistCampsitesAndExistingReservations(input)
        List<Campsite> availableCampsites = campsiteReservationService.findAllAvailableCampsites(input.search, input.gapRules)

        assertEquals(3, availableCampsites.size())
        assertTrue(availableCampsites.contains(new Campsite([id: 3L, name: 'Jonny Appleseed Log Cabin'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 6L, name: 'Teddy Roosevelt Tent Site'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 8L, name: 'Bear Grylls Cozy Cave'])))
    }
}

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
    private InputFile input

    @Before
    public void setUp() {
        campsiteReservationService = new CampsiteReservationService()
        input = TestUtil.readFile('test-case')
        campsiteReservationService.persistCampsitesAndExistingReservations(input)
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
        List<Campsite> availableCampsites = campsiteReservationService.findAllAvailableCampsites(input.search, input.gapRules)

        assertEquals(3, availableCampsites.size())
        assertTrue(availableCampsites.contains(new Campsite([id: 3L, name: 'Jonny Appleseed Log Cabin'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 6L, name: 'Teddy Roosevelt Tent Site'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 8L, name: 'Bear Grylls Cozy Cave'])))
    }

    @Test
    public void test_findAllAvailableCampsites_forDefaultTestCaseWithoutGapRules() {
        List<Campsite> availableCampsites = campsiteReservationService.findAllAvailableCampsites(input.search, null)

        assertEquals(7, availableCampsites.size())
        assertTrue(availableCampsites.contains(new Campsite([id: 1L, name: 'Grizzly Adams Adventure Cabin'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 3L, name: 'Jonny Appleseed Log Cabin'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 4L, name: 'Davey Crockett Camphouse'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 5L, name: 'Daniel Boone Bungalow'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 6L, name: 'Teddy Roosevelt Tent Site'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 8L, name: 'Bear Grylls Cozy Cave'])))
        assertTrue(availableCampsites.contains(new Campsite([id: 9L, name: 'Wyatt Earp Corral'])))
    }
}

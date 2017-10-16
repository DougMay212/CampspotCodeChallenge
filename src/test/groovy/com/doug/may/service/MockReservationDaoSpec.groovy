package com.doug.may.service

import com.doug.may.model.InputFile
import com.doug.may.model.Reservation
import com.doug.may.model.Search
import com.doug.may.util.TestUtil
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class MockReservationDaoSpec {
    private long ID = 1L
    private MockReservationDao reservationDao

    @Before
    public void setUp() {
        reservationDao = new MockReservationDao()
    }

    @Test
    public void testReservationsAreSortedByCampsiteId() {
        InputFile input = TestUtil.readFile('test-case')
        reservationDao.persistReservations(input.reservations)

        assertEquals(9, reservationDao.reservationsByCampsiteId.size())
        assertEquals(3, reservationDao.findReservationsByCampsite(6L).size())
        assertEquals(2, reservationDao.findReservationsByCampsite(3L).size())
        assertEquals(1, reservationDao.findReservationsByCampsite(2L).size())
        assertNull(reservationDao.findReservationsByCampsite(0L))
    }

    @Test
    public void testPersistingANullReservationFailsQuietly() {
        reservationDao.persistReservation(null)

        assertEquals(0, reservationDao.reservationsByCampsiteId.size())
    }

    @Test
    public void test_existReservationsByCampsiteThatOverlapWith() {
        Date reservationStartDate = TestUtil.date('2017-10-15')
        Date reservationEndDate = TestUtil.date('2017-10-25')

        Search before = new Search([startDate: TestUtil.date('2017-10-11'), endDate: TestUtil.date('2017-10-13')])
        Search rightBefore = new Search([startDate: TestUtil.date('2017-10-12'), endDate: reservationStartDate])
        Search overlapsBeginning = new Search([startDate: TestUtil.date('2017-10-14'), endDate: TestUtil.date('2017-10-16')])
        Search sameTimeRange = new Search([startDate: reservationStartDate, endDate: reservationEndDate])
        Search containedWithin = new Search([startDate: TestUtil.date('2017-10-16'), endDate: TestUtil.date('2017-10-24')])
        Search engulfs = new Search([startDate: TestUtil.date('2017-10-01'), endDate: TestUtil.date('2017-10-31')])
        Search rightAfter = new Search([startDate: reservationEndDate, endDate: TestUtil.date('2017-10-26')])
        Search after = new Search([startDate: TestUtil.date('2017-10-29'), endDate: TestUtil.date('2017-10-31')])

        reservationDao.persistReservation(new Reservation([campsiteId: ID, startDate: reservationStartDate, endDate: reservationEndDate]))

        assertFalse(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, before))
        assertFalse(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, rightBefore))
        assertTrue(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, overlapsBeginning))
        assertTrue(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, sameTimeRange))
        assertTrue(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, containedWithin))
        assertTrue(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, engulfs))
        assertFalse(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, rightAfter))
        assertFalse(reservationDao.existReservationsByCampsiteThatOverlapWith(ID, after))
    }

    @Test
    public void test_existReservationsByCampsiteWithStartDatesOrEndDates() {
        Date reservation1Start = TestUtil.date('2017-10-15')
        Date reservation1End = TestUtil.date('2017-10-25')
        Date reservation2Start = TestUtil.date('2017-10-30')
        Date reservation2End = TestUtil.date('2017-11-02')
        Date reservation3Start = TestUtil.date('2017-11-07')
        Date reservation3End = TestUtil.date('2017-11-12')
        reservationDao.persistReservation(new Reservation([campsiteId: ID, startDate: reservation1Start, endDate: reservation1End]))
        reservationDao.persistReservation(new Reservation([campsiteId: ID, startDate: reservation2Start, endDate: reservation2End]))
        reservationDao.persistReservation(new Reservation([campsiteId: ID, startDate: reservation3Start, endDate: reservation3End]))

        assertFalse(reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(ID, [], []))
        assertFalse(reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(ID, [TestUtil.date('2017-10-13'), TestUtil.date('2017-10-14')], []))
        assertFalse(reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(ID, [reservation1End, reservation2End], [TestUtil.date('2017-10-13'), TestUtil.date('2017-10-14')]))
        assertFalse(reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(ID, [TestUtil.date('2017-10-13'), TestUtil.date('2017-10-14')], [reservation1Start, reservation2Start]))
        assertTrue(reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(ID, [reservation1Start, reservation2End], [TestUtil.date('2017-10-13'), TestUtil.date('2017-10-14')]))
        assertTrue(reservationDao.existReservationsByCampsiteWithStartDatesOrEndDates(ID, [TestUtil.date('2017-10-13'), TestUtil.date('2017-10-14')], [reservation1Start, reservation2End]))
    }
}

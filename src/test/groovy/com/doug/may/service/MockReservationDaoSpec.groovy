package com.doug.may.service

import com.doug.may.model.InputFile
import com.doug.may.util.TestUtil
import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

class MockReservationDaoSpec {

    @Test
    public void testReservationsAreSortedByCampsiteId() {
        InputFile input = TestUtil.readFile('test-case')
        MockReservationDao reservationDao = new MockReservationDao()
        reservationDao.persistReservations(input.reservations)

        assertEquals(9, reservationDao.reservationsByCampsiteId.size())
        assertEquals(3, reservationDao.findReservationsByCampsiteId(6L).size())
        assertEquals(2, reservationDao.findReservationsByCampsiteId(3L).size())
        assertEquals(1, reservationDao.findReservationsByCampsiteId(2L).size())
        assertNull(reservationDao.findReservationsByCampsiteId(0L))
    }

    @Test
    public void testPersistingANullReservationFailsQuietly() {
        MockReservationDao reservationDao = new MockReservationDao()
        reservationDao.persistReservation(null)

        assertEquals(0, reservationDao.reservationsByCampsiteId.size())
    }
}

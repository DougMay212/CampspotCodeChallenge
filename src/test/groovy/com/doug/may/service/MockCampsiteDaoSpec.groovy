package com.doug.may.service

import com.doug.may.model.Campsite
import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

class MockCampsiteDaoSpec {

    @Test
    public void testCanPersistCampsitesAndThenRetrieveThemById() {
        Campsite campsite1 = new Campsite([id: 1L, name: 'Campsite 1'])
        Campsite campsite2 = new Campsite([id: 2L, name: 'Campsite 2'])
        Campsite campsite3 = new Campsite([id: 3L, name: 'Campsite 3'])
        def campsites = [campsite1, campsite2, campsite3]
        MockCampsiteDao campsiteDao = new MockCampsiteDao()
        campsiteDao.persistCampsites(campsites as Set)

        assertEquals(3, campsiteDao.campsites.size())
        assertEquals(campsite1, campsiteDao.findCampsiteById(1L))
        assertEquals(campsite2, campsiteDao.findCampsiteById(2L))
        assertEquals(campsite3, campsiteDao.findCampsiteById(3L))
        assertNull(campsiteDao.findCampsiteById(0L))
    }

    @Test
    public void testPersistingANullCampsiteFailsQuietly() {
        MockCampsiteDao campsiteDao = new MockCampsiteDao()
        campsiteDao.persistCampsite(null)

        assertEquals(0, campsiteDao.campsites.size())
    }
}

package com.doug.may.service

import com.doug.may.model.Campsite

/** For this code challenge, all input will be stored in memory, but this class could be easily converted to interface
 *  with a relational database.
 *
 *  Its purpose is to make accessing campsites quicker by storing them in a Map indexed by their IDs
 */
class MockCampsiteDao {
    private Map<Long, Campsite> campsites = [:]

    public void persistCampsites(Set<Campsite> campsites) {
        campsites?.each { persistCampsite(it) }
    }

    public void persistCampsite(Campsite campsite) {
        if (!campsite) return

        campsites.put(campsite.id, campsite)
    }

    public Campsite findCampsiteById(long id) {
        return campsites.get(id)
    }
}

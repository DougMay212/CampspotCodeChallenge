package com.doug.may.service

import com.doug.may.model.Campsite

/** For this code challenge, all input will be stored in memory, but this class could be easily converted to interface
 *  with a relational database.
 *
 *  Its purpose is to make accessing campsites quicker by storing them in a Map indexed by their IDs
 */
class MockCampsiteDao {
    private List<Campsite> campsites = []
    private Map<Long, Campsite> campsitesById = [:]

    public void persistCampsites(Set<Campsite> campsites) {
        campsites?.each { persistCampsite(it) }
    }

    public void persistCampsite(Campsite campsite) {
        if (!campsite) return

        campsites.add(campsite)
        campsitesById.put(campsite.id, campsite)
    }

    public Campsite findCampsiteById(long id) {
        return campsitesById.get(id)
    }

    public List<Campsite> retrieveAllCampsites() {
        return campsites
    }
}

package com.doug.may.service

import com.doug.may.model.Reservation

/** For this code challenge, all input will be stored in memory, but this class could be easily converted to interface
 *  with a relational database.
 *
 *  Its purpose is to organize reservations by the campsite to which they apply.
 */
class MockReservationDao {
    private Map<Long, List<Reservation>> reservationsByCampsiteId = [:]

    public void persistReservations(List<Reservation> reservations) {
        reservations.each { persistReservation(it) }
    }

    public void persistReservation(Reservation reservation) {
        if (!reservation) return

        if (reservationsByCampsiteId.containsKey(reservation.campsiteId)) {
            reservationsByCampsiteId.get(reservation.campsiteId).add(reservation)
        } else {
            reservationsByCampsiteId.put(reservation.campsiteId, [ reservation ])
        }
    }

    public List<Reservation> findReservationsByCampsiteId(long campsiteId) {
        return reservationsByCampsiteId.get(campsiteId)
    }
}

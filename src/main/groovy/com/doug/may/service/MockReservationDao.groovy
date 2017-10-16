package com.doug.may.service

import com.doug.may.model.Reservation
import com.doug.may.model.Search

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

    public List<Reservation> findReservationsByCampsite(long campsiteId) {
        return reservationsByCampsiteId.get(campsiteId)
    }

    public boolean existReservationsByCampsiteThatOverlapWith(long campsiteId, Search search) {
        /* To determine if the search overlaps with a reservation, I compare the vectors:
         *        - search.endDate ===> reservation.startDate
         *        - search.startDate ===> reservation.endDate
         *      If the vectors are pointing in the same direction, than there is no overlap. Otherwise,
         *      the search and the reservation do overlap. Please see this project's documentation for
         *      a more detailed demonstration of why this holds true.
         *
         *  The SQL query to use for this would be:
         *      SELECT COUNT(*) as overlappingReservations
         *      FROM RESERVATIONS
         *      WHERE CAMPSITE_ID=${campsiteId} AND
         *          ((${search.endDate.getTime()}-START_DATE)*(${search.startDate.getTime()}-END_DATE)) < 0
         */
        for (Reservation reservation : findReservationsByCampsite(campsiteId)) {
            if ((search.endDate.getTime() - reservation.startDate.getTime()) *
                    (search.startDate.getTime() - reservation.endDate.getTime()) < 0) {
                return true
            }
        }
        return false
    }

    public boolean existReservationsByCampsiteWithStartDatesOrEndDates(long campsiteId, List<Date> startDates, List<Date> endDates) {
        /*
         *  The SQL query to use for this would be:
         *      SELECT COUNT(*) as matchingReservations
         *      FROM RESERVATIONS
         *      WHERE CAMPSITE_ID=${campsiteId} AND
         *          (START_DATE IN ${startDates as List<Long>} OR END_DATE IN ${endDates as List<Long>))
         */
        for (Reservation reservation : findReservationsByCampsite(campsiteId)) {
            if(startDates?.contains(reservation.startDate) || endDates?.contains(reservation.endDate)) {
                return true
            }
        }
        return false
    }
}

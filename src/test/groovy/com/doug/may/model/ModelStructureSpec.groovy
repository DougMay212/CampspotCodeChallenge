package com.doug.may.model

import com.doug.may.util.TestUtil
import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class ModelStructureSpec {

    @Test
    public void testTheModelsCorrectlyReflectTheExpectedJsonStructure() {
        InputFile input = TestUtil.readFile('test-case')
        assertEquals(9, input.campsites?.size())
        assertEquals(19, input.reservations?.size())
        assertEquals(2, input.gapRules?.size())
        assertTrue(input.campsites?.contains(new Campsite([id: 1L, name: 'Grizzly Adams Adventure Cabin'])))
        assertTrue(input.reservations.contains(new Reservation([campsiteId: 4L,
            startDate: TestUtil.date('2016-06-13'), endDate: TestUtil.date('2016-06-14')])))
        assertTrue(input.gapRules.containsAll([new GapRule([gapSize: 2]), new GapRule([gapSize: 3])]))
        assertEquals(TestUtil.date('2016-06-07'), input.search?.startDate)
    }
}

package com.doug.may.util

import com.doug.may.CampspotCodeChallenge
import com.doug.may.model.InputFile
import com.fasterxml.jackson.databind.ObjectMapper

class TestUtil {
    public static InputFile readFile(String fileName) {
        File file = new File(getClass().getResource("/${fileName}.json")?.getFile())
        if (!file.exists()) {
            throw new FileNotFoundException("Could not find file ${fileName}")
        }

        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.setDateFormat(CampspotCodeChallenge.EXPECTED_DATE_FORMAT)
        return objectMapper.readValue(file, InputFile.class)
    }

    public static Date date(String date) {
        return CampspotCodeChallenge.EXPECTED_DATE_FORMAT.parse(date)
    }
}

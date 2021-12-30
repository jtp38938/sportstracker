package de.saring.exerciseviewer.parser.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import de.saring.exerciseviewer.core.EVException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.saring.exerciseviewer.data.EVExercise;
import de.saring.exerciseviewer.parser.AbstractExerciseParser;

/**
 * This class contains all unit tests for the PolarHsrRawParser class.
 * <p/>
 * This file is based on PolarSRawParser.java by Stefan Saring
 *
 * @author Remco den Breeje
 */
public class PolarHsrParserTest {

    /**
     * Instance to be tested.
     */
    private AbstractExerciseParser parser;

    /**
     * This method initializes the environment for testing.
     */
    @BeforeEach
    public void setUp() throws Exception {
        parser = new PolarHsrRawParser();
    }

    /**
     * This method must fail on parsing an exerise file which doesn't exists.
     */
    @Test
    public void testParseExerciseMissingFile() {
        assertThrows(EVException.class, () ->
                parser.parseExercise("missing-file.srd"));
    }

    /**
     * This method tests the parser with an Polar S510 raw exercise file
     * recorded in metric units.
     * This test is taken from the C# test class so the code could be better :-)
     */
    @Test
    public void testParseS510ExerciseCyclingWithMetricUnits() throws EVException {
        // parse exercise file
        EVExercise exercise = parser.parseExercise("misc/testdata/s510/cycling-metric.hsr");

        // check exercise data
        assertEquals(exercise.getFileType(), EVExercise.ExerciseFileType.S510RAW);
        assertEquals("Polar S4xx/S5xx Series", exercise.getDeviceName());
        assertEquals(LocalDateTime.of(2007, 11, 3, 11, 03, 53), exercise.getDateTime());
        assertEquals(exercise.getSportType(), "BasicUse");
        assertTrue(exercise.getRecordingMode().isHeartRate());
        assertEquals(exercise.getRecordingMode().isAltitude(), false);
        assertEquals(exercise.getRecordingMode().isSpeed(), true);
        assertEquals(exercise.getRecordingMode().isCadence(), false);
        assertEquals(exercise.getRecordingMode().isPower(), false);
        assertEquals(2, exercise.getRecordingMode().getBikeNumber().intValue());
        assertEquals(exercise.getDuration().intValue(), 10 * ((2 * 60 * 60) + (14 * 60) + 51) + 5);
        assertEquals(exercise.getRecordingInterval().intValue(), 120);
        assertEquals(exercise.getHeartRateAVG().intValue(), 171);
        assertEquals(exercise.getHeartRateMax().intValue(), 194);
        assertEquals(exercise.getSpeed().getDistance(), 45800);

        assertEquals(Math.round(exercise.getSpeed().getSpeedAvg() * 10), 206);
        assertEquals(Math.round(exercise.getSpeed().getSpeedMax() * 10), 494);
        assertEquals(exercise.getCadence(), null);
        assertEquals(exercise.getAltitude(), null);
        assertEquals(exercise.getTemperature(), null);
        assertEquals(exercise.getEnergy().intValue(), 1616);
        assertEquals(exercise.getEnergyTotal().intValue(), 160947);
        assertEquals(exercise.getSumExerciseTime().intValue(), (644 * 60) + 41);
        assertEquals(exercise.getSumRideTime().intValue(), 8173);
        assertEquals(exercise.getOdometer().intValue(), 3007);

        // check heart rate limits
        assertEquals(exercise.getHeartRateLimits().size(), 3);
        assertTrue(exercise.getHeartRateLimits().get(0).isAbsoluteRange());
        assertEquals(exercise.getHeartRateLimits().get(0).getLowerHeartRate(), (short) 150);
        assertEquals(exercise.getHeartRateLimits().get(0).getUpperHeartRate(), (short) 160);
        assertEquals(exercise.getHeartRateLimits().get(0).getTimeBelow().intValue(), (0 * 60 * 60) + (0 * 60) + 0);
        assertEquals(exercise.getHeartRateLimits().get(0).getTimeWithin(), (0 * 60 * 60) + (0 * 60) + 0);
        assertEquals(exercise.getHeartRateLimits().get(0).getTimeAbove().intValue(), (0 * 60 * 60) + (0 * 60) + 0);

        assertTrue(exercise.getHeartRateLimits().get(1).isAbsoluteRange());
        assertEquals(exercise.getHeartRateLimits().get(1).getLowerHeartRate(), (short) 183);
        assertEquals(exercise.getHeartRateLimits().get(1).getUpperHeartRate(), (short) 187);
        assertEquals(exercise.getHeartRateLimits().get(1).getTimeBelow().intValue(), (0 * 60 * 60) + (0 * 60) + 0);
        assertEquals(exercise.getHeartRateLimits().get(1).getTimeWithin(), (0 * 60 * 60) + (0 * 60) + 0);
        assertEquals(exercise.getHeartRateLimits().get(1).getTimeAbove().intValue(), (0 * 60 * 60) + (0 * 60) + 0);

        assertTrue(exercise.getHeartRateLimits().get(2).isAbsoluteRange());
        assertEquals(exercise.getHeartRateLimits().get(2).getLowerHeartRate(), (short) 90);
        assertEquals(exercise.getHeartRateLimits().get(2).getUpperHeartRate(), (short) 130);
        assertEquals(exercise.getHeartRateLimits().get(2).getTimeBelow().intValue(), (0 * 60 * 60) + (0 * 60) + 0);
        assertEquals(exercise.getHeartRateLimits().get(2).getTimeWithin(), (0 * 60 * 60) + (0 * 60) + 0);
        assertEquals(exercise.getHeartRateLimits().get(2).getTimeAbove().intValue(), (0 * 60 * 60) + (0 * 60) + 0);

        // check lap data
        assertEquals(exercise.getLapList().size(), 2);
        assertEquals(exercise.getLapList().get(0).getTimeSplit(), (1 * 60 * 60 * 10) + (31 * 60 * 10) + (11 * 10) + 7);
        assertEquals(exercise.getLapList().get(0).getHeartRateSplit().intValue(), 156);
        assertEquals(exercise.getLapList().get(0).getHeartRateAVG().intValue(), 173);
        assertEquals(exercise.getLapList().get(0).getHeartRateMax().intValue(), 194);
        assertEquals(Math.round(exercise.getLapList().get(0).getSpeed().getSpeedEnd() * 10), 164);
        assertEquals(Math.round(exercise.getLapList().get(0).getSpeed().getDistance() / 100f), 310);
        assertEquals(exercise.getLapList().get(0).getAltitude(), null);
        assertEquals(exercise.getLapList().get(0).getTemperature(), null);

        assertEquals(exercise.getLapList().get(1).getTimeSplit(), (2 * 60 * 60 * 10) + (14 * 60 * 10) + (51 * 10) + 5);
        assertEquals(exercise.getLapList().get(1).getHeartRateSplit().intValue(), 155);
        assertEquals(exercise.getLapList().get(1).getHeartRateAVG().intValue(), 169);
        assertEquals(exercise.getLapList().get(1).getHeartRateMax().intValue(), 189);
        assertEquals(Math.round(exercise.getLapList().get(1).getSpeed().getSpeedEnd() * 10), 3);
        assertEquals(Math.round(exercise.getLapList().get(1).getSpeed().getDistance() / 100), 458);
        assertEquals(exercise.getLapList().get(1).getAltitude(), null);
        assertEquals(exercise.getLapList().get(1).getTemperature(), null);

        // check sample data (first, two from middle and last only)
        assertEquals(exercise.getSampleList().size(), 67);
        assertEquals(0, exercise.getSampleList().get(0).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(0).getHeartRate().intValue(), 152);
        assertNull(exercise.getSampleList().get(0).getAltitude());
        assertEquals(Math.round(exercise.getSampleList().get(0).getSpeed() * 10), 230);
        assertNull(exercise.getSampleList().get(0).getCadence());
        assertEquals(exercise.getSampleList().get(0).getDistance().intValue(), 0);

        assertEquals(24 * 120 * 1000, exercise.getSampleList().get(24).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(24).getHeartRate().intValue(), 165);
        assertNull(exercise.getSampleList().get(24).getAltitude());
        assertEquals(Math.round(exercise.getSampleList().get(24).getSpeed() * 10), 270);
        assertNull(exercise.getSampleList().get(24).getCadence());
        assertEquals(exercise.getSampleList().get(24).getDistance().intValue(), 16500);

        assertEquals(48 * 120 * 1000, exercise.getSampleList().get(48).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(48).getHeartRate().intValue(), 164);
        assertNull(exercise.getSampleList().get(48).getAltitude());
        assertEquals(Math.round(exercise.getSampleList().get(48).getSpeed() * 10), 190);
        assertNull(exercise.getSampleList().get(48).getCadence());
        assertEquals(exercise.getSampleList().get(48).getDistance().intValue(), 32933);

        assertEquals(66 * 120 * 1000, exercise.getSampleList().get(66).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(66).getHeartRate().intValue(), 161);
        assertNull(exercise.getSampleList().get(66).getAltitude());
        assertEquals(Math.round(exercise.getSampleList().get(66).getSpeed() * 10), 210);
        assertNull(exercise.getSampleList().get(66).getCadence());
        assertEquals(exercise.getSampleList().get(66).getDistance().intValue(), 45800);
    }

    /**
     * This method tests the parser with an Polar S510 raw exercise file
     * recorded in metric units.
     * This test is taken from the C# test class so the code could be better :-)
     */
    @Test
    public void testParseS510ExerciseRunningIntervaltrainingWithMetricUnits() throws EVException {
        // parse exercise file
        EVExercise exercise = parser.parseExercise("misc/testdata/s510/running-interval-metric.hsr");

        // check exercise data
        assertEquals(exercise.getFileType(), EVExercise.ExerciseFileType.S510RAW);
        assertEquals("Polar S4xx/S5xx Series", exercise.getDeviceName());
        assertEquals(LocalDateTime.of(2007, 8, 20, 21, 10, 11), exercise.getDateTime());
        assertEquals(exercise.getSportType(), "ExeSet5");
        assertTrue(exercise.getRecordingMode().isHeartRate());
        assertEquals(exercise.getRecordingMode().isAltitude(), false);
        assertEquals(exercise.getRecordingMode().isSpeed(), false);
        assertEquals(exercise.getRecordingMode().isCadence(), false);
        assertEquals(exercise.getRecordingMode().isPower(), false);
        assertNull(exercise.getRecordingMode().getBikeNumber());
        assertEquals(exercise.getDuration().intValue(), 10 * ((0 * 60 * 60) + (59 * 60) + 31) + 7);
        assertEquals(exercise.getRecordingInterval().intValue(), 30);
        assertEquals(exercise.getHeartRateAVG().intValue(), 161);
        assertEquals(exercise.getHeartRateMax().intValue(), 190);
        assertEquals(exercise.getSpeed(), null);
        assertEquals(exercise.getCadence(), null);
        assertEquals(exercise.getAltitude(), null);
        assertEquals(exercise.getTemperature(), null);
        assertEquals(exercise.getEnergy().intValue(), 645);
        assertEquals(exercise.getEnergyTotal().intValue(), 142456);
        assertEquals(exercise.getSumExerciseTime().intValue(), (617 * 60) + 29);
        assertEquals(exercise.getSumRideTime().intValue(), 7516);
        assertEquals(exercise.getOdometer().intValue(), 2793);

        // check heart rate limits
        assertEquals(exercise.getHeartRateLimits().size(), 3);
        assertTrue(exercise.getHeartRateLimits().get(0).isAbsoluteRange());
        assertEquals(exercise.getHeartRateLimits().get(0).getLowerHeartRate(), (short) 150);
        assertEquals(exercise.getHeartRateLimits().get(0).getUpperHeartRate(), (short) 160);
        assertEquals(exercise.getHeartRateLimits().get(0).getTimeBelow().intValue(), (0 * 60 * 60) + (13 * 60) + 42);
        assertEquals(exercise.getHeartRateLimits().get(0).getTimeWithin(), (0 * 60 * 60) + (23 * 60) + 30);
        assertEquals(exercise.getHeartRateLimits().get(0).getTimeAbove().intValue(), (0 * 60 * 60) + (22 * 60) + 19);

        assertTrue(exercise.getHeartRateLimits().get(1).isAbsoluteRange());
        assertEquals(exercise.getHeartRateLimits().get(1).getLowerHeartRate(), (short) 183);
        assertEquals(exercise.getHeartRateLimits().get(1).getUpperHeartRate(), (short) 187);
        assertEquals(exercise.getHeartRateLimits().get(1).getTimeBelow().intValue(), (0 * 60 * 60) + (46 * 60) + 10);
        assertEquals(exercise.getHeartRateLimits().get(1).getTimeWithin(), (0 * 60 * 60) + (12 * 60) + 46);
        assertEquals(exercise.getHeartRateLimits().get(1).getTimeAbove().intValue(), (0 * 60 * 60) + (0 * 60) + 35);

        assertTrue(exercise.getHeartRateLimits().get(2).isAbsoluteRange());
        assertEquals(exercise.getHeartRateLimits().get(2).getLowerHeartRate(), (short) 90);
        assertEquals(exercise.getHeartRateLimits().get(2).getUpperHeartRate(), (short) 130);
        assertEquals(exercise.getHeartRateLimits().get(2).getTimeBelow().intValue(), (0 * 60 * 60) + (0 * 60) + 9);
        assertEquals(exercise.getHeartRateLimits().get(2).getTimeWithin(), (0 * 60 * 60) + (0 * 60) + 17);
        assertEquals(exercise.getHeartRateLimits().get(2).getTimeAbove().intValue(), (0 * 60 * 60) + (59 * 60) + 5);

        // check lap data (first, two from middle and last only)
        assertEquals(exercise.getLapList().size(), 7);
        assertEquals(exercise.getLapList().get(0).getTimeSplit(), 10 * ((0 * 60 * 60) + (15 * 60) + 0) + 0);
        assertEquals(exercise.getLapList().get(0).getHeartRateSplit().intValue(), 154);
        assertEquals(exercise.getLapList().get(0).getHeartRateAVG().intValue(), 155);
        assertEquals(exercise.getLapList().get(0).getHeartRateMax().intValue(), 168);
        assertEquals(exercise.getLapList().get(0).getSpeed(), null);
        assertEquals(exercise.getLapList().get(0).getAltitude(), null);
        assertEquals(exercise.getLapList().get(0).getTemperature(), null);

        assertEquals(exercise.getLapList().get(2).getTimeSplit(), 10 * ((0 * 60 * 60) + (27 * 60) + 20) + 0);
        assertEquals(exercise.getLapList().get(2).getHeartRateSplit().intValue(), 187);
        assertEquals(exercise.getLapList().get(2).getHeartRateAVG().intValue(), 182);
        assertEquals(exercise.getLapList().get(2).getHeartRateMax().intValue(), 188);

        assertEquals(exercise.getLapList().get(4).getTimeSplit(), 10 * ((0 * 60 * 60) + (39 * 60) + 30) + 0);
        assertEquals(exercise.getLapList().get(4).getHeartRateSplit().intValue(), 147);
        assertEquals(exercise.getLapList().get(4).getHeartRateAVG().intValue(), 182);
        assertEquals(exercise.getLapList().get(4).getHeartRateMax().intValue(), 147);

        assertEquals(exercise.getLapList().get(6).getTimeSplit(), 10 * ((0 * 60 * 60) + (59 * 60) + 31) + 7);
        assertEquals(exercise.getLapList().get(6).getHeartRateSplit().intValue(), 126);
        assertEquals(exercise.getLapList().get(6).getHeartRateAVG().intValue(), 161);
        assertEquals(exercise.getLapList().get(6).getHeartRateMax().intValue(), 190);


        // check sample data (first, two from middle and last only)
        assertEquals(exercise.getSampleList().size(), 119);
        assertEquals(0, exercise.getSampleList().get(0).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(0).getHeartRate().intValue(), 143);
        assertNull(exercise.getSampleList().get(0).getAltitude());
        assertNull(exercise.getSampleList().get(0).getSpeed());
        assertNull(exercise.getSampleList().get(0).getCadence());
        assertNull(exercise.getSampleList().get(0).getDistance());

        assertEquals(44 * 30 * 1000, exercise.getSampleList().get(44).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(44).getHeartRate().intValue(), 151);
        assertNull(exercise.getSampleList().get(44).getAltitude());
        assertNull(exercise.getSampleList().get(44).getSpeed());
        assertNull(exercise.getSampleList().get(44).getCadence());
        assertNull(exercise.getSampleList().get(44).getDistance());

        assertEquals(88 * 30 * 1000, exercise.getSampleList().get(88).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(88).getHeartRate().intValue(), 153);
        assertNull(exercise.getSampleList().get(88).getAltitude());
        assertNull(exercise.getSampleList().get(88).getSpeed());
        assertNull(exercise.getSampleList().get(88).getCadence());
        assertNull(exercise.getSampleList().get(88).getDistance());

        assertEquals(118 * 30 * 1000, exercise.getSampleList().get(118).getTimestamp().intValue());
        assertEquals(exercise.getSampleList().get(118).getHeartRate().intValue(), 140);
        assertNull(exercise.getSampleList().get(118).getAltitude());
        assertNull(exercise.getSampleList().get(118).getSpeed());
        assertNull(exercise.getSampleList().get(118).getCadence());
        assertNull(exercise.getSampleList().get(118).getDistance());
    }
}

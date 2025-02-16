package tartan.smarthome.resources;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaticTartanStateEvaluatorTest {

    private StaticTartanStateEvaluator evaluator;
    private Map<String, Object> inState;
    private StringBuffer log;

    @BeforeEach
    void setUp() {
        // Common setup for all tests
        evaluator = new StaticTartanStateEvaluator();
        inState = new HashMap<>();
        log = new StringBuffer();

        // Initialize common state variables
        inState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant
        inState.put(IoTValues.LIGHT_STATE, false);     // Light is off
        inState.put(IoTValues.DOOR_STATE, false);      // Door is closed
        inState.put(IoTValues.ALARM_STATE, false);     // Alarm is not enabled
        inState.put(IoTValues.TEMP_READING, 70);       // Current temperature
        inState.put(IoTValues.TARGET_TEMP, 70);        // Target temperature
        inState.put(IoTValues.HUMIDIFIER_STATE, false); // Humidifier is off
        inState.put(IoTValues.HEATER_STATE, false);    // Heater is off
        inState.put(IoTValues.CHILLER_STATE, false);   // Chiller is off
        inState.put(IoTValues.ALARM_ACTIVE, false);    // Alarm is not active
        inState.put(IoTValues.HVAC_MODE, "Off");       // HVAC is off
        inState.put(IoTValues.ALARM_PASSCODE, "1234"); // Default passcode
        inState.put(IoTValues.GIVEN_PASSCODE, "");     // User has not entered a passcode
        inState.put(IoTValues.AWAY_TIMER, false);      // Away timer is not activated
    }

    @Test
    @DisplayName("R1: If the house is vacant, then the light cannot be turned on.")
    void lightCannotOpenWhenHouseVacantTest() {

        inState.put(IoTValues.LIGHT_STATE, true);     // Attempt to turn on the light

        // Act: Call the evaluateState method
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the light is not turned on
        assertFalse((Boolean) resultState.get(IoTValues.LIGHT_STATE), "Light should not turn on when the house is vacant");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Cannot turn on light because user not home"), "Log should record why the light cannot turn on");
    }


    @Test
    @DisplayName("R2: If the alarm is enabled, and the door gets opened, then sound the alarm")
    void alarmSoundsWhenDoorOpensAndAlarmIsEnabledTest() {

        inState.put(IoTValues.ALARM_STATE, true);      // Alarm is enabled
        inState.put(IoTValues.DOOR_STATE, false);     // Door is closed
        inState.put(IoTValues.ALARM_ACTIVE, false);   // Alarm is not sounding

        // Act: Simulate the door opening
        inState.put(IoTValues.DOOR_STATE, true); // Door opens
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the alarm is sounding
        assertTrue((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "Alarm should sound when the door is opened while the alarm is enabled");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Break in detected: Activating alarm"), "Log should record that the alarm was activated because the door was opened");
    }

    @Test
    @DisplayName("R3: If the house is vacant, then close the door")
    void closeDoorWhenHouseVacantTest() {

        inState.put(IoTValues.DOOR_STATE, true);     // Door is open

        // Act: Call the evaluateState method
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the door is closed
        assertFalse((Boolean) resultState.get(IoTValues.DOOR_STATE), "Door should close when the house is vacant");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Closed door because house vacant"), "Log should record why the door was closed");
    }

    @Test
    @DisplayName("R4: If the alarm is enabled and the house gets suddenly occupied, then sound the alarm.")
    void alarmSoundsWhenHouseGetsOccupiedAndAlarmIsEnabledTest() {

        // Initialize all state variables
        inState.put(IoTValues.ALARM_STATE, true);       // Alarm is enabled
        inState.put(IoTValues.PROXIMITY_STATE, false); // House is unoccupied
        inState.put(IoTValues.ALARM_ACTIVE, false);    // Alarm is not sounding

        // Act: Simulate the house becoming occupied
        inState.put(IoTValues.PROXIMITY_STATE, true); // Someone is detected
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the alarm is sounding
        assertTrue((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "Alarm should sound when the house becomes occupied while the alarm is enabled");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Break in detected: Activating alarm"), "Log should record that the alarm was activated due to proximity detection");
    }

    @Test
    @DisplayName("R5: If the house is empty, then start the away timer.")
    void awayTimerStartsWhenHouseIsEmptyTest() {

        // Act: Call the evaluateState method
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the away timer has started
        assertTrue((Boolean) resultState.get(IoTValues.AWAY_TIMER), "Away timer should start when the house is empty");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Away timer started because house is empty"), "Log should record that the away timer has started");
    }

//    @Test
//    @DisplayName("R6: When the away timer expires, then turn off the light, arm the alarm, and close the door")
//    void awayTimerExpirationActionsTest() {
//        inState.put(IoTValues.AWAY_TIMER, true);
//        inState.put(IoTValues.LIGHT_STATE, true);
//        inState.put(IoTValues.DOOR_STATE, true);
//        inState.put(IoTValues.ALARM_STATE, false);
//
//        // Act: Simulate away timer expiration
//        Map<String, Object> resultState = evaluator.evaluateState(inState, log);
//
//        // Assert: Verify expected actions when away timer expires
//        assertFalse((Boolean) resultState.get(IoTValues.LIGHT_STATE), "Light should turn off when away timer expires");
//        assertTrue((Boolean) resultState.get(IoTValues.ALARM_STATE), "Alarm should be armed when away timer expires");
//        assertFalse((Boolean) resultState.get(IoTValues.DOOR_STATE), "Door should close when away timer expires");
//
//        // Check that the log contains the correct information
//        String logContent = log.toString();
//        assertTrue(logContent.contains("Away timer expired: turning off lights"), "Log should record light being turned off");
//        assertTrue(logContent.contains("Away timer expired: arming alarm"), "Log should record alarm being armed");
//        assertTrue(logContent.contains("Away timer expired: closing door"), "Log should record door being closed");
//    }





    // G2 - Additional Tests


    @Test
    @DisplayName("R1: Light can turn on when the house is occupied")
    void lightCanTurnOnWhenHouseOccupied() {
        inState.put(IoTValues.PROXIMITY_STATE, true);
        inState.put(IoTValues.LIGHT_STATE, true);
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);
        assertTrue((Boolean) resultState.get(IoTValues.LIGHT_STATE), "Light should turn on when house is occupied");
    }
    @Test
    @DisplayName("R2: Alarm should NOT sound when door opens and alarm is disabled")
    void alarmDoesNotSoundWhenDisabled() {
        inState.put(IoTValues.ALARM_STATE, false);
        inState.put(IoTValues.DOOR_STATE, true);
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);
        assertFalse((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "Alarm should NOT sound if disabled");
    }

    @Test
    @DisplayName("R3: Door remains unchanged when house is occupied")
    void doorRemainsSameWhenOccupied() {
        inState.put(IoTValues.PROXIMITY_STATE, true);
        inState.put(IoTValues.DOOR_STATE, true);
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);
        assertTrue((Boolean) resultState.get(IoTValues.DOOR_STATE), "Door should remain open when house is occupied");
    }

    @Test
    @DisplayName("R4: Alarm should NOT trigger when house gets occupied but alarm is OFF")
    void alarmDoesNotSoundIfDisabledAndOccupied() {
        inState.put(IoTValues.ALARM_STATE, false);
        inState.put(IoTValues.PROXIMITY_STATE, true);
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);
        assertFalse((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "Alarm should NOT trigger if disabled");
    }

    @Test
    @DisplayName("R5: Away timer resets when house is occupied")
    void awayTimerResetsWhenHouseOccupied() {
        inState.put(IoTValues.AWAY_TIMER, true);
        inState.put(IoTValues.PROXIMITY_STATE, true);
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);
        assertFalse((Boolean) resultState.get(IoTValues.AWAY_TIMER), "Away timer should reset if house is occupied");
    }


    @Test
    @DisplayName("Light Off: Should log 'Light off' when house is vacant")
    void lightOffShouldLogCorrectly() {
        inState.put(IoTValues.LIGHT_STATE, false); // Light is off
        inState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant

        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Check that the log contains the correct entry
        assertTrue(log.toString().contains("Light off"), "Log should contain 'Light off'");
    }

    @Test
    @DisplayName("Alarm Active: Ensure alarm active state is processed correctly")
    void alarmActiveShouldBeProcessed() {
        // Arrange: Set initial state
        inState.put(IoTValues.ALARM_STATE, true);   // Alarm is enabled
        inState.put(IoTValues.DOOR_STATE, false);   // Door is closed
        inState.put(IoTValues.PROXIMITY_STATE, true); // Someone enters (unexpected presence)

        // Act: Evaluate the state
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Alarm should be active
        assertNotNull(resultState.get(IoTValues.ALARM_ACTIVE), "Alarm active state should not be null");
        assertTrue((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "Alarm active state should be recorded correctly");
    }


    @Test
    @DisplayName("Humidity Reading: Ensure humidity value is processed correctly")
    void humidityReadingShouldBeProcessed() {
        inState.put(IoTValues.HUMIDITY_READING, 45);

        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        assertNotNull(resultState.get(IoTValues.HUMIDITY_READING), "Humidity should not be null");
        assertEquals(45, resultState.get(IoTValues.HUMIDITY_READING), "Humidity should be recorded correctly");
    }


    @Test
    @DisplayName("Door Closed: Ensure 'Closed door' is logged when alarm is off and no presence detected")
    void doorClosedShouldLogCorrectly() {
        // Arrange
        inState.put(IoTValues.DOOR_STATE, false);     // Door is closed
        inState.put(IoTValues.ALARM_STATE, false);    // Alarm is disabled
        inState.put(IoTValues.PROXIMITY_STATE, false); // No one detected

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert
        assertTrue(log.toString().contains("Closed door"), "Log should record 'Closed door' when alarm is off and no presence detected");
    }

    @Test
    @DisplayName("Closed Door: Ensure 'Closed door' is logged when alarm is ON but no presence detected")
    void closedDoorWhenAlarmOnButNoProximity() {
        // Arrange
        inState.put(IoTValues.DOOR_STATE, false);     // Door is closed
        inState.put(IoTValues.ALARM_STATE, true);     // Alarm is enabled
        inState.put(IoTValues.PROXIMITY_STATE, false); // No presence detected

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert
        assertFalse((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "Alarm should NOT activate when no presence is detected");
        assertTrue(log.toString().contains("Closed door"), "Log should record 'Closed door' when alarm is ON but no presence detected");
    }

    @Test
    @DisplayName("Light Off: Ensure 'Light off' is logged when light is off")
    void lightOffShouldBeLogged() {
        // Arrange
        inState.put(IoTValues.LIGHT_STATE, false); // Light is OFF

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert
        assertTrue(log.toString().contains("Light off"), "Log should record 'Light off' when light is turned off");
    }


    @Test
    @DisplayName("Unrecognized Key: Ensure warning is logged for unknown keys")
    void unrecognizedKeyShouldLogWarning() {
        // Arrange: Add an unknown key
        inState.put("UNKNOWN_KEY", true);  // Key does not exist in IoTValues

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert
        assertTrue(log.toString().contains("Warning: Unrecognized key in input state - UNKNOWN_KEY"),
                "Log should contain warning about unknown key");
    }

    @Test
    @DisplayName("Alarm State: Defaults to false when null")
    void alarmStateDefaultsToFalseWhenNull() {
        // Arrange: Set ALARM_STATE to null
        inState.put(IoTValues.PROXIMITY_STATE, true);
        inState.put(IoTValues.ALARM_STATE, null);

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that alarmState is now false
        assertFalse((Boolean) resultState.get(IoTValues.ALARM_STATE), "alarmState should default to false when null");
    }

    @Test
    @DisplayName("Door State: Defaults to false when null")
    void doorStateDefaultsToFalseWhenNull() {
        // Arrange: Set DOOR_STATE to null
        inState.put(IoTValues.DOOR_STATE, null);

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that doorState is now false
        assertFalse((Boolean) resultState.get(IoTValues.DOOR_STATE), "doorState should default to false when null");
    }

    @Test
    @DisplayName("Proximity State: Defaults to false when null")
    void proximityStateDefaultsToFalseWhenNull() {
        // Arrange: Set PROXIMITY_STATE to null
        inState.put(IoTValues.PROXIMITY_STATE, null);

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that proximityState is now false
        assertFalse((Boolean) resultState.get(IoTValues.PROXIMITY_STATE), "proximityState should default to false when null");
    }

    @Test
    @DisplayName("Light State: Defaults to false when null")
    void lightStateDefaultsToFalseWhenNull() {
        // Arrange: Set LIGHT_STATE to null
        inState.put(IoTValues.LIGHT_STATE, null);

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that lightState is now false
        assertFalse((Boolean) resultState.get(IoTValues.LIGHT_STATE), "lightState should default to false when null");
    }

    @Test
    @DisplayName("Alarm Active State: Defaults to false when null")
    void alarmActiveStateDefaultsToFalseWhenNull() {
        // Arrange: Set ALARM_ACTIVE to null
        inState.put(IoTValues.ALARM_ACTIVE, null);

        // Act
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that alarmActiveState is now false
        assertFalse((Boolean) resultState.get(IoTValues.ALARM_ACTIVE), "alarmActiveState should default to false when null");
    }
}


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
}


package tartan.smarthome.resources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaticTartanStateEvaluatorTest {

    @Test
    @DisplayName("If the house is vacant, then the light cannot be turned on.")
    void lightCannotOpenWhenHouseVacantTest() {
        // Arrange: Create the evaluator and input state
        StaticTartanStateEvaluator evaluator = new StaticTartanStateEvaluator();
        Map<String, Object> inState = new HashMap<>();
        StringBuffer log = new StringBuffer();

        // Initialize all state variables
        inState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant
        inState.put(IoTValues.LIGHT_STATE, true);     // Attempt to turn on the light
        inState.put(IoTValues.DOOR_STATE, false);    // Door is closed
        inState.put(IoTValues.ALARM_STATE, false);   // Alarm is not enabled
        inState.put(IoTValues.TEMP_READING, 70);     // Current temperature
        inState.put(IoTValues.TARGET_TEMP, 70);      // Target temperature
        inState.put(IoTValues.HUMIDIFIER_STATE, false); // Humidifier is off
        inState.put(IoTValues.HEATER_STATE, false);  // Heater is off
        inState.put(IoTValues.CHILLER_STATE, false); // Chiller is off
        inState.put(IoTValues.ALARM_ACTIVE, false);  // Alarm is not active
        inState.put(IoTValues.HVAC_MODE, "Off");     // HVAC is off
        inState.put(IoTValues.ALARM_PASSCODE, "1234"); // Default passcode
        inState.put(IoTValues.GIVEN_PASSCODE, "");   // User has not entered a passcode
        inState.put(IoTValues.AWAY_TIMER, false);    // Away timer is not activated

        // Act: Call the evaluateState method
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the light is not turned on
        assertFalse((Boolean) resultState.get(IoTValues.LIGHT_STATE), "Light should not turn on when the house is vacant");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Cannot turn on light because user not home"), "Log should record why the light cannot turn on");
    }

    @Test
    @DisplayName("If the house is vacant, then close the door")
    void closeDoorWhenHouseVacantTest() {
        // Arrange: Create the evaluator and input state
        StaticTartanStateEvaluator evaluator = new StaticTartanStateEvaluator();
        Map<String, Object> inState = new HashMap<>();
        StringBuffer log = new StringBuffer();

        // Initialize all state variables
        inState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant
        inState.put(IoTValues.DOOR_STATE, true);     // Door is open
        inState.put(IoTValues.LIGHT_STATE, false);   // Light is off
        inState.put(IoTValues.ALARM_STATE, false);   // Alarm is not enabled
        inState.put(IoTValues.TEMP_READING, 70);     // Current temperature
        inState.put(IoTValues.TARGET_TEMP, 70);      // Target temperature
        inState.put(IoTValues.HUMIDIFIER_STATE, false); // Humidifier is off
        inState.put(IoTValues.HEATER_STATE, false);  // Heater is off
        inState.put(IoTValues.CHILLER_STATE, false); // Chiller is off
        inState.put(IoTValues.ALARM_ACTIVE, false);  // Alarm is not active
        inState.put(IoTValues.HVAC_MODE, "Off");     // HVAC is off
        inState.put(IoTValues.ALARM_PASSCODE, "1234"); // Default passcode
        inState.put(IoTValues.GIVEN_PASSCODE, "");   // User has not entered a passcode
        inState.put(IoTValues.AWAY_TIMER, false);    // Away timer is not activated

        // Act: Call the evaluateState method
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: Verify that the door is closed
        assertFalse((Boolean) resultState.get(IoTValues.DOOR_STATE), "Door should close when the house is vacant");

        // Check that the log contains the correct information
        String logContent = log.toString();
        assertTrue(logContent.contains("Closed door because house vacant"), "Log should record why the door was closed");
    }
}


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tartan.smarthome.resources.TartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTConnectManager;
import tartan.smarthome.resources.iotcontroller.IoTControlManager;

import java.util.Map;
import java.util.Hashtable;

class IoTControlManagerTest {

    private IoTControlManager controlManager;

    // Define the stub class inside the test file
    private class StubIoTConnectManager extends IoTConnectManager {
        private Map<String, Object> simulatedState;

        public StubIoTConnectManager() {
            super(null); // Passing null since no real connection is used
            this.simulatedState = new Hashtable<>();
        }

        @Override
        public synchronized Map<String, Object> getState() {
            return simulatedState; // Return the in-memory state
        }

        @Override
        public synchronized Boolean setState(Map<String, Object> state) {
            simulatedState.putAll(state); // Update the in-memory state
            return true; // Simulate a successful state update
        }

        @Override
        public Boolean isConnected() {
            return true; // Always simulate a connected state
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        // Provide a simple implementation of TartanStateEvaluator for testing
        TartanStateEvaluator evaluator = (state, log) -> {
            boolean alarmEnabled = (Boolean) state.getOrDefault("alarmState", false);
            boolean doorOpen = (Boolean) state.getOrDefault("doorState", false);

            if (alarmEnabled && doorOpen) {
                state.put("alarmActiveState", true); // Alarm should sound
                log.append("Alarm activated: Door opened while alarm is enabled.\n");
            } else {
                state.put("alarmActiveState", false); // Alarm stays silent
                log.append("Alarm not activated.\n");
            }
            return state;
        };

        // Initialize IoTControlManager
        controlManager = new IoTControlManager("admin", "1234", evaluator);

        // Inject the stub IoTConnectManager
        StubIoTConnectManager stubConnMgr = new StubIoTConnectManager();
        var connMgrField = IoTControlManager.class.getDeclaredField("connMgr");
        connMgrField.setAccessible(true);
        connMgrField.set(controlManager, stubConnMgr);
    }

    @Test
    void testAlarmSoundsWhenDoorOpensAndAlarmIsEnabled() {
        // Arrange: Set the initial state with alarm enabled and door closed
        controlManager.processStateUpdate(Map.of(
                "alarmState", true,  // Alarm enabled
                "doorState", false   // Door closed
        ));

        // Act: Simulate the door opening
        controlManager.processStateUpdate(Map.of("doorState", true));

        // Assert: Verify that the alarm is sounding
        Map<String, Object> currentState = controlManager.getCurrentState();
        assertTrue((Boolean) currentState.get("alarmActiveState"),
                "Alarm should sound when the door is opened while the alarm is enabled.");
    }

    @Test
    void testAlarmDoesNotSoundWhenAlarmDisabled() {
        // Arrange: Set the initial state with alarm disabled and door closed
        controlManager.processStateUpdate(Map.of(
                "alarmState", false, // Alarm disabled
                "doorState", false   // Door closed
        ));

        // Act: Simulate the door opening
        controlManager.processStateUpdate(Map.of("doorState", true));

        // Assert: Verify that the alarm is not sounding
        Map<String, Object> currentState = controlManager.getCurrentState();
        assertFalse((Boolean) currentState.get("alarmActiveState"),
                "Alarm should not sound when the door is opened while the alarm is disabled.");
    }

    @Test
    void testAlarmInactiveWhenDoorClosed() {
        // Arrange: Set the initial state with alarm enabled but door closed
        controlManager.processStateUpdate(Map.of(
                "alarmState", true,  // Alarm enabled
                "doorState", false   // Door closed
        ));

        // Act: Ensure the door remains closed
        controlManager.processStateUpdate(Map.of("doorState", false));

        // Assert: Verify that the alarm remains inactive
        Map<String, Object> currentState = controlManager.getCurrentState();
        assertFalse((Boolean) currentState.get("alarmActiveState"),
                "Alarm should not sound when the door is closed, even if the alarm is enabled.");
    }
}
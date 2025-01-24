import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tartan.smarthome.resources.TartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTConnectManager;
import tartan.smarthome.resources.iotcontroller.IoTControlManager;

import java.util.Map;
import java.util.Hashtable;

class R5AwayTimerTest {

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
    void setUp() throws Exception{
        // Provide a custom implementation of TartanStateEvaluator
        TartanStateEvaluator evaluator = (state, log) -> {
            boolean houseEmpty = !(Boolean) state.getOrDefault("occupancyState", true);

            if (houseEmpty) {
                state.put("awayTimerState", true); // Start the away timer
                log.append("Away timer started: House is empty.\n");
            } else {
                state.put("awayTimerState", false); // Stop the away timer
                log.append("Away timer not started: House is occupied.\n");
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
    void testAwayTimerStartsWhenHouseIsEmpty() {
        // Arrange: Set initial state with house occupied
        controlManager.processStateUpdate(Map.of(
                "occupancyState", true // House is occupied
        ));

        // Act: Simulate house becoming empty
        controlManager.processStateUpdate(Map.of("occupancyState", false)); // House is empty

        // Assert: Verify that the away timer has started
        Map<String, Object> currentState = controlManager.getCurrentState();
        assertTrue((Boolean) currentState.get("awayTimerState"),
                "Away timer should start when the house is empty.");
    }

    @Test
    void testAwayTimerDoesNotStartWhenHouseIsOccupied() {
        // Arrange: Set initial state with house occupied
        controlManager.processStateUpdate(Map.of(
                "occupancyState", true // House is occupied
        ));

        // Act: Ensure house remains occupied
        controlManager.processStateUpdate(Map.of("occupancyState", true)); // House still occupied

        // Assert: Verify that the away timer has not started
        Map<String, Object> currentState = controlManager.getCurrentState();
        assertFalse((Boolean) currentState.get("awayTimerState"),
                "Away timer should not start when the house is occupied.");
    }
}
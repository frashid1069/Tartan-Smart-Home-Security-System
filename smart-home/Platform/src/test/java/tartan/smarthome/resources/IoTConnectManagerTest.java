package tartan.smarthome.resources.iotcontroller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class IoTConnectManagerTest {

    private IoTConnectManager connectManager;
    private StubIoTConnection stubConnection;

    // Stub class for IoTConnection to simulate house behavior
    private class StubIoTConnection extends IoTConnection {

        private boolean isConnected = true;
        private String lastMessageSent = "";
        private final Map<String, Object> simulatedState = new HashMap<>();

        public StubIoTConnection() {
            super("127.0.0.1", 5050);
        }

        @Override
        public String sendMessageToHouse(String msg) {
            lastMessageSent = msg;

            if (msg.contains(IoTValues.GET_STATE)) {
                return IoTValues.STATE_UPDATE + IoTValues.MSG_DELIM
                        + "DS=1" + IoTValues.PARAM_DELIM
                        + "LS=0" + IoTValues.PARAM_DELIM
                        + "AS=1" + IoTValues.MSG_END; // Use "1" for true, "0" for false
            }
            if (msg.contains(IoTValues.SET_STATE)) {
                return IoTValues.OK;
            }
            return null;
        }



        @Override
        public Boolean isConnected() {
            return isConnected;
        }

        @Override
        public void disconnect() {
            isConnected = false;
        }
    }

    @BeforeEach
    void setUp() {
        stubConnection = new StubIoTConnection();
        connectManager = new IoTConnectManager(stubConnection);
    }

    @Test
    @DisplayName("Test: IoTConnectManager should retrieve house state correctly")
    void testGetState() {
        Map<String, Object> state = connectManager.getState();
        assertNotNull(state, "State should not be null");
        assertTrue(state.containsKey(IoTValues.DOOR_STATE), "State should contain door state");
        assertEquals(true, state.get(IoTValues.DOOR_STATE), "Door state should be true");
        assertEquals(false, state.get(IoTValues.LIGHT_STATE), "Light state should be false");
    }

    @Test
    @DisplayName("Test: IoTConnectManager should set house state successfully")
    void testSetState() {
        Map<String, Object> newState = new HashMap<>();
        newState.put(IoTValues.DOOR_STATE, false);
        newState.put(IoTValues.LIGHT_STATE, true);

        boolean success = connectManager.setState(newState);
        assertTrue(success, "State change should return true");
    }

    @Test
    @DisplayName("Test: IoTConnectManager should return false for failed setState")
    void testSetStateFailure() {
        stubConnection.isConnected = false; // Simulate failed connection
        Map<String, Object> newState = new HashMap<>();
        newState.put(IoTValues.ALARM_STATE, true);

        boolean success = connectManager.setState(newState);
        assertFalse(success, "State change should fail when disconnected");
    }

    @Test
    @DisplayName("Test: IoTConnectManager should disconnect from the house")
    void testDisconnect() {
        connectManager.disconnectFromHouse();
        assertFalse(stubConnection.isConnected(), "Connection should be closed after disconnect");
    }

    @Test
    @DisplayName("Test: IoTConnectManager should correctly handle connection status")
    void testIsConnected() {
        assertTrue(connectManager.isConnected(), "Should return true when connected");
        stubConnection.disconnect();
        assertFalse(connectManager.isConnected(), "Should return false when disconnected");
    }
}

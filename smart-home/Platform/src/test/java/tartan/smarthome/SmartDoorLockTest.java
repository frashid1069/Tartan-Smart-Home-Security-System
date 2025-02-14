package tartan.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmartDoorLockTest {

    private SmartDoorController smartDoorController;
    private DoorLockService doorLockService;
    private final String BASE_URL = "http://localhost:8080/smarthome";

    @BeforeEach
    void setup() {
        doorLockService = new DoorLockService();  // Use the simple implementation of DoorLockService
        smartDoorController = new SmartDoorController(doorLockService);
    }

    // ---------------------- UNIT TESTS ----------------------

    @Test
    void testLockDoor() {
        boolean result = smartDoorController.lockDoor("house123");
        assertTrue(result);
    }

    @Test
    void testUnlockDoor_CorrectPasscode() {
        boolean result = smartDoorController.unlockDoor("house123", "1234");
        assertTrue(result);
    }

    @Test
    void testUnlockDoor_IncorrectPasscode() {
        boolean result = smartDoorController.unlockDoor("house123", "wrongPass");
        assertFalse(result);
    }

    @Test
    void testKeylessEntry() {
        boolean autoUnlock = smartDoorController.handleKeylessEntry("house123");
        assertTrue(autoUnlock);
    }

    @Test
    void testIntruderDefense_LockOnDetection() {
        boolean autoLock = smartDoorController.handleIntruderDefense("house123");
        assertTrue(autoLock);
    }

    @Test
    void testIntruderDefense_Alert() {
        boolean alertSent = smartDoorController.sendIntruderAlert("house123");
        assertTrue(alertSent);
    }

    @Test
    void testNightLock_AutoLock() {
        boolean autoLocked = smartDoorController.handleNightLock("house123");
        assertTrue(autoLocked);
    }

    @Test
    void testNightLock_RelockIfUnlocked() {
        boolean relocked = smartDoorController.relockAtNight("house123");
        assertTrue(relocked);
    }

    // ---------------------- INTEGRATION TESTS ----------------------

    @Test
    void testLockUnlockSequence() {
        boolean lockResult = smartDoorController.lockDoor("house123");
        boolean unlockResult = smartDoorController.unlockDoor("house123", "1234");

        assertTrue(lockResult);
        assertTrue(unlockResult);
    }
}

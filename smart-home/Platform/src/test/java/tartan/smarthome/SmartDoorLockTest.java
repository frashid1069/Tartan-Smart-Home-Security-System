package tartan.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SmartDoorLockTest {

    private SmartDoorController smartDoorController;
    private DoorLockService doorLockService;
    private OkHttpClient client = new OkHttpClient();
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

    // ---------------------- SYSTEM TESTS (API) ----------------------

    // Test for Lock Door API
    @Test
    void testApiLockDoor() throws Exception {
        // Create a request to lock the door for house123
        Request request = new Request.Builder()
                .url("http://localhost:8080/door/lock?houseId=house123")
                .post(null) // You can add a body if needed
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            // Assert that the response status code is 200 (OK)
            assertEquals(200, response.code());
            // You can also check the response body if needed
            String responseBody = response.body() != null ? response.body().string() : "";
            // Optionally assert the response body
            System.out.println(responseBody);  // Print response body for inspection
        }
    }

    // Test for Unlock Door API with correct passcode
    @Test
    void testApiUnlockDoor_CorrectPasscode() throws Exception {
        // Create a request to unlock the door with a valid passcode
        Request request = new Request.Builder()
                .url("http://localhost:8080/door/unlock?houseId=house123&passcode=1234")
                .post(null) // You can add a body if needed
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            // Assert that the response status code is 200 (OK)
            assertEquals(200, response.code());
            // Optionally check the response body
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println(responseBody);  // Print response body for inspection
        }
    }

    // Test for Unlock Door API with incorrect passcode
    @Test
    void testApiUnlockDoor_IncorrectPasscode() throws Exception {
        // Create a request to unlock the door with an incorrect passcode
        Request request = new Request.Builder()
                .url("http://localhost:8080/door/unlock?houseId=house123&passcode=wrongPass")
                .post(null) // You can add a body if needed
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            // Assert that the response status code is 403 (Forbidden)
            assertEquals(403, response.code());
            // Optionally check the response body
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println(responseBody);  // Print response body for inspection
        }
    }

    // Test for Keyless Entry API
    @Test
    void testApiKeylessEntry() throws Exception {
        // Create a request for keyless entry
        Request request = new Request.Builder()
                .url("http://localhost:8080/door/keyless-entry?houseId=house123")
                .post(null) // You can add a body if needed
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            // Assert that the response status code is 200 (OK)
            assertEquals(200, response.code());
            // Optionally check the response body
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println(responseBody);  // Print response body for inspection
        }
    }

    // Test for Intruder Defense API
    @Test
    void testApiIntruderDefense() throws Exception {
        // Create a request for intruder defense
        Request request = new Request.Builder()
                .url("http://localhost:8080/door/intruder-defense?houseId=house123")
                .post(null) // You can add a body if needed
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            // Assert that the response status code is 200 (OK)
            assertEquals(200, response.code());
            // Optionally check the response body
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println(responseBody);  // Print response body for inspection
        }
    }

    // Test for Night Lock API
    @Test
    void testApiNightLock() throws Exception {
        // Create a request for night lock
        Request request = new Request.Builder()
                .url("http://localhost:8080/door/night-lock?houseId=house123")
                .post(null) // You can add a body if needed
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            // Assert that the response status code is 200 (OK)
            assertEquals(200, response.code());
            // Optionally check the response body
            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println(responseBody);  // Print response body for inspection
        }
    }
}

////package tartan.smarthome;
////
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import static org.junit.jupiter.api.Assertions.*;
////import okhttp3.OkHttpClient;
////import okhttp3.Request;
////import okhttp3.Response;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class SmartDoorLockTest {
//
////    private SmartDoorController smartDoorController;
////    private DoorLockService doorLockService;
////    private OkHttpClient client = new OkHttpClient();
////    private final String BASE_URL = "http://localhost:8080/smarthome";
////
////    @BeforeEach
////    void setup() {
////        doorLockService = new DoorLockService();  // Use the simple implementation of DoorLockService
////        smartDoorController = new SmartDoorController(doorLockService);
////    }
//
//    // ---------------------- UNIT TESTS ----------------------
//
//    private SmartLock doorLock;
//
//    @BeforeEach
//    public void setUp() {
//        doorLock = new SmartLock();
//    }
//
//    // Test for Locking with Correct Passcode
//    @Test
//    public void testLockWithCorrectPasscode() {
//        // Setup
//        String correctPasscode = "1234";
//        doorLock.setPasscode(correctPasscode);
//
//        // Act
//        boolean result = doorLock.lock("1234");
//
//        // Assert
//        assertTrue("Door should be locked with correct passcode", result);
//    }
//
//    // Test for Locking with Incorrect Passcode
//    @Test
//    public void testLockWithIncorrectPasscode() {
//        // Setup
//        String correctPasscode = "1234";
//        doorLock.setPasscode(correctPasscode);
//
//        // Act
//        boolean result = doorLock.lock("0000");
//
//        // Assert
//        assertFalse("Door should not be locked with incorrect passcode", result);
//    }
//
//    // Test for Unlocking with Correct Passcode
//    @Test
//    public void testUnlockWithCorrectPasscode() {
//        // Setup
//        String correctPasscode = "1234";
//        doorLock.setPasscode(correctPasscode);
//        doorLock.lock("1234");
//
//        // Act
//        boolean result = doorLock.unlock("1234");
//
//        // Assert
//        assertTrue("Door should be unlocked with correct passcode", result);
//    }
//
//    // Test for Unlocking with Incorrect Passcode
//    @Test
//    public void testUnlockWithIncorrectPasscode() {
//        // Setup
//        String correctPasscode = "1234";
//        doorLock.setPasscode(correctPasscode);
//        doorLock.lock("1234");
//
//        // Act
//        boolean result = doorLock.unlock("0000");
//
//        // Assert
//        assertFalse("Door should not be unlocked with incorrect passcode", result);
//    }
//
//    // Test for Unlocking with Proximity (Registered Phone)
//    @Test
//    public void testUnlockWithProximity() {
//        // Setup
//        doorLock.setRegisteredPhone("user_phone");
//
//        // Act
//        doorLock.detectProximity("user_phone");
//
//        // Assert
//        assertTrue("Door should unlock with registered phone proximity", doorLock.isUnlocked());
//    }
//
//    // Test for Proximity Failure (Unregistered Phone)
//    @Test
//    public void testNoUnlockWithUnregisteredPhone() {
//        // Setup
//        doorLock.setRegisteredPhone("user_phone");
//
//        // Act
//        doorLock.detectProximity("other_phone");
//
//        // Assert
//        assertFalse("Door should not unlock with unregistered phone proximity", doorLock.isUnlocked());
//    }
//
//    // Test for Lock on Intruder Detection
//    @Test
//    public void testLockOnIntruderDetection() {
//        // Setup
//        doorLock.lock("1234"); // Door initially locked
//
//        // Act
//        doorLock.detectIntruder();
//
//        // Assert
//        assertTrue("Door should remain locked when intruder detected", doorLock.isLocked());
//    }
//
//    // Test for Unlock on Intruder Clear
//    @Test
//    public void testUnlockOnIntruderClear() {
//        // Setup
//        doorLock.lock("1234");
//        doorLock.detectIntruder(); // Intruder detected
//
//        // Act
//        doorLock.clearIntruder(); // Intruder cleared
//
//        // Assert
//        assertFalse("Door should be unlocked when intruder is cleared", doorLock.isLocked());
//    }
//
//    // Test for Auto Lock During Night
//    @Test
//    public void testAutoLockDuringNight() {
//        // Setup
//        doorLock.setNightStart(22); // 10:00 PM
//        doorLock.setNightEnd(6); // 6:00 AM
//        doorLock.lock("1234");
//
//        // Act
//        LocalTime now = LocalTime.of(23, 0); // It's 11:00 PM, within night time
//        doorLock.checkNightLock(now);
//
//        // Assert
//        assertTrue("Door should be locked at night", doorLock.isLocked());
//    }
//
//    // Test for Relocking if Unlocked During Night
//    @Test
//    public void testRelockIfUnlockedDuringNight() {
//        // Setup
//        doorLock.setNightStart(22); // 10:00 PM
//        doorLock.setNightEnd(6); // 6:00 AM
//        doorLock.lock("1234");
//
//        // Act
//        doorLock.unlock("1234"); // Unlock door
//        LocalTime now = LocalTime.of(23, 0); // It's 11:00 PM, within night time
//        doorLock.checkNightLock(now);
//
//        // Assert
//        assertTrue("Door should relock at night", doorLock.isLocked());
//    }
//
//    // Test for Locking and Unlocking Multiple Times
//    @Test
//    public void testLockAndUnlockMultipleTimes() {
//        // Setup
//        doorLock.setPasscode("1234");
//
//        // Act & Assert
//        assertTrue("Door should lock successfully", doorLock.lock("1234"));
//        assertTrue("Door should unlock successfully", doorLock.unlock("1234"));
//        assertFalse("Door should not unlock with wrong passcode", doorLock.unlock("0000"));
//    }
//
//    // Test for Locking with Empty Passcode
//    @Test
//    public void testPasscodeEmpty() {
//        // Setup
//        doorLock.setPasscode("");
//
//        // Act & Assert
//        assertFalse("Door should not lock with empty passcode", doorLock.lock(""));
//    }
//
//    // ---------------------- INTEGRATION TESTS ----------------------
//
//    @Test
//    void testLockUnlockSequence() {
//        boolean lockResult = smartDoorController.lockDoor("house123");
//        boolean unlockResult = smartDoorController.unlockDoor("house123", "1234");
//
//        assertTrue(lockResult);
//        assertTrue(unlockResult);
//    }
//
//    // ---------------------- SYSTEM TESTS (API) ----------------------
//
//    // Test for Lock Door API
//    @Test
//    void testApiLockDoor() throws Exception {
//        // Create a request to lock the door for house123
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/door/lock?houseId=house123")
//                .post(null) // You can add a body if needed
//                .build();
//
//        // Execute the request and get the response
//        try (Response response = client.newCall(request).execute()) {
//            // Assert that the response status code is 200 (OK)
//            assertEquals(200, response.code());
//            // You can also check the response body if needed
//            String responseBody = response.body() != null ? response.body().string() : "";
//            // Optionally assert the response body
//            System.out.println(responseBody);  // Print response body for inspection
//        }
//    }
//
//    // Test for Unlock Door API with correct passcode
//    @Test
//    void testApiUnlockDoor_CorrectPasscode() throws Exception {
//        // Create a request to unlock the door with a valid passcode
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/door/unlock?houseId=house123&passcode=1234")
//                .post(null) // You can add a body if needed
//                .build();
//
//        // Execute the request and get the response
//        try (Response response = client.newCall(request).execute()) {
//            // Assert that the response status code is 200 (OK)
//            assertEquals(200, response.code());
//            // Optionally check the response body
//            String responseBody = response.body() != null ? response.body().string() : "";
//            System.out.println(responseBody);  // Print response body for inspection
//        }
//    }
//
//    // Test for Unlock Door API with incorrect passcode
//    @Test
//    void testApiUnlockDoor_IncorrectPasscode() throws Exception {
//        // Create a request to unlock the door with an incorrect passcode
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/door/unlock?houseId=house123&passcode=wrongPass")
//                .post(null) // You can add a body if needed
//                .build();
//
//        // Execute the request and get the response
//        try (Response response = client.newCall(request).execute()) {
//            // Assert that the response status code is 403 (Forbidden)
//            assertEquals(403, response.code());
//            // Optionally check the response body
//            String responseBody = response.body() != null ? response.body().string() : "";
//            System.out.println(responseBody);  // Print response body for inspection
//        }
//    }
//
//    // Test for Keyless Entry API
//    @Test
//    void testApiKeylessEntry() throws Exception {
//        // Create a request for keyless entry
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/door/keyless-entry?houseId=house123")
//                .post(null) // You can add a body if needed
//                .build();
//
//        // Execute the request and get the response
//        try (Response response = client.newCall(request).execute()) {
//            // Assert that the response status code is 200 (OK)
//            assertEquals(200, response.code());
//            // Optionally check the response body
//            String responseBody = response.body() != null ? response.body().string() : "";
//            System.out.println(responseBody);  // Print response body for inspection
//        }
//    }
//
//    // Test for Intruder Defense API
//    @Test
//    void testApiIntruderDefense() throws Exception {
//        // Create a request for intruder defense
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/door/intruder-defense?houseId=house123")
//                .post(null) // You can add a body if needed
//                .build();
//
//        // Execute the request and get the response
//        try (Response response = client.newCall(request).execute()) {
//            // Assert that the response status code is 200 (OK)
//            assertEquals(200, response.code());
//            // Optionally check the response body
//            String responseBody = response.body() != null ? response.body().string() : "";
//            System.out.println(responseBody);  // Print response body for inspection
//        }
//    }
//
//    // Test for Night Lock API
//    @Test
//    void testApiNightLock() throws Exception {
//        // Create a request for night lock
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/door/night-lock?houseId=house123")
//                .post(null) // You can add a body if needed
//                .build();
//
//        // Execute the request and get the response
//        try (Response response = client.newCall(request).execute()) {
//            // Assert that the response status code is 200 (OK)
//            assertEquals(200, response.code());
//            // Optionally check the response body
//            String responseBody = response.body() != null ? response.body().string() : "";
//            System.out.println(responseBody);  // Print response body for inspection
//        }
//    }
//}

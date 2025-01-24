package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaticTartanStateEvaluatorTest {

    @Test
    void lightCannotOpenWhenHouseVacantTest() {
        // Arrange: 创建评估器和输入状态
        StaticTartanStateEvaluator evaluator = new StaticTartanStateEvaluator();
        Map<String, Object> inState = new HashMap<>();
        StringBuffer log = new StringBuffer();

        // 初始化所有状态变量
        inState.put(IoTValues.PROXIMITY_STATE, false); // 房屋为空
        inState.put(IoTValues.LIGHT_STATE, true);     // 尝试打开灯
        inState.put(IoTValues.DOOR_STATE, false);    // 门关闭
        inState.put(IoTValues.ALARM_STATE, false);   // 警报未启用
        inState.put(IoTValues.TEMP_READING, 70);     // 当前温度
        inState.put(IoTValues.TARGET_TEMP, 70);      // 目标温度
        inState.put(IoTValues.HUMIDIFIER_STATE, false); // 加湿器关闭
        inState.put(IoTValues.HEATER_STATE, false);  // 加热器关闭
        inState.put(IoTValues.CHILLER_STATE, false); // 冷却器关闭
        inState.put(IoTValues.ALARM_ACTIVE, false);  // 警报未激活
        inState.put(IoTValues.HVAC_MODE, "Off");     // HVAC 关闭
        inState.put(IoTValues.ALARM_PASSCODE, "1234"); // 默认密码
        inState.put(IoTValues.GIVEN_PASSCODE, "");   // 用户未输入密码
        inState.put(IoTValues.AWAY_TIMER, false);    // 离家计时器未激活

        // Act: 调用 evaluateState 方法
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: 验证灯未打开
        assertFalse((Boolean) resultState.get(IoTValues.LIGHT_STATE), "Light should not turn on when the house is vacant");

        // 检查日志是否包含正确信息
        String logContent = log.toString();
        assert logContent.contains("Cannot turn on light because user not home") : "Log should record why the light cannot turn on";
    }

    @Test
    void closeDoorWhenHouseVacantTest() {
        // Arrange: 创建评估器和输入状态
        StaticTartanStateEvaluator evaluator = new StaticTartanStateEvaluator();
        Map<String, Object> inState = new HashMap<>();
        StringBuffer log = new StringBuffer();

        // 初始化所有状态变量
        inState.put(IoTValues.PROXIMITY_STATE, false); // 房屋为空
        inState.put(IoTValues.DOOR_STATE, true);     // 门尝试打开
        inState.put(IoTValues.LIGHT_STATE, false);   // 灯关闭
        inState.put(IoTValues.ALARM_STATE, false);   // 警报未启用
        inState.put(IoTValues.TEMP_READING, 70);     // 当前温度
        inState.put(IoTValues.TARGET_TEMP, 70);      // 目标温度
        inState.put(IoTValues.HUMIDIFIER_STATE, false); // 加湿器关闭
        inState.put(IoTValues.HEATER_STATE, false);  // 加热器关闭
        inState.put(IoTValues.CHILLER_STATE, false); // 冷却器关闭
        inState.put(IoTValues.ALARM_ACTIVE, false);  // 警报未激活
        inState.put(IoTValues.HVAC_MODE, "Off");     // HVAC 关闭
        inState.put(IoTValues.ALARM_PASSCODE, "1234"); // 默认密码
        inState.put(IoTValues.GIVEN_PASSCODE, "");   // 用户未输入密码
        inState.put(IoTValues.AWAY_TIMER, false);    // 离家计时器未激活

        // Act: 调用 evaluateState 方法
        Map<String, Object> resultState = evaluator.evaluateState(inState, log);

        // Assert: 验证门已关闭
        assertFalse((Boolean) resultState.get(IoTValues.DOOR_STATE), "Door should close when the house is vacant");

        // 检查日志是否包含正确信息
        String logContent = log.toString();
        assertTrue(logContent.contains("Closed door because house vacant"), "Log should record why the door was closed");
    }
}

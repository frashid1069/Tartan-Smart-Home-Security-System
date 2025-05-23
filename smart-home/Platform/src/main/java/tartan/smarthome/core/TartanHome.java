package tartan.smarthome.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import java.time.LocalDate;
import java.util.*;

/**
 * The mode for a Tartan Home to be serialized as JSON. This is managed by Jackson via Dropwizard.
 * See https://www.dropwizard.io/1.0.0/docs/getting-started.html#jackson-for-json
 */
public class TartanHome {

    // The name of the home
    @JsonProperty
    private String name;

    // The network address of the home
    @JsonProperty
    private String address;

    // The desired temperature
    @JsonProperty
    private String targetTemp;

    @JsonProperty
    private String nightStart;

    @JsonProperty
    private String nightEnd;

    // the current temperature
    @JsonProperty
    private String temperature;

    // the current humidity
    @JsonProperty
    private String humidity;

    // the state of the door (true if open, false if closed)
    @JsonProperty
    private String door;

    // the state of the light (true if on, false if off)
    @JsonProperty
    private String light;

    // the humidifier state (true if on, false if off)
    @JsonProperty
    private String humidifier;

    // the state of the proximity sensor (true of address occupied, false if vacant)
    @JsonProperty
    private String proximity;

    // the heater state (true if on, false if off)
    @JsonProperty
    private String hvacMode;

    // The state of the HVAC system
    @JsonProperty
    private String hvacState;

    // the alarm active state (true if alarm sounding, false if alarm not sounding)
    @JsonProperty
    private String alarmActive;

    // the alarm delay timeout
    @JsonProperty
    private String alarmDelay;

    // the alarm enabled state
    @JsonProperty
    private String alarmArmed;

    // the intruder state
    @JsonProperty
    private String intruderState;

    // phone proximity state
    @JsonProperty
    private String phoneProximity;

    @JsonProperty
    private String doorLockState;

    // Properties that are not part of the historical record
    @JsonProperty
    private List<String> eventLog;

    @JsonProperty
    private String authenticated;

    @JsonProperty
    private String alarmPasscode;

    @JsonProperty
    private String doorLockPasscode;

    @JsonProperty
    private String groupExperiment;

    @JsonProperty
    private Long minutesLightsOn;

    @JsonProperty
    private Map<LocalDate, Long> pastLightUsage = new HashMap<>();

    @JsonProperty
    private Integer intruderOccurrences;

    /**
     * Empty constructor needed by Jackson deserialization
     */
    public TartanHome() {  }


    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the address
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the target temperature
     * @return the target temperature
     */
    public String getTargetTemp() {
        return targetTemp;
    }

    /**
     * Set the target temperature
     * @param targetTemp the new target temperature
     */
    public void setTargetTemp(String targetTemp) { this.targetTemp = targetTemp; }

    public String getNightStart() {
        return nightStart;
    }

    public void setNightStart(String nightStart) {
        this.nightStart = nightStart;
    }

    public String getNightEnd() {
        return nightEnd;
    }

    public void setNightEnd(String nightEnd) {
        this.nightEnd = nightEnd;
    }

    /**
     * Get the current temperature
     * @return the temperature
     */
    public String getTemperature() {
        return this.temperature;
    }

    /**
     * Set the temperature
     * @param temperature the new temperature
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Get the humidity
     * @return Current humidity
     */
    public String getHumidity() {
        return this.humidity;
    }

    /**
     * Set the humidity
     * @param humidity the new humidity
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Get the door state
     * @return the door state
     */
    public String getDoor() {
        return this.door;
    }

    /**
     * Set the door state
     * @param door the new door state
     */
    public void setDoor(String door) {
        this.door = door;
    }

    /**
     * Get the light state
     * @return the light state
     */
    public String getLight() {
        return this.light;
    }

    /**
     * Set the light state
     * @param light the new light state
     */
    public void setLight(String light) {
        this.light = light;
    }

    /**
     * Get the dehumidifier state
     * @return the dehumidifier state
     */
    public String getHumidifier() {
        return humidifier;
    }

    /**
     * Set the dehumidifier state
     * @param humidifier the new state
     */
    public void setHumidifier(String humidifier) {
        this.humidifier = humidifier;
    }

    /**
     * Get the motion sensor state
     * @return the motion sensor state
     */
    public String getProximity() {
        return proximity;
    }

    /**
     * Set the motion sensor state
     * @param proximity the new state
     */
    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    /**
     * Get the alarm armed state
     * @return the status of the alarm
     */
    public String getAlarmArmed() {
        return alarmArmed;
    }

    /**
     * Arm/Disarm the alarm
     * @param alarmArmed the new state
     */
    public void setAlarmArmed(String alarmArmed) {
        this.alarmArmed = alarmArmed;
    }

    public String getIntruderState() {
        return intruderState;
    }

    public void setIntruderState(String intruderState) {
        this.intruderState = intruderState;
    }

    /**
     * Get phone proxmity state
     * @return the status of the phone proximity sensor
     */

    public  String getPhoneProximity(){
        return  phoneProximity;
    }

    public void setPhoneProximity(String phoneProximity){
        this.phoneProximity = phoneProximity;
    }

    /**
     * Get the HVAC mode
     * @return the HVAC mode
     */
    public String getHvacMode() {
        return hvacMode;
    }

    /**
     * Set the HVAC mode
     * @param hvacMode the new mode
     */
    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }

    /**
     * Get the alarm active state
     * @return the current state
     */
    public String getAlarmActive() {
        return alarmActive;
    }

    /**
     * Set the alarm active state
     * @param alarmActive the new state
     */
    public void setAlarmActive(String alarmActive) {
        this.alarmActive = alarmActive;
    }

    /**
     * Get the alarm delay
     * @return the current delay
     */
    public String getAlarmDelay() {
        return alarmDelay;
    }

    /**
     * Set the alarm delay
     * @param alarmDelay the new delay
     */
    public void setAlarmDelay(String alarmDelay) {
        this.alarmDelay = alarmDelay;
    }

    /**
     * Get the HVAC state
     * @return the current state
     */
    public String getHvacState() {
        return hvacState;
    }

    /**
     * Set the HVAC state
     * @param hvacState the new state
     */
    public void setHvacState(String hvacState) {
        this.hvacState = hvacState;
    }

    /**
     * Get the house event log
     * @return the log
     */
    public List<String> getEventLog() { return eventLog;  }

    /**
     * Set the house event log
     * @param eventLog the log
     */
    public void setEventLog(List<String> eventLog) {
        this.eventLog = eventLog;
    }

    /**
     * Get the authenticated state
     * @return the state
     */
    public String getAuthenticated() { return authenticated; }

    /**
     * Set the authenticated state
     * @param authenticated the new state
     */
    public void setAuthenticated(String authenticated) { this.authenticated = authenticated;  }

    /**
     * Get the alarm passcode
     * @return the passcode
     */
    public String getAlarmPasscode() { return alarmPasscode; }

    /**
     * Set the alarm passcode
     * @param alarmPasscode the new passcode
     */
    public void setAlarmPasscode(String alarmPasscode) { this.alarmPasscode = alarmPasscode; }

    /**
     * Get the doorlock passcode
     * @return the passcode
     */
    public String getDoorLockPasscode() { return doorLockPasscode; }

    /**
     * Set the doorlock passcode
     * @param doorLockPasscode the new passcode
     */
    public void setDoorLockPasscode(String doorLockPasscode) { this.doorLockPasscode = doorLockPasscode; }

    /**
     * Get the doorlock state
     * @return the state
     */
    public String getDoorLockState() { return doorLockState; }

    /**
     * Set the doorlock state
     * @param doorLockState the state
     */
    public void setDoorLockState(String doorLockState) { this.doorLockState = doorLockState; }

    public Long getMinutesLightsOn(){
        return this.minutesLightsOn;
    }

    public void setMinutesLightsOn(Long minutesLightsOn){
        this.minutesLightsOn = minutesLightsOn;
    }

    public String getGroupExperiment(){
        return this.groupExperiment;
    }

    public void setGroupExperiment(String groupExperiment) {
        this.groupExperiment = groupExperiment;
    }

    public Map<LocalDate, Long> getPastLightUsage() {
        return pastLightUsage;
    }

    public void setPastLightUsage(Map<LocalDate, Long> pastLightUsage) {
        this.pastLightUsage = pastLightUsage;
    }

    public Integer getIntruderOccurrences() {
        return intruderOccurrences;
    }

    public void setIntruderOccurrences(Integer intruderOccurrences) {
        this.intruderOccurrences = intruderOccurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TartanHome)) {
            return false;
        }
        final TartanHome that = (TartanHome) o;
        return Objects.equals(this.name, that.name);
    }

}

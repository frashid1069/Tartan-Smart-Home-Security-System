package tartan.smarthome.core;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a database table for home status
 */
@Entity
@Table(name = "Home")
public class TartanHomeData {

    // Primary key for the table. Not meant to be used
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // the creation time
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    private Date createTimeStamp;

    @Column(name = "home_name", nullable = false)
    private String homeName;

    @Column(name = "address", nullable = false)
    private String address;

    // The desired temperature
    @Column(name = "target_temp")
    private String targetTemp;
    

    @Column(name = "night_start")
    private String nightStart;

    @Column(name = "night_end")
    private String nightEnd;

    // the current temperature
    @Column(name = "temperature")
    private String temperature;

    // the current humidity
    @Column(name = "humidity")
    private String humidity;

    // the state of the door (true if open, false if closed)
    @Column(name = "door_state")
    private String door;

    // the state of the light (true if on, false if off)
    @Column(name = "light_state")
    private String light;

    // the humidifier state (true if on, false if off)
    @Column(name = "humidifier_state")
    private String humidifier;

    // the state of the proximity sensor (true of address occupied, false if vacant)
    @Column(name = "proximity_state")
    private String proximity;
    // the heater state (true if on, false if off)
    @Column(name = "hvac_mode")
    
    private String hvacMode;
    // The state of the HVAC system
    @Column(name = "hvac_state")
    private String hvacState;

    // the alarm active state (true if alarm sounding, false if alarm not sounding)
    @Column(name = "alarm_active_state")
    private String alarmActive;

    // the alarm delay timeout
    @Column(name = "alarm_delay")
    private String alarmDelay;

    // the alarm enabled state
    @Column(name = "alarm_enabled_state")
    private String alarmArmed;

    @Column(name = "intruder_state")
    private String intruderState;

    @Column(name = "phone_proximity")
    private String phoneProximity;

    @Column(name = "door_lock_state")
    private String doorLockState;

    @Column(name = "group_experiment")
    private String groupExperiment;

    @Column(name = "minutes_lights_on")
    private Long minutesLightsOn;

    @Column(name = "intruder_occurrences")
    private Integer intruderOccurrences;

    /**
     * Create a mew data set from a TartanHome model
     * @param h the home model
     */
    public TartanHomeData(TartanHome h) {
        this.homeName = h.getName();
        this.address = h.getAddress();
        this.targetTemp = h.getTargetTemp();
        this.nightStart = h.getNightStart();
        this.nightEnd = h.getNightEnd();
        this.temperature = h.getTemperature();
        this.humidity = h.getHumidity();
        this.door = h.getDoor();
        this.light = h.getLight();
        this.humidifier = h.getHumidifier();
        this.proximity = h.getProximity();
        this.hvacMode = h.getHvacMode();
        this.hvacState = h.getHvacState();
        this.alarmActive = h.getAlarmActive();
        this.alarmDelay = h.getAlarmDelay();
        this.alarmArmed = h.getAlarmArmed();
        this.intruderState = h.getIntruderState();
        this.phoneProximity = h.getPhoneProximity();
        this.doorLockState = h.getDoorLockState();
        // Remember when this record is created
        this.createTimeStamp = new Date();
        this.minutesLightsOn = h.getMinutesLightsOn();
        this.groupExperiment = h.getGroupExperiment();
    }

    /**
     * Get the name
     * @return the name
     */
    public String getHomeName() {
        return homeName;
    }

    /**
     * Set the name
     * @param homeName the new name
     */
    public void setHomeName(String homeName) {
        this.homeName = homeName;
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

    public String getGroupExperiment(){
        return this.groupExperiment;
    }

    public void setGroupExperiment(String groupExperiment){
        this.groupExperiment = groupExperiment;
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

    public void setDoorLockState(String doorLoackState){ this.doorLockState = doorLoackState;}

    public String getDoorLockState(){return  this.doorLockState = doorLockState;}

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

    public Long getMinutesLightsOn(){
        return minutesLightsOn;
    }

    public void setMinutesLightsOn(Long minutesLightsOn){
        this.minutesLightsOn = minutesLightsOn;
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

    public String getIntruderState() {
        return intruderState;
    }

    public void setIntruderState(String intruderState) {
        this.intruderState = intruderState;
    }

    public  String getPhoneProximity(){
        return  phoneProximity;
    }

    public void setPhoneProximity(String phoneProximity){
        this.phoneProximity = phoneProximity;
    }
    /**
     * Get the ID
     * @return the ID
     */
    public long getId() {
        return id;
    }

    /**
     * Set the ID
     * @param id the new ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the creation time for this record
     * @return the creation time
     */
    public Date getCreateTimeStamp() { return createTimeStamp; }

    /**
     * Set the creation time
     * @param createTimeStamp the new timestamp
     */
    public void setCreateTimeStamp(Date createTimeStamp) { this.createTimeStamp = createTimeStamp; }

    @Override
    public int hashCode() {
        return Objects.hash(id, homeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TartanHomeData)) {
            return false;
        }
        final TartanHomeData that = (TartanHomeData) o;
        return Objects.equals(this.homeName, that.homeName);
    }

}

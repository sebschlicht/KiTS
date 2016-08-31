package de.jablab.sebschlicht.series.activities.preferences;

/**
 * Target device for playback.<br>
 * This class supports to be put on a wire.
 *
 * @author sebschlicht
 *
 */
public enum PlaybackDevice {

    /**
     * automatically detect to which device the playback should be redirected to<br>
     * plays on local device if there is no server available
     */
    AUTO("auto"),

    /**
     * play on local device only
     */
    DEVICE("device");

    /**
     * audio playback device preferences key
     */
    public static final String KEY_AUDIO = "playback_device_audio";

    /**
     * video playback device preference key
     */
    public static final String KEY_VIDEO = "playback_device_video";

    /**
     * playback device identifier
     */
    private final String identifier;

    /**
     * Creates a playback device.
     *
     * @param identifier
     *            identifier of the new playback device
     */
    private PlaybackDevice(
            final String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return playback device identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return this.getIdentifier();
    }

    /**
     * Parses a playback device identifier to a playback device.
     *
     * @param identifier
     *            playback device identifier
     * @return playback device represented by the identifier passed
     */
    public static PlaybackDevice parseString(String identifier) {
        if (AUTO.getIdentifier().equals(identifier)) {
            return AUTO;
        } else if (DEVICE.getIdentifier().equals(identifier)) {
            return DEVICE;
        }
        return null;
    }
}

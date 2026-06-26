package net.enderbyteprograms.sjo;

import java.io.Serializable;

public interface SafetySerializable extends Serializable {
    static long SerialVersionUID = 1L;

    /**
     * Ensure that things are updated to the last version
     */
    public void safetyCheck();
}

package net.enderbyteprograms.unihome.structures;

import net.enderbyteprograms.sjo.SafetySerializable;

public class PlayerInfo implements SafetySerializable {
    public String uuid;
    public String name;
    public String comparableName;
    public String homeWorld;
    public Double homeX;
    public Double homeY;
    public Double homeZ;
    public Integer playtimeInTicks;
    public Integer joinDay;//Epoch days
    public Integer lastSeenDay;//Epoch days
    public Boolean pvpEnabled;
    public static final long SerialVersionUID = 1L;
    //These are all objects so they can be nulled

    public PlayerInfo() {}

    /**
     * Ensure that this object is updated to the latest version
     */
    @Override
    public void safetyCheck() {
        //Since this is V1 the object either exists or doesn't
    }

    public boolean hasHome() {
        return homeWorld != null;
    }

    public boolean joinDataFilledIn() {
        return joinDay != null;
    }

    public boolean lastSeenDataFilledIn() {
        return lastSeenDay != null;
    }
}

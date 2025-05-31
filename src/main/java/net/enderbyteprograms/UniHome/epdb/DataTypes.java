package net.enderbyteprograms.UniHome.epdb;

public enum DataTypes {
    String,
    Number,
    Boolean;

    public int toNum(DataTypes d) {
        if (d == String) {
            return 0;
        } else if (d == Number) {
            return 1;
        } else {
            return 2;
        }
    }
}

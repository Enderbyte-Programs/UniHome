package net.enderbyteprograms.unihome;

import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Playtime {

    public static Duration getPlaytime(UUID playerUUID) {


        try {

            int rawPlaytime = Data.playerInformation.get(playerUUID).playtimeInTicks;
            return Duration.of(rawPlaytime / 20,ChronoUnit.SECONDS);

        } catch (Exception e) {
            return Duration.of(0, ChronoUnit.SECONDS);
        }

    }




    public static Duration getPlaytime(String playerName) {
        UUID playerUUID = Data.getUUIDFromName(playerName);

        if (playerUUID == null) {
            return Duration.of(0, ChronoUnit.SECONDS);
        } else {
            return getPlaytime(playerUUID);
        }
    }




    public static Instant getJoinTime(String playerUsername) {

        UUID playerUUID = Data.getUUIDFromName(playerUsername);

        if (playerUUID == null) {
            return null;
        }

        if (!Data.playerInformation.get(playerUUID).joinDataFilledIn()) {
            return null;
        }

        Instant candidateResult;
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerUUID);

        if (p.hasPlayedBefore()) {

            int serverStoredRecord = (int)(p.getFirstPlayed() / (1000 * 60 * 60 * 24));//Convert to epoch days
            //int cachedRecord = Data.joinTimeTable.select(new Comparison("uuid",playerUUID.toString(),false)).get(0).getInt("epochdays");
            int cachedRecord = Data.playerInformation.get(playerUUID).joinDay;

            if (cachedRecord < serverStoredRecord) {
                candidateResult = Instant.ofEpochSecond(cachedRecord * (60 * 60 * 24));
            } else {
                candidateResult = Instant.ofEpochSecond((long) serverStoredRecord * 60 * 60 * 24);
                Data.playerInformation.get(playerUUID).joinDay = serverStoredRecord;
            }

        } else {

            int prs = Data.playerInformation.get(playerUUID).joinDay;

            candidateResult = Instant.ofEpochSecond((long) prs * 60 * 60 * 24);

        }

        return candidateResult;

    }




    public static Instant getLastSeen(String playerUsername) {

        UUID playerUUID = Data.getUUIDFromName(playerUsername);


        if (playerUUID == null) {
            return null;
        }

        if (!Data.playerInformation.get(playerUUID).lastSeenDataFilledIn()) {
            return null;
        }

        Instant candidateResult;
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerUUID);

        if (p.hasPlayedBefore()) {

            int serverStoredRecord = (int)(p.getLastPlayed() / (1000 * 60 * 60 * 24));//Convert to epoch days
            int cachedRecord = Data.playerInformation.get(playerUUID).lastSeenDay;

            if (cachedRecord < serverStoredRecord) {
                candidateResult = Instant.ofEpochSecond(cachedRecord * (60 * 60 * 24));
            } else {
                candidateResult = Instant.ofEpochSecond((long) serverStoredRecord * 60 * 60 * 24);
                Data.playerInformation.get(playerUUID).lastSeenDay = serverStoredRecord;
            }

        } else {

            int prs = Data.playerInformation.get(playerUUID).lastSeenDay;

            candidateResult = Instant.ofEpochSecond((long) prs * 60 * 60 * 24);

        }

        return candidateResult;

    }

}

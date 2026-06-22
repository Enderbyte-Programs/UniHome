package net.enderbyteprograms.UniHome;

import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

public class Playtime {

    public static Duration getPlaytime(UUID playerUUID) {

        OfflinePlayer p = Bukkit.getOfflinePlayer(playerUUID);

        try {

            int rawPlaytime = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
            return Duration.of(rawPlaytime / 20,ChronoUnit.SECONDS);

        } catch (IllegalArgumentException e) {
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

        Instant candidateResult;
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerUUID);

        if (p.hasPlayedBefore()) {

            int serverStoredRecord = (int)(p.getFirstPlayed() / (1000 * 60 * 60 * 24));//Convert to epoch days
            int cachedRecord = Data.joinTimeTable.select(new Comparison("uuid",playerUUID.toString(),false)).get(0).getInt("epochdays");

            if (cachedRecord < serverStoredRecord) {
                candidateResult = Instant.ofEpochSecond(cachedRecord * (60 * 60 * 24));
            } else {
                candidateResult = Instant.ofEpochSecond((long) serverStoredRecord * 60 * 60 * 24);
            }

        } else {

            ResultSet prs = Data.joinTimeTable.select(new Comparison("uuid",playerUUID.toString(),false));
            if (prs.size() == 0) {
                return null;
            }

            candidateResult = Instant.ofEpochSecond((long) prs.get(0).getInt("epochdays") * 60 * 60 * 24);

        }

        return candidateResult;

    }




    public static Instant getLastSeen(String playerUsername) {

        UUID playerUUID = Data.getUUIDFromName(playerUsername);

        if (playerUUID == null) {
            return null;
        }

        Instant candidateResult;
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerUUID);

        if (p.hasPlayedBefore()) {

            int serverStoredRecord = (int)(p.getLastPlayed() / (1000 * 60 * 60 * 24));//Convert to epoch days
            int cachedRecord = Data.joinTimeTable.select(new Comparison("uuid",playerUUID.toString(),false)).get(0).getInt("lastseen");

            if (cachedRecord < serverStoredRecord) {
                candidateResult = Instant.ofEpochSecond(cachedRecord * (60 * 60 * 24));
            } else {
                candidateResult = Instant.ofEpochSecond((long) serverStoredRecord * 60 * 60 * 24);
            }

        } else {

            ResultSet prs = Data.joinTimeTable.select(new Comparison("uuid",playerUUID.toString(),false));
            if (prs.size() == 0) {
                return null;
            }

            candidateResult = Instant.ofEpochSecond((long) prs.get(0).getInt("lastseen") * 60 * 60 * 24);

        }

        return candidateResult;

    }

}

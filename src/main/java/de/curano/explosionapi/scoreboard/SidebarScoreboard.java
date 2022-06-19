package de.curano.explosionapi.scoreboard;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class SidebarScoreboard {

    private Scoreboard scoreboard;
    private String displayName;
    private Objective sidebar = null;

    public SidebarScoreboard(Scoreboard scoreboard, String displayName) {
        this.scoreboard = scoreboard;
        this.displayName = displayName;
    }

    public Objective getObjective() {
        sidebar = scoreboard.getObjective("sidebar-scoreboard");
        if (sidebar == null) {
            sidebar = scoreboard.registerNewObjective("sidebar-scoreboard", "dummy", displayName);
        }
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        return sidebar;
    }

    public Score getScore(String entry) {
        return getObjective().getScore(entry);
    }

    @Deprecated
    public Score getScore(OfflinePlayer player) {
        return getObjective().getScore(player);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public String getDisplayName() {
        return getObjective().getDisplayName();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        getObjective().setDisplayName(displayName);
    }

    public void unregister() {
        getObjective().unregister();
    }

    public void set(String[] entries) {
        boolean changed = true;
        while (changed) {
            changed = false;
            ArrayList<String> knownStuff = new ArrayList<>(List.of(entries));
            for (int i = 0; i < entries.length; i++) {
                if (knownStuff.contains(entries[i])) {
                    knownStuff.remove(entries[i]);
                } else {
                    changed = true;
                    entries[i] = entries[i] + " ";
                }
            }
        }
        HashMap<String, Integer> scores = new HashMap<String, Integer>();
        for (int i = 0; i < entries.length; i++) {
            scores.put(entries[i], i);
        }
        for (String entry : getObjective().getScoreboard().getEntries()) {
            if (!scores.containsKey(entry)) {
                getObjective().getScoreboard().resetScores(entry);
            }
        }
        for (String keys : scores.keySet()) {
            if (!getObjective().getScoreboard().getEntries().contains(keys)) {
                getObjective().getScore(keys).setScore(scores.getOrDefault(keys, 0));
            }
        }
    }

    public void set(Set<String> entries) {
        set(entries.toArray(new String[entries.size()]));
    }

    public void set(List<String> entries) {
        set(entries.toArray(new String[entries.size()]));
    }

    public Set<String> get() {
        return getObjective().getScoreboard().getEntries();
    }

}

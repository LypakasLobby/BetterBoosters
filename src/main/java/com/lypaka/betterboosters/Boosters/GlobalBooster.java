package com.lypaka.betterboosters.Boosters;

import java.util.List;

public class GlobalBooster {

    private final String name;
    private final List<Booster> boosters;
    private final int startDay;
    private final int startHour;
    private final int startMinute;
    private final int startMonth;
    private final int endDay;
    private final int endHour;
    private final int endMinute;
    private final int endMonth;
    private final List<String> permissions;
    private boolean active;

    public GlobalBooster (String name, List<Booster> boosters, List<String> permissions, int endDay, int endHour, int endMinute, int endMonth, int startDay, int startHour, int startMinute, int startMonth) {

        this.name = name;
        this.boosters = boosters;
        this.startDay = startDay;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.startMonth = startMonth;
        this.endDay = endDay;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.endMonth = endMonth;
        this.permissions = permissions;
        this.active = false;

    }

    public void create() {

        BoosterUtils.globalBoosters.put(this.name, this);

    }

    public String getName() {

        return this.name;

    }

    public List<Booster> getBoosters() {

        return this.boosters;

    }

    public int getStartDay() {

        return this.startDay;

    }

    public int getStartHour() {

        return this.startHour;

    }

    public int getStartMinute() {

        return this.startMinute;

    }

    public int getStartMonth() {

        return this.startMonth;

    }

    public int getEndDay() {

        return this.endDay;

    }

    public int getEndHour() {

        return this.endHour;

    }

    public int getEndMinute() {

        return this.endMinute;

    }

    public int getEndMonth() {

        return this.endMonth;

    }

    public List<String> getPermissions() {

        return this.permissions;

    }

    public boolean isActive() {

        return this.active;

    }

    public void setActive (boolean active) {

        this.active = active;

    }

}

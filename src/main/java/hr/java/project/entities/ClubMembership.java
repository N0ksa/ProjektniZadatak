package hr.java.project.entities;

import java.time.LocalDate;

public class ClubMembership {
    private LocalDate joinDate;
    boolean isActiveMember;
    private String membershipID;

    public ClubMembership(LocalDate joinDate, String membershipId) {
        this.joinDate = joinDate;
        this.isActiveMember = true;
        this.membershipID = membershipId;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isActiveMember() {
        return isActiveMember;
    }

    public void setActiveMember(boolean activeMember) {
        isActiveMember = activeMember;
    }

    public String getMembershipID() {
        return membershipID;
    }

    public void setMembershipID(String membershipID) {
        this.membershipID = membershipID;
    }

}

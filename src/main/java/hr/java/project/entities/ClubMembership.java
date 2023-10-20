package hr.java.project.entities;

import java.util.Date;

public class ClubMembership {
    private Date joinDate;
    boolean isActiveMember;
    private String membershipID;

    public ClubMembership(Date joinDate, boolean isActiveMember, String membershipID) {
        this.joinDate = joinDate;
        this.isActiveMember = isActiveMember;
        this.membershipID = membershipID;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
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

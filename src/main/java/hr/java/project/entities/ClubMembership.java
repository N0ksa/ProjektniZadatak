package hr.java.project.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Predstavlja članstvo u matematičkom klubu.
 */
public class ClubMembership {
    boolean isActiveMember;
    private LocalDate joinDate;
    private String membershipID;

    /**
     * Konstruktor za stvaranje instance članstva u matematičkom klubu.
     *
     * @param joinDate Datum pridruživanja klubu.
     * @param membershipId Identifikacijski broj članstva.
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubMembership that = (ClubMembership) o;
        return isActiveMember == that.isActiveMember && Objects.equals(joinDate, that.joinDate)
                && Objects.equals(membershipID.toLowerCase(), that.membershipID.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isActiveMember, joinDate, membershipID);
    }
}

package hr.java.project.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Predstavlja članstvo u matematičkom klubu.
 */
public class ClubMembership {
    private LocalDate joinDate;
    private String membershipID;

    /**
     * Konstruktor za stvaranje instance članstva u matematičkom klubu.
     *
     * @param joinDate     Datum pridruživanja klubu.
     * @param membershipId Identifikacijski broj članstva.
     */
    public ClubMembership(String membershipId, LocalDate joinDate) {
        this.membershipID = membershipId;
        this.joinDate = joinDate;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
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
        return Objects.equals(joinDate, that.joinDate) && Objects.equals(membershipID, that.membershipID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joinDate, membershipID);
    }
}

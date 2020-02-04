package com.wocken.wedding.invitation

import com.google.common.collect.Lists

import javax.inject.Inject
import javax.inject.Singleton
import java.sql.Array
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@Singleton
class InvitationDao {

    private final Connection connection
    private final String url
    private final String username
    private final String password

    //TODO: generally figure out transactions
    @Inject
    InvitationDao() {
        this.url = "jdbc:postgresql://localhost:5432/wedding_website"
        this.username = "postgres"
        this.password = "postgres"
        this.connection = DriverManager.getConnection(url, username, password)
    }

    List<Invitation> getAllInvitations() {
        Statement statement = connection.createStatement()
        ResultSet rs = statement.executeQuery("SELECT * FROM invitation")
        List<Invitation> invitations = new ArrayList<>()
        try {
            while (rs.next()) {
                Invitation invitation = mapToInvitation(rs)
                invitations.add(invitation)
            }
        } finally {
            statement.close()
        }
        return invitations
    }

    //TODO: create invitation method
    void createInvitation(Invitation invitation) {
        if (!(invitation?.getInvitationId()
                || invitation?.getGuestIds()
                || invitation?.getRsvped())) {
            return
        }
        long invitationId = invitation.getInvitationId()
        boolean rsvped = invitation.getRsvped()
        long[] guestIds = invitation.guestIds?.toArray()
        Array guestIdArray = connection.createArrayOf("bigint", guestIds)
        String insertInvitationSql = "INSERT INTO invitation (id, rsvped, guests) VALUES (?, ?, ?)"
        PreparedStatement ps = connection.prepareStatement(insertInvitationSql)
        try {
            ps.setLong(1, invitationId)
            ps.setBoolean(2, rsvped)
            ps.setArray(3, guestIdArray)
            ps.executeUpdate()
        } finally {
            ps.close()
        }
    }

    Invitation getInvitation(long invitationId) {
        Statement statement = connection.createStatement()
        ResultSet rs = statement.executeQuery("SELECT * FROM invitation WHERE id = $invitationId LIMIT 1")
        try {
            while (rs.next()) {
                return mapToInvitation(rs)
            }
        } finally {
            statement.close()
        }
    }

    /**
     * Only returns first invitation in case
     * guest is part of multiple invitations for some
     * weird reason.
     * @param guestId
     * @return
     */
    Invitation getInvitationForGuestId(long guestId) {
        String findByGuestIdSql = "SELECT * FROM invitation WHERE $guestId = ANY (guests) LIMIT 1"
        Statement statement = connection.createStatement()
        ResultSet rs = statement.executeQuery(findByGuestIdSql)
        try {
            while (rs.next()) {
                return mapToInvitation(rs)
            }
        } finally {
            statement.close()
        }
    }

    private Invitation mapToInvitation(ResultSet rs) {
        long invitationId = rs.getLong('id')
        boolean rsvped = rs.getBoolean('rsvped')
        Array guestIdsArray = rs.getArray('guests')
        long[] dbGuestIds = (long[]) guestIdsArray.getArray()
        List<Long> guestIds = Lists.newArrayList(dbGuestIds)
        Invitation invitation = new Invitation(
                invitationId,
                rsvped,
                guestIds
        )
        return invitation
    }

}

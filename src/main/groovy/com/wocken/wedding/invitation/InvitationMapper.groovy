package com.wocken.wedding.invitation

import com.wocken.wedding.guest.Guest
import com.wocken.wedding.guest.GuestDao
import com.wocken.wedding.invitationsearch.InvitationDTO

import javax.inject.Inject
import javax.inject.Singleton
import java.util.stream.Collectors

@Singleton
class InvitationMapper {

    private final GuestDao guestDao

    @Inject
    InvitationMapper(GuestDao guestDao) {
        this.guestDao = guestDao
    }

    List<InvitationDTO> mapToInvitationDTOs(List<Invitation> invitations) {
        return invitations.stream()
                .filter({ Invitation invitation ->
                    null != invitation
                })
                .map({ Invitation invitation ->
                    mapToInvitationDTO(invitation)
                })
                .collect(Collectors.toList())
    }

    InvitationDTO mapToInvitationDTO(Invitation invitation) {
        if (!invitation) {
            return null
        }
        List<Guest> guests = invitation.guestIds.stream()
                .map({ Long guestId ->
                    guestDao.getGuestById(guestId)
                })
                .filter({ Guest guest ->
                    null != guest
                })
                .collect(Collectors.toList())
        return new InvitationDTO(
                invitation.getInvitationId(),
                invitation.getRsvped(),
                guests
        )
    }

}

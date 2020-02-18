package com.wocken.wedding.invitationsearch

import com.wocken.wedding.guest.Guest
import com.wocken.wedding.guest.GuestDao
import com.wocken.wedding.invitation.Invitation
import com.wocken.wedding.invitation.InvitationDao

import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Collectors

@Singleton
class GuestInvitationService {

    private final GuestDao guestDao
    private final InvitationDao invitationDao

    @Inject
    GuestInvitationService(GuestDao guestDao, InvitationDao invitationDao) {
        this.guestDao = guestDao
        this.invitationDao = invitationDao
    }

    //TODO: add transaction at this level
    void updateInvitation(InvitationDTO invitationUpdate) {
        if (!invitationUpdate) {
            return
        }
        invitationUpdate.getGuests().forEach({ Guest guest ->
            if (null != guest.attending) {
                guestDao.updateIsAttending(guest.guestId, guest.attending.booleanValue())
            }
            if (null != guest.meal) {
                guestDao.updateMeal(guest.guestId, guest.getMeal())
            }
        })
        if (null != invitationUpdate.getRsvped()) {
            invitationDao.updateRsvped(
                    invitationUpdate.invitationId,
                    invitationUpdate.getRsvped().booleanValue()
            )
        }
    }

    List<Invitation> findInvitationsByGuestSearch(String searchString) {
        if (!searchString || searchString.isEmpty()) {
            return []
        }
        String lowerCaseSearchString = searchString.toLowerCase()
        List<Guest> guestsMatchingSearch = guestDao.getGuestsForSearchString(lowerCaseSearchString)
        return guestsMatchingSearch.stream()
            .filter({ Guest guest ->
                null != guest && null != guest.guestId
            })
            .map({ Guest guest ->
                invitationDao.getInvitationForGuestId(guest.guestId)
            })
            .filter({ Invitation invitation ->
                null != invitation
            })
            .collect(Collectors.toList())
    }

}

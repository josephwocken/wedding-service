package com.wocken.wedding.invitationsearch

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.wocken.wedding.guest.Guest
import com.wocken.wedding.invitation.Invitation

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class InvitationDTO {
    @JsonProperty("invitation_id")
    private final long invitationId
    @JsonProperty("rsvped")
    private final Boolean rsvped
    @JsonProperty("guests")
    private final List<Guest> guests

    @JsonCreator
    InvitationDTO(
            @JsonProperty("invitation_id") long invitationId,
            @JsonProperty("rsvped") Boolean rsvped,
            @JsonProperty("guests") List<Guest> guests
    ) {
        this.invitationId = invitationId
        this.rsvped = rsvped
        this.guests = guests
    }

    @JsonIgnore
    List<Guest> getGuests() {
        return this.guests
    }

    @JsonIgnore
    long getInvitationId() {
        return this.invitationId
    }

    @JsonIgnore
    Boolean getRsvped() {
        return this.rsvped
    }
}

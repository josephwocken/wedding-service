package com.wocken.wedding.invitation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Invitation {

    @JsonProperty("invitation_id")
    private final long invitationId
    @JsonProperty("rsvped")
    private final boolean rsvped
    @JsonProperty("guest_ids")
    private final List<Long> guestIds

    @JsonCreator
    Invitation(
            @JsonProperty("invitation_id") long invitationId,
            @JsonProperty("rsvped") boolean rsvped,
            @JsonProperty("guest_ids") List<Long> guestIds
    ) {
        this.invitationId = invitationId
        this.rsvped = rsvped
        this.guestIds = guestIds
    }

    @JsonIgnore
    long getInvitationId() {
        return invitationId
    }

    @JsonIgnore
    boolean getRsvped() {
        return rsvped
    }

    @JsonIgnore
    List<Long> getGuestIds() {
        return guestIds
    }
}

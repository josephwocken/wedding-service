package com.wocken.wedding.invitationsearch

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class GuestInvitationSearch {
    @JsonProperty("guest_search_string")
    private String guestSearchString

    @JsonCreator
    GuestInvitationSearch(@JsonProperty("guest_search_string") String guestSearchString) {
        this.guestSearchString = guestSearchString
    }

    @JsonIgnore
    String getGuestSearchString() {
        return this.guestSearchString
    }
}

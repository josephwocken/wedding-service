package com.wocken.wedding.guest

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Guest {
    @JsonProperty("guest_id")
    private final long id
    @JsonProperty("guest_name")
    private final String name
    @JsonProperty("attending")
    private final Boolean attending
    @JsonProperty("invitation_id")
    private final long invitationId
    @JsonProperty("meal")
    private final Meal meal

    @JsonCreator
    Guest(
            @JsonProperty("guest_id") long id,
            @JsonProperty("guest_name") String name,
            @JsonProperty("attending") Boolean attending,
            @JsonProperty("invitation_id") long invitationId,
            @JsonProperty("meal") Meal meal
    ) {
        this.id = id
        this.name = name
        this.attending = attending
        this.invitationId = invitationId
        this.meal = meal
    }

    @JsonIgnore
    long getGuestId() {
        return this.id
    }

    @JsonIgnore
    String getGuestName() {
        return this.name
    }

    @JsonIgnore
    Boolean isAttending() {
        return this.attending
    }

    @JsonIgnore
    long getInvitationId() {
        return this.invitationId
    }

    @JsonIgnore
    Meal getMeal() {
        return this.meal
    }
}

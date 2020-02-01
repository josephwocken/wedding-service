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

    @JsonCreator
    Guest(
            @JsonProperty("guest_id") long id,
            @JsonProperty("guest_name") String name
    ) {
        this.id = id
        this.name = name
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
    String toString() {
        return "Guest = (id = $id, name = $name)"
    }
}

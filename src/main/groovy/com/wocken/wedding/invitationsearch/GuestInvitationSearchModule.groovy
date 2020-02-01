package com.wocken.wedding.invitationsearch

import com.google.inject.AbstractModule

class GuestInvitationSearchModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GuestInvitationService)
        bind(GuestInvitationSearchHandler)
    }
}

package com.wocken.wedding.invitation

import com.google.inject.AbstractModule

class InvitationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(InvitationDao).toInstance(new InvitationDao())
        bind(InvitationHandler)
    }
}

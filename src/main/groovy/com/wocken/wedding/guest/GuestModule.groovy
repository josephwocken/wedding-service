package com.wocken.wedding.guest

import com.google.inject.AbstractModule
import com.wocken.wedding.guest.GuestDao
import com.wocken.wedding.guest.GuestHandler

class GuestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GuestDao).toInstance(new GuestDao())
        bind(GuestHandler)
    }
}

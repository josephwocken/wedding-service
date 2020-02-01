package com.wocken.wedding

import com.google.inject.AbstractModule

class ResponseHeaderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ResponseHeaderHandler)
    }
}

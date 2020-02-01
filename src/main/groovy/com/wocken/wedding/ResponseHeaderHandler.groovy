package com.wocken.wedding

import ratpack.handling.Context
import ratpack.handling.Handler

import javax.inject.Singleton

@Singleton
class ResponseHeaderHandler implements Handler {
    @Override
    void handle(Context ctx) throws Exception {
        ctx.getResponse().getHeaders()
                .add("Access-Control-Allow-Origin", "*")
                .add("Access-Control-Allow-Headers", "Content-Type, Accept")
        ctx.next()
    }
}

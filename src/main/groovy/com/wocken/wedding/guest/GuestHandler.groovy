package com.wocken.wedding.guest

import com.fasterxml.jackson.databind.ObjectMapper
import com.wocken.wedding.guest.Guest
import com.wocken.wedding.guest.GuestDao
import ratpack.func.Block
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.http.Status
import ratpack.jackson.Jackson

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestHandler implements Handler {

    private final GuestDao guestDao
    private final ObjectMapper objectMapper

    @Inject
    GuestHandler(GuestDao guestDao) {
        this.guestDao = guestDao
        this.objectMapper = new ObjectMapper()
    }

    @Override
    void handle(Context ctx) throws Exception {
        ctx.byMethod({ ByMethodSpec m -> m
            .get(new Block() {
                @Override
                void execute() throws Exception {
                    String guestId = ctx.getPathTokens().get('id')
                    if (guestId) {
                        Guest guest = guestDao.getGuestById(guestId.toLong())
                        if (guest) {
                            ctx.render(Jackson.json(guest))
                        } else {
                            ctx.getResponse()
                                .status(Status.NOT_FOUND)
                                .send()
                        }
                    } else {
                        ctx.render(Jackson.json(guestDao.getAllGuests()))
                    }
                }
            })
            .post(new Block() {
                @Override
                void execute() throws Exception {
                    ctx
                        .parse(Jackson.fromJson(Guest))
                        .then({ Guest guest ->
                            println(objectMapper.writeValueAsString(guest))
                            guestDao.createGuest(guest)
                        })
                    ctx.getResponse()
                        .status(Status.CREATED)
                        .send("OK")
                }
            })
            .delete(new Block() {
                @Override
                void execute() throws Exception {
                    String guestId = ctx.getPathTokens().get('id')
                    if (guestId) {
                        println("deleting guest id: $guestId")
                        guestDao.deleteGuest(guestId.toLong())
                        ctx.getResponse()
                                .status(Status.OK)
                                .send()
                    } else {
                        println("no guest id provided")
                        ctx.getResponse()
                                .status(Status.BAD_REQUEST)
                                .send()
                    }
                }
            })
        })
    }
}

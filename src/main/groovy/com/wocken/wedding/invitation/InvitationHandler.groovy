package com.wocken.wedding.invitation

import com.fasterxml.jackson.databind.ObjectMapper
import ratpack.func.Block
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.http.Status
import ratpack.jackson.Jackson

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationHandler implements Handler {

    private final InvitationDao invitationDao
    private final ObjectMapper objectMapper

    @Inject
    InvitationHandler(InvitationDao invitationDao) {
        this.invitationDao = invitationDao
        this.objectMapper = new ObjectMapper()
    }

    @Override
    void handle(Context ctx) throws Exception {
        ctx.byMethod({ ByMethodSpec m -> m
            .get(new Block() {
                @Override
                void execute() throws Exception {
                    ctx.render(Jackson.json(invitationDao.getAllInvitations()))
                }
            })
            .post(new Block() {
                @Override
                void execute() throws Exception {
                    ctx
                        .parse(Jackson.fromJson(Invitation))
                        .then({ Invitation invitation ->
                            println(objectMapper.writeValueAsString(invitation))
                            invitationDao.createInvitation(invitation)
                        })
                    ctx.getResponse()
                        .status(Status.CREATED)
                        .send("OK")
                }
            })
        })
    }
}

package com.wocken.wedding.invitation

import com.fasterxml.jackson.databind.ObjectMapper
import com.wocken.wedding.invitationsearch.InvitationDTO
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
    private final InvitationMapper invitationMapper
    private final ObjectMapper objectMapper

    @Inject
    InvitationHandler(InvitationDao invitationDao, InvitationMapper invitationMapper) {
        this.invitationDao = invitationDao
        this.invitationMapper = invitationMapper
        this.objectMapper = new ObjectMapper()
    }

    @Override
    void handle(Context ctx) throws Exception {
        ctx.byMethod({ ByMethodSpec m -> m
            .get(new Block() {
                @Override
                void execute() throws Exception {
                    String invitationId = ctx.getPathTokens().get('id')
                    if (invitationId) {
                        Invitation invitation = invitationDao.getInvitation(invitationId.toLong())
                        if (invitation) {
                            InvitationDTO invitationDTO = invitationMapper.mapToInvitationDTO(invitation)
                            String serializedInvitation = objectMapper.writeValueAsString(invitationDTO)
                            ctx.getResponse()
                                .status(Status.OK)
                                .getHeaders()
                                .set("Content-Type", "application/json")
                            ctx.getResponse()
                                .send(serializedInvitation)
                        } else {
                            ctx.getResponse()
                                .status(Status.NOT_FOUND)
                                .send()
                        }
                    } else {
                        ctx.render(Jackson.json(invitationDao.getAllInvitations()))
                    }
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

package com.wocken.wedding.invitation

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.wocken.wedding.invitationsearch.GuestInvitationService
import com.wocken.wedding.invitationsearch.InvitationDTO
import ratpack.exec.Blocking
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
    private final GuestInvitationService guestInvitationService
    private final ObjectMapper objectMapper

    @Inject
    InvitationHandler(
            InvitationDao invitationDao,
            InvitationMapper invitationMapper,
            GuestInvitationService guestInvitationService
    ) {
        this.invitationDao = invitationDao
        this.invitationMapper = invitationMapper
        this.guestInvitationService = guestInvitationService
        this.objectMapper = new ObjectMapper()
    }

    @Override
    void handle(Context ctx) throws Exception {
        ctx.byMethod({ ByMethodSpec m -> m
            .options(new Block() {
                @Override
                void execute() throws Exception {
                    ctx.getResponse()
                            .status(Status.OK)
                            .getHeaders()
                            .set("Allow", "POST, GET, PUT")
                            .set("Access-Control-Allow-Methods", "GET,PUT,POST")
                    ctx.getResponse().send()
                }
            })
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
            .put(new Block() {
                @Override
                void execute() throws Exception {
//                    Blocking.exec({
//
//                    })
                    ctx.parse(Jackson.fromJson(InvitationDTO))
                        .onError(JsonProcessingException, {
                            println('failed to deserialize invitation update')
                            //TODO: figure out how to return error
                            ctx.getResponse()
                                .status(Status.BAD_REQUEST)
                                .send()
                        })
                        .then({ InvitationDTO invitationUpdate ->
                            println(objectMapper.writeValueAsString(invitationUpdate))
                            try {
                                guestInvitationService.updateInvitation(invitationUpdate)
                            } catch (Exception exception) {
                                exception.printStackTrace()
                                ctx.getResponse()
                                    .status(Status.INTERNAL_SERVER_ERROR)
                                    .send()
                            }
                        })
//                        .onError(Exception, {
//                            ctx.getResponse()
//                                .status(Status.INTERNAL_SERVER_ERROR)
//                                .send()
//                        })
                    //TODO: validate that this doesn't get called on exception
                    ctx.getResponse()
                        .status(Status.OK)
                        .send()
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

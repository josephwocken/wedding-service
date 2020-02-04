package com.wocken.wedding.invitationsearch

import com.fasterxml.jackson.databind.ObjectMapper
import com.wocken.wedding.guest.Guest
import com.wocken.wedding.guest.GuestDao
import com.wocken.wedding.invitation.Invitation
import com.wocken.wedding.invitation.InvitationMapper
import ratpack.exec.Promise
import ratpack.func.Block
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.http.Status
import ratpack.http.TypedData
import ratpack.jackson.Jackson

import javax.inject.Inject
import javax.inject.Singleton
import java.nio.charset.Charset
import java.util.stream.Collectors

@Singleton
class GuestInvitationSearchHandler implements Handler {

    private final GuestInvitationService guestInvitationService
    private final GuestDao guestDao
    private final InvitationMapper invitationMapper
    private final ObjectMapper objectMapper

    @Inject
    GuestInvitationSearchHandler(GuestInvitationService guestInvitationService, GuestDao guestDao, InvitationMapper invitationMapper) {
        this.guestInvitationService = guestInvitationService
        this.guestDao = guestDao
        this.invitationMapper = invitationMapper
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
                        .set("Allow", "POST, GET")
                    ctx.getResponse().send()
                }
            })
            .post(new Block() {
                @Override
                void execute() throws Exception {
                    ctx.getRequest().getBody()
                        .map({ TypedData typedDataRequest ->
                            String requestBody = typedDataRequest.getText(Charset.defaultCharset())
                            println("Request Body: ${requestBody}")
                            return objectMapper.readValue(requestBody, GuestInvitationSearch)
                        })
                        .map({ GuestInvitationSearch search ->
                            return guestInvitationService.findInvitationsByGuestSearch(
                                    search.getGuestSearchString()
                            )
                        })
                        .then({ List<Invitation> invitations ->
                            if (invitations && !invitations.isEmpty()) {
                                List<InvitationDTO> invitationDTOs = invitationMapper.mapToInvitationDTOs(invitations)
                                String response = objectMapper.writeValueAsString(invitationDTOs)
                                println("Response: $response")
                                ctx.getResponse()
                                        .status(Status.OK)
                                        .send("application/json", response)
                            } else {
                                ctx.getResponse()
                                        .status(Status.NOT_FOUND)
                                        .send()
                            }


                        })
                }
            })
        })
    }
}

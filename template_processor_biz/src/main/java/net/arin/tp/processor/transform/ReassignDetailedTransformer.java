package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.TicketPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.api.service.NetService;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;

import javax.mail.Message;

public class ReassignDetailedTransformer extends ComplexSwipTransformer
{
    @Override
    TicketedRequestPayload performSwip( NetService netService, String parentNetHandle, String apiKey, NetPayload net )
    {
        return netService.reassign( parentNetHandle, apiKey, net );
    }

    @Override
    Message setupTicketedResponse( TemplateMessage message, TicketPayload ticket, OrgPayload org )
    {
        return Response.detailedReassignTicketed( message, ticket, org );
    }

    @Override
    Message setupUnticketedResponse( TemplateMessage message, NetPayload net, OrgPayload org )
    {
        return Response.detailedReassignSuccessful( message, net, org );
    }
}

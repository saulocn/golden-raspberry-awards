package br.com.saulocn.goldenraspberryawards.resource;

import br.com.saulocn.goldenraspberryawards.resource.vo.ProducerIntervals;
import br.com.saulocn.goldenraspberryawards.service.IntervalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("intervals")
@Produces("application/json")
@Consumes("application/json")
public class IntervalResource {

    @Inject
    private IntervalService intervalService;

    @GET
    @Operation(summary = "List max and min interval of winners")
    public ProducerIntervals getIntervals() {
        return intervalService.getIntervals();
    }
}

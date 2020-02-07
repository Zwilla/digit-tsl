package service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.model.wadl.Description;

@Path("PrettyPrint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Description(value = "PrettyPrint interface for PDF generation")
public interface PrettyPrintService {

    @POST
    @Path("generatePdf")
    String generatePdf(String xmlPath);
}

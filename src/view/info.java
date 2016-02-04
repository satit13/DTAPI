package view;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.List;

//import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import bean.VersionInfoBean;

@Path(value="/info")
public class info {
	@Path("/getversion") 
	@GET
//	@Consumes( {MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})

	public VersionInfoBean  ver() throws IOException {
		

		
		VersionInfoBean ver= new VersionInfoBean();
		ver.setVersionId("19/09/2015 - Billing Draft Included");
		return ver;

}

}

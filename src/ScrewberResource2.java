import Model.Estimate;
import Model.Estimate2;
import Model.FinalResult;
import Model.Position;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Path("/api2")
public class ScrewberResource2 {

    public static List<Estimate2> estimates = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/compute")
    public Response computeBestPath(@QueryParam("source") String source,
                                    @QueryParam("destination") String destination,
                                    @QueryParam("type") String type) throws Exception {

        Screwber screwber = new Screwber();
        List<Position> points = screwber.getPoints(source, destination);
        Double[] latLong = screwber.getLatLong(source, destination);
        Main main = new Main();
        Estimate estimate = main.calculateFareAndTime(latLong[0].floatValue(), latLong[1].floatValue(), latLong[2].floatValue(), latLong[3].floatValue(), type);
        if (estimate != null) {
            estimates.add(estimate.clone(estimate));
        }
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for (Position position : points) {
            Runnable worker = new WorkerThread(position, latLong, type);
            executor.execute(worker);
        }
        executor.shutdown();
        try{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Estimate2 min = Collections.min(estimates);
        String finalSource = "";
        String finalDest = "";
        String breakPoint = "";
        if(min.getPosition()!=null){
            breakPoint = screwber.getAddress(min.getPosition().getX(), min.getPosition().getY());
        }
        int netSavings = estimate.getAverageCost() - min.getAverageCost();
        int initialCost = estimate.getAverageCost();
        int FinalCost = min.getAverageCost();
        int initialTime = estimate.getEstimatedTime();
        int finalTime = min.getEstimatedTime();
        FinalResult finalResult = new FinalResult(breakPoint, finalSource, finalDest, netSavings, finalTime, initialTime, initialCost, FinalCost);
        estimates.clear();
        return Response.status(Response.Status.OK).entity(finalResult).build();
    }
}
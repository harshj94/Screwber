import Model.Estimate;
import Model.Estimate2;
import Model.FinalResult;
import Model.ListProduct;
import Model.Position;
import Model.Product;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.Session;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.ProductsResponse;
import com.uber.sdk.rides.client.services.RidesService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
//        return Response.status(Response.Status.OK).entity(new GenericEntity<List<Estimate>>(estimates) {
//        }).build();
        estimates.clear();
        return Response.status(Response.Status.OK).entity(finalResult).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products")
    public Response getAvailableProducts(@QueryParam("source") String source,
                                         @QueryParam("destination") String destination) {
        Set<Product> products = new HashSet<>();
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("6Zq8vClPJJBGHYQbJgm-KYVEIIeepxpc")
                .setServerToken("q_BWZN1silU3GhBxJcDlR79s2Qpp_oHHjYrK3kpW")
                .build();
        Session session = new ServerTokenSession(config);
        RidesService service = UberRidesApi.with(session).build().createService();
        Screwber screwber = new Screwber();
        Float latitude = null;
        Float longitude = null;
        try {
            Double[] latLong = screwber.getLatLong(source, destination);
            latitude = latLong[0].floatValue();
            longitude = latLong[1].floatValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println(latitude + " " + longitude);
            retrofit2.Response<ProductsResponse> productsResponse = service.getProducts(latitude, longitude).execute();
            List<com.uber.sdk.rides.client.model.Product> productsList = productsResponse.body().getProducts();
            Product productName;
            for (com.uber.sdk.rides.client.model.Product product : productsList) {
                productName = new Product();
                productName.setProductName(product.getDisplayName());
                products.add(productName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListProduct listProduct = new ListProduct();
        listProduct.setProductList(products);
        return Response.status(Response.Status.OK).entity(listProduct).build();
    }
}
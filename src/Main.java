import Model.Estimate;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.Session;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.PriceEstimate;
import com.uber.sdk.rides.client.model.PriceEstimatesResponse;
import com.uber.sdk.rides.client.services.RidesService;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        long l = System.currentTimeMillis();
        Estimate estimate = main.calculateFareAndTime(
                28.5550838f,
                77.0822128f,
                28.5473971f,
                77.2717317f,
                "UberGo");
        l = System.currentTimeMillis() - l;
        System.out.println(l / 1000);
        if (estimate != null) {
            System.out.println(estimate.getAverageCost() + " " + estimate.getEstimatedTime() / 60);
        }
    }

    //start_latitude=28.5550838
    // start_longitude=77.0822128
    // end_latitude=28.5473971
    // end_longitude=77.2717317

    public Estimate calculateFareAndTime(Float startLat, Float startLon, Float endLat, Float endLon, String productType) {
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("6Zq8vClPJJBGHYQbJgm-KYVEIIeepxpc")
                .setServerToken("q_BWZN1silU3GhBxJcDlR79s2Qpp_oHHjYrK3kpW")
                .build();
        Session session = new ServerTokenSession(config);
        RidesService service = UberRidesApi.with(session).build().createService();
        try {
            Response<PriceEstimatesResponse> execute = service.getPriceEstimates(startLat, startLon, endLat, endLon).execute();
            List<PriceEstimate> prices = execute.body().getPrices();
            for (PriceEstimate price : prices) {
                if (price.getDisplayName().equals(productType)) {
                    Integer averageCost = price.getHighEstimate();
                    Integer duration = price.getDuration();
                    return new Estimate(averageCost, duration);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

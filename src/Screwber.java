import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Model.Position;
import org.json.*;

public class Screwber {


    public String getAddress(Double lat,Double lng) throws Exception{
        String des = "Hauz%20Khas";
        URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+lat+","+lng+"&destination="+des+"&key=AIzaSyDHQ_lHGb2-BgX01cHvCS69z3b5zUMUBLY");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        int status = con.getResponseCode();
        System.out.println("Status : "+status);
        System.out.println("\n\n\n\n\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        //System.out.println(content.toString());
        //Gson g = new Gson();
        JSONObject json = new JSONObject(content.toString());
        //System.out.println(json);
        JSONArray routes = json.getJSONArray("routes");
        JSONObject jsonRoutes =  (JSONObject)(routes.get(0));
        JSONArray legs = jsonRoutes.getJSONArray("legs");
        JSONObject jsonLegs = (JSONObject)(legs.get(0));
        String address = jsonLegs.getString("start_address");
        return address;
    }

    public List<Position> getPoints(String source, String des) throws IOException, JSONException {
        /*Screwber s = new Screwber();
        try{

            Double l[] = s.getLatLong();
            System.out.println(l[0]+" "+l[1]+" "+l[2]+" "+l[3]+" ");
        }catch(Exception e){}*/
       // String source = "IIIT,Delhi";
       // String des = "Hauz%20Khas";
        source=source.replaceAll(" ","%20");
        des=des.replaceAll(" ","%20");
        URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+source+"&destination="+des+"&key=AIzaSyDHQ_lHGb2-BgX01cHvCS69z3b5zUMUBLY");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        int status = con.getResponseCode();
        System.out.println("Status : "+status);
        System.out.println("\n\n\n\n\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        //System.out.println(content.toString());
        //Gson g = new Gson();
        JSONObject json = new JSONObject(content.toString());
        //System.out.println(json);
        JSONArray routes = json.getJSONArray("routes");
        JSONObject jsonRoutes =  (JSONObject)(routes.get(0));
        //for(JSONObject j: jsonArray.getJSONObject(0))
        //System.out.println(jsonRoutes.getJSONArray("legs"));
        JSONArray legs = jsonRoutes.getJSONArray("legs");
        JSONObject jsonLegs = (JSONObject)(legs.get(0));
        JSONArray steps = jsonLegs.getJSONArray("steps");
        int stepsLength = steps.length();
        int dist[] = new int[stepsLength];
        Position start[] = new Position[stepsLength];
        Position end[] = new Position[stepsLength];
        for(int i =0;i< stepsLength;i++){
            JSONObject jsonStep = (JSONObject) steps.get(i);
            //System.out.println("Distance : "+jsonStep.getJSONObject("distance").getInt("value"));
            dist[i] = jsonStep.getJSONObject("distance").getInt("value");
            Double startX = jsonStep.getJSONObject("start_location").getDouble("lat");
            Double startY = jsonStep.getJSONObject("start_location").getDouble("lng");
            start[i] = new Position(startX,startY);
            Double endX = jsonStep.getJSONObject("end_location").getDouble("lat");
            Double endY = jsonStep.getJSONObject("end_location").getDouble("lng");
            end[i] = new Position(endX,endY);
            System.out.print("Distance : "+dist[i]);
            System.out.print("Start : "+start[i].getX()+" , "+start[i].getY());
            System.out.println("End : "+end[i].getX()+" , "+end[i].getY());
        }
        int sum = 0;
        List<Position> pos = new ArrayList<>();
        for(int i=0;i<stepsLength;i++){
            int a = 2000 - sum;
            sum = sum + dist[i];
            if(sum > 2000){
                int b = sum - 2000;
                if(b < a){
                    pos.add(end[i]);
                    sum = 0;
                }
                else{
                    pos.add(start[i]);
                    b = dist[i];
                    if(b > 2000){
                        int points = 2 + (int)Math.floor(b/2000);
                        int temp = points - 2;
                        b = b - temp*2000;
                        Double facX = (end[i].getX() - start[i].getX()) / points;
                        Double facY = (end[i].getY() - start[i].getY()) / points;
                        while(temp > 0){
                            Double x = start[i].getX() + facX;
                            Double y = start[i].getY() + facY;
                            Position p = new Position(x,y);
                            pos.add(p);
                            facX = facX + (end[i].getX() - start[i].getX()) / points;
                            facY = facY + (end[i].getY() - start[i].getY()) / points;
                            temp--;
                        }

                    }
                    sum = b ;
                }

            }
        }
        for(Position p:pos){
            System.out.println("X : "+p.getX()+" Y : "+p.getY());
        }

        in.close();
        con.disconnect();
        return pos;
    }

    public Double[] getLatLong(String source,String des) throws Exception{
        //String source = "IIIT,Delhi";
        //String des = "Hauz%20Khas";
        source=source.replaceAll(" ","%20");
        des=des.replaceAll(" ","%20");
        URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+source+"&destination="+des+"&key=AIzaSyDHQ_lHGb2-BgX01cHvCS69z3b5zUMUBLY");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        int status = con.getResponseCode();
        System.out.println("Status : "+status);
        System.out.println("\n\n\n\n\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        //System.out.println(content.toString());
        //Gson g = new Gson();
        JSONObject json = new JSONObject(content.toString());
        //System.out.println(json);
        JSONArray routes = json.getJSONArray("routes");
        JSONObject jsonRoutes =  (JSONObject)(routes.get(0));
        JSONArray legs = jsonRoutes.getJSONArray("legs");
        JSONObject jsonLegs = (JSONObject)(legs.get(0));
        Double latLong[] = new Double[4];
        latLong[0] = jsonLegs.getJSONObject("start_location").getDouble("lat");
        latLong[1] = jsonLegs.getJSONObject("start_location").getDouble("lng");
        latLong[2] = jsonLegs.getJSONObject("end_location").getDouble("lat");
        latLong[3] = jsonLegs.getJSONObject("end_location").getDouble("lng");
        return latLong;
    }
}
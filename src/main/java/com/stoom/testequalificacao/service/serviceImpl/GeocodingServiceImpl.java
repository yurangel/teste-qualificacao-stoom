package com.stoom.testequalificacao.service.serviceImpl;

import com.stoom.testequalificacao.model.Address;
import com.stoom.testequalificacao.service.GeocodingService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.stoom.testequalificacao.utils.Constants.KEY;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    @Override
    public Address generateLinkGeocoding(Address address) {
        Address addressTO = address;
        StringBuffer outputToJson = new StringBuffer();;
        try {
            StringBuffer link = new StringBuffer();
            link.append("https://maps.googleapis.com/maps/api/geocode/json?address=");
            link.append(address.getNumber()+"+");
            link.append(address.getStreetName()+"+");
            link.append(address.getCity()+"+");
            link.append(address.getState()+"+");
            link.append(address.getCountry());
            link.append("&key="+ KEY);

            URL geocodingUrl = new URL(URLEncoder.encode(link.toString(), "UTF-8"));

            HttpURLConnection conn = (HttpURLConnection) geocodingUrl.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            System.out.println("Output from Server .... \n");
            String output = null;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                outputToJson.append(output);
            }

            JSONObject jsonObject = new JSONObject(outputToJson.toString());
            addressTO.setLatitude(String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat")));
            addressTO.setLongitude(String.valueOf(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng")));

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addressTO;
    }
}

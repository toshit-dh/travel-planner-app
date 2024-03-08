package com.example.travelplanner.api;

import com.example.travelplanner.data.LoginUser;

import com.example.travelplanner.data.MyPrefs;
import com.example.travelplanner.data.Trip;
import com.example.travelplanner.data.TripChatsImages;
import com.example.travelplanner.data.User;
import com.example.travelplanner.data.UserData;
import com.example.travelplanner.data.WeatherData;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("api/auth/register")
    Call<SignUpResponse> onSignup(@Body User user);

    @POST("api/auth/login")
    Call<SignUpResponse> onLogin(@Body LoginUser loginUser);

    @GET("api/auth/getData")
    Call<UserData> onGetData(@HeaderMap Map<String,String> headers);

    @GET("get/amadeus/getHotels/")
    Call <List<HotelData>> onHotelData(@Query("code") String iata, @HeaderMap Map<String,String> headers);

    @GET("get/amadeus/getFlights")
    Call <List<FlightsData>> onFlightData(@Query("src") String src,@Query("des") String des,@Query("date") String date,@Query("range") String range,@HeaderMap Map<String,String> headers);

    @Multipart
    @POST("trip/addTrip")
    Call <StringResponse> onTripData(@Part("departureDate")RequestBody date, @Part("returndate") RequestBody returndate, @Part("departureTime")RequestBody departure, @Part("city") RequestBody city, @Part("tripmates") ArrayList<String> tripmates, @Part MultipartBody.Part file, @HeaderMap Map<String,String> headers);

    @GET("trip/getTrip")
    Call <List<Trip>> onTripData(@HeaderMap Map<String,String> headers);

    @GET("trip/planItenary")
    Call<ItenaryData> onItenaryData(@Query("location") String location,@Query("stay_days") String staydays,@Query("budget") String budget,@HeaderMap Map<String,String> headers);

    @GET("trip/getWeather")
    Call <List<WeatherData>> onWeatherData(@Query("cityName") String cityName,@HeaderMap Map<String,String> headers);

    @GET("trip/cancelTrip")
    Call <StringResponse> onCancelTrip(@Query("id") String id,@HeaderMap Map<String,String> headers );

    @GET("get/amadeus/getDest")
    Call<List<DestinationData>> onDestData(@Query("area") String area,@HeaderMap Map<String,String> headers );

    @GET("get/amadeus/getActivity")
    Call<List<ActivityData>> onActivityData(@Query("area") String area,@HeaderMap Map<String,String> headers);

    @GET("get/amadeus/getHotelReview")
    Call<ReviewData> onHotelReview(@Query("hotelId") String hotelId,@HeaderMap Map<String,String> headers);

    @GET("sugg/addSugg")
    Call<StringResponse> onAddSuggestion(@Query("tag") String tag,@Query("city") String city,@Query("country") String country,@Query("msg") String msg,@HeaderMap Map<String,String> headers);

    @GET("sugg/removeSugg")
    Call<StringResponse> onRemoveSuggestion(@Query("suggId") String id,@HeaderMap Map<String,String> headers);

    @GET("sugg/mySugg")
    Call<List<SuggestionData>> onSuggestion(@HeaderMap Map<String,String> headers);

    @GET("sugg/getSugg")
    Call<List<SuggestionData>> onSearchSuggestion(@Query("tag") String tag,@Query("city") String city,@HeaderMap Map<String,String> headers);

    @GET("sugg/voteSugg")
    Call<StringResponse> onVotingSuggestion(@Query("suggId") String suggId,@HeaderMap Map<String,String> headers);

    @GET("sugg/removeVote")
    Call<StringResponse> onRemoveVote(@Query("suggId") String suggId,@HeaderMap Map<String,String> headers);

    @GET("chats/getChatItems")
    Call<ChatItems> onChatItems(@HeaderMap Map<String,String> headers);

    @GET("chats/addFriend")
    Call<StringResponse> onAddFriend(@Query("add_who") String id,@HeaderMap Map<String,String> headers);

    @GET("chats/acceptFriend")
    Call<StringResponse> onAcceptFriend(@Query("ofUserId") String userId,@HeaderMap Map<String,String> headers);

    @GET("chats/rejectFriend")
    Call<StringResponse> onRejectFriend(@Query("ofUserId") String userId,@HeaderMap Map<String,String> headers);

    @GET("chats/getTripMessages")
    Call<TripChatsImages> onGetMessages(@Query("tripId") String tripId, @HeaderMap Map<String,String> headers);

    @GET("chats/addMessage")
    Call<StringResponse> onAddMessage(@Query("text") String msg,@Query("tripId") String tripId,@HeaderMap Map<String,String> headers);

    @Multipart
    @POST("chats/addImagesMessage")
    Call <StringResponse> onAddImagesMessage(@Query("tripId") String tripId,@Part ArrayList<MultipartBody.Part> images,@HeaderMap Map<String,String> headers);
}

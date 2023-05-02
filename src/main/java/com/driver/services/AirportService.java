package com.driver.services;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repositories.AirportRepository;
import io.swagger.models.auth.In;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AirportService {

    AirportRepository airportRepository = new AirportRepository();

    public void addAirport(Airport airport) {

        Optional<Airport> optionalAirport = getAirport(airport.getAirportName());
        if(optionalAirport.isPresent()) return;

        airportRepository.addAirport(airport);
        return;
    }

    public Optional<Airport> getAirport(String airportName) {

        return airportRepository.getAirport(airportName);
    }

    public String getLargestAirportName() {

        Optional<List<Airport>> optionalAirports = getAllAirports();
        if(optionalAirports.isEmpty()) return "";

        List<Airport> allAirports = optionalAirports.get();

        String largestAirport = "";
        int maxTerm = 0;

        for(Airport curr : allAirports){

            if(maxTerm<curr.getNoOfTerminals()){
                maxTerm = curr.getNoOfTerminals();
                largestAirport = curr.getAirportName();
            }

            if(maxTerm == curr.getNoOfTerminals()){
                if(largestAirport.compareTo(curr.getAirportName()) >= 0){
                    largestAirport = curr.getAirportName();
                }
            }
        }

        return largestAirport;
    }

    public Optional<List<Airport>> getAllAirports() {

        return airportRepository.getAllAirports();
    }

    public void addFlight(Flight flight) {

        Optional<Flight> optionalFlight = getFlight(flight.getFlightId());

        if(optionalFlight.isPresent()) return;

        airportRepository.addFlight(flight);
        return;
    }

    public Optional<Flight> getFlight(int flightId) {

        return airportRepository.getFlight(flightId);
    }

    public void addPassenger(Passenger passenger) {

        Optional<Passenger> optionalPassenger = getPassenger(passenger.getPassengerId());

        if(optionalPassenger.isPresent()) return;

        airportRepository.addPassenger(passenger);
        return;
    }

    public Optional<Passenger> getPassenger(int passengerId) {

        return airportRepository.getPassenger(passengerId);
    }

    public Optional<Double> getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {

        Optional<List<Flight>> optionalFlights = getAllFlights();
        if(optionalFlights.isEmpty()) return Optional.empty();

        Double minDuration = 0.0;
        List<Flight> allFlights = optionalFlights.get();
        for(Flight curr : allFlights){

            if(curr.getFromCity().equals(fromCity) && curr.getToCity().equals(toCity)){
                minDuration = Math.min(minDuration,curr.getDuration());
            }
        }

        return Optional.of(minDuration);
    }

    public Optional<List<Flight>> getAllFlights() {

        return airportRepository.getAllFlights();
    }

    public Optional<String> getAirportNameFromFlightId(Integer flightId) {

        Optional<Flight> optionalFlight = getFlight(flightId);
        if(optionalFlight.isEmpty()) return Optional.empty();

        City fromCity = optionalFlight.get().getFromCity();

        Optional<List<Airport>> optionalAirports = getAllAirports();
        if(optionalAirports.isEmpty()) return Optional.empty();

        for(Airport curr : optionalAirports.get()){

            if(curr.getCity().equals(fromCity)){
                return Optional.of(curr.getAirportName());
            }
        }

        return Optional.empty();
    }

    public Optional<Integer> getNumberOfPeopleOn(Date date, String airportName) {

        Optional<List<Flight>> optionalFlights = getAllFlights();
        if(optionalFlights.isEmpty()) return Optional.empty();
        List<Flight> flights = optionalFlights.get();

        Optional<Airport> optionalAirport = getAirport(airportName);
        if(optionalAirport.isEmpty()) return Optional.empty();

        Airport curr = optionalAirport.get();
        City currCity = curr.getCity();
        List<Integer> targetFlights = new ArrayList<>();

        for(Flight currFlight : flights){
            if(currFlight.getFlightDate().equals(date)){
                if(currFlight.getToCity().equals(currCity) || currFlight.getFromCity().equals(currCity)){
                    targetFlights.add(currFlight.getFlightId());
                }
            }
        }

        Integer ct=0;

        for(Integer id : targetFlights){

            Optional<List<Integer>> optionalPassList = airportRepository.getFlightPassengers(id);
            if(optionalPassList.isPresent())
                ct += optionalPassList.get().size();
        }

        if(ct == 0) {
            return Optional.empty();
        }
        return Optional.of(ct);
    }

    public Optional<Integer> getCountOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        Optional<List<Integer>> optionalBookingHistory = getBookingHistory(passengerId);
        if(optionalBookingHistory.isEmpty()) return Optional.empty();

        return Optional.of(optionalBookingHistory.get().size());
    }

    public Optional<List<Integer>> getBookingHistory(Integer passengerId) {

        return airportRepository.getBookingHistory(passengerId);
    }

    public Integer calculateFlightFare(Integer flightId) {

        Optional<Integer> numPeopleAlreadyBooked = getNumberOfPeopleWhoBookedFlight(flightId);
        if(numPeopleAlreadyBooked.isEmpty()){
            return 3000;
        }

        return 3000 + (numPeopleAlreadyBooked.get()*50);
    }

    public Optional<Integer> getNumberOfPeopleWhoBookedFlight(Integer flightId) {

        return airportRepository.getNumberOfPeopleWhoBookedFlight(flightId);
    }
}

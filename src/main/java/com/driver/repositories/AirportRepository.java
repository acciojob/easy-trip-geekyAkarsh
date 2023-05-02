package com.driver.repositories;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {

    private Map<String,Airport> airportMap = new HashMap<>();
    private Map<Integer, Passenger> passengerMap = new HashMap<>();
    private Map<Integer, Flight> flightMap = new HashMap<>();
    private Map<Integer,List<Integer>> passengerToFlightMap = new HashMap<>(); // passId vs flightId
    private Map<Integer,List<Integer>> flightToPassengerMap = new HashMap<>();// flightId vs PassID

    public void addAirport(Airport airport) {

        airportMap.put(airport.getAirportName(),airport);
        return;
    }

    public Optional<Airport> getAirport(String airportName) {

        if(airportMap.containsKey(airportName)){
            return Optional.of(airportMap.get(airportName));
        }
        return Optional.empty();
    }

    public Optional<List<Airport>> getAllAirports() {

        if (airportMap.size() == 0) return Optional.empty();
        return Optional.of(new ArrayList<>(airportMap.values()));
    }

    public Optional<Flight> getFlight(int flightId) {

        if(flightMap.containsKey(flightId)){
            return Optional.of(flightMap.get(flightId));
        }
        return Optional.empty();
    }

    public void addFlight(Flight flight) {

        flightMap.put(flight.getFlightId(),flight);
        return;
    }

    public void addPassenger(Passenger passenger) {

        passengerMap.put(passenger.getPassengerId(),passenger);
        return;
    }

    public Optional<Passenger> getPassenger(int passengerId) {

        if(passengerMap.containsKey(passengerId)){
            return Optional.of(passengerMap.get(passengerId));
        }
        return Optional.empty();
    }

    public Optional<List<Flight>> getAllFlights() {

        if(flightMap.isEmpty()) return Optional.empty();
        return Optional.of(new ArrayList<>(flightMap.values()));
    }

    public Optional<List<Integer>> getFlightPassengers(Integer id) {

        if(flightToPassengerMap.containsKey(id))
            return Optional.of(flightToPassengerMap.get(id));

        return Optional.empty();
    }

    public Optional<List<Integer>> getBookingHistory(Integer passengerId) {

        if(passengerToFlightMap.containsKey(passengerId)){
            List<Integer> flightList = passengerToFlightMap.get(passengerId);
            return Optional.of(flightList);
        }
        return Optional.empty();
    }

    public Optional<Integer> getNumberOfPeopleWhoBookedFlight(Integer flightId) {

        if(flightToPassengerMap.containsKey(flightId)){
            return Optional.of(flightToPassengerMap.get(flightId).size());
        }
        return Optional.empty();
    }

    public Optional<List<Integer>> getAllPassengerOnFlight(Integer flightId) {

        if(flightToPassengerMap.containsKey(flightId)){
            return Optional.of(flightToPassengerMap.get(flightId));
        }
        return Optional.empty();
    }

    public void bookATicket(Integer flightId, Integer passengerId) {

        List<Integer> passengersOnFlight = flightToPassengerMap.get(flightId);
        passengersOnFlight.add(passengerId);
        flightToPassengerMap.put(flightId,passengersOnFlight);

        List<Integer> flightsForPassenger = passengerToFlightMap.get(passengerId);
        flightsForPassenger.add(flightId);
        passengerToFlightMap.put(passengerId,flightsForPassenger);
        return;
    }

    public void cancelATicket(Integer flightId, Integer passengerId) {

        List<Integer> flightHistory = passengerToFlightMap.get(passengerId);
        flightHistory.remove(flightId);
        if(flightHistory.size() == 0)
            passengerToFlightMap.remove(passengerId);
        else
            passengerToFlightMap.put(passengerId,flightHistory);

        List<Integer> passHistory = flightToPassengerMap.get(flightId);
        passHistory.remove(passengerId);
        if(passHistory.size() == 0)
            flightToPassengerMap.remove(flightId);
        else
            flightToPassengerMap.put(flightId,passHistory);

        return;
    }
}

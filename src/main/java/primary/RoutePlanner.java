package primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import permissions.BusManager;
import permissions.StationManager;

/*The purpose of this class is to collect a list of station and convert them to a
usable route, choosing a bus that can travel the route and verifying if the route is
possible while rerouting with refueling stations if necessary
*/
public class RoutePlanner {
    private List<RouteLeg> route;// list of route legs to create the full route
    private Bus chosenBus;// the bus that is chosen to travel the route

    // constructor that determines the input of a station list and converts it to
    // a collection of route legs
    public RoutePlanner(List<Station> stations) {
        // create temporary route list to push later
        List<RouteLeg> temp = new ArrayList<>();

        // for each station, connect it to the next station in list
        for (int i = 0; i < stations.size() - 1; i++) {
            Station start = stations.get(i);
            Station end = stations.get(i + 1);

            temp.add(new RouteLeg(start, end));
        }

        // set the planner's route as the temporary list
        route = temp;
    }

    // return the collection of route legs to other classes
    public List<RouteLeg> getRoute() {
        return route;
    }

    // find a possible refuel station within range to allow the bus to travel longer
    public boolean reroute() {
        // declare the max range of the bus in terms of mileage
        float miles = chosenBus.calcMaxRange();
        // create a temporary new route that will account for refueling
        List<RouteLeg> newRoute = new ArrayList<>();

        // if a leg is short enough to take, reduce the bus' fuel and add to new route
        for (RouteLeg r : route) {
            if (r.getDistance() < miles) {
                newRoute.add(r);
                miles -= r.getDistance();
            } else {
                // otherwise, find a nearby refuel station using getNewLeg()
                List<RouteLeg> refuelRoute = getNewLeg(r, miles, chosenBus);
                // if getNewLeg succeeeds, add it to the new route and refresh fuel
                if (refuelRoute != null) {
                    newRoute.addAll(refuelRoute);
                    miles = chosenBus.calcMaxRange() - refuelRoute.get(
                            1).getDistance();
                } else {
                    // if getNewLeg fails, return false, indicate that the route
                    // isn't possible
                    return false;
                }
            }
        }

        // if the new route is proven successful, set the route to the reroute
        route = newRoute;
        return true;
    }

    // A method to split a leg into two legs that detours to a refueling station
    private List<RouteLeg> getNewLeg(RouteLeg oldLeg, float remaining, Bus bus) {
        // hold the two new legs that are created
        List<RouteLeg> newLegs = new ArrayList<>();

        // get the list of stations, only count it if it's of the refuel subclass
        for (Station s : StationManager.getStations()) {
            if (s instanceof Station.RefuelStation refuelStation) {
                // if the refuel station supports the bus' fuel type, make 2 new legs
                if (refuelStation.getFuelType().contains(bus.getFuel())) {
                    RouteLeg legA = new RouteLeg(oldLeg.getStart(), s);
                    RouteLeg legB = new RouteLeg(s, oldLeg.getEnd());

                    // if distance to the station is within the bus' fuel capacity, add them
                    if (legA.getDistance() < remaining
                            && legB.getDistance() < bus.calcMaxRange()) {
                        newLegs.add(legA);
                        newLegs.add(legB);
                        return newLegs;
                    }
                }
            }
        }
        // if no suitable refuel stations are found, call method to make a new one
        return newRefuel(oldLeg, remaining, bus);
    }

    // Make a new refuel station if it is needed
    private List<RouteLeg> newRefuel(RouteLeg oldLeg, float remaining, Bus bus) {
        // hold the two new legs that are created
        List<RouteLeg> newLegs = new ArrayList<>();

        // get the info of the stations
        Station start = oldLeg.getStart();
        Station end = oldLeg.getEnd();

        // get the midpoint between the two stations
        float distLat = (end.getLatitude() - start.getLatitude()) / 2;

        float distLong = (end.getLongitude() - start.getLongitude()) / 2;

        // set the midpoint as coordinates
        float newLat = distLat + start.getLatitude();
        float newLong = distLong + start.getLongitude();

        // generate a name for the station
        String autoname = "Auto Station" +
                ThreadLocalRandom.current().nextInt(1, 10000);

        // set the stations supported fuel to the bus
        ArrayList<String> tempFuels = new ArrayList<>();
        tempFuels.add(bus.getFuel());

        // add the temporary station to the database
        Station.RefuelStation temp = new Station.RefuelStation(
                autoname, newLat, newLong, tempFuels);

        StationManager.addStation(temp);

        // create a new route that stops at the new refuel station
        RouteLeg newA = new RouteLeg(start, temp);
        RouteLeg newB = new RouteLeg(temp, end);
        newLegs.add(newA);
        newLegs.add(newB);

        return newLegs;
    }

    // A method to find a suitable bus to travel the route
    public void findBus() {
        float maxDist = 0;

        // Find longest leg
        for (RouteLeg r : route) {
            if (r.getDistance() > maxDist) {
                maxDist = r.getDistance();
            }
        }

        Bus best = null;

        for (Bus b : BusManager.getBuses()) {
            // Must be able to complete the longest leg
            if (b.calcMaxRange() >= maxDist) {

                if (best == null) {
                    best = b;
                } else {
                    // Choose the faster bus
                    float etaBest = getDistance() / best.getCruiseSpeed();
                    float etaNew = getDistance() / b.getCruiseSpeed();

                    if (etaNew < etaBest) {
                        best = b;
                    }
                }
            }
        }

        //if no bus can handle the route, choose the first in list
        if (best == null && !BusManager.getBuses().isEmpty()) {
            best = BusManager.getBuses().get(0);
        }

        chosenBus = best;
    }

    // add the distance of the route by adding the distance of each leg
    public float getDistance() {
        float total = 0;
        for (RouteLeg d : route) {
            total += d.getDistance();
        }
        return total;
    }

    // get the estimated time by dividing the total distance by the bus' speed
    public float getETA() {
        return getDistance() / chosenBus.getCruiseSpeed();
    }

    // get the stations in a route (in case the route is changed by validation)
    public List<Station> getPoints() {
        List<Station> points = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            if (i == 0) {
                points.add(route.get(i).getStart());
            }
            points.add(route.get(i).getEnd());
        }
        return points;
    }

    // if a bus hasn't been chosen yet, find one, then return the chosen bus
    public Bus getChosenBus() {
        if (chosenBus == null) {
            findBus();
        }
        return chosenBus;
    }

    // test if the route is possible
    public boolean testRoute() {
        // call findbus again to find a best suit
        findBus();

        // if any leg is longer than the bus' max range, or the bus is null, return
        // false
        for (RouteLeg r : route) {
            if (chosenBus == null || r.getDistance() > chosenBus.calcMaxRange()) {
                return false;
            }
        }
        return true;
    }

    // validate the route by checking if the bus and route is valid, otherwise
    // reroute
    public boolean validateRoute() {
        if (chosenBus == null) {
            findBus();
        }

        if (testRoute()) {
            return true;
        }

        return reroute();
    }
}

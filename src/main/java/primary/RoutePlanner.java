package primary;

import java.util.ArrayList;
import java.util.List;

import permissions.BusManager;
import permissions.StationManager;

/*The purpose of this class is to collect a list of station and convert them to a
usable route, choosing a bus that can travel the route and verifying if the route is
possible while rerouting with refueling stations if necessary
*/
public class RoutePlanner {
    private List<RouteLeg> route;//list of route legs to create the full route
    private Bus chosenBus;//the bus that is chosen to travel the route

    //constructor that determines the input of a station list and converts it to
    //a collection of route legs
    public RoutePlanner(List<Station> stations) {
        //create temporary route list to push later
        List<RouteLeg> temp = new ArrayList<>();

        //for each station, connect it to the next station in list
        for (int i = 0; i < stations.size() - 1; i++) {
            Station start = stations.get(i);
            Station end = stations.get(i + 1);

            temp.add(new RouteLeg(start, end));
        }

        //set the planner's route as the temporary list
        route = temp;
    }

    //return the collection of route legs to other classes
    public List<RouteLeg> getRoute(){
        return route;
    }

    //find a possible refuel station within range to allow the bus to travel longer
    public boolean reroute() {
        //declare the max range of the bus in terms of mileage
        float miles = chosenBus.calcMaxRange();
        //create a temporary new route that will account for refueling
        List<RouteLeg> newRoute = new ArrayList<>();

        //if a leg is short enough to take, reduce the bus' fuel and add to new route
        for (RouteLeg r : route) {
            if (r.getDistance() < miles) {
                newRoute.add(r);
                miles -= r.getDistance();
            } else {
                //otherwise, find a nearby refuel station using getNewLeg()
                List<RouteLeg> refuelRoute = getNewLeg(r, miles, chosenBus);
                //if getNewLeg succeeeds, add it to the new route and refresh fuel
                if (refuelRoute != null) {
                    newRoute.addAll(refuelRoute);
                    miles = chosenBus.calcMaxRange() - refuelRoute.get(
                            1).getDistance();
                } else {
                    //if getNewLeg fails, return false, indicate that the route
                    //isn't possible
                    return false;
                }
            }
        }

        //if the new route is proven successful, set the route to the reroute
        route = newRoute;
        return true;
    }

    //A method to split a leg into two legs that detours to a refueling station
    private List<RouteLeg> getNewLeg(RouteLeg oldLeg, float remaining, Bus bus) {
        //hold the two new legs that are created
        List<RouteLeg> newLegs = new ArrayList<>();

        //get the list of stations, only count it if it's of the refuel subclass
        for (Station s : StationManager.getStations()) {
            if (s instanceof Station.RefuelStation refuelStation) {
                //if the refuel station supports the bus' fuel type, make 2 new legs
                if (refuelStation.getFuelType().contains(bus.getFuel())) {
                    RouteLeg legA = new RouteLeg(oldLeg.getStart(), s);
                    RouteLeg legB = new RouteLeg(s, oldLeg.getEnd());

                    //if distance to the station is within the bus' fuel capacity, add them
                    if (legA.getDistance() < remaining
                            && legB.getDistance() < bus.calcMaxRange()) {
                        newLegs.add(legA);
                        newLegs.add(legB);
                        return newLegs;
                    }
                }
            }
        }
        //if no suitable refuel stations are found, return a null value
        return null;
    }

    //A method to find a suitable bus to travel the route
    public void findBus() {
        //determine the largest distance of a leg in the route
        float maxDist = 0;
        for (RouteLeg r : route) {
            if (r.getDistance() > maxDist) {
                maxDist = r.getDistance();
            }
        }

        //declare the best bus to take the route
        Bus best = null;

        //for each bus, if it can travel the largest leg distance and can travel
        //longer than the previous bus, set it to the best
        for (Bus b : BusManager.getBuses()) {
            if (b.calcMaxRange() > maxDist) {
                if (best == null || b.calcMaxRange() < best.calcMaxRange()) {
                    best = b;
                }
            }
        }

        //set the chosen bus as the best bus
        chosenBus = best;
    }

    //add the distance of the route by adding the distance of each leg
    public float getDistance() {
        float total = 0;
        for (RouteLeg d : route) {
            total += d.getDistance();
        }
        return total;
    }

    //get the estimated time by dividing the total distance by the bus' speed
    public float getETA() {
        return getDistance() / chosenBus.getCruiseSpeed();
    }

    //if a bus hasn't been chosen yet, find one, then return the chosen bus
    public Bus getChosenBus() {
        if (chosenBus == null) {
            findBus();
        }
        return chosenBus;
    }

    //test if the route is possible
    public boolean testRoute() {
        //call findbus again to find a best suit
        findBus();

        //if any leg is longer than the bus' max range, or the bus is null, return false
        for (RouteLeg r : route) {
            if (r.getDistance() > chosenBus.calcMaxRange() || chosenBus == null) {
                return false;
            }
        }
        return true;
    }

    //validate the route by checking if the bus and route is valid, otherwise reroute
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

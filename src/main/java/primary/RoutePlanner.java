package primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        //find bus if it wasn't found already
        if (chosenBus == null) {
            findBus();
        }
        //if a suitable bus wasn't found, abort
        if (chosenBus == null) {
            return false;
        }

        //get the bus' range and remaining fuel
        float maxRange = chosenBus.calcMaxRange();
        float fuelLeft = maxRange;

        List<RouteLeg> newRoute = new ArrayList<>();
        List<RouteLeg> queue = new ArrayList<>(route);

        int safetyCounter = 0; // prevent infinite looping

        while (!queue.isEmpty()) {
            if (safetyCounter++ > 1000) {
                return false;
            }

            RouteLeg current = queue.remove(0);
            float dist = current.getDistance();

            // the leg is already possible
            if (dist <= fuelLeft) {
                newRoute.add(current);
                fuelLeft -= dist;
                continue;
            }

            //try to refuel before attempting the leg
            List<RouteLeg> split = findRefuel(current, fuelLeft, chosenBus);

            // no possible reroute
            if (split == null) {
                return false;
            }

            // put both legs back into queue
            queue.add(0, split.get(1)); // second leg
            queue.add(0, split.get(0)); // first leg

            // Simulate refuel
            fuelLeft = maxRange;
        }

        //set new route as default and test
        route = newRoute;
        return testRoute();
    }

    // A method that allows the route to detour to a refueling station
    private List<RouteLeg> findRefuel(RouteLeg oldLeg, float remaining, Bus bus) {
        //get the old start and end
        Station start = oldLeg.getStart();
        Station end = oldLeg.getEnd();

        //get the best suited station as a variable
        Station bestStation = null;
        float bestProgress = -1;

        for (Station s : StationManager.getStations()) {
            if (s instanceof Station.RefuelStation refuelStation) {

                //only consider stations that have the bus' fuel
                if (!refuelStation.getFuelType().contains(bus.getFuel())) {
                    continue;
                }

                //make 2 new legs that go through the station
                RouteLeg legA = new RouteLeg(start, s);
                RouteLeg legB = new RouteLeg(s, end);

                //get the new distance
                float distA = legA.getDistance();
                float distB = legB.getDistance();

                if (distA <= remaining && distB <= bus.calcMaxRange()) {

                    // choose station that's the best fit
                    if (distA > bestProgress) {
                        bestProgress = distA;
                        bestStation = s;
                    }
                }
            }
        }

        //if a suitable station was found, return new legs
        if (bestStation != null) {
            List<RouteLeg> result = new ArrayList<>();
            result.add(new RouteLeg(start, bestStation));
            result.add(new RouteLeg(bestStation, end));
            return result;
        }

        // otherwise, create a new station
        return createRefuel(oldLeg, remaining, bus);
    }

    // Make a new refuel station if it is needed
    private List<RouteLeg> createRefuel(RouteLeg oldLeg, float remaining, Bus bus) {
        //get the old start and end
        Station start = oldLeg.getStart();
        Station end = oldLeg.getEnd();

        //get the distance of the old leg
        float totalDist = oldLeg.getDistance();

        // place station at the max distance
        float ratio = remaining / totalDist;

        //abort if the ratio isn't between 0 and 1
        if (ratio <= 0 || ratio >= 1) {
            return null;
        }

        //set location to reachable distance
        float newLat = start.getLatitude() +
                (end.getLatitude() - start.getLatitude()) * ratio;

        float newLong = start.getLongitude() +
                (end.getLongitude() - start.getLongitude()) * ratio;

        //name station automatically
        String name = "Auto Station: "
            + ThreadLocalRandom.current().nextInt(1, 10000);

        //set supported fuels to bus' fuel
        ArrayList<String> fuels = new ArrayList<>();
        fuels.add(bus.getFuel());

        //create the new station and add to database
        Station.RefuelStation station = new Station.RefuelStation(
            name, newLat, newLong, fuels);
        StationManager.addStation(station);

        //return the new reroute
        List<RouteLeg> result = new ArrayList<>();
        result.add(new RouteLeg(start, station));
        result.add(new RouteLeg(station, end));

        return result;
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

        // if no bus can handle the route, choose the first in list
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

        float milesRemaining = chosenBus.calcMaxRange();


        //if any leg is longer than the bus can travel with remaining fuel,
        //return false
        for (RouteLeg r : route) {
            if (chosenBus == null || r.getDistance() > milesRemaining) {
                return false;
            }
            milesRemaining -= r.getDistance();
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

        System.out.println(chosenBus.calcMaxRange());
        return reroute();
    }
}

package primary;

import java.util.ArrayList;
import java.util.List;

import permissions.BusManager;
import permissions.StationManager;

public class RoutePlanner {

    private List<RouteLeg> route;
    private Bus chosenBus;

    public RoutePlanner(List<Station> stations) {
        List<RouteLeg> temp = new ArrayList<>();

        for (int i = 0; i < stations.size() - 1; i++) {
            Station start = stations.get(i);
            Station end = stations.get(i + 1);

            System.out.println("Start: " + start);
            System.out.println("Start: " + end);

            if (start == null || end == null) {
                throw new IllegalArgumentException("Route contains null station at index " + i);
            }

            temp.add(new RouteLeg(start, end));
        }

        route = temp;
    }

    public boolean reroute() {
        float miles = chosenBus.calcMaxRange();
        List<RouteLeg> newRoute = new ArrayList<>();

        for (RouteLeg r : route) {
            if (r.getDistance() < miles) {
                newRoute.add(r);
                miles -= r.getDistance();
            } else {
                List<RouteLeg> refuelRoute = getNewLeg(r, miles, chosenBus);
                if (refuelRoute != null) {
                    newRoute.addAll(refuelRoute);
                    miles = chosenBus.calcMaxRange() - refuelRoute.get(
                            1).getDistance();
                } else {
                    return false;
                }
            }
        }

        route = newRoute;
        return true;
    }

    private List<RouteLeg> getNewLeg(RouteLeg oldLeg, float remaining, Bus bus) {
        List<RouteLeg> newLegs = new ArrayList<>();
        for (Station s : StationManager.getStations()) {
            if (s instanceof Station.RefuelStation refuelStation) {
                if (refuelStation.getFuelType().contains(bus.getFuel())) {
                    RouteLeg legA = new RouteLeg(oldLeg.getStart(), s);
                    RouteLeg legB = new RouteLeg(s, oldLeg.getEnd());
                    if (legA.getDistance() < remaining
                            && legB.getDistance() < bus.calcMaxRange()) {
                        newLegs.add(legA);
                        newLegs.add(legB);
                        return newLegs;
                    }
                }
            }
        }
        return null;
    }

    public void findBus() {
        float maxDist = 0;
        for (RouteLeg r : route) {
            if (r.getDistance() > maxDist) {
                maxDist = r.getDistance();
            }
        }

        Bus best = null;

        for (Bus b : BusManager.getBuses()) {
            if (b.calcMaxRange() > maxDist) {
                if (best == null || b.calcMaxRange() < best.calcMaxRange()) {
                    best = b;
                }
            }
        }

        chosenBus = best;
    }

    public float getDistance() {
        float total = 0;
        for (RouteLeg d : route) {
            total = total + d.getDistance();
        }
        return total;
    }

    public float getETA() {
        return getDistance() / chosenBus.getCruiseSpeed();
    }

    public Bus getChosenBus(){
        if(chosenBus == null){
            findBus();
        }
        return chosenBus;
    }
    public boolean testRoute() {
        findBus();
        
        for (RouteLeg r : route) {
            if (r.getDistance() > chosenBus.calcMaxRange()) {
                return false;
            }
        }
        return true;
    }

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

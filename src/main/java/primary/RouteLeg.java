package primary;

/*
This class is for individual connections between two stations, determining their
distance and direction based on the coordinates of the starting and ending stations
*/
public class RouteLeg {

    private Station start;// The station that the leg starts from
    private Station end;// The station that the leg ends at

    // declare a new leg, with start and end station
    public RouteLeg(Station start, Station end) {
        // Throw an exception if either station is null
        if (start == null || end == null) {
            throw new IllegalArgumentException("RouteLeg stations cannot be null");
        }
        this.start = start;
        this.end = end;
    }

    // ----------------------set/get start and end stations----------------------
    public void setStart(Station start) {
        this.start = start;
    }

    public Station getStart() {
        return start;
    }

    public void setEnd(Station end) {
        this.end = end;
    }

    public Station getEnd() {
        return end;
    }

    // Determine the direction of the station based on the getDegrees() method
    public String getHeading() {
        float degrees = getDegrees();

        // if degrees makes a full circle, reduce by 360 degrees
        if (degrees > 360) {
            degrees = degrees - 360;
        }

        // determine heading in cardinal and ordinal directions
        if (degrees == 0) {
            return degrees + "° North";
        } else if (degrees == 90) {
            return degrees + "° East";
        } else if (degrees == 180) {
            return degrees + "° South";
        } else if (degrees == 270) {
            return degrees + "° West";
        } else if (0 < degrees && degrees < 90) {
            return degrees + "° North-East";
        } else if (90 < degrees && degrees < 180) {
            return degrees + "° South-East";
        } else if (180 < degrees && degrees < 270) {
            return degrees + "° South-West";
        } else {
            return degrees + "° North-West";
        }
    }

    //get the distance between stations
    public float getDistance() {
        float lat1 = start.getLatitude();
        float lat2 = end.getLatitude();
        float lon1 = start.getLongitude();
        float lon2 = end.getLongitude();

        float dLat = lat2 - lat1;
        float dLon = lon2 - lon1;

        // get average longitude
        float avgLat = (lat1 + lat2) / 2;

        //conver longitude and latitude to miles
        float milesPerLat = 69f;
        float milesPerLon = (float) (69f * Math.cos(Math.toRadians(avgLat)));

        //multiply coordinate by conversion
        float a = dLat * milesPerLat;
        float b = dLon * milesPerLon;

        //get hypotenuse between points
        return (float) Math.sqrt(a * a + b * b);
    }

    // find degrees between stations
    public float getDegrees() {
        // dy is difference between end and start latitudes
        double dy = this.end.getLatitude() - this.start.getLatitude();
        // dx is difference between end and start longitudes
        double dx = this.end.getLongitude() - this.start.getLongitude();

        // get angle of tangent between dy and dx
        double angle = Math.toDegrees(Math.atan2(dx, dy));
        // find remainder of angle divided by 360 degrees
        return (float) ((angle + 360) % 360);
    }
}

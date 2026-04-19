package primary;
/*
This class is for individual connections between two stations, determining their
distance and direction based on the coordinates of the starting and ending stations
*/
public class RouteLeg {

    private Station start;//The station that the leg starts from
    private Station end;//The station that the leg ends at

    //declare a new leg, with start and end station
    public RouteLeg(Station start, Station end) {
        //Throw an exception if either station is null
        if (start == null || end == null) {
            throw new IllegalArgumentException("RouteLeg stations cannot be null");
        }
        this.start = start;
        this.end = end;
    }

    //----------------------set/get start and end stations----------------------
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

    //Determine the direction of the station based on the getDegrees() method
    public String getHeading() {
        float degrees = getDegrees();

        //if degrees makes a full circle, reduce by 360 degrees
        if (degrees > 360) {
            degrees = degrees - 360;
        }

        //determine heading in cardinal and ordinal directions
        if (degrees == 0) {
            return "North";
        } else if (degrees == 90) {
            return "East";
        } else if (degrees == 180) {
            return "South";
        } else if (degrees == 270) {
            return "West";
        } else if (0 < degrees && degrees < 90) {
            return "North-East";
        } else if (90 < degrees && degrees < 180) {
            return "South-East";
        } else if (180 < degrees && degrees < 270) {
            return "South-West";
        } else {
            return "North-West";
        }
    }

    /*get the distance by converting latitude and longitude to miles, then
    find the hypotenuse using the pythagorean theorem
    */
    public float getDistance() {
        float a = 69 * (this.end.getLatitude() - this.start.getLatitude());
        float b = (float) (69
                * Math.cos(this.end.getLongitude() - this.start.getLongitude()));

        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    //find degrees between stations
    public float getDegrees() {
        //dy is difference between end and start latitudes
        double dy = this.end.getLatitude() - this.start.getLatitude();
        //dx is difference between end and start longitudes
        double dx = this.end.getLongitude() - this.start.getLongitude();

        //get angle of tangent between dy and dx
        double angle = Math.toDegrees(Math.atan2(dx, dy));
        //find remainder of angle divided by 360 degrees
        return (float) ((angle + 360) % 360);
    }
}

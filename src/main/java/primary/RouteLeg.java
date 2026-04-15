package primary;

public class RouteLeg {

    private Station start;
    private Station end;

    public RouteLeg(Station start, Station end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("RouteLeg stations cannot be null");
        }

        this.start = start;
        this.end = end;
    }

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

    public String getHeading() {
        float degrees = getDegrees();

        if (degrees > 360) {
            degrees = degrees - 360;
        }

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

    public float getDistance() {
        float a = 69 * (this.end.getLatitude() - this.start.getLatitude());
        float b = (float) (69
                * Math.cos(this.end.getLongitude() - this.start.getLongitude()));

        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

    }

    public float getDegrees() {
        float alpha = (float) ((this.end.getLatitude() - this.start.getLatitude())
                / Math.sqrt((Math.pow(
                        (this.end.getLongitude() - this.start.getLongitude()), 2)))
                + Math.sqrt(Math.pow(
                        this.end.getLatitude() - this.start.getLatitude(), 2)));
        return (float) (alpha * 180 / Math.PI);
    }
}

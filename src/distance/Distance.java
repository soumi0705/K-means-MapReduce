package distance;

import points.PointWritable;

public interface Distance {
	double calculate(PointWritable p1, PointWritable p2);
}

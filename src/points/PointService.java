package points;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PointService {
	/* get list point from text file
	 * each point is written on one line, recording the coordinates of the points one by one
	 * */
	public static List<PointWritable> getListPoints(String path) throws IOException {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<PointWritable> listPoints = new ArrayList();
		
		@SuppressWarnings("resource")
		BufferedReader buffer = new BufferedReader(new FileReader(path));

		// get list of points
		String line = buffer.readLine();
		while (line != null) {
			String tmpString[] = line.split(",");
			//System.out.println(tmpString[2]+" "+tmpString[7]);
			Integer tmp0 = Integer.parseInt(tmpString[2]);
			Double tmp1 = Double.parseDouble(tmpString[7]);
			Integer tmp2 = Double.valueOf(tmp1).intValue();
			PointWritable tmpPoint = new PointWritable(tmp0, tmp2);

			listPoints.add(tmpPoint);
			line = buffer.readLine();
		}
		
		return listPoints;
	}
	
	public static List<PointWritable> getListCenPoints(String path) throws IOException {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<PointWritable> listPoints = new ArrayList();
		
		@SuppressWarnings("resource")
		BufferedReader buffer = new BufferedReader(new FileReader(path));

		// get list of points
		String line = buffer.readLine();
		while (line != null) {
			String tmpString[] = line.split(" ");
			System.out.println(tmpString[0]+" "+tmpString[1]);
			Integer tmp0 = Integer.parseInt(tmpString[0]);
			Double tmp1 = Double.parseDouble(tmpString[1]);
			Integer tmp2 = Double.valueOf(tmp1).intValue();
			PointWritable tmpPoint = new PointWritable(tmp0, tmp2);

			listPoints.add(tmpPoint);
			line = buffer.readLine();
		}
		
		return listPoints;
	}
}

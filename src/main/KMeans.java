package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import points.PointWritable;
import points.PointService;

public class KMeans {
	private int k;
	List<PointWritable> listPoints;
	
	public KMeans(int k) throws IOException {
		this.k = k;
		listPoints = PointService.getListPoints("/home/soumi07/Desktop/LSDP/K-means MapReduce/data/input.txt");
	}
	
	public int getNumberOfCircle() {
		return listPoints.size() / 100;
	}
	
	/*
	 * Write example k center before run MapReduce
	 */
	public void writeExempleCenter(Configuration conf) throws IOException {
		String pathCenter = conf.get("pathCenter");
		
		// random first k point center, write them to sequence file pathCenter
		Random rand = new Random();
		int numberOfPoint = listPoints.size();
		try (PrintWriter printWriter = new PrintWriter(pathCenter)) {
			for(int i=0; i<k; i++) {
				int rand_num = rand.nextInt(numberOfPoint-1);
				PointWritable randomPoint = listPoints.get(rand_num);
				System.out.print(randomPoint.getX().get()+" "+randomPoint.getY().get()+"\n");
				printWriter.write(randomPoint.getX().get()+" "+randomPoint.getY().get()+ "\n");
			}
		}
	}
	
	public static void main(String[] args)
			throws NumberFormatException, IOException, ClassNotFoundException, InterruptedException {
		String out = args[0];
		String in = "/data/input";
		String cen = "/home/soumi07/Desktop/LSDP/K-means MapReduce/data/cen.seq";

		KMeans kmeans = new KMeans(3); // 3 cluster
		Configuration conf = new Configuration();

		conf.set("pathIn", in);
		conf.set("pathCenter", cen);
		conf.set("pathOut", out);

		kmeans.writeExempleCenter(conf);
		
		int time = 2;//kmeans.getNumberOfCircle();
		while(time > 0) {
			Job job = Job.getInstance(conf, "K-Means MapReduce");
			job.setJarByClass(KMeans.class);
			job.setMapperClass(KmeansMap.class);
			//job.setCombinerClass(KmeansReduce.class);
			job.setReducerClass(KmeansReduce.class);
			//job.setNumReduceTasks(0);
			
			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			job.setOutputKeyClass(PointWritable.class);
			job.setOutputValueClass(PointWritable.class);

			FileInputFormat.addInputPath(job, new Path(in));
			FileSystem fs = FileSystem.get(conf); // delete file output when it exists
			if (fs.exists(new Path(out))) {
				fs.delete(new Path(out), true);
			}

			FileOutputFormat.setOutputPath(job, new Path(out));

			if(job.waitForCompletion(true)) {
				System.out.println("SUBMIT WAS SUCCESSFUL!");
			} else {
				System.out.println("SUBMIT WAS FAIL!");
			}
			
			time--;	
		}
		
	}
}


import java.io.* ;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
public class Reduce
{
 public static class Join_LookupMapper
extends Mapper<Object, Text, Text, Text>
{
 public void map(Object key, Text value, Context context)
throws IOException, InterruptedException
 {
 String record = value.toString() ;
 String[] parts = record.split(",") ;
 context.write(new Text(parts[0]), new
Text("Join_Lookup,"+parts[1])) ;
 }
 }
 public static class Join_SampleinputMapper
extends Mapper<Object, Text, Text, Text>
{
 public void map(Object key, Text value, Context context)
throws IOException, InterruptedException
 {
 String record = value.toString() ;
 String[] parts = record.split(",") ;
 context.write(new Text(parts[0]), new
Text("Join_Sampleinput,"+parts[1])) ;
 }
 }
 public static class ReduceReducer
 extends Reducer<Text, Text, Text, Text>
 {
 public void reduce(Text key, Iterable<Text> values,
 Context context)
 throws IOException, InterruptedException
 {
 String name = "" ;
double total = 0.0 ;
 int count = 0 ;
 for(Text t: values)
 {
 String parts[] = t.toString().split(",") ;
 if (parts[0].equals("Join_Lookup"))
 {
 count++ ;
 total+= Float.parseFloat(parts[1]) ;
 }
 else if (parts[0].equals("Join_Sampleinput"))
 {
 name = parts[1] ;
 }
 }
 String str = String.format("%d %f", count, total) ;
 context.write(new Text(name), new Text(str)) ;
 }
 }
 public static void main(String[] args) throws Exception
{
 Configuration conf = new Configuration();
 Job job = new Job(conf, "Reduce-side join");
 job.setJarByClass(Reduce.class);
 job.setReducerClass(ReduceReducer.class);
 job.setOutputKeyClass(Text.class);
 job.setOutputValueClass(Text.class);
MultipleInputs.addInputPath(job, new Path(args[0]),
TextInputFormat.class, Join_LookupMapper.class) ;
MultipleInputs.addInputPath(job, new Path(args[1]),
TextInputFormat.class, Join_SampleinputMapper.class) ;
 Path outputPath = new Path(args[2]);
 FileOutputFormat.setOutputPath(job, outputPath);
outputPath.getFileSystem(conf).delete(outputPath);
 System.exit(job.waitForCompletion(true) ? 0 : 1);
 }
}
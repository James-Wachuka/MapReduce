#### JOINS
Few problems use a single set of data. In many cases, there are easy ways to obviate the need
to try and process numerous discrete yet related data sets within the MapReduce framework.
The analogy here is, of course, to the concept of join in a relational database. It is very
natural to segment data into numerous tables and then use SQL statements that join tables
together to retrieve data from multiple sources. The canonical example is where a main
table has only ID numbers for particular facts, and joins against other tables are used to
extract data about the information referred to by the unique ID.
##### Map-side joins
The great advantage of a map-side join is that by performing all joining and more critically data volume reduction within
the mapper, the amount of data transferred to the reduce stage is greatly minimized. The
drawback of map-side joins is that you either need to find a way of ensuring one of the
data sources is very small or you need to define the job input to follow very specific criteria.
Often, the only way to do that is to preprocess the data with another MapReduce job whose
sole purpose is to make the data ready for a map-side join.

##### reduce-side join 
Has the multiple data streams processed through the map stage without performing any join logic and does the joining in the reduce stage. The
potential drawback of this approach is that all the data from each source is pulled through
the shuffle stage and passed into the reducers, where much of it may then be discarded by
the join operation. For large data sets, this can become a very significant overhead.
The main advantage of the reduce-side join is its simplicity; you are largely responsible
for how the jobs are structured and it is often quite straightforward to define a reduce-side

An example has been provided for joining tables sales and accounts  based on unique ID.

A common situation in many companies is that sales records are kept separate from the
client data. There is, of course, a relationship between the two; usually a sales record
contains the unique ID of the user account through which the sale was performed.
In the Hadoop world, these would be represented by two types of data files: one containing
records of the user IDs and information for sales, and the other would contain the full data
for each user account.
Frequent tasks require reporting that uses data from both these sources; say, for example,
we wanted to see the total number of sales and total value for each user but do not want
to associate it with an anonymous ID number, but rather with a name. This may be valuable
when customer service representatives wish to call the most frequent customers data from
the sales records but want to be able to refer to the person by name and not just a number.
Time for action  reduce-side join using MultipleInputs

create  a working directory for this example and transfer the files to the folder :
`hadoop jar files` `sales.txt and accounts.txt` `reduce.java`
start hadoop  and switch to the working directory. Compile the java file and create its jar
`cd\mapreduce_join`
`javac Reduce.java -cp hadoop-common-2.*.*.jar;hadoop-mapreduce-client-core-2.*.*.jar`
` jar -cvf Reduce.jar Reduce*class`
create an input directory in hadoop for this job and put the sales.txt and accounts .txt
`hadoop -mkdir /reduce_join`
`hadoop fs -put sales.txt accounts.txt /reduce_join`
run the job.This job will accept two input files 
`hadoop jar Reduce.jar  Reduce /reduce_join/sales.txt;/reduce_join/accounts.txt join_output`
view the output
`hadoop fs -cat /user/opt/reduce_join/part-r-00000`

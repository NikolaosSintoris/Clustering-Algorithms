# Clustering-Algorithms
Implementation of K-means and LVQ clustering algorithms


## Dataset
We create random points (x1, x2) in the plane as follows:\
a) 100 points in the circle with center (0, 0) and radius 0.3\
b) 100 points on the square [-1.1, -0.5] x [0.5, 1.1]\
c) 100 points on the square [-1.1, -0.5] x [-1.1, -0.5]\
d) 100 points on the square [0.5, 1.1] x [-1.1, -0.5]\
e) 100 points on the square [0.5, 1.1] x [0.5, 1.1]\
f) 100 points on the square [-1, 1] x [-1, 1].



## K-means
I programmed a clustering program with M clusters based on the K-means algorithm. The program will load the file with the examples (which is our data set), execute the K-means algorithm with M cliusters and at the end it will save the coordinates of all the centers. Also at the end the clustering error should be calculated: for each example x we calculate a distance || x-μ ||^2 from the center μ of the cluster to which it belongs and we add the distances for all the examples x. The initial position of each center is made by randomly selecting one of the examples.


## LVQ
I programmed a clustering program with M clusters based on the LVQ algorithm for clustering. The program will load the file with the examples (which is our data set), execute the LVQ algorithm with M clusters and at the end it will save the coordinates of all the centers. The clustering error should also be calculated at the end. The initial position of each center should be done by randomly selecting one of the examples. The learning rate n decreases at the end of each season (eg n (t + 1) = 0.95 n (t)) starting from an appropriate initial value (eg n = 0.1).

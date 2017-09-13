# Social Network Analysis 
My capstone project for the [Object Oriented Java Programming: Data Structures and Beyond Specialization](https://www.coursera.org/specializations/java-object-oriented), offered by UC - San Diego on Coursera.

The project investigates the relationships we can uncover between research papers hosted on Google Scholar, using only the citations of a paper by another paper.

### Data
I am using the Google Scholar citation relations
[data set](http://www3.cs.stonybrook.edu/~leman/data/gscholar.db).


This dataset contains research papers (titles only) hosted on [Google Scholar](https://scholar.google.com/), and the citations in this paper to other papers in the dataset. I am unsure of the origins of the dataset, and therefore do not know how this dataset might have already been filtered from the entire Google Scholar project.

The dataset is stored in a SQLite 3 database, organized neatly as a graph:
- Node: Research paper
- (Directed) Edge: Citation of some research paper by a research paper
- Number of nodes (papers) = 82,937
- Number of edges (citations) = 148,116

### Questions
**Easier:** Given a paper, can we find similar papers by identifying those with common citations?

**Harder:** Given a paper, can we find similar papers by segregating the entire dataset into communities? How will this compare to our method from the easier question?

### Algorithms, Data Structures, and Answer to Question
**Main Data Structure:** The data set has been modeled as a graph through an adjacency list implementation. Each node in the graph is a research paper, and a directed edge between nodes represents one paper’s citation of the other paper. Each node is stored as the key in a HashMap, with values representing the node’s outgoing edges, stored as a HashSet for quick O(1) lookup.

**Easier Question Algorithm:** Identify a given paper’s cited papers, and find other papers which cited the same papers. Those papers with the most similar citations could be considered similar to the given paper.

**Answer to Easier Question:** Yes, we can find similar papers by matching papers with common citations. When I tested the algorithm for the node entitled “An_overview_of_3D_software_visualization,” the top 3 papers returned were entitled:  
   1. “Evaluating_X3D_for_use_in_software_visualization”
   2. “Software_visualization”
   3. “Software_visualization”

**Harder Question Algorithm:** 
Implemented the community-finding algorithm Mia described in the course videos, with a few helper methods and some modified data structures to save time at the expense of memory.

**Answer to Harder Question:**
The algorithm could not efficiently identify communities on the entire graph due to the graph’s size. I attempted to reproduce my results using a smaller subset of the graph, centered around the node from the easier question. The smallest subgraph I could produce with the same ‘similar node’ results was a 30000+ node graph, and this was still too large for the community-finding algorithm.

### Correctness verification (i.e. testing):

**Easier Question:**
I first created a very small test graph with my own set of nodes and edges. This ensured that I could fully test the algorithm by hand, and not have to worry about potential performance issues through running on the much larger actual data set.

I first found the out-neighbors for each node. After that I could pick which node I wanted to start with. I chose the first node, because in my contrived data set, this node had the most ‘similar’ nodes, as defined by most matching out-neighbors.

Then I ran my program for this first node. It worked! Mostly. There was an issue with the order of the results which the program was returning. I corrected that and verified it, still using my small test set.

I then loaded the real data set, and, as a sort of stress test, ran it for the node with the most out- neighbors. Performance ended up not being an issue. But the best part was that the resulting ‘similar’ nodes that I found were in fact, very similar. My data set includes the titles of each research paper. When I ran it for the node entitled “An_overview_of_3D_software_visualization,” and the top 3 resulting papers were titled, “Evaluating_X3D_for_use_in_software_visualization,” “Software_visualization,” and “Software_visualization,” that was the best and most satisfying validation of my method for finding similar papers.

**Harder Question:**
Similar to my approach with the easier question, I first verified the community-finding algorithm worked correctly using a small, hand-made dataset (N=12). It worked as expected. When I tried running the algorithm on the complete dataset it never finished. So, I attempted to pull a smaller subset of the real graph, with its root centered at the same node used in my test for the easier question. This allowed me to run the community slicing algorithm on this smaller graph (N=1015).
To create this subgraph, I defined a new algorithm, getNodeReach:
1)    Create a new Graph
2)    Define a Stack of nodes to visit, starting with the root node.
3)    Until the Stack is empty or we have reached our desired number of nodes:
    - Pop node from Stack
    - Add node to graph
    - If node’s in- and out-neighbors don’t already exist in Graph, add to Graph and Stack.
4)    Return the subGraph

### Reflection
At least with this dataset, it was inefficient to try and identify “communities” of papers to achieve the goal of finding papers “similar” to a given paper. Even when narrowing the scope of the graph to be centered around the single node, the resulting community for the single node did not contain any of the nodes found using the getSimilar function from the easier question. I needed to expand the size of the ‘small’ subgraph to N>30000 before my getSimilar function returned the same original results, and the graph at this size was too large to run the community-finding algorithm.

One thing I noticed with this particular community-finding algorithm is that it does not identify communities that are naturally segregated in the graph. In other words, given a group of nodes for which there is no path to some other node, we would expect the group of nodes to be in a different community from the other node. But the algorithm does not identify this, since it cannot compute a shortest path between the node and the group of nodes, and since we identify communities as those on either side of an edge that has the greatest number of shortest paths passing through it, the group of nodes is not considered a community. This algorithm works well only in graphs which have some path from a node to all other nodes.

It could be that this type of graph (almost entirely a Directed Acyclic Graph) in general doesn’t work well with this community-finding algorithm. Even when all nodes were connected, as in my subgraph, many of the communities that were found in my testing were comprised of single nodes, with the root node residing in a very large community nearly the size of the original subgraph.

It could also be that the problem I was trying to solve (finding similar papers) truly is better solved with a more straight-to-the-point method as used in the easier solution. Ultimately, this isn’t such a bad finding, as the easier method was efficient to build and run across the entire original dataset.


### How can I get the code?
Feel free to clone the GitHub repo and play around with the data sets and java code.

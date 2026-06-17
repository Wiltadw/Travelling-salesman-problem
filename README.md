# Traveling Salesman Problem (TSP) Solver and Algorithm Comparison

## Overview

This project implements and compares multiple approaches for solving the Traveling Salesman Problem (TSP), one of the most famous NP-Hard optimization problems in computer science.

Given a set of cities and the distances between them, the objective is to determine the shortest possible route that:

- Starts from a city
- Visits every city exactly once
- Returns to the starting city
- Minimizes the total travel distance

The project was developed as part of an Algorithm Design course and focuses on comparing exact and heuristic approaches in terms of solution quality, execution time, and scalability.

---

## Implemented Algorithms

### 1. Dynamic Programming (Held-Karp Algorithm)

An exact algorithm based on state-compressed dynamic programming using bitmasking.

The algorithm stores the minimum cost required to reach a city after visiting a specific subset of cities.

Features:

- Guarantees the optimal solution
- Uses bitmask representation for visited cities
- Reconstructs the optimal path using parent tracking
- Suitable for small and medium-sized problem instances

Complexity:

Time Complexity:

O(N² · 2ᴺ)

Space Complexity:

O(N · 2ᴺ)

---

### 2. Nearest Neighbor + 2-Opt Improvement

A heuristic approach designed for faster execution.

Phase 1:

The Nearest Neighbor algorithm repeatedly selects the closest unvisited city.

Phase 2:

The resulting route is improved using the 2-Opt local search optimization technique, which attempts to eliminate inefficient edge crossings and shorten the overall tour.

Features:

- Fast execution
- Good solution quality
- Effective for larger datasets
- Significant improvement over plain Nearest Neighbor

Complexity:

Nearest Neighbor:

O(N²)

2-Opt:

O(k·N²)

where k is the number of optimization passes.

---

### 3. Greedy Edge Matching (Cheapest-Link Heuristic)

A graph-based greedy algorithm.

The algorithm:

1. Sorts all edges by weight.
2. Selects the shortest valid edges.
3. Uses Union-Find to prevent premature cycles.
4. Constructs a Hamiltonian cycle.

Features:

- Efficient approximate solution
- Uses Disjoint Set Union (Union-Find)
- Prevents invalid tours through degree constraints
- Performs well on dense graphs

Complexity:

O(N² log N)

---

## Project Architecture

AlgorithmDesignProject.java

├── Dynamic Programming Solver

├── Path Reconstruction

├── Nearest Neighbor Heuristic

├── 2-Opt Local Optimization

├── Greedy Edge Matching

├── Union-Find Data Structure

└── Performance Measurement Utilities

---

## Input Format

The program expects:

1. Number of cities (N)
2. N × N distance matrix

Example:

4

0 10 15 20

10 0 35 25

15 35 0 30

20 25 30 0

---

## Example Output

---------- Dynamic Programming ----------

Cost(distance): 80.0

Route sort: [0, 1, 3, 2, 0]

Run Time: 2.431 ms


---------- Nearest Neighbor + 2-Opt ----------

Total Cost: 80.0

Route Order [0, 1, 3, 2, 0]

Required Time: 0.217 ms


---------- Greedy Edge Matching ----------

Total Cost: 95.0

Route Order [0, 2, 1, 3, 0]

Required Time: 0.118 ms

---

## Algorithms Comparison

| Algorithm | Type | Optimal | Scalability |
|------------|------------|------------|------------|
| Dynamic Programming | Exact | Yes | Low |
| Nearest Neighbor + 2-Opt | Heuristic | No | High |
| Greedy Edge Matching | Heuristic | No | High |

---

## Data Structures Used

The implementation makes use of:

- Dynamic Programming Tables
- Bitmask State Compression
- Array-Based Graph Representation
- Adjacency Lists
- Union-Find (Disjoint Set Union)
- Parent Tracking Arrays
- Local Search Optimization

---

## Key Computer Science Concepts

This project demonstrates practical applications of:

- Dynamic Programming
- Graph Theory
- NP-Hard Problems
- Greedy Algorithms
- Heuristic Optimization
- State Compression
- Path Reconstruction
- Union-Find
- Time and Space Complexity Analysis

---

## Performance Considerations

Dynamic Programming provides optimal solutions but becomes computationally expensive as the number of cities increases.

To address scalability challenges, two heuristic approaches were implemented:

- Nearest Neighbor + 2-Opt
- Greedy Edge Matching

These algorithms sacrifice guaranteed optimality in exchange for significantly lower execution times and memory requirements.

---

## Possible Future Improvements

Future versions may include:

- Branch and Bound
- Genetic Algorithm
- Ant Colony Optimization
- Simulated Annealing
- Parallel Processing
- Graphical Route Visualization
- Benchmark Dataset Integration
- Performance Charts and Analysis

---

## Technologies

Language:

- Java

Libraries:

- java.util
- java.io

---

## Author

Soheil Khorrami

Algorithm Design Course Project

University Assignment – Traveling Salesman Problem

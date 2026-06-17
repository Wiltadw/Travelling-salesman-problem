# Traveling Salesman Problem (TSP) Algorithm Comparison

A Java implementation of multiple algorithms for solving the Traveling Salesman Problem (TSP), including both exact and heuristic approaches.

This project was developed as part of an Algorithm Design course to compare solution quality and execution performance across different TSP-solving techniques.

---

## Overview

The Traveling Salesman Problem (TSP) is one of the most well-known NP-Hard optimization problems.

Given a set of cities and the distance between each pair of cities, the goal is to find the shortest possible route that:

- Visits every city exactly once
- Returns to the starting city
- Minimizes the total travel distance

This project implements and compares three different approaches:

| Algorithm | Type | Optimal Solution |
|------------|------------|------------|
| Dynamic Programming (Held-Karp) | Exact | ✅ Yes |
| Nearest Neighbor + 2-Opt | Heuristic | ❌ No |
| Greedy Edge Matching | Heuristic | ❌ No |

---

## Implemented Algorithms

### 1. Dynamic Programming (Held-Karp)

An exact solution based on bitmask dynamic programming.

Features:

- Guarantees the optimal route
- Uses state compression with bitmasks
- Reconstructs the optimal path using a parent table

Complexity:

```text
Time: O(N² × 2ᴺ)
Memory: O(N × 2ᴺ)

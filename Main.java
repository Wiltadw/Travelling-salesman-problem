import java.util.*;
import java.io.*;

public class Main {
    
    static int N;
    static double[][] dist;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("How many town we have: ");
        N = sc.nextInt();
        dist = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                dist[i][j] = sc.nextDouble();


//      Dynamic Programming Part, with  Exact solution
        if (N >= 2 && N <= 22) {
            long startDP = System.nanoTime();
            DPResult dpRes = solveDP();
            long endDP = System.nanoTime();

            System.out.println("---------- (Dynamic Programming) ----------");
            System.out.println("Cost(distance): " + dpRes.cost);
            System.out.println("Route sort: " + dpRes.path);
            System.out.printf("Run Time: %.3f ms%n%n", (endDP - startDP) / 1_000_000.0);
        }
        else if (N < 2)
            System.out.println("WTF are we supposed to do with 1 town bro ??!!");
        else
            System.out.println("Too big N can cause to memory issues !");

//      Greedy Algorithm part, with approximate (but fast) solution

        // Nearest Neighbor + 2-opt local improvement
        long start1 = System.nanoTime();
        TourResult nn = nearestNeighborWith2Opt(0);
        long end1 = System.nanoTime();

        System.out.println("---------- Nearest Neighbor + 2-Opt ----------");
        System.out.println("Total Cost: " + nn.cost);
        System.out.println("Route Order " + nn.path);
        System.out.printf("Required time : %.3f ms%n%n",
                (end1 - start1) / 1_000_000.0);


        // Greedy Edge (Christofides-like edge selection)
        long start2 = System.nanoTime();
        TourResult ge = greedyEdgeMatching();
        long end2 = System.nanoTime();

        System.out.println("---------- Greedy Edge Matching ----------");
        System.out.println("Total Cost: " + ge.cost);
        System.out.println("Route Order " + ge.path);
        System.out.printf("Required time : %.3f ms%n%n",
                (end2 - start2) / 1_000_000.0);
    }


    //               Dynamic Programming Part
    static class DPResult {
        double cost;
        List<Integer> path;
    }

    static DPResult solveDP() {
        //  This is a binary number(but represented in decimal) for the town we've visited(right to left)
        int FULL = 1 << N;                      // Or just  FULL = 2^N

        // dp[X][Y] The min (cost/distance) we spend to got to the Y by passing from X
        double[][] dp = new double[FULL][N];        // Our main DP table
        int[][] parent = new int[FULL][N];      // parent[x][y] = t --> we've been at t, for getting to y by passing though X


        for (double[] row : dp)
            Arrays.fill(row, Double.MAX_VALUE / 2);   // Like inf, that means the default cost is MAX
        for (int[] row : parent)
            Arrays.fill(row, -1);                   // -1 means we've been in this town before

        // Start Point
        dp[1][0] = 0;   // We've just seen the first town, And are in the same town, And no cost yet !


        // TC --> 2^N * N * N
        for (int mask = 1; mask < FULL; mask++) {
            if ((mask & 1) == 0)    // make sure we've seen the started from the 0'th city
                continue;

            for (int last = 0; last < N; last++) {          // last city we've visited
                if ((mask & (1 << last)) == 0)              // Check if we've visited this town before (based on the current mask)
                    continue;
                if (dp[mask][last] == Double.MAX_VALUE / 2)   // Check if we've even found a route or not !
                    continue;

                // This loop seeks for the route to other unvisited towns
                for (int next = 0; next < N; next++) {
                    if ((mask & (1 << next)) != 0)
                        continue; // Skip already visited town

                    int newMask = mask | (1 << next);
                    double newCost = dp[mask][last] + dist[last][next];

                    // Set min the new cost from current position to other ones
                    if (newCost < dp[newMask][next]) {
                        dp[newMask][next] = newCost;
                        parent[newMask][next] = last;
                    }
                }
            }
        }

        // Return back to town 0
        double best = Long.MAX_VALUE;
        int bestLast = -1;
        int fullMask = FULL - 1;
        // Check the best route, when our final town is from 1 to N
        for (int last = 1; last < N; last++) {
            if (dp[fullMask][last] == Double.MAX_VALUE / 2) continue;
            double total = dp[fullMask][last] + dist[last][0];
            if (total < best) {
                best = total;
                bestLast = last;
            }
        }

        // When we only have 1 town !!
        if (N == 1) {
            DPResult r = new DPResult();
            r.cost = 0;
            r.path = new ArrayList<>(List.of(0, 0));
            return r;
        }

        // Recreating Path Based on 'parent' table
        List<Integer> path = new ArrayList<>();
        int mask = fullMask;
        int cur = bestLast;
        while (cur != -1) {         // -1 means we've done with th path (OR hit the start town)
            path.add(cur);
            int p = parent[mask][cur];
            mask ^= (1 << cur);     // Remove the current town (cur) from the mask
            cur = p;
        }
        Collections.reverse(path);
        path.add(0);

        DPResult r = new DPResult();
        r.cost = best;
        r.path = path;
        return r;
    }

    static class TourResult {
        double cost;
        List<Integer> path;
    }

    static double tourCost(List<Integer> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += dist[path.get(i)][path.get(i + 1)];
        }
        return total;
    }

    //  Greedy Nearest Algorithm + 2 Optimization
    static TourResult nearestNeighborWith2Opt(int startCity) {
        boolean[] visited = new boolean[N];
        List<Integer> path = new ArrayList<>();
        int current = startCity;
        visited[current] = true;
        path.add(current);

        for (int i = 0; i < N-1; i++) {
            int next = -1;
            double bestDist = Double.MAX_VALUE;

            //  Find the nearest one we haven't visited yet
            for (int j = 0; j < N; j++) {
                if (!visited[j] && dist[current][j] < bestDist) {
                    bestDist = dist[current][j];
                    next = j;
                }
            }
            visited[next] = true;
            path.add(next);
            current = next;
        }
        path.add(startCity); // Back to start town (0)


        //  Optimization the founded path using '2opt' algorithm
        path = twoOptImprove(path);

        TourResult res = new TourResult();
        res.path = path;
        res.cost = tourCost(path);
        return res;
    }


    // Local improvement(Optimization) algorithm
    static List<Integer> twoOptImprove(List<Integer> tour) {
        int n = tour.size();
        boolean improved = true;
        // limit the functionality for bigger N, since it causes more required time
        int maxPasses = N <= 300 ? 50 : 3;
        int pass = 0;

        while (improved && pass < maxPasses) {
            improved = false;
            pass++;
            for (int i = 1; i < n - 2; i++) {
                for (int j = i + 1; j < n - 1; j++) {
                    int a = tour.get(i - 1), b = tour.get(i);
                    int c = tour.get(j), d = tour.get(j + 1);

                    double oldCost = dist[a][b] + dist[c][d];
                    double newCost = dist[a][c] + dist[b][d];

                    if (newCost < oldCost) {
                        // Reversing
                        Collections.reverse(tour.subList(i, j + 1));
                        improved = true;
                    }
                }
            }
        }
        return tour;
    }

    //  Greedy Edge Matching (Cheapest-Link Algorithm)
    static TourResult greedyEdgeMatching() {
        List<double[]> edges = new ArrayList<>(); // {dist, i, j}
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                edges.add(new double[]{dist[i][j], i, j});
            }
        }
        edges.sort((a, b) -> Double.compare(a[0], b[0]));

        int[] degree = new int[N];
        // Union-Find subpath detection
        int[] parent = new int[N];
        for (int i = 0; i < N; i++) parent[i] = i;

        // neighbors list in the final path (each town -> 2 neighbor at max)
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < N; i++) adj.add(new ArrayList<>());

        int edgesUsed = 0;

        // ============================================================
        //  Step 1: Sort all edges by weight (shortest first)
        // ============================================================
        for (double[] e : edges) {
            // We need exactly N edges for a Hamiltonian cycle
            if (edgesUsed == N) break;
            int u = (int)e[1], v = (int)e[2];

            // Each city can have at most 2 neighbors (degree ≤ 2)
            if (degree[u] >= 2 || degree[v] >= 2) continue;

            // Check if adding this edge creates a cycle (using Union-Find)
            int ru = find(parent, u);
            int rv = find(parent, v);

            // If they're already connected and it's not the last edge, skip (prevent cycle)
            if (ru == rv && edgesUsed < N - 1) continue;

            adj.get(u).add(v); // Add the edge to adjacency list
            adj.get(v).add(u);
            degree[u]++; // Update degrees
            degree[v]++;
            parent[ru] = rv; // Union the two sets (connect the components)
            edgesUsed++;
        }

        // ============================================================
        //  Step 2: Connect remaining endpoints (if any)
        // ============================================================
        connectRemainingEndpoints(adj, degree, parent);

        // ============================================================
        //  Step 3: Build the final path from adjacency list
        // ============================================================
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[N];
        int current = 0; // Start from city 0
        int prev = -1; // No previous city yet
        path.add(current);
        visited[current] = true;

        // Visit all cities one by one
        for (int step = 1; step < N; step++) {
            int next = -1;

            // Try to find an unvisited neighbor (prefer not going back to prev)
            for (int cand : adj.get(current)) {
                if (cand != prev) {
                    next = cand;
                    break;
                }
            }

            // If no valid neighbor found, try any unvisited neighbor
            if (next == -1 || visited[next]) {
                for (int cand : adj.get(current)) {
                    if (!visited[cand]) {
                        next = cand;
                        break;
                    }
                }
            }

            // Fallback: If still no neighbor found (should not happen),
            // use Nearest Neighbor heuristic
            if (next == -1) {

                double best = Double.MAX_VALUE;
                for (int j = 0; j < N; j++) {
                    if (!visited[j] && dist[current][j] < best) {
                        best = dist[current][j];
                        next = j;
                    }
                }
            }

            // Mark as visited and add to path
            visited[next] = true;
            path.add(next);
            prev = current;
            current = next;
        }

        // Return to starting city (close the cycle)
        path.add(0);

        // Create result object
        TourResult res = new TourResult();
        res.path = path;
        res.cost = tourCost(path);
        return res;
    }

    // Connects remaining cities that still have degree < 2
    // This ensures we get a full Hamiltonian cycle
    static void connectRemainingEndpoints(List<List<Integer>> adj, int[] degree, int[] parent) {

        // Find all cities with degree < 2 (they need more connections)
        List<Integer> endpoints = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (degree[i] < 2) endpoints.add(i);
        }

        // Calculate how many edges we currently have
        int totalEdgesNeeded = N;           // A Hamiltonian cycle needs exactly N edges
        int currentEdges = 0;
        for (int d : degree) currentEdges += d;
        currentEdges /= 2;               // Each edge contributes 2 to total degree

         // Keep connecting until we have all N edges
        while (currentEdges < totalEdgesNeeded) {

            // Remove cities that already have degree 2
            endpoints.removeIf(x -> degree[x] >= 2);

            int u = -1, v = -1;
            double best = Double.MAX_VALUE;

            // Find the shortest edge between two endpoints
            for (int i = 0; i < endpoints.size(); i++) {
                for (int j = i + 1; j < endpoints.size(); j++) {

                    int a = endpoints.get(i), b = endpoints.get(j);
                    if (a == b) continue;

                    // Check if adding this edge creates a cycle
                    int ra = find(parent, a), rb = find(parent, b);
                    boolean lastEdge = (currentEdges == totalEdgesNeeded - 1);

                    // If they're already connected and it's not the last edge, skip
                    if (ra == rb && !lastEdge) continue;

                    // Keep the shortest valid edge
                    if (dist[a][b] < best) {
                        best = dist[a][b];
                        u = a;
                        v = b;
                    }
                }
            }

            // If no valid edge found, break (should not happen)
            if (u == -1) break;

            // Add the edge
            adj.get(u).add(v);
            adj.get(v).add(u);
            degree[u]++;
            degree[v]++;
            parent[find(parent, u)] = find(parent, v);
            currentEdges++;
        }
    }

    // Union-Find: Find the root of the set containing x
    // Uses path compression for optimization
    static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path compression: make parent[x] point directly to root
            x = parent[x];
        }
        return x;
    }
}
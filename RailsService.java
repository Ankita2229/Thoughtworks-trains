package trains;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RailsService {
    // creating an adjacency table to store the relationship of each vertex
    private Map<String, Map<String, Integer>> adj;
    private int number_of_vertices;
    private Set<String> all_vertices;

    /**
     * Build graph from input String
     * @param str Source town, Destination town, and distance between them is provided in a comma separated string
     * This method splits input string by ',' and each element is considered a route that is further split into source node, target node, and distance between source and target node.
     * An adjacency list is generated for each vertex - for eg, {A={B=5, D=5}}.
     */
    public RailsService(String str) {
        all_vertices = new HashSet<>();
        adj = new HashMap<>();
        if (str == null || str.length() <=0 ) return;
        String[] routes = str.split(",");
        for (String route : routes) {
            if (route.length() != 3) continue;
            String source = String.valueOf(route.charAt(0));
            String target = String.valueOf(route.charAt(1));
            int weight = route.charAt(2) - '0';
            Map<String, Integer> sourceMap = adj.get(source);
            if (sourceMap == null) {
                sourceMap = new HashMap<>();
                adj.put(source, sourceMap);
            }
            sourceMap.put(target, weight);
            if (all_vertices.add(source)) number_of_vertices++;
            if (all_vertices.add(target)) number_of_vertices++;
        }
    }

    /**
     * Problem 1 to 5
     * Calculate the distance between vertices, if the path between vertices doesn't exist return NO SUCH ROUTE
     * @param vertices String array separated by - static from test file(as guided in question)
     * @return String distance between source & target is returned in a string format and returns NO SUCH ROUTE if there exist no path between vertices.
     * This method finds distance between two nodes. We initialized a variable named distance and it gets updated once we find our target in the edgeMap. We update EdgeMap if source node is available in the adjacency list.
     * For eg - A-D-5. We check if A exist in adj list. If it does, we fetch it's value and store it in EdgeMap. We check if target node exist in edgeMap. If it does, fetch its value i.e distance/weight.
     */
    public String findDistanceOfPath(String[] vertices) {
        String source = vertices[0];
        int distance = 0;
        for (int i = 1; i < vertices.length; i++) {
            String target = vertices[i];
            Map<String, Integer> edgeMap = adj.get(source);
            if (edgeMap != null && edgeMap.containsKey(target)) {
                distance += edgeMap.get(target);
            } else {
                return "NO SUCH ROUTE";
            }
            source = target;
        }
        return String.valueOf(distance);
    }

    /**
    * Problem 6
    * Calculate the number of trips that starts with given vertex, ends with given vertex, and has maximum of 3 stops
    * @param start denotes source node, end denotes target node, maxStops denotes number of stops i.e if number is 5 then there can be 5 or less stops before we reach our destination
    * @return String - number of possible paths that matches maxStops criteria
    * AtomicInteger - It's a google reference. Searched for a feature that increments counter atomically. Found increment and get - works like a charm!
    * This is recursive method - a queue is declared at the start which is updated based on the last added entry to routes map.
    * As we move further, queue acts as a chaining. For eg - adj.get("C") will add D=? and E=? to the routes. If one of the nodes inside routes map isn't the end node then there will be no effect on the count variable.
    * We add the last added node to the queue and look for its routes and check if end node exists in it. This process will continue until the condition - queue.size < maxStops
    */
    public String countMaxStops(String start, String end , int maxStops){
        AtomicInteger count = new AtomicInteger();
        Deque<Map.Entry<String, Integer>> queue = new LinkedList<>();
        countTripsMaxStops(start, end, maxStops, queue, count);
        return String.valueOf(count);
    }

    private void countTripsMaxStops(String start, String end, int maxStop, Deque<Map.Entry<String, Integer>> queue, AtomicInteger count){
        if(queue.size() < maxStop){
            Map<String, Integer> routes = adj.get(start);
            for(Map.Entry<String, Integer> entry: routes.entrySet()){
                String town = entry.getKey();
                if(town.equals(end)){
                    count.incrementAndGet();
                }
                queue.addLast(entry);
                countTripsMaxStops(town, end, maxStop, queue, count);
                queue.removeLast();
            }
        }
    }

    /**
     * Problem 7
     * Calculate the number of trips that starts with given vertex, ends with given vertex, and has exactly 4 stops
     * @param start denotes source node, end denotes target node, exactStops denotes number of stops i.e if number is 5 then there has to be exactly 5 stops before we reach our destination
     * @return String - number of possible paths that matches exactStops criteria
     * AtomicInteger - It's a google reference. Searched for a feature that increments counter atomically. Found increment and get - works like a charm!
     * This is recursive method - a queue is declared at the start which is updated based on the last added entry to routes map.
     * As we move further, queue acts as a chaining. For eg - adj.get("C") will add D=? and E=? to the routes.
     * To achieve exactStops one of the nodes inside routes map should match the end node and queue size should match exactStops -1 then the count variable will be incremented.
     * We add the last added node to the queue and look for its routes and check if end node exists in it. This process will continue until the condition - queue.size < exactStops
     */
    public String countExactStops(String start, String end, int exactStops){
        AtomicInteger count = new AtomicInteger();
        Deque<Map.Entry<String, Integer>> queue = new LinkedList<>();
        countExact(start, end, exactStops, queue, count);
        return String.valueOf(count);
    }

    private void countExact(String start, String end, int exactStops, Deque<Map.Entry<String, Integer>> queue, AtomicInteger count){
        if(queue.size() < exactStops){
            Map<String, Integer> routes = adj.get(start);
            for(Map.Entry<String, Integer> entry: routes.entrySet()){
                String town = entry.getKey();
                if(town.equals(end) && queue.size() == exactStops -1){
                    count.incrementAndGet();
                }
                queue.addLast(entry);
                countExact(town, end, exactStops, queue, count);
                queue.removeLast();
            }
        }
    }

    /**
     * Problem 8,9
     * Calculate the shortest distance between the two vertices
     * @param s denotes source node, and t denotes target node
     * @return int - shortest distance between s and t
     * Followed Dijkstra's Algorithm - to calculate the closest distance of all nodes from the start node.
     * Return distance if there are no nodes to calculate from.
     * For Current node, dist = distance. Once we calculate distances to each node from start node, consider the shortest one.
     */
    public int findShortestDistance(String s, String t) {
        //store the vertex and distance closest to each vertex
        Map<String, Vertex> finalMap = new HashMap<>(this.number_of_vertices);
        for (String vertex : all_vertices) {
            finalMap.put(vertex, new Vertex(s, Integer.MAX_VALUE));
        }
        finalMap.put(s, null);
        Queue<Vertex> pq = new PriorityQueue<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Vertex v1 = (Vertex) o1;
                Vertex v2 = (Vertex) o2;
                return v1.dist - v2.dist;
            }
        });
        pq.add(new Vertex(s, 0));
        Set<String> visited = new HashSet<>(this.number_of_vertices);
        while (!pq.isEmpty()) {
            Vertex minVertex = pq.poll();
            if (!visited.add(minVertex.v)) {
                continue;
            }
            Map<String, Integer> edgeMap = adj.get(minVertex.v);
            for (Map.Entry<String, Integer> entry : edgeMap.entrySet()) {
                int distance = minVertex.dist + entry.getValue();
                if (finalMap.get(entry.getKey()) != null && distance < finalMap.get(entry.getKey()).dist) {
                    finalMap.get(entry.getKey()).dist = distance;
                    finalMap.get(entry.getKey()).v = minVertex.v;
                }
                pq.add(new Vertex(entry.getKey(), distance));
            }
        }
        if (!s.equals(t)){
            return finalMap.get(t).dist;
        }else{
            return calculateDistance(s, finalMap);
        }
    }

    /**
     * Decomposing the problem into smaller parts. Distance between the starting vertex and its closest vertex
     */
    private int calculateDistance(String s, Map<String, Vertex> finalMap) {
        int dist = Integer.MAX_VALUE;
        for (Map.Entry<String, Vertex> entry : finalMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().v.equals(s) && entry.getValue().dist < dist){
                String next = entry.getKey();
                int currDist = entry.getValue().dist + findShortestDistance(next , s);
                if (currDist < dist){
                    dist = currDist;
                }
            }
        }
        return dist;
    }

    /**
     * Problem 10
     * Calculate different routes from given start and end vertices and the distance should be less than 30
     * @param start denotes source node, end denotes target node, maxDistance denotes path distance should be less than the given number before we reach destination
     * @return String - number of possible paths that matches maxDistance criteria
     * AtomicInteger - It's a google reference. Searched for a feature that increments counter atomically. Found increment and get - works like a charm!
     * This is recursive method - a queue is declared at the start which is updated based on the last added entry to routes map.
     * As we move further, queue acts as a chaining. For eg - adj.get("C") will add D=? and E=? to the routes.
     * Total variable is initialized to keep track of distance until we reach the end node. When we each end node and are still under maxDistance then the count variable will be incremented.
     * We add value to total if we reached the end node but if town didn't match the end node then we subtract value from total. This process will continue until the condition - queue.size < maxDistance
     */
    public String calculateDiffRoutes(String start, String end, int maxDistance){
        AtomicInteger count = new AtomicInteger();
        int total = 0;
        Deque<Map.Entry<String, Integer>> queue = new LinkedList<>();
        diffRoutes(start, end, maxDistance, queue, count, total);
        return String.valueOf(count);
    }

    private void diffRoutes(String start, String end, int maxDistance, Deque<Map.Entry<String, Integer>> queue, AtomicInteger count, int total) {
        if(queue.size() < maxDistance){
            Map<String, Integer> routes = adj.get(start);
            for(Map.Entry<String, Integer> entry: routes.entrySet()){
                String town = entry.getKey();
                if (total + entry.getValue() >= maxDistance) {
                    continue;
                }
                if (town.equals(end)) {
                    count.getAndIncrement();
                }
                total += entry.getValue();
                diffRoutes(town, end, maxDistance, queue, count, total);
                total -= entry.getValue();
            }
        }
    }

    /**
     * The vertex distance class saves the target vertex and the distance between the starting vertex and the target vertex
     */
    private class Vertex{
        public String v;
        //distance of start vertex and v
        public int dist;
        public Vertex(String v, int dist) {
            this.v = v;
            this.dist = dist;
        }
    }
}

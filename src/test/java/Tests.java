import map.Graph;
import map.MapNode;
import map.MapEdge;
import map.WayPointAdapter;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import plan.*;
import java.io.PrintWriter;
import java.io.FileWriter;
import static org.junit.Assert.assertEquals;

public class Tests {
    @Test
    public void sampleTest() {
        String osmFile = "./data/toronto.osm";
        String cyclistsAccidentFile = "./data/Cyclists.csv";
        Graph torontoGraph = new Graph(osmFile, cyclistsAccidentFile);

        Planner bfsPlanner = new BFSPlanner();

        //Manually specify sourceNode and endNode
        long sourceNodeId = 6374148719L;
        long endNodeId = 6374051128L;
        MapNode sourceNode = torontoGraph.nodes.get(sourceNodeId);
        MapNode endNode = torontoGraph.nodes.get(endNodeId);
        List<MapNode> nodeList = bfsPlanner.plan(sourceNode, endNode).path;
        List<Long> actual = new ArrayList<>();
        for (MapNode node : nodeList) {
            actual.add(node.id);
        }

        List<Long> expected = Arrays.asList(6374148719L, 6662926503L, 389678174L, 389678175L, 1480794735L, 389678176L,
                3983181527L, 3983181528L, 389678212L, 389678213L, 389678214L, 389678215L, 389678216L, 7311057931L,
                389678220L, 389678221L, 389678222L, 389677908L, 749952029L, 389677909L, 389677910L,
                389677911L, 389677912L, 391186184L, 389677913L, 389677914L, 6374051128L);

        assertEquals(expected, actual);
    }

    @Test
    public void writeResults(){
        String osmFile = "./data/toronto_full.osm";
        String cyclistsAccidentFile = "./data/Cyclists.csv";
        Graph torontoGraph = new Graph(osmFile, cyclistsAccidentFile);

        CostFunction costFunction = new CostFunctionAllFeatures(torontoGraph);
        Heuristic heuristic = new AStarHeuristic(torontoGraph);

        Planner bfsPlanner = new BFSPlanner();
        Planner dfsPlanner = new DFSPlanner(500);
        Planner uniformCostPlanner = new UniformCostPlanner(costFunction);
        Planner greedyBestFirstPlanner = new GreedyBestFirstPlanner(heuristic);
        Planner aStarPlanner = new AStarPlanner(heuristic, costFunction);

        List<List<MapNode>> startEndPairs = new ArrayList<>();

        long sourceNodeId1 = 1298514559L;
        long endNodeId1 = 6369148345L;
        List<MapNode> pair1 = Arrays.asList(torontoGraph.nodes.get(sourceNodeId1), torontoGraph.nodes.get(endNodeId1));
        startEndPairs.add(pair1);

        long sourceNodeId2 = 6400935393L;
        long endNodeId2 = 828418148L;
        List<MapNode> pair2 = Arrays.asList(torontoGraph.nodes.get(sourceNodeId2), torontoGraph.nodes.get(endNodeId2));
        startEndPairs.add(pair2);

        long sourceNodeId3 = 828418148L;
        long endNodeId3 = 2129327601L;
        List<MapNode> pair3 = Arrays.asList(torontoGraph.nodes.get(sourceNodeId3), torontoGraph.nodes.get(endNodeId3));
        startEndPairs.add(pair3);

        long sourceNodeId4 = 902158662L;
        long endNodeId4 = 280891618L;
        List<MapNode> pair4 = Arrays.asList(torontoGraph.nodes.get(sourceNodeId4), torontoGraph.nodes.get(endNodeId4));
        startEndPairs.add(pair4);

        long sourceNodeId5 = 1298515679L;
        long endNodeId5 = 944736247L;
        List<MapNode> pair5 = Arrays.asList(torontoGraph.nodes.get(sourceNodeId5), torontoGraph.nodes.get(endNodeId5));
        startEndPairs.add(pair5);

        long sourceNodeId6 = 4576044261L;
        long endNodeId6 = 3479751850L;
        List<MapNode> pair6 = Arrays.asList(torontoGraph.nodes.get(sourceNodeId6), torontoGraph.nodes.get(endNodeId6));
        startEndPairs.add(pair6);

        try (PrintWriter writer = new PrintWriter(new FileWriter("MyResults.txt"))) {
            writer.println("Pair\t\tPlanner\t\tTime(ms)\t\tNodes Expanded\t\tPath Cost");
            for (List<MapNode> pair : startEndPairs) {
                String pairString = String.format("%d/%d", pair.get(0).id, pair.get(1).id);

                runAndWriteResults(writer, pairString, bfsPlanner, pair.get(0), pair.get(1), torontoGraph);
                runAndWriteResults(writer, pairString, dfsPlanner, pair.get(0), pair.get(1), torontoGraph);
                runAndWriteResults(writer, pairString, uniformCostPlanner, pair.get(0), pair.get(1), torontoGraph);
                runAndWriteResults(writer, pairString, greedyBestFirstPlanner, pair.get(0), pair.get(1), torontoGraph);
                runAndWriteResults(writer, pairString, aStarPlanner, pair.get(0), pair.get(1), torontoGraph);

                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runAndWriteResults(PrintWriter writer, String pairString, Planner planner, MapNode start, MapNode end, Graph graph) {
        long startTime = System.currentTimeMillis();
        PlanResult result = planner.plan(start, end);
        long endTime = System.currentTimeMillis();

        long time = (endTime - startTime);

        // calculate total path cost
        double totalPathCost = getPathCost(new CostFunctionAllFeatures(graph), result.path);

        writer.printf("%s\t%-20s %-12s %-20s %-20s%n", pairString, planner.getName(), time, result.expandedNodeCount, totalPathCost);
    }

    public static double getPathCost(CostFunction costFunction, List<MapNode> path) {
        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            MapNode curNode = path.get(i);
            MapNode nextNode = path.get(i + 1);
            for (MapEdge edge : curNode.edges) {
                if (edge.destinationNode.id == nextNode.id) {
                    cost += costFunction.getCost(edge);
                }
            }
        }
        return cost;
    }
}

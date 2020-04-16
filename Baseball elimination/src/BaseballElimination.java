import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private final int n;
    private final ST<String, Integer> teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final int[][] g;
    private int[] otherIds;
    private final String[] names;
    private int nGameNodes;
    private int totalSCapacity;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        n = Integer.parseInt(in.readLine());
        wins = new int[n];
        losses = new int[n];
        remains = new int[n];
        g = new int[n][n];
        names = new String[n];
        teams = new ST<>();

        String st = in.readLine();
        int id = 0;
        while (st != null) {
            String[] fields = st.trim().split("\\s+");
            String team = fields[0];
            teams.put(team, id);
            names[id] = team;
            wins[id] = Integer.parseInt(fields[1]);
            losses[id] = Integer.parseInt(fields[2]);
            remains[id] = Integer.parseInt(fields[3]);
            for (int j = 0; j < n; j++) {
                g[id][j] = Integer.parseInt(fields[j + 4]);
            }
            id += 1;
            st = in.readLine();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        return remains[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2)) throw new IllegalArgumentException();
        return g[teams.get(team1)][teams.get(team2)];
    }


    private FordFulkerson fordFulkerson(String team) {
        int x = teams.get(team);

        int nStartEndNodes = 2;
        int nTeamNodes = n - 1;
        nGameNodes = nTeamNodes * (nTeamNodes - 1) / 2;
        int nNodes = nStartEndNodes + nTeamNodes + nGameNodes;
        int startNode = 0;
        int endNode = nNodes - 1;

        otherIds = new int[n - 1];
        int i = 0;
        for (String key : teams.keys()) {
            if (!key.equals(team)) {
                otherIds[i++] = teams.get(key);
            }
        }

        int nodeId = 1;
        FlowNetwork network = new FlowNetwork(nNodes);
        totalSCapacity = 0;

        for (int v = 0; v < n - 1; v++) {
            for (int w = v + 1; w < n - 1; w++) {
                int cap = g[otherIds[v]][otherIds[w]];
                FlowEdge gameEdge = new FlowEdge(startNode, nodeId, cap);
                network.addEdge(gameEdge);
                totalSCapacity += cap;

                int teamNodeId1 = nGameNodes + 1 + v;
                int teamNodeId2 = nGameNodes + 1 + w;
                FlowEdge teamEdge1 = new FlowEdge(nodeId, teamNodeId1, Double.POSITIVE_INFINITY);
                FlowEdge teamEdge2 = new FlowEdge(nodeId, teamNodeId2, Double.POSITIVE_INFINITY);
                network.addEdge(teamEdge1);
                network.addEdge(teamEdge2);


                nodeId += 1;

            }

            int teamNodeId = nGameNodes + 1 + v;
            FlowEdge endEdge = new FlowEdge(teamNodeId, endNode, wins[x] + remains[x] - wins[otherIds[v]]);
            network.addEdge(endEdge);

        }
        return new FordFulkerson(network, startNode, endNode);

    }

    private ArrayList<String> trivial(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();

        ArrayList<String> res = new ArrayList<>();
        int x = teams.get(team);
        for (int i = 0; i < n; i++) {
            if (x != i && wins[x] + remains[x] < wins[i]) {
                res.add(names[i]);
            }
        }
        return res;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();

        if (!trivial(team).isEmpty()) return true;

        FordFulkerson fordFulkerson = fordFulkerson(team);

        if (fordFulkerson.value() == totalSCapacity) {
            return false;
        }

        return true;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        if (!isEliminated(team)) return null;
        ArrayList<String> res = trivial(team);
        if (!res.isEmpty()) return res;

        FordFulkerson fordFulkerson = fordFulkerson(team);

        for (int i = 0; i < otherIds.length; i++) {
//            System.out.println(nGameNodes + 1 + i);
//            System.out.println(nGameNodes + 1 + i + " " + fordFulkerson.inCut(nGameNodes + 1 + i));
            if (fordFulkerson.inCut(nGameNodes + 1 + i)) {
                res.add(names[otherIds[i]]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
//        String team = "Montreal";
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class homework {

    int n, p;
    double time;
    long start, TIME_LIMIT;
    Board initial;

    public class Board {
        char[] arr;
        ArrayList<int[]> moves = new ArrayList<int[]>();

        public Board(char[] arr){
            this.arr = arr;
        }

        public Board move(int m){
            char[] clone = arr.clone();
            int[] move = moves.get(m);
            for(int i : move){
                clone[i] = '*';
            }
            return new Board(clone);
        }

        public void gravity(){
            for(int i = (n*n)-1; i > n-1; i--){
                if(arr[i]=='*'){
                    for (int j = i-n; j > -1 ; j-=n) {
                        if(arr[j]!='*') {
                            arr[i] = arr[j];
                            arr[j] = '*';
                            break;
                        }
                    }
                }
            }
        }

        public void getMoves(){
            this.gravity();
            char[] clone = arr.clone();
            for(int i = 0; i<n*n; i++){
                int[] a = dfs(i, clone, clone[i], new ArrayList<Integer>(), true);
                if(a!=null)
                    moves.add(a);
            }
        }

        public int[] dfs(int m, char[] c, char find, ArrayList<Integer> move, boolean isStart){
            if(m<0 || m>=n*n)
                return null;

            // base case
            if(c[m] == '*' || c[m] != find)
                return null;

            if(m%n == 0 && !isStart)
                return null;

            c[m] = '*';

            move.add(m);

            dfs(m+1, c, find, move, false);
            if(m%n!=0)
                dfs(m-1, c, find, move, isStart);
            dfs(m+n, c, find, move, isStart);
            dfs(m-n, c, find, move, isStart);

            int a[] = new int[move.size()];

            Iterator<Integer> it = move.iterator();

            for (int i = 0; i < a.length ; i++) {
                a[i] = it.next().intValue();
            }

            return a;

        }

    }

    private class Node {
        ArrayList<Node> children;
        Board state;
        int[] move; // the move that got us this board
        double score;

        public Node(double score, Board state, int[] move){
            this.score = score;
            this.state = state;
            this.move = move;
            children = new ArrayList<Node>();
            this.state.getMoves();
        }

        public void addChild(Node node){
            children.add(node);
        }

        public void setScore(double score){
            this.score = score;
        }

    }

    public homework(long start){
        this.start = start;
    }

    public void setLimit(){
        double n = time/2.0;
        n = (n)*1000;
        TIME_LIMIT = (long) n;
    }

    public double minimax(Node node, double alpha, double beta, boolean isMax){
        double bestScore;
        int paths = node.children.size();

        if(paths == 0)
            bestScore = node.score;
        else if(isMax){
            bestScore = alpha;

            for (int i = 0; i < paths ; i++) {
                double childScore = minimax(node.children.get(i), bestScore, beta, false);
                bestScore = Math.max(bestScore, childScore);
                if(beta <= bestScore)
                    break;
            }
        }
        else {
            bestScore = beta;

            for (int i = 0; i < paths ; i++) {
                double childScore = minimax(node.children.get(i), alpha, bestScore, true);
                bestScore = Math.min(bestScore, childScore);
                if(bestScore <= alpha)
                    break;
            }

        }

        return bestScore;

    }

    public double eval(Node node, boolean isMax){
        return isMax ? node.score+node.move.length : node.score-node.move.length;
    }

    public void play() throws IOException{
        Node root = new Node(0, initial, null);
        createTree(root, true);
        root.setScore(Double.MIN_VALUE);
        double score;
        double maxScore = Double.MIN_VALUE;
        Node bestNode = null;
        for(Node child: root.children){
            score = minimax(child, Double.MIN_VALUE, Double.MAX_VALUE, false);
            if(score>maxScore){
                maxScore = score;
                bestNode = child;
            }
        }
        writeOutput(bestNode.move, bestNode.state);
    }

    public void playIter() throws IOException {
        Node root = new Node(0, initial, null);
        ArrayList<Node> tree = new ArrayList<>();
        tree.add(root);
        boolean isMax = true;
        Node bestNode = null;
        double maxScore;
        int limit = 29;

        outerloop:
        for(int i = 1; i<limit; i++){
            if((System.currentTimeMillis()-start) > TIME_LIMIT)
                break;
            maxScore = Double.MIN_VALUE;
            boolean moreDepth = false;
            double score;
            ArrayList<Node> next = new ArrayList<Node>();
            for (int j = 0; j < tree.size() ; j++) {
                moreDepth = moreDepth || createTreeIter(tree.get(j), isMax);
                if(System.currentTimeMillis()-start >= TIME_LIMIT)
                    break outerloop;
                next.addAll(tree.get(j).children);
            }
            if(moreDepth == false)
                break;
            bestNode = null;
            for(Node child: root.children){
                score = minimax(child, Double.MIN_VALUE, Double.MAX_VALUE, false);
                if(score>maxScore){
                    maxScore = score;
                    bestNode = child;
                }
            }
            isMax = !isMax;
            tree = next;
        }

        bestNode.state.gravity();
        writeOutput(bestNode.move, bestNode.state);
    }

    public boolean createTreeIter(Node node, boolean isMax){
        if(node.state.moves.size() < 1)
            return false;

        Iterator<int[]> it = node.state.moves.iterator();

        int i = 0;
        while(it.hasNext()){
            Node child = new Node(node.score, node.state.move(i), it.next());
            child.setScore(eval(child, isMax));
            node.addChild(child);
            i++;
        }

        return true;
    }

    public Node createTree(Node node, boolean isMax){
        if(node.state.moves.size() < 1)
            return node;

        Iterator<int[]> it = node.state.moves.iterator();

        int i = 0;
        while(it.hasNext()){
            Node child = new Node(node.score, node.state.move(i), it.next());
            child.setScore(eval(child, isMax));
            createTree(child, !isMax);
            node.addChild(child);
            i++;
        }

        return null;
    }

    void writeOutput(int[] move, Board moveState) throws IOException {
        BufferedWriter lineWrite = new BufferedWriter(new FileWriter("/Users/ketaki/Downloads/output.txt"));
        StringBuilder moveString = new StringBuilder();
        int moveInt = move[0];
        int row = (moveInt/n)+1;
        char column = (char) (65+(moveInt%n));
        moveString.append(column);
        moveString.append(row);

        lineWrite.write(moveString.toString());

        for(int i = 0; i<n*n; i++){
            if(i%n == 0)
                lineWrite.write('\n');
            lineWrite.write(moveState.arr[i]);
        }

        lineWrite.write('\n');
        lineWrite.close();
    }

    void readInput(String filename) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        this.n = Integer.parseInt(input.readLine());
        this.p = Integer.parseInt(input.readLine());
        this.time = Double.parseDouble(input.readLine());
        String line;
        while((line = input.readLine()) != null){
            sb.append(line);
        }

        initial = new Board(sb.toString().toCharArray());
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        homework obj = new homework(start);
        obj.readInput("/Users/ketaki/Downloads/input.txt");
        obj.setLimit();
        if(obj.n<5)
            obj.play();
        else
            obj.playIter();
    }

}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class PuzzleSolver {
    //Necessary variables.
    static Puzzle[] initialStates = new Puzzle[100];
    static int[][] goalstate = {{1,2,3},{4,5,6},{7,8,0}};
    static Puzzle sol = new Puzzle(goalstate,0);
    static Graph newGraph = new SingleGraph("graph");
    static int id = 1;
    static int ide = 0;
    static ArrayList<Node1> newList = new ArrayList<Node1>();
    static int numofmoves = 0;
    //creates the random initial states. It is used only once, then it saves the arrays into arrays.txt
    public static void createinitialstates(){
        int count = 0;

        Random gen = new Random();
        while(count < 100) {
            initialStates[count] = new Puzzle(goalstate,0);
            for (int i = 0; i < 30; i++) {
                int tmp = gen.nextInt(4);
                if (tmp == 0) {
                    initialStates[count].moveUp(0);
                }
                else if (tmp == 1) {
                    initialStates[count].moveDown(0);
                }
                else if (tmp == 2) {
                    initialStates[count].moveLeft(0);
                }
                else
                    initialStates[count].moveRight(0);
            }
            initialStates[count].setDistancetraveled(0);
            if(checkifexists(initialStates[count])){
                count--;
            }
            count++;
        }
    }
    //Checks whether two puzzle is identical or not
    public static boolean checkidentical(Puzzle n1, Puzzle n2){
        boolean flag = true;
        for(int i = 0 ; i < 3 && flag; i++) {
            for(int j = 0; j < 3; j++)
                if (n1.getPuzzle()[i][j] != n2.getPuzzle()[i][j])
                    flag = false;
        }
        return flag;
    }
    //It checks whether the initialstates are the same or not.
    public static boolean checkifexists(Puzzle n){
        int coherence = 0;
        for(int i = 0; i < initialStates.length; i++){
            if(initialStates[i] != null)
                if(checkidentical(initialStates[i],n))
                    coherence++;
        }
        if(coherence > 1)
            return true;
        return false;
    }
    //getting the root which is helpful for searching the entire tree(checkTree() method).
    public static Node1 getRoot(Node1 ptr){
        if(ptr.getParent() == null)
            return ptr;
        return getRoot(ptr.getParent());
    }
    //basic function for calculating manhattan distance.
    public static int getManhattanDistance(Puzzle n){
        int returnval = 0;
        for(int i = 1; i < 9; i++){
            int row = (i-1)/3;
            int column = (i-1)%3;
            int[] loc = n.findloc(i);
            returnval = returnval + Math.abs(row-loc[0]) + Math.abs(column-loc[1]);
        }
        return returnval;
    }
    //gets the heuristic which is distance traveled plus the manhattan distance.
    public static int getHeuristic(Puzzle n){
        return n.getDistancetraveled() + getManhattanDistance(n);
    }

    //Checks the entire tree for the same element.
    public static int checkTree(Node1 ptr, Puzzle n, Node1 ptr1){
        if(ptr == ptr1 || ptr == null){
            return -1;
        }
        else if(checkidentical((Puzzle)(ptr.getData()), n)){
            return getHeuristic((Puzzle)(ptr.getData()));
        }
        else{
            int flag = -1;
            List<Node1> children = ptr.getChildren();
            for(int i = 0;i < children.size(); i++){
                flag = checkTree(children.get(i),n,ptr1);
            }
            return flag;
        }
    }
    //Checks for loop in the tree.
    public static boolean checkForLoop(Node1 parent, Puzzle n){
        if(parent == null)
            return false;
        else if(checkidentical((Puzzle)(parent.getData()),n)){
            return true;
        }
        else{
            boolean flag = false;
            return flag || checkForLoop(parent.getParent(),n);
        }
    }
    public static void aStarSearch(Node1 ptr, int ptrid){
        //If we reach the solution at the end of the recursion, print completed. Result is shown in blue.
        if(checkidentical(((Puzzle)(ptr.getData())),sol)){
            Node n = newGraph.getNode("" + ptrid);
            n.setAttribute("ui.style","fill-color: rgb(0,0,255);");
            System.out.println("completed");
            return;
        }
        //necessary temporary variables
        Puzzle[] pos = new Puzzle[4];
        Node1[] cptr = new Node1[4];
        boolean[] loo = new boolean[4];

        for(int i = 0; i < 4 ; i++){
            pos[i] = new Puzzle(((Puzzle)(ptr.getData())).getPuzzle(),((Puzzle)(ptr.getData())).getDistancetraveled());
            //creating the children based on four different moves.
            switch(i){
                case 0: pos[i].moveUp(0); break;
                case 1: pos[i].moveDown(0); break;
                case 2: pos[i].moveRight(0); break;
                case 3: pos[i].moveLeft(0); break;
            }
            numofmoves++;
            cptr[i] = new Node1(pos[i],pos[i].getDistancetraveled()+getManhattanDistance(pos[i]));
            ptr.addChild(cptr[i]);

            //checks for loop, returns true if there is.
            loo[i] = checkForLoop(ptr, pos[i]);

            if(loo[i]){
                ptr.removeChild(cptr[i]);
                numofmoves--;
            }

            else {
                //check tree returns -1 if the new child does not exist at all in anywhere of the tree, or gives the heuristic of the existing one.
                int x = checkTree(getRoot(ptr), pos[i], cptr[i]);
                if (x != -1) {
                    //if the heuristic of the child is more or equal to the previous node, then we should not continue with this child.
                    if (getHeuristic(pos[i]) >= x) {
                        cptr[i].setContinueto(false);
                    }
                }

                //Visualization stuff.
                newGraph.addNode("" + id);
                newGraph.addEdge(""+ide,""+ptrid,""+id);
                cptr[i].setId(id);
                Node n = newGraph.getNode("" + id);
                n.addAttribute("ui.label",""+(Puzzle) (cptr[i].getData()) + "/" + getHeuristic((Puzzle) (cptr[i].getData())));
                n.setAttribute("ui.style","fill-color: rgb(255,0,0);");
                id++;
                ide++;
            }

        }

        //Firstly, smallest is set as parent's heuristic
        int smallest = getHeuristic((Puzzle)(ptr.getData()));
        //getting the leafs
        getLeafs(ptr);
        //entry id is important for the graph
        int entrid = ptrid;
        boolean flagforloop = true;

        //Finds the smallest leaf since it will continue the search from that leaf.(Using manhattan distance)
        while(flagforloop){
            for(int i = 0; i < newList.size(); i++){

                if(getHeuristic((Puzzle)(newList.get(i).getData())) <= smallest) {
                    ptr = newList.get(i);
                    smallest = getHeuristic((Puzzle) (newList.get(i).getData()));
                    entrid = newList.get(i).getId();
                }

            }
            //If the smallest is an element we should not continue, it picks the next smallest.
            if(!ptr.isContinueto()){
                newList.remove(ptr);
            }
            else{
                flagforloop = false;
            }
        }

        aStarSearch(ptr,entrid);

    }
    public static List<Node1> getLeafs(Node1 ptr){
        if(ptr.isLeaf())
            newList.add(ptr);
        else{
            for(int i = 0;i < ptr.getChildren().size(); i++){
                getLeafs((Node1)ptr.getChildren().get(i));
            }
        }
        return newList;
    }

    //writes into arrays.txt
    public static void writeTofile(String txt){
        try{
            PrintWriter pw = new PrintWriter(txt);
            for(int i = 0; i < 100; i++){
                pw.println(initialStates[i]);
            }
            pw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Reads from arrays.txt
    public static void readfromfile(String txt){
        try{
            FileReader fr = new FileReader(txt);
            BufferedReader br = new BufferedReader(fr);
            for(int i = 0; i < initialStates.length; i++){
                int [][] temp = {{0,0,0},{0,0,0},{0,0,0}};
                initialStates[i] = new Puzzle(temp,0);
                initialStates[i].createFromString(br.readLine());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        //Only runned once in order to get the arrays.
        //createinitialstates();
        //Only used once in order to write the arrays into the file.
        //writeTofile("arrays.txt");

        readfromfile("arrays.txt");
        int h = getHeuristic(initialStates[69]);
        Node1 root = new Node1(initialStates[69],h);
        newGraph.addNode("0");
        long t = System.nanoTime();
        aStarSearch(root,0);
        t = System.nanoTime()-t;
        System.out.println(numofmoves);
        System.out.println(t);
        //Displays the graph
        newGraph.display();


    }
}

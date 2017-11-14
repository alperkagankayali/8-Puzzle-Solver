/* Basic Puzzle structure which we can use in a* search*/

public class Puzzle {
    private int distancetraveled;
    private int[][] puzzle = new int[3][3];
    public Puzzle(int[][] puzzle,int distancetraveled){
        copyContent(puzzle);
        this.distancetraveled = distancetraveled;
    }
    public void copyContent(int [][] puzzlec){
        for(int i = 0; i < puzzlec.length; i++){
            for(int j = 0; j < puzzlec[i].length; j++)
                puzzle[i][j] = puzzlec[i][j];
        }
    }
    public int[] findloc(int key){
        int[] found = {-1,-1};
        for(int i = 0; i < puzzle.length;i++){
            for(int j = 0; j < puzzle[i].length; j++)
                if(puzzle[i][j] == key){
                    found[0] = i;
                    found[1] = j;
                }
        }
        return found;
    }

    public void setDistancetraveled(int distancetraveled) {
        this.distancetraveled = distancetraveled;
    }

    public void swap(int key1, int key2, int key3, int key4){
        int temp = puzzle[key1][key2];
        puzzle[key1][key2] = puzzle[key3][key4];
        puzzle[key3][key4] = temp;
        distancetraveled++;
    }
    public void moveUp(int key){
        int[] keyloc = findloc(key);
        if((keyloc[0] == -1 && keyloc[1] == -1) || keyloc[0] == 0)
            return;
        else{
            swap(keyloc[0],keyloc[1],keyloc[0]-1,keyloc[1]);
        }
    }
    public void moveDown(int key){
        int[] keyloc = findloc(key);
        if((keyloc[0] == -1 && keyloc[1] == -1) || keyloc[0] == 2)
            return;
        else{
            swap(keyloc[0],keyloc[1],keyloc[0]+1,keyloc[1]);
        }
    }
    public void moveLeft(int key){
        int[] keyloc = findloc(key);
        if((keyloc[0] == -1 && keyloc[1] == -1) || keyloc[1] == 0)
            return;
        else{
            swap(keyloc[0],keyloc[1],keyloc[0],keyloc[1]-1);
        }
    }
    public void moveRight(int key){
        int[] keyloc = findloc(key);
        if((keyloc[0] == -1 && keyloc[1] == -1) || keyloc[1] == 2)
            return;
        else{
            swap(keyloc[0],keyloc[1],keyloc[0],keyloc[1]+1);
        }
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int getDistancetraveled() {
        return distancetraveled;
    }
    public String toString(){
        String str = "";
        for(int j = 0; j < 3; j++){
            for(int k = 0; k < 3; k++){
                str = str + puzzle[j][k];
            }
        }
        return str;
    }
    public void createFromString(String arr){
        for(int i = 0; i < arr.length(); i++){
            int x = Integer.parseInt(arr.substring(i,i+1));
            switch(i){
                case 0: puzzle[0][0] = x; break;
                case 1: puzzle[0][1] = x; break;
                case 2: puzzle[0][2] = x; break;
                case 3: puzzle[1][0] = x; break;
                case 4: puzzle[1][1] = x; break;
                case 5: puzzle[1][2] = x; break;
                case 6: puzzle[2][0] = x; break;
                case 7: puzzle[2][1] = x; break;
                case 8: puzzle[2][2] = x; break;
            }
        }
    }
}

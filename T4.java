import java.util.*;
import java.io.*;
import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Paths;

class T4{
   public static void display(ArrayList<Integer> grid, int size){
      for(int i = 0; i < grid.size(); i++){
         if(i % size == 0){
            System.out.println();
         }
         System.out.print("     " + grid.get(i));
      }
      System.out.println();
   }
   public static ArrayList<Integer> playerTurn(ArrayList<Integer> grid, int size, int move){
      if(grid.get(move - 1) == 0){
         grid.set(move - 1, -1);
      }else{
         System.out.println("ERROR! OVERLAP! " + grid.get(move));
      }
      return(grid);
   }
   public static int randomValidMove(ArrayList<Integer> grid) {
       Random rand = new Random(); // Use Random instance for better efficiency
       int move;
       
       // Loop until a valid move (empty space) is found
       do {
           move = rand.nextInt(grid.size()); // Random index within grid range
       } while (grid.get(move) != 0);
       
       return move;
   }
   public static int tommy(ArrayList<Integer> grid, ArrayList<Double> weights){
      if(Math.random() < 0.1){
         return(randomValidMove(grid));
      }   
      ArrayList<Double> Hnode = new ArrayList();
      double total = 0;
      for(int i = 0; i < grid.size(); i++){
         for(int b = 0; b < grid.size(); b++){
            //System.out.println(i + " " + grid.size() + " " + b + " " + weights.size());
            total += grid.get(b) * weights.get(b);
         }
         Hnode.add(total);
      }
      ArrayList<Double> outNode = new ArrayList();
      double total2 = 0;
      for(int i = 0; i < Hnode.size(); i++){
         for(int b = 0; b < Hnode.size(); b++){
            total2 += Hnode.get(b) * weights.get(b + grid.size());
         }
         outNode.add(total2);
      }
      double sigmoid = 0;
      for(int c = 0; c < outNode.size(); c++){
         sigmoid = 1 / (1 + Math.exp(outNode.get(c)));
         outNode.set(c, sigmoid);
      }
      double topnode = 0;
      int topnodespot = 0;
      for(int d = 0; d < outNode.size(); d++){
         if(outNode.get(d) > topnode && grid.get(d) == 0){
            topnode = outNode.get(d);
            topnodespot = d;
         }
      }
      return(topnodespot);
      
   }
   public static int tommy2(ArrayList<Integer> grid, ArrayList<Double> weights){
      if(Math.random() < 0.1){
         return(randomValidMove(grid));
      }   
      ArrayList<Double> Hnode = new ArrayList();
      double total = 0;
      for(int i = 0; i < grid.size(); i++){
         for(int b = 0; b < grid.size(); b++){
            //System.out.println(i + " " + grid.size() + " " + b + " " + weights.size());
            total += grid.get(b) * weights.get(b);
         }
         Hnode.add(total);
      }
      ArrayList<Double> outNode = new ArrayList();
      double total2 = 0;
      for(int i = 0; i < Hnode.size(); i++){
         for(int b = 0; b < Hnode.size(); b++){
            total2 += Hnode.get(b) * weights.get(b + grid.size());
         }
         outNode.add(total2);
      }
      double sigmoid = 0;
      for(int c = 0; c < outNode.size(); c++){
         sigmoid = 1 / (1 + Math.exp(outNode.get(c)));
         outNode.set(c, sigmoid);
      }
      double topnode = 0;
      int topnodespot = 0;
      for(int d = 0; d < outNode.size(); d++){
         if(outNode.get(d) > topnode && grid.get(d) == 0){
            topnode = outNode.get(d);
            topnodespot = d;
         }
      }
      return(topnodespot);
      
   }

   public static ArrayList<Integer> TommyTurn(ArrayList<Integer> grid, int size, int move, ArrayList<Integer> moves, ArrayList<Double> weights){
   boolean picked = false;
      while(picked == false){
            if(grid.get(move) == 0){
               grid.set(move, 1);
               moves.add(move);
               picked = true;
            }else{
               move = tommy(grid, weights);
            }
         }
      return(grid);
   }
   public static ArrayList<Integer> TommyTurn2(ArrayList<Integer> grid, int size, int move, ArrayList<Integer> moves, ArrayList<Double> weights){
   boolean picked = false;
      while(picked == false){
            if(grid.get(move) == 0){
               grid.set(move, -1);
               moves.add(move);
               picked = true;
            }else{
               move = tommy2(grid, weights);
            }
         }
      return(grid);
   }

   public static int winnMove(ArrayList<Integer> grid, int size){
      for (int row = 0; row < size; row++) {
          int left = grid.get(row * size);
          boolean detect = true;
          
          for (int col = 0; col < size; col++) {
              if (grid.get(row * size + col) != left) {
                  detect = false;
                  break;
              }
          }
          if (detect) return left;
      }
      boolean verticalcont;
      for(int i = 0; i < size; i++){
         verticalcont = true;
         for(int a = 0; (a + i) < grid.size(); a = a + size){
            if(grid.get(a + i) == grid.get(i) && verticalcont){
               verticalcont = true;
            }else{verticalcont = false;}
         }
         if(verticalcont){
            return(grid.get(i));
         }
      }
    int first = grid.get(0);
    boolean leftDiagWin = true;
    for (int i = 0; i < size; i++) {
        if (grid.get(i * (size + 1)) != first) {
            leftDiagWin = false;
            break;
        }
    }
    if (leftDiagWin) return first;
    first = grid.get(size - 1);
    boolean rightDiagWin = true;
    for (int i = 0; i < size; i++) {
        if (grid.get((i + 1) * (size - 1)) != first) {
            rightDiagWin = false;
            break;
        }
    }
    if (rightDiagWin) return first;

      return 0;
   }
   public static ArrayList<Double> Learn(ArrayList<Double> weights, ArrayList<Integer> movesMade, int wm, double LR, int size) {
       double rewardMultiplier = wm == 1 ? 1 : (wm == -1 ? -1 : 0.75); 
   
       for (int i = 0; i < size * size; i++) { 
           weights.set(i, weights.get(i) + LR * rewardMultiplier); 
       }
      double gradient = 0.1;
       for (int move : movesMade) {
           weights.set(move + size * size, weights.get(move + size * size) + LR * rewardMultiplier * gradient);
           gradient = gradient + 1;
       }
       double minClamp = -1000;
       double maxClamp = 1000;
       for (int i = 0; i < weights.size(); i++) {
           weights.set(i, Math.max(minClamp, Math.min(maxClamp, weights.get(i))));
       }
   
       return weights;
   }

   public static void recordWeights(ArrayList<Double> weights, int size) {
       String filename = size + ".txt"; // Filename based on board size
   
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
           for (double weight : weights) {
               writer.write(weight + "\n"); // Write each weight on a new line
           }
           System.out.println("Weights saved successfully in " + filename);
       } catch (IOException e) {
           System.out.println("Error writing weights to file: " + e.getMessage());
       }
   }
   public static ArrayList<Double> readWeights(int size) {
       String filename = size + ".txt"; // Load file based on board size
       ArrayList<Double> weights = new ArrayList<>();
   
       if (Files.exists(Paths.get(filename))) { // Check if file exists
           try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
               String line;
               while ((line = reader.readLine()) != null) {
                   weights.add(Double.parseDouble(line)); // Convert and store weight
               }
               System.out.println("Weights loaded successfully from " + filename);
           } catch (IOException e) {
               System.out.println("Error reading weights: " + e.getMessage());
           }
       } else {
           System.out.println("No existing weights file (" + filename + "), generating random weights...");
         for(int w = 0; w < ((size * size) * 2); w++){
            weights.add((-(Math.sqrt(6))/(Math.sqrt(2 * size)) + Math.random() * ((Math.sqrt(6))/(Math.sqrt(2 * size)) - (-(Math.sqrt(6))/(Math.sqrt(2 * size))))));
         }
       }
       return weights;
   }

   public static void main(String[] args){
   
      System.out.println("what square would you like to run?");
      Scanner scanner = new Scanner(System.in);
      int size = scanner.nextInt();
      double lr = 0.001;
      ArrayList<Integer> grid = new ArrayList();
      ArrayList<Double> weights = new ArrayList();
      ArrayList<Double> weights2 = new ArrayList();
      ArrayList<Integer> moves = new ArrayList();
      weights = readWeights(size);
      for(int w = 0; w < ((size * size) * 2); w++){
         weights2.add((-(Math.sqrt(6))/(Math.sqrt(2 * size)) + Math.random() * ((Math.sqrt(6))/(Math.sqrt(2 * size)) - (-(Math.sqrt(6))/(Math.sqrt(2 * size))))));
      }
      int esc = 0;
      while(esc == 0){
         grid.clear();
         for(int i = 0; i < (size * size); i++){
            grid.add(0);
         }
         int wm = 0;
         display(grid, size);
         while(wm == 0){
            System.out.println("move?");
            Scanner scanner2 = new Scanner(System.in);
            int move = scanner2.nextInt();
            grid = playerTurn(grid, size, move);
//             grid = TommyTurn2(grid, size, tommy2(grid, weights2), moves, weights);
            display(grid, size);
            boolean breaked2 = true;
            for(int i = 0; i < grid.size(); i++){
               if(grid.get(i) == 0){
                  breaked2 = false;
               }
            }
            if(breaked2){
               wm = 0;
               System.out.println("break");
               break;
            }
            if(winnMove(grid, size) != 0){
               wm = winnMove(grid, size);
               break;
            }
            grid = TommyTurn(grid, size, tommy(grid, weights), moves, weights);
            display(grid, size);
            wm = winnMove(grid, size);
         }
         System.out.println(wm);
         System.out.println("Before update: " + weights);
         Learn(weights, moves, wm, lr, size);
         System.out.println("After update: " + weights);
         recordWeights(weights, size);
         System.out.println("Before update: " + weights2);
         Learn(weights2, moves, wm, lr, size);
         System.out.println("After update: " + weights2);
         System.out.println("play again? [0 = yes] [1 = no]");
         Scanner playgain = new Scanner(System.in);
         esc = 0;
   }
  }
}
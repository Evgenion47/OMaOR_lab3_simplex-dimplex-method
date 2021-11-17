package simplex;

public class Main {


    public static void main(String[] args) {
        int[] C = new int[]{7, 8, 6};
        int[][] AB = new int[][]{
                {9, 9, 2, 180},
                {4, 3, 2, 120},
                {1, 2, 4, 220}
        };
        SimplexMethod sm = new SimplexMethod(AB, C, true);
        sm.Invoke();
    }
}

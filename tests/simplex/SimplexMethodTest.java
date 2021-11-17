package simplex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class SimplexMethodTest {

    @DisplayName("Метод Invoke")
    @Test()
    void invoke() {
        SimplexData[] input = new SimplexData[]{
                new SimplexData(new int[][]{
                        {9, 9, 2, 180},
                        {4, 3, 2, 120},
                        {1, 2, 4, 220}
                }, new int[]{7, 8, 6}, new Rational(355, 1)),

                new SimplexData(new int[][]{
                        {2, 3, 6, 240},
                        {4, 2, 4, 200},
                        {4, 6, 8, 160}
                }, new int[]{4, 5, 4}, new Rational(160, 1)),
                new SimplexData(new int[][]{
                        {-1, 2, 4},
                        {3, 2, 14}
                }, new int[]{1, -2}, new Rational(14, 3)),
                new SimplexData(new int[][]{
                        {1, 2, 16},
                        {5, 2, 40}
                }, new int[]{2, -14}, new Rational(16, 1)),
                new SimplexData(new int[][]{
                        {1, -1, 2},
                        {2, 1, 6}
                }, new int[]{3, 2}, new Rational(12, 1)),
                new SimplexData(new int[][]{
                        {3, 5, 2, 60},
                        {4, 4, 4, 72},
                        {2, 4, 5, 100}
                }, new int[]{5, 10, 8}, new Rational(160, 1))
        };

        assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
            Rational[] actual = Arrays.stream(input)
                    .map(s -> new SimplexMethod(s.getAB(), s.getC(), true).Invoke())
                    .toArray(Rational[]::new);
            Rational[] expected = Arrays.stream(input)
                    .map(SimplexData::getAns)
                    .toArray(Rational[]::new);

            assertArrayEquals(expected, actual);
        });

    }
}

class SimplexData {
    private final int[][] AB;
    private final int[] C;
    private final Rational ans;

    public int[][] getAB() {
        return AB;
    }

    public int[] getC() {
        return C;
    }

    public Rational getAns() {
        return ans;
    }

    public SimplexData(int[][] AB, int[] C, Rational ans) {
        this.AB = AB;
        this.C = C;
        this.ans = ans;
    }
}
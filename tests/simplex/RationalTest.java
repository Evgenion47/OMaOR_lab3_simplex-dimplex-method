package simplex;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


class BooleanUtils {
    private BooleanUtils() {}

    public static boolean[] listToArray(List<Boolean> list) {
        int length = list.size();
        boolean[] arr = new boolean[length];
        for (int i = 0; i < length; i++)
            arr[i] = list.get(i);
        return arr;
    }

    public static final Collector<Boolean, ?, boolean[]> TO_BOOLEAN_ARRAY
            = Collectors.collectingAndThen(Collectors.toList(), BooleanUtils::listToArray);
}

@DisplayName("Тест дробей")
class RationalTest {
    static Map<Rational, Object[]> ratMap = new HashMap<>();

    @BeforeAll
    static void startUp(){
        Rational r0 = new Rational(1, 2);
        ratMap.put(r0, new Object[]{
                1,
                1,
                2,
                new boolean[] {
                        true,
                        false
                },
                new Rational[] {
                        new Rational(1, 1),
                        new Rational(-1, 4)
                },
                new Rational[] {
                        new Rational(0, 1),
                        new Rational(5, 4)
                },
                new Rational[] {
                        new Rational(1, 4),
                        new Rational(-3, 8)
                },
                new Rational[] {
                        new Rational(1, 1),
                        new Rational(-2, 3)
                },
                "1/2",
                0.5,
                new int[] {
                        0,
                        1
                }
        });

        Rational r1 = new Rational(-3, 4);
        ratMap.put(r1, new Object[] {
                -1,
                3,
                4,
                new boolean[] {
                        false,
                        true
                },
                new Rational[] {
                        new Rational(-1, 4),
                        new Rational(-3, 2)
                },
                new Rational[] {
                        new Rational(-5, 4),
                        new Rational(0, 1)
                },
                new Rational[] {
                        new Rational(-3, 8),
                        new Rational(9, 16)
                },
                new Rational[] {
                        new Rational(-3, 2),
                        new Rational(1, 1)
                },
                "-3/4",
                (-0.75),
                new int[] {
                        -1,
                        0
                }
        });
    }

    @DisplayName("Знак дроби")
    @Test
    void getSign() {
        int[] expected = ratMap.values().stream().mapToInt((s) -> (int)s[0]).toArray();
        int[] actual = ratMap.keySet().stream().mapToInt(Rational::getSign).toArray();
        System.out.println("expected = " + Arrays.toString(expected));
        System.out.println("actual = " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Числитель")
    @Test
    void getN() {
        int[] expected = ratMap.values().stream().mapToInt((s) -> (int)s[1]).toArray();
        int[] actual = ratMap.keySet().stream().mapToInt(Rational::getN).toArray();
        System.out.println("expected = " + Arrays.toString(expected));
        System.out.println("actual = " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Знаменатель")
    @Test
    void getD() {
        int[] expected = ratMap.values().stream().mapToInt((s) -> (int)s[2]).toArray();
        int[] actual = ratMap.keySet().stream().mapToInt(Rational::getD).toArray();
        System.out.println("expected = " + Arrays.toString(expected));
        System.out.println("actual = " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Равенство")
    @Test
    void testEquals() {
        boolean[][] expected = ratMap.values().stream().map((s) -> s[3]).toArray(boolean[][]::new);
        boolean[][] actual = ratMap.keySet().stream().map(
                (s) -> ratMap.keySet().stream().map(s::equals).collect(BooleanUtils.TO_BOOLEAN_ARRAY)
        ).toArray(boolean[][]::new);
        System.out.println("expected = " + Arrays.deepToString(expected));
        System.out.println("actual = " + Arrays.deepToString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Сложение")
    @Test
    void add() {
        Rational[][] expected = ratMap.values().stream().map((s) -> s[4]).toArray(Rational[][]::new);
        Rational[][] actual = ratMap.keySet().stream().map(
                (s) -> ratMap.keySet().stream().map((r) -> Rational.add(s, r)).toArray(Rational[]::new)
        ).toArray(Rational[][]::new);
        System.out.println("expected = " + Arrays.deepToString(expected));
        System.out.println("actual = " + Arrays.deepToString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Вычитание")
    @Test
    void sub() {
        Rational[][] expected = ratMap.values().stream().map((s) -> s[5]).toArray(Rational[][]::new);
        Rational[][] actual = ratMap.keySet().stream().map(
                (s) -> ratMap.keySet().stream().map((r) -> Rational.sub(s, r)).toArray(Rational[]::new)
        ).toArray(Rational[][]::new);
        System.out.println("expected = " + Arrays.deepToString(expected));
        System.out.println("actual = " + Arrays.deepToString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Умножение")
    @Test
    void mul() {
        Rational[][] expected = ratMap.values().stream().map((s) -> s[6]).toArray(Rational[][]::new);
        Rational[][] actual = ratMap.keySet().stream().map(
                (s) -> ratMap.keySet().stream().map((r) -> Rational.mul(s, r)).toArray(Rational[]::new)
        ).toArray(Rational[][]::new);
        System.out.println("expected = " + Arrays.deepToString(expected));
        System.out.println("actual = " + Arrays.deepToString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Деление")
    @Test
    void div() {
        Rational[][] expected = ratMap.values().stream().map((s) -> s[7]).toArray(Rational[][]::new);
        Rational[][] actual = ratMap.keySet().stream().map(
                (s) -> ratMap.keySet().stream().map((r) -> Rational.div(s, r)).toArray(Rational[]::new)
        ).toArray(Rational[][]::new);
        System.out.println("expected = " + Arrays.deepToString(expected));
        System.out.println("actual = " + Arrays.deepToString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Перевод в строку")
    @Test
    void testToString() {
        String[] expected = ratMap.values().stream().map((s) -> s[8]).toArray(String[]::new);
        String[] actual = ratMap.keySet().stream().map(Rational::toString).toArray(String[]::new);
        System.out.println("expected = " + Arrays.toString(expected));
        System.out.println("actual = " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Перевод в double")
    @Test
    void toDouble() {
        double[] expected = ratMap.values().stream().mapToDouble((s) -> (double) s[9]).toArray();
        double[] actual = ratMap.keySet().stream().mapToDouble(Rational::toDouble).toArray();
        System.out.println("expected = " + Arrays.toString(expected));
        System.out.println("actual = " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @DisplayName("Сравнение")
    @Test
    void compare() {
        int[][] expected = ratMap.values().stream().map((s) -> s[10]).toArray(int[][]::new);
        int[][] actual = ratMap.keySet().stream().map(
                (s) -> ratMap.keySet().stream().mapToInt((r) -> Rational.compare(s, r)).toArray()
                ).toArray(int[][]::new);
        System.out.println("expected = " + Arrays.deepToString(expected));
        System.out.println("actual = " + Arrays.deepToString(actual));
        assertArrayEquals(expected, actual);
    }
}
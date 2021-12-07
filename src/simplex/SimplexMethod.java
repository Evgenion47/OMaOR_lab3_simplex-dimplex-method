package simplex;

import UTFE.TableOutput.Table;
import java.util.ArrayList;

public class SimplexMethod {


    int n; //число переменных
    int m; //число уравнений
    int[] bInput;
    int[] cInput;
    int[][] aInput;
    boolean print = false;

    public SimplexMethod(int[][] AB, int[] C) {
        n = C.length;
        m = AB.length;
        cInput = C;
        aInput = new int[m][n];
        bInput = new int[m];
        for (int i = 0; i < m; ++i) {
            bInput[i] = AB[i][n];
            System.arraycopy(AB[i], 0, aInput[i], 0, n);
        }
    }

    public SimplexMethod(int[][] AB, int[] C, boolean print) {
        this(AB, C);
        this.print = print;
    }

    public Double Invoke() {
        System.out.println("===================================================================");

        //вывод целевой функции
        StringBuilder sb = new StringBuilder("F(X) = ");
        for (int i = 0; i < n; ++i) {
            sb.append(cInput[i]).append("*X").append((i + 1));
            if (i != n - 1) sb.append(" + ");
        }
        sb.append(" --> max");
        if (print) System.out.println(sb);

        //вывод систмы ограничений
        char up = '┏';
        char mid = '┃';
        char down = '┗';
        sb = new StringBuilder();
        sb.append(up).append("\n");
        for (int i = 0; i < n; ++i) {
            sb.append(mid).append(" ");
            for (int j = 0; j < n; j++) {
                sb.append(aInput[i][j]).append("*X").append((j + 1));
                if (j != n - 1) sb.append(" + ");
            }
            sb.append(" <= ").append(bInput[i]).append("\n");
        }
        sb.append(mid).append(" ");
        sb.append("Xi >= 0, i = 1...").append(n).append("\n");
        sb.append(down).append("\n");
        if (print) System.out.println(sb);

        //увеличиваем массив c, т.к. добавили перменные
        int[] c = new int[n * 2];
        System.arraycopy(cInput, 0, c, 0, n);

        //заполянем массив a
        Double[][] a = new Double[n][n * 2];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                a[i][j] = (double)aInput[i][j];
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = n; j < n * 2; ++j) {
                a[i][j] = 0.0;
                if (i == j - n) a[i][j] = 1.0;
            }
        }

        //заполняем столбец базовых переменных
        int[] basisNumbers = new int[n];
        for (int i = 0; i < n; ++i) basisNumbers[i] = i + n + 1;

        //создаем столбец Cp/Cj
        int[] CpCj = new int[n];

        //переменная номера плана
        int planNumber = -1;

        //заполняем столбец y0
        Double[] y0 = new Double[n];
        for (int i = 0; i < n; ++i) y0[i] = (double)bInput[i];

        //создаем массивы min, delta и epsilon
        Double[] minArray = new Double[n];
        Double[] delta = new Double[n * 2];
        Double[] epsilon = new Double[n * 2 + 1];

        //переменные для опорного столбца и строки
        int column = 0;
        int line = 0;

        while (true) {
            if (print) System.out.printf("План X%d:\n", ++planNumber);

            column = 0;
            line = 0;

            //заводим таблицу для вывода и делаем шапку
            ArrayList<Object[]> table = new ArrayList<>();
            Object[] head = new Object[2 * n + 4];
            head[0] = "Cp/Cj";
            head[1] = "БП";
            head[2] = "y0";
            for (int i = 0; i < 2 * n; ++i) {
                head[i + 3] = "y" + (i + 1);
            }
            head[head.length - 1] = "min";
            table.add(head);

            //считаем дельту
            boolean isDeltaNegativeOrNull = true;
            for (int j = 0; j < n * 2; ++j) {
                double sum = 0;
                for (int i = 0; i < n; ++i) {
                    sum = sum+CpCj[i]*a[i][j];
                }
                delta[j] = c[j]-sum;
                if (Double.compare(delta[j], 0) > 0) isDeltaNegativeOrNull = false;
            }

            //собираем строку функции текущего плана
            sb = new StringBuilder("F(X" + planNumber + ") = ");
            double fx = 0;
            for (int i = 0; i < 2 * n; ++i) {
                int tmp = -1;
                for (int j = 0; j < n; ++j) {
                    if (basisNumbers[j] - 1 == i) {
                        tmp = j;
                        break;
                    }
                }
                Double y = (double)0;
                if (tmp != -1) y = y0[tmp];
                sb.append(String.format("%d*%s", c[i], y.toString()));
                if (i != 2 * n - 1) sb.append(" + ");
                fx = fx + (y * c[i]);
            }

            //выход из цикла, если все дельты не положительные
            if (isDeltaNegativeOrNull) {

                //вывод последней неполной таблицы
                for (int i = 0; i < n; ++i) {
                    Object[] tableLine = new Object[2 * n + 4];
                    tableLine[0] = CpCj[i];
                    tableLine[1] = String.format("X%d", basisNumbers[i]);
                    tableLine[2] = y0[i];
                    System.arraycopy(a[i], 0, tableLine, 3, 2 * n);
                    tableLine[2 * n + 3] = " ";
                    table.add(tableLine);
                }
                Object[] deltaLine = new Object[2 * n + 4];
                for (int i = 0; i < 2 * n + 3; ++i) {
                    if (i < 3) deltaLine[i] = " ";
                    else deltaLine[i] = delta[i - 3];
                }
                deltaLine[1] = "Δ";
                table.add(deltaLine);

                if (print) System.out.println(Table.TableToString(table.toArray(Object[][]::new)));

                //вывод последнего плана
                if (print) {
                    System.out.println(sb);
                    System.out.println("F(X" + planNumber + ") = " + fx);
                    System.out.println("Answer: " + fx);
                }

                return fx;
            }

            //ищем опорный столбец по delta
            Double max = delta[0];
            for (int i = 0; i < n * 2; ++i) {
                if (Double.compare(max, delta[i]) < 0) {
                    max = delta[i];
                    column = i;
                }
            }

            //считаем столбец min
            for (int i = 0; i < n; ++i)
                if (Double.compare(a[i][column], 0) == 0) {
                    minArray[i] = -1.0;
                } else {
                    minArray[i] = y0[i]/ a[i][column];
                }

            //ищем опорную строку по min
            Double min = Double.MAX_VALUE;
            for (int i = 0; i < n; ++i) {
                if (Double.compare(min, minArray[i]) > 0 && Double.compare(minArray[i], 0) > 0) {
                    min = minArray[i];
                    line = i;
                }
            }

            //считаем epsilon
            epsilon[0] = y0[line]/ a[line][column];
            for (int i = 0; i < n * 2; ++i) {
                epsilon[i + 1] = a[line][i]/ a[line][column];
            }

            //переписываем все в таблицу
            for (int i = 0; i < n; ++i) {
                Object[] tableLine = new Object[2 * n + 4];
                tableLine[0] = CpCj[i];
                tableLine[1] = String.format("X%d", basisNumbers[i]);
                tableLine[2] = y0[i];
                System.arraycopy(a[i], 0, tableLine, 3, 2 * n);
                tableLine[2 * n + 3] = minArray[i]< 0 ? "-" : minArray[i];
                table.add(tableLine);
            }
            Object[] deltaLine = new Object[2 * n + 4];
            Object[] epsLine = new Object[2 * n + 4];
            for (int i = 0; i < 2 * n + 3; ++i) {
                if (i < 3) deltaLine[i] = " ";
                else deltaLine[i] = delta[i - 3];
                if (i < 2) epsLine[i] = " ";
                else epsLine[i] = epsilon[i - 2];
            }
            epsLine[2 * n + 3] = deltaLine[2 * n + 3] = " ";
            deltaLine[1] = "Δ";
            epsLine[1] = "ε";
            table.add(deltaLine);
            table.add(epsLine);
            if (print) System.out.println(Table.TableToString(table.toArray(Object[][]::new)));

            //вывод плана
            if (print) {
                System.out.println(sb);
                System.out.println("F(X" + planNumber + ") = " + fx);
                System.out.println();
            }

            //временные массивы для новой таблицы
            Double[][] aNew = new Double[n][n * 2];
            Double[] y0New = new Double[n];
            int[] bNumbersNew = new int[n];
            int[] cpcjNew = new int[n];

            //строим стобец БП
            System.arraycopy(basisNumbers, 0, bNumbersNew, 0, n);
            bNumbersNew[line] = column + 1;

            //строим столбец Cp/Cj
            for (int i = 0; i < n; ++i) cpcjNew[i] = c[bNumbersNew[i] - 1];

            //строим столбец y0 и матрицу a
            for (int i = 0; i < n; ++i) {
                y0New[i] = y0[i]-y0[line]*a[i][column]/ a[line][column];
                for (int j = 0; j < n * 2; ++j) {
                    aNew[i][j] = a[i][j]-a[line][j]*a[i][column]/ a[line][column];
                    if (j == column) aNew[i][j] = 0.0;
                }
            }
            y0New[line] = epsilon[0];

            //копируем все в исходные массивы
            System.arraycopy(epsilon, 1, aNew[line], 0, 2 * n);
            System.arraycopy(aNew, 0, a, 0, n);
            System.arraycopy(y0New, 0, y0, 0, n);
            System.arraycopy(bNumbersNew, 0, basisNumbers, 0, n);
            System.arraycopy(cpcjNew, 0, CpCj, 0, n);
        }
    }
}
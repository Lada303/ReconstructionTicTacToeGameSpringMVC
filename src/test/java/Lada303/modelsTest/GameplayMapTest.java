package Lada303.modelsTest;

import Lada303.services.gameplay.gameplaymap.Cell;
import Lada303.services.gameplay.gameplaymap.GameplayMap;
import Lada303.utils.enums.Dots;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameplayMapTest {

    private static final GameplayMap gameMap33 = new GameplayMap(3, 3);
    private static final GameplayMap gameMap53 = new GameplayMap(5, 3);
    private static final GameplayMap gameMap55 = new GameplayMap(5, 5);

    @ParameterizedTest
    @MethodSource("dataForTestIsCellValid")
    public void testIsCellValid(GameplayMap gameMap, boolean result, int x, int y) {
        Assertions.assertEquals(result, gameMap.isCellValid(x, y));
    }
    public static Stream<Arguments> dataForTestIsCellValid() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, true, 0, 0));
        out.add(Arguments.arguments(gameMap33, true, 1, 1));
        out.add(Arguments.arguments(gameMap33, true, 2, 2));
        out.add(Arguments.arguments(gameMap33, false, 3, 3));
        out.add(Arguments.arguments(gameMap33, false, 3, 1));

        out.add(Arguments.arguments(gameMap53, true, 3, 2));
        out.add(Arguments.arguments(gameMap53, true, 4, 1));
        out.add(Arguments.arguments(gameMap53, false, 5, 0));
        out.add(Arguments.arguments(gameMap53, false, 3, 3));
        out.add(Arguments.arguments(gameMap53, false, 5, 3));

        out.add(Arguments.arguments(gameMap55, true, 3, 3));
        out.add(Arguments.arguments(gameMap55, true, 4, 4));
        out.add(Arguments.arguments(gameMap55, false, 3, 5));
        out.add(Arguments.arguments(gameMap55, false, 5, 5));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetCountRow")
    public void testGetCountRow(GameplayMap gameMap, int result) {
        Assertions.assertEquals(result, gameMap.getCountRow());
    }
    public static Stream<Arguments> dataForTestGetCountRow() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, 3));
        out.add(Arguments.arguments(gameMap53, 3));
        out.add(Arguments.arguments(gameMap55, 5));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetCountColumn")
    public void testGetCountColumn(GameplayMap gameMap, int result) {
        Assertions.assertEquals(result, gameMap.getCountColumn());
    }
    public static Stream<Arguments> dataForTestGetCountColumn() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, 3));
        out.add(Arguments.arguments(gameMap53, 5));
        out.add(Arguments.arguments(gameMap55, 5));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetCell1")
    public void testGetCell1(GameplayMap gameMap, Cell result, int x, int y) {
        Assertions.assertEquals(result, gameMap.getCell(x, y));
    }
    public static Stream<Arguments> dataForTestGetCell1() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, new Cell(0, 0), 0, 0));
        out.add(Arguments.arguments(gameMap53, new Cell(4, 2), 4, 2));
        out.add(Arguments.arguments(gameMap55, new Cell(4, 4), 4, 4));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetCell2")
    public void testGetCell2(GameplayMap gameMap, Cell result) {
        Assertions.assertEquals(result, gameMap.getCell(result));
    }
    public static Stream<Arguments> dataForTestGetCell2() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, new Cell(0, 0)));
        out.add(Arguments.arguments(gameMap53, new Cell(4, 2)));
        out.add(Arguments.arguments(gameMap55, new Cell(4, 4)));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetRow")
    public void testGetRow(GameplayMap gameMap, Cell cell, Cell[] result) {
        Assertions.assertArrayEquals(result, gameMap.getRow(cell));
    }
    public static Stream<Arguments> dataForTestGetRow() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, new Cell(0, 0),
                new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)}));
        out.add(Arguments.arguments(gameMap33, new Cell(0, 1),
                new Cell[]{new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)}));
        out.add(Arguments.arguments(gameMap33, new Cell(1, 2),
                new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}));

        out.add(Arguments.arguments(gameMap53, new Cell(4, 2),
                new Cell[]{new Cell(0, 2), new Cell(1, 2), new Cell(2, 2), new Cell(3, 2), new Cell(4, 2)}));

        out.add(Arguments.arguments(gameMap55, new Cell(0, 0),
                new Cell[]{new Cell(0, 0), new Cell(1, 0), new Cell(2, 0), new Cell(3, 0), new Cell(4, 0)}));
        out.add(Arguments.arguments(gameMap55, new Cell(3, 3),
                new Cell[]{new Cell(0, 3), new Cell(1, 3), new Cell(2, 3), new Cell(3, 3), new Cell(4, 3)}));
        out.add(Arguments.arguments(gameMap55, new Cell(2, 4),
                new Cell[]{new Cell(0, 4), new Cell(1, 4), new Cell(2, 4), new Cell(3, 4), new Cell(4, 4)}));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetColumn")
    public void testGetColumn(GameplayMap gameMap, Cell cell, Cell[] result) {
        Assertions.assertArrayEquals(result, gameMap.getColumn(cell));
    }
    public static Stream<Arguments> dataForTestGetColumn() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, new Cell(0, 0),
                new Cell[]{new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)}));
        out.add(Arguments.arguments(gameMap33, new Cell(1, 1),
                new Cell[]{new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)}));
        out.add(Arguments.arguments(gameMap33, new Cell(2, 2),
                new Cell[]{new Cell(2, 0), new Cell(2, 1), new Cell(2, 2)}));

        out.add(Arguments.arguments(gameMap53, new Cell(4, 2),
                new Cell[]{new Cell(4, 0), new Cell(4, 1), new Cell(4, 2)}));


        out.add(Arguments.arguments(gameMap55, new Cell(3, 3),
                new Cell[]{new Cell(3, 0), new Cell(3, 1), new Cell(3, 2), new Cell(3, 3), new Cell(3, 4)}));
        out.add(Arguments.arguments(gameMap55, new Cell(4, 4),
                new Cell[]{new Cell(4, 0), new Cell(4, 1), new Cell(4, 2), new Cell(4, 3), new Cell(4, 4)}));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetD1")
    public void testGetD1(GameplayMap gameMap, Cell cell, Cell[] result) {
        Assertions.assertArrayEquals(result, gameMap.getD1(cell));
    }
    public static Stream<Arguments> dataForTestGetD1() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, new Cell(0, 0),
                new Cell[]{new Cell(0, 0), new Cell(1, 1), new Cell(2, 2)}));
        out.add(Arguments.arguments(gameMap33, new Cell(0, 1),
                new Cell[]{null, new Cell(0, 1), new Cell(1, 2)}));
        out.add(Arguments.arguments(gameMap33, new Cell(0, 2),
                new Cell[]{null, null, new Cell(0, 2)}));

        out.add(Arguments.arguments(gameMap53, new Cell(4, 2),
                new Cell[]{new Cell(2, 0), new Cell(3, 1), new Cell(4, 2)}));
        out.add(Arguments.arguments(gameMap53, new Cell(4, 1),
                new Cell[]{new Cell(3, 0), new Cell(4, 1), null}));
        out.add(Arguments.arguments(gameMap53, new Cell(4, 0),
                new Cell[]{new Cell(4, 0), null, null}));

        out.add(Arguments.arguments(gameMap55, new Cell(2, 4),
                new Cell[]{null, null, new Cell(0, 2), new Cell(1, 3), new Cell(2, 4)}));
        out.add(Arguments.arguments(gameMap55, new Cell(4, 2),
                new Cell[]{new Cell(2, 0), new Cell(3, 1), new Cell(4, 2), null, null}));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetD2")
    public void testGetD2(GameplayMap gameMap, Cell cell, Cell[] result) {
        Assertions.assertArrayEquals(result, gameMap.getD2(cell));
    }
    public static Stream<Arguments> dataForTestGetD2() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, new Cell(0, 0),
                new Cell[]{new Cell(0, 0), null, null}));
        out.add(Arguments.arguments(gameMap33, new Cell(0, 1),
                new Cell[]{new Cell(1, 0), new Cell(0, 1), null}));
        out.add(Arguments.arguments(gameMap33, new Cell(0, 2),
                new Cell[]{new Cell(2, 0), new Cell(1, 1), new Cell(0, 2)}));

        out.add(Arguments.arguments(gameMap53, new Cell(4, 2),
                new Cell[]{null, null, new Cell(4, 2)}));
        out.add(Arguments.arguments(gameMap53, new Cell(4, 1),
                new Cell[]{null, new Cell(4, 1), new Cell(3, 2)}));
        out.add(Arguments.arguments(gameMap53, new Cell(4, 0),
                new Cell[]{new Cell(4, 0), new Cell(3, 1), new Cell(2, 2)}));


        out.add(Arguments.arguments(gameMap55, new Cell(2, 4),
                new Cell[]{null, null, new Cell(4, 2), new Cell(3, 3), new Cell(2, 4)}));
        out.add(Arguments.arguments(gameMap55, new Cell(0, 2),
                new Cell[]{new Cell(2, 0), new Cell(1, 1), new Cell(0, 2), null, null}));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestIsD1")
    public void testIsD1(GameplayMap gameMap, boolean result, Cell cell, int length) {
        Assertions.assertEquals(result, gameMap.isD1(cell, length));
    }
    public static Stream<Arguments> dataForTestIsD1() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, true, new Cell(0, 0), 3));
        out.add(Arguments.arguments(gameMap33, true, new Cell(1, 1), 3));
        out.add(Arguments.arguments(gameMap33, true, new Cell(2, 2), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(0, 2), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(2, 0), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(1, 0), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(0, 1), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(1, 2), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(2, 1), 3));

        out.add(Arguments.arguments(gameMap55, true, new Cell(0, 0), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(0, 1), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(1, 0), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(1, 1), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(1, 2), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(2, 1), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(2, 2), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(2, 3), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(3, 2), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(3, 3), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(3, 4), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(4, 3), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(4, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(0, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(0, 3), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(0, 2), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(1, 3), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(1, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(2, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(2, 0), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(3, 0), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(3, 1), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(4, 0), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(4, 1), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(4, 2), 4));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestIsD2")
    public void testIsD2(GameplayMap gameMap, boolean result, Cell cell, int length) {
        Assertions.assertEquals(result, gameMap.isD2(cell, length));
    }
    public static Stream<Arguments> dataForTestIsD2() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, true, new Cell(0, 2), 3));
        out.add(Arguments.arguments(gameMap33, true, new Cell(1, 1), 3));
        out.add(Arguments.arguments(gameMap33, true, new Cell(2, 0), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(0, 0), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(2, 2), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(1, 0), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(0, 1), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(1, 2), 3));
        out.add(Arguments.arguments(gameMap33, false, new Cell(2, 1), 3));

        out.add(Arguments.arguments(gameMap55, true, new Cell(0, 3), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(0, 4), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(1, 2), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(1, 3), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(1, 4), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(2, 1), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(2, 2), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(2, 3), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(3, 0), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(3, 1), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(3, 2), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(4, 0), 4));
        out.add(Arguments.arguments(gameMap55, true, new Cell(4, 1), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(0, 0), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(0, 1), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(0, 2), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(1, 0), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(1, 1), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(2, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(2, 0), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(3, 3), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(3, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(4, 4), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(4, 3), 4));
        out.add(Arguments.arguments(gameMap55, false, new Cell(4, 2), 4));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestGetSize")
    public void testGetSize(GameplayMap gameMap, String result) {
        Assertions.assertEquals(result, gameMap.getSize());
    }
    public static Stream<Arguments> dataForTestGetSize() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, "3*3"));
        out.add(Arguments.arguments(gameMap53, "5*3"));
        out.add(Arguments.arguments(gameMap55, "5*5"));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestMapAsString")
    public void testMapAsString(GameplayMap gameMap, List<String> result) {
        Assertions.assertLinesMatch(result, gameMap.mapAsString());
    }
    public static Stream<Arguments> dataForTestMapAsString() {
        List<String> result33= new ArrayList<>();
        result33.add("_|1|2|3|");
        result33.add("1|_|_|_|");
        result33.add("2|_|_|_|");
        result33.add("3|_|_|_|");
        result33.add("---------");

        gameMap53.getCell(2, 1).setDot(Dots.X);
        List<String> result53= new ArrayList<>();
        result53.add("_|1|2|3|4|5|");
        result53.add("1|_|_|_|_|_|");
        result53.add("2|_|_|x|_|_|");
        result53.add("3|_|_|_|_|_|");
        result53.add("---------");

        gameMap55.getCell(4, 4).setDot(Dots.O);
        gameMap55.getCell(0, 0).setDot(Dots.O);
        gameMap55.getCell(2, 2).setDot(Dots.X);
        List<String> result55= new ArrayList<>();
        result55.add("_|1|2|3|4|5|");
        result55.add("1|o|_|_|_|_|");
        result55.add("2|_|_|_|_|_|");
        result55.add("3|_|_|x|_|_|");
        result55.add("4|_|_|_|_|_|");
        result55.add("5|_|_|_|_|o|");
        result55.add("---------");

        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(gameMap33, result33));
        out.add(Arguments.arguments(gameMap53, result53));
        out.add(Arguments.arguments(gameMap55, result55));
        return out.stream();
    }


}

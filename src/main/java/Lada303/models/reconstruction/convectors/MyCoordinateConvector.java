package Lada303.models.reconstruction.convectors;
/*
Получает строку и возвращает координыта ячейки в нужном формате - массив из двух целых чисел Х и Y
Переводит в координаты строки:
- координаты представляют собой числа из 1 знака от 1..9
- два числа записаны подря без разделителей
- два числа разделенных любыми символами
- если поле 3*3 и ячейки записаны под номерами 1..9
*/

public class MyCoordinateConvector implements CoordinateConvector {

    public int[] mapCoordinateConvector(String strMapCoordinate) {
        String str;
        if (strMapCoordinate.length() == 1) {
            str = convector1(strMapCoordinate);
        } else {
            str = strMapCoordinate.replaceAll("[^0-9]+", "");
        }
        int[] xy = new int[2];
        xy[0] = Integer.parseInt(str.substring(0, 1)) - 1;
        xy[1] = Integer.parseInt(str.substring(1)) - 1;
        return xy;
    }

    private String convector1 (String strMapCoordinate) {
        switch (strMapCoordinate) {
            case "1" -> {return "11";}
            case "2" -> {return "21";}
            case "3" -> {return "31";}
            case "4" -> {return "12";}
            case "5" -> {return "22";}
            case "6" -> {return "32";}
            case "7" -> {return "13";}
            case "8" -> {return "23";}
            case "9" -> {return "33";}
        }
        return "";
    }
}

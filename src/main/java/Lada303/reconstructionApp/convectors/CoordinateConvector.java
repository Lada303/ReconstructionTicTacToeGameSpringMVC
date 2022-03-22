package Lada303.reconstructionApp.convectors;

/*
Общия интрефейс для создания конвекторов координат ячеки
Получает строку и возвращает координыта ячейки в нужном формате - массив из двух целых чисел Х и Y
*/
public interface CoordinateConvector {
    int[] mapCoordinateConvector(String strMapCoordinate);
}

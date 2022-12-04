// Класс, представляющий доску.
public class Board {

    // Вспомогательный класс - поле доски.
    private class Field {
        private FieldType fieldType;

        public Field() {
            fieldType = FieldType.EMPTY;
        }

        public void ChangeColor() {
            if (fieldType == FieldType.EMPTY) {
                return;
            }
            if (fieldType == FieldType.BLACK) {
                fieldType = FieldType.WHITE;
            } else {
                fieldType = FieldType.BLACK;
            }
        }

        public void SetColor(FieldType color) {
            fieldType = color;
        }

        public FieldType GetColor() {
            return fieldType;
        }

        public FieldType GetReversedColor() {
            if (fieldType == FieldType.EMPTY) {
                return FieldType.EMPTY;
            } else if (fieldType == FieldType.BLACK) {
                return FieldType.WHITE;
            } else {
                return FieldType.BLACK;
            }
        }

        public boolean isEmpty() {
            return fieldType == FieldType.EMPTY;
        }
    }

    private Field[][] board = new Field[8][8];
    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Field();
            }
        }
        board[3][3].SetColor(FieldType.WHITE);
        board[4][3].SetColor(FieldType.BLACK);
        board[3][4].SetColor(FieldType.BLACK);
        board[4][4].SetColor(FieldType.WHITE);
    }

    // Метод отрисовки доски в консоли.
    public void DrawBoard() {
        char whiteChip = '\u26AA';
        char blackChip = '\u26AB';
        char prediction = '\u2662';
        char verticalBorder = '|';
        String horizontalBorder = "    " + "\u2014\u2014\u2014\u2014 ".repeat(8);
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        System.out.println(horizontalBorder);
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + (i + 1) + " ");
            System.out.print(verticalBorder);
            for (int j = 0; j < 8; j++) {
                System.out.print(" ");
                if (board[i][j].GetColor() == FieldType.WHITE) {
                    System.out.print(whiteChip);
                } else if (board[i][j].GetColor() == FieldType.BLACK) {
                    System.out.print(blackChip);
                } else {
                    System.out.print("  ");
                }
                System.out.print(" ");
                System.out.print(verticalBorder);
            }
            System.out.println();
            System.out.println(horizontalBorder);
        }
        System.out.print("    ");
        for (int i = 0; i < 8; i++) {
            System.out.print(" ");
            System.out.print(letters[i]);
            System.out.print("   ");
        }
        System.out.println();
    }

    // Метод совершения хода.
    public void MakeMove(FieldType playerColor, int x, int y) {
        // Проверка корректности хода.
        if (!CheckMove(playerColor, x, y)) {
            throw new IllegalArgumentException();
        }
        board[y][x].SetColor(playerColor);
        // Перекрашиваем "замкнутые" фишки по всем направлениям.
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (CheckDirection(playerColor, x, y, i, j)) {
                    for (int k = 1; (isCorrectCoord(x + j * k) && isCorrectCoord(y + i * k)); k++) {
                        if (board[y + i * k][x + j * k].GetColor() == playerColor) {
                            break;
                        } else {
                            board[y + i * k][x + j * k].SetColor(board[y + i * k][x + j * k].GetReversedColor());
                        }
                    }
                }
            }
        }
    }

    public FieldType GetFieldType(int x, int y) {
        if (!isCorrectCoord(x) || !isCorrectCoord(y)) {
            throw new IllegalArgumentException();
        }
        return board[y][x].GetColor();
    }

    public void SetFieldType(FieldType type, int x, int y) {
        board[x][y].SetColor(type);
    }

    // Проверка доступности поля для совершения хода.
    public boolean fieldIsAvailable(FieldType playerColor, int x, int y) {
        return CheckMove(playerColor, x, y);
    }

    // Проверка доступности поля для совершения хода.
    private boolean CheckMove(FieldType playerColor, int x, int y) {
        if (!isCorrectCoord(x) || !isCorrectCoord(y)) {
            return false;
        }
        if (board[y][x].GetColor() != FieldType.EMPTY) {
            return false;
        }
        boolean res = false;

        // проверка по вертикали вверх
        res = res || CheckDirection(playerColor, x, y, -1, 0);
        // по вертикали вниз
        res = res || CheckDirection(playerColor, x, y, 1, 0);
        // по горизонтали вправо
        res = res || CheckDirection(playerColor, x, y, 0, 1);
        // по горизонтали влево
        res = res || CheckDirection(playerColor, x, y, 0, -1);
        // по диагонали вправо вверх
        res = res || CheckDirection(playerColor, x, y, 1, -1);
        // по диагонали вправо вниз
        res = res || CheckDirection(playerColor, x, y, 1, 1);
        // по диагонали влево вверх
        res = res || CheckDirection(playerColor, x, y, -1, -1);
        // по диагонали влево вниз
        res = res || CheckDirection(playerColor, x, y, -1, 1);

        return res;
    }

    private boolean isCorrectCoord(int coord) {
        return (coord >= 0 && coord <= 7);
    }

    // Метод проверки направления на возможность постановки на нём фишки.
    // x - координата x, y - координата y, vert и hor - 0, 1 или -1, в каком
    // направлении двигаться по вертикали/горизонтали (0 - никуда, 1 - вперёд,
    // -1 - назад)
    private boolean CheckDirection(FieldType color, int x, int y, int vertical, int horizontal) { // 0 1
        if (!isCorrectCoord(x + horizontal) || !isCorrectCoord(y + vertical)) {
            return false;
        }
        if (board[y + vertical][x + horizontal].GetReversedColor() == color) {
            for (int i = 1; (isCorrectCoord(x + horizontal * i) && isCorrectCoord(y + vertical * i)); i++) {
                if (board[y + vertical * i][x + horizontal * i].GetColor() == color) {
                    return true;
                } else if (board[y + vertical * i][x + horizontal * i].GetColor() == FieldType.EMPTY) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isAvailableDirection(FieldType color, int x, int y, int vertical, int horizontal) {
        return CheckDirection(color, x, y, vertical, horizontal);
    }

    public Board CopyBoard() {
        var result = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result.board[i][j] = new Field();
                result.board[i][j].SetColor(this.board[i][j].GetColor());
            }
        }
        return result;
    }

    // Сброс доски к начальному состоянию.
    public void ResetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].SetColor(FieldType.EMPTY);
            }
        }
        board[3][3].SetColor(FieldType.WHITE);
        board[4][3].SetColor(FieldType.BLACK);
        board[3][4].SetColor(FieldType.BLACK);
        board[4][4].SetColor(FieldType.WHITE);
    }

}

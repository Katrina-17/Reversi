// Компьютерный игрок, учитывающий возможные ответные ходы противника.
public final class ComputerPlayerPro extends ComputerPlayer {

    public ComputerPlayerPro(Board board, FieldType color) {
        this.board = board;
        this.color = color;
    }

    // Оценка хода с учётом возможных ответных ходов противника.
    protected double GetMoveEstimation(int x, int y) {
        // Сохранение текущей доски.
        var oldBoard = board;
        board = oldBoard.CopyBoard();

        // Совершение предполагаемого хода (x, y) на временной доске
        double result = GetMoveEstimationNaive(x, y);
        try {
            board.MakeMove(color, x, y);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error in prediction.");
        }

        // Вычисление оценки наилучшего хода противника с учётом сделанного на позицию (x, y) хода.
        FieldType opponentColor = (color == FieldType.BLACK) ? FieldType.WHITE : FieldType.BLACK;
        double maxPlayerEstimation = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board.fieldIsAvailable(opponentColor, i, j)) {
                    double currentRes = GetMoveEstimationNaive(i, j);
                    if (currentRes > maxPlayerEstimation) {
                        maxPlayerEstimation = currentRes;
                    }
                }
            }
        }
        // Окончательная оценка хода компьютера на (x, y)
        result -= maxPlayerEstimation;
        // Возвращение доски в изначальное состояние.
        board = oldBoard;
        return result;
    }


    // Оценка хода на (x, y) без учёта возможных ответных ходов (используется для
    // получения продвинутой оценки).
    private double GetMoveEstimationNaive(int x, int y) {
        double result = 0;
        int nTurnedFields = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (board.isAvailableDirection(color, x, y, i, j)) {
                    for (int k = 0; (isCorrectCoord(x + j * k) && isCorrectCoord(y + i * k)); k++) {
                        if (board.GetFieldType(x + j * k, y + i * k) == color) {
                            break;
                        } else {
                            nTurnedFields++;
                            result += GetSi(x + j * k, y + i * k);
                        }
                    }
                }
            }
        }
        result += GetSS(x, y);
        return result;
    }
}

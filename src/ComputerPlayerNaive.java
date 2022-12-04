// Компьютерный игрок, не учитывающий возможные ответные ходы противника.
public final class ComputerPlayerNaive extends ComputerPlayer {

    public ComputerPlayerNaive(Board board, FieldType color) {
        this.board = board;
        this.color = color;
    }

    // Вычисление ценности хода без учёта возможных ответных ходов противника.
    protected double GetMoveEstimation(int x, int y) {
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

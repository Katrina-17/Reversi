// Базовый класс, представляющий игрока.
public abstract class Player {
    protected int points;
    protected Board board;
    protected FieldType color;
    public Player() {
        this.color = FieldType.WHITE;
    }

    public void AddPoints(int points) {
        this.points += points;
    }

    public int GetPoints() { return points; }

    public abstract boolean MakeMove();

    public Player(Board board, FieldType color) {
        this.board = board;
        this.color = color;
    }

    // Оценка возможности сделать следующий ход.
    public boolean CanMakeMove() {
        boolean res = false;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board.GetFieldType(x, y) != FieldType.EMPTY) {
                    continue;
                }
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i == 0 && j == 0) {
                            continue;
                        }
                        res = res | board.isAvailableDirection(color, x, y, i, j);
                        if (res) {
                            return true;
                        }
                    }
                }
            }
        }
        return res;
    }

}

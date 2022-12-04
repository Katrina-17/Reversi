// Класс, представляющий компьютерного игрока.
public abstract class ComputerPlayer extends Player {

    public ComputerPlayer(Board board, FieldType color) {
        this.board = board;
        this.color = color;
    }

    public ComputerPlayer() {
        this.color = FieldType.WHITE;
    }

    protected class Coords {
        private int x;
        private int y;
        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int X() {
            return x;
        }
        public int Y() {
            return y;
        }
    }

    public boolean MakeMove() {
        System.out.println("Press Enter key to continue the game...");
        try
        {
            System.in.read();
        }
        catch(Exception e) {}
        if (!CanMakeMove()) {
            System.out.println("The computer cannot make a move. Othello misses a move.");
            return false;
        }
        Coords nextMove = GetNextMove();
        try {
            board.MakeMove(color, nextMove.X(), nextMove.Y());
        } catch (IllegalArgumentException ex) {
            System.out.println("Cannot make a move. Othello gives up(((");
        }
        return true;
    }

    // Методы определения констант Si и SS для оценочной функции.
    protected int GetSi(int xi, int yi) {
        if (xi == 0 || xi == 7 || yi == 0 || yi == 7) {
            return 2;
        } else {
            return 1;
        }
    }

    protected double GetSS(int x, int y) {
        if (x == 0) {
            if (y == 0 || y == 7) {
                return 0.8;
            } else {
                return 0.4;
            }
        } else {
            if (y == 0 || y == 7) {
                return 0.4;
            } else {
                return 0;
            }
        }
    }

    // Метод вычисления ценности конкретного хода.
    protected abstract double GetMoveEstimation(int x, int y);

    // Метод определения координат для следующего хода на основе оценочной функции.
    protected Coords GetNextMove() {
        int resX = 0;
        int resY = 0;
        double maxEstimation = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board.fieldIsAvailable(color, i, j)) {
                    double currentRes = GetMoveEstimation(i, j);
                    if (currentRes > maxEstimation) {
                        maxEstimation = currentRes;
                        resX = i;
                        resY = j;
                    }
                }
            }
        }
        return new Coords(resX, resY);
    }

    protected boolean isCorrectCoord(int coord) {
        return (coord >= 0 && coord <= 7);
    }
}

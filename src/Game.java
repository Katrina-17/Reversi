import java.util.Scanner;

// класс, отвечающий за общий ход игры.
public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Board board;
    private Score highScore;

    // информация о том, смогли ли игроки сделать ход в последний раз, когда была их очередь
    private boolean firstPlayerCanMove = true;
    private boolean secondPlayerCanMove = true;

    public void StartGame() {
        boolean startNewGame = true;
        while (startNewGame) {
            board.ResetBoard();
            board.DrawBoard();
            int emptyFields = 60;
            while(!isGameFinished() && emptyFields > 0) {
                firstPlayerCanMove = firstPlayer.MakeMove();
                board.DrawBoard();
                secondPlayerCanMove = secondPlayer.MakeMove();
                board.DrawBoard();
                emptyFields -= 2;
            }
            CountPoints();
            PrintResults();
            UpdateHighScore();
            PrintHighScore();
            System.out.println();
            System.out.println("Do you want to play again? (y or n)");
            startNewGame = StartNewGame();
        }
    }

    private void SelectGameMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the game mode:\n(1) PvP\n(2) PvE simple\n(3) PvE advanced");
        String answer = scanner.nextLine();
        while (!(answer.length() == 1 && (answer.equals("1") || answer.equals("2") || answer.equals("3")))) {
            System.out.println("Incorrect input: try again!");
            answer = scanner.nextLine();
        }
        switch (answer) {
            case "1":
                secondPlayer = new HumanPlayer(board, FieldType.WHITE);
                break;
            case "2":
                secondPlayer = new ComputerPlayerNaive(board, FieldType.WHITE);
                break;
            default:
                secondPlayer = new ComputerPlayerPro(board, FieldType.WHITE);
                break;
        }
    }

    public Game() {
        board = new Board();
        firstPlayer = new HumanPlayer(board, FieldType.BLACK);
        System.out.println("Welcome to the Reversi/Othello game!");
        SelectGameMode();
    }

    // игра заканчивается, когда оба игрока не могут сделать следующий ход.
    private boolean isGameFinished() {
        return (!firstPlayerCanMove && !secondPlayerCanMove);
    }

    // за каждую занятую клетку на доске начисляется очко.
    private void CountPoints() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.GetFieldType(i, j) == FieldType.BLACK) {
                    firstPlayer.AddPoints(1);
                } else if(board.GetFieldType(i, j) == FieldType.WHITE) {
                    secondPlayer.AddPoints(1);
                }
            }
        }
    }

    private void PrintResults() {
        System.out.println();
        System.out.println();
        System.out.println("            GAME OVER!            ");
        System.out.println();
        System.out.println("=============RESULTS============");
        System.out.println("Player 1: " + firstPlayer.GetPoints() + " points");
        if (secondPlayer instanceof ComputerPlayer) {
            System.out.println("Othello (computer): " + secondPlayer.points + " points");
        } else {
            System.out.println("Player 2: " + secondPlayer.GetPoints() + " points");
        }
        System.out.println();
        if (firstPlayer.GetPoints() > secondPlayer.GetPoints()) {
            System.out.println("Player 1 wins!");
        } else {
            if (secondPlayer instanceof ComputerPlayer) {
                System.out.println("Othello wins!");
            } else {
                System.out.println("Player 2 wins!");
            }
        }
    }

    // Лучший результат запоминается только для человека, не для компьютера.
    private void UpdateHighScore() {
        if (highScore == null) {
            if (firstPlayer.GetPoints() > secondPlayer.GetPoints()) {
                highScore = new Score("Player 1", firstPlayer.GetPoints());
            } else {
                if (secondPlayer instanceof HumanPlayer) {
                    highScore = new Score("Player 2", secondPlayer.GetPoints());
                } else {
                    highScore = new Score("Player 1", firstPlayer.GetPoints());
                }
            }
        } else {
            if (firstPlayer.GetPoints() > secondPlayer.GetPoints()) {
                if (firstPlayer.GetPoints() > highScore.GetScore()) {
                    highScore = new Score("Player 1", firstPlayer.GetPoints());
                }
            } else {
                if (secondPlayer instanceof HumanPlayer) {
                    highScore = new Score("Player 2", secondPlayer.GetPoints());
                } else if (firstPlayer.GetPoints() > highScore.GetScore()) {
                    highScore = new Score("Player 1", firstPlayer.GetPoints());
                }
            }
        }
    }

    private void PrintHighScore() {
        if (highScore == null) {
            return;
        } else {
            System.out.println();
            System.out.println("High score: ");
            System.out.println(highScore.GetName() + ": " + highScore.GetScore() + " points");
        }
    }

    private boolean StartNewGame() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while(!(input.equals("y") || input.equals("n"))) {
            input = scanner.nextLine();
        }
        return input.equals("y");
    }


    // Вспомогательный класс для хранения текущего рекорда.
    private class Score {
        private final String name;
        private final int score;
        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }
        public String GetName() {
            return name;
        }
        public int GetScore() {
            return score;
        }
    }
}

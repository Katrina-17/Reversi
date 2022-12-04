import java.util.Scanner;

// Класс, представляющий игрока-человека.
public class HumanPlayer extends Player {

    // Возможность отменить ход в данный момент.
    private boolean canUndo = false;
    // Массив для сохранения предыдущего состояния доски.
    private FieldType[][] previousBoard = new FieldType[8][8];

    public HumanPlayer(Board board, FieldType color) {
        this.board = board;
        this.color = color;
    }

    // Метод совершения хода.
    public boolean MakeMove() {
        if (!CanMakeMove()) {
            System.out.println("You don't have any available moves. You're missing a move.");
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Move with the " + ((color == FieldType.BLACK) ? "blacks." : "whites."));
        System.out.println("Enter the position where you want to place a chip (example: a1)");
        System.out.println("Enter \"undo\" to undo your last move");
        boolean made = false;
        while (!made) {
            String input = scanner.nextLine();
            if (input.equals("undo")) {
                Undo();
            }
            RememberBoardState();
            while (!isCorrectInput(input)) {
                if (!input.equals("undo")) {
                    System.out.println("Incorrect input: try again!");
                }
                input = scanner.nextLine();
            }
            int x = GetX(input);
            int y = GetY(input);
            try {
                board.MakeMove(color, x, y);
                made = true;
            } catch (IllegalArgumentException ex) {
                System.out.println("Incorrect move: try again!");
            }
        }
        return true;
    }

    // Проверка корректности введённых координат.
    private boolean isCorrectInput(String input) {
        String correctLetters = "abcdefgh";
        String correctNumbers = "12345678";
        if (input.length() != 2) {
            return false;
        }
        if (!correctLetters.contains(input.substring(0, 1)) ||
                !correctNumbers.contains(input.substring(1))) {
            return false;
        }
        return true;
    }

    private int GetX(String input) {
        return switch (input.charAt(0)) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> 0;
        };
    }

    private int GetY(String input) {
        return switch (input.charAt(1)) {
            case '1' -> 0;
            case '2' -> 1;
            case '3' -> 2;
            case '4' -> 3;
            case '5' -> 4;
            case '6' -> 5;
            case '7' -> 6;
            case '8' -> 7;
            default -> 0;
        };
    }

    // Запоминание текущего состояния доски для будущей отмены хода.
    public void RememberBoardState() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                previousBoard[i][j] = board.GetFieldType(i, j);
            }
        }
        canUndo = true;
    }

    // Отмена хода.
    public void Undo() {
        if (!canUndo) {
            System.out.println("Impossible to undo the last move!");
            return;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.SetFieldType(previousBoard[i][j], i, j);
            }
        }
        board.DrawBoard();
        canUndo = false;
    }
}

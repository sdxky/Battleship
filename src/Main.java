import java.util.Random;
import java.util.Scanner;

public class Main {
    static final int SIZE = 10;
    static char[][] playerBoard = new char[SIZE][SIZE];
    static char[][] compBoard = new char[SIZE][SIZE];
    static char[][] displayBoard = new char[SIZE][SIZE];
    static Random rand = new Random();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initBoards();
        placeShip(playerBoard);
        placeShip(compBoard);

        System.out.println("Welcome to Battleship!");
        while (true) {
            printBoard(displayBoard, "Players field");
            playerTurn();
            if (isAllShipsSunk(compBoard)) {
                System.out.println("🎉 You've won!");
                break;
            }
            compTurn();
            if (isAllShipsSunk(playerBoard)) {
                System.out.println("The computer won 💥");
                break;
            }
        }
    }

    private static void compTurn() {
        int row, col;
        do {
            row = rand.nextInt(SIZE);
            col = rand.nextInt(SIZE);
        } while (playerBoard[row][col] == 'X' || playerBoard[row][col] == 'O');

        System.out.println("The computer shoots at " + (char) ('A' + row) + (col + 1));

        if (playerBoard[row][col] == 'S') {
            System.out.println("You're hit!");
            playerBoard[row][col] = 'X';
        } else {
            System.out.println("Missed!");
            playerBoard[row][col] = 'O';
        }
    }

    static void playerTurn() {
        while (true) {
            System.out.print("Enter the coordinates (e.g. A5): ");
            String input = scanner.next().toUpperCase();

            if (input.length() < 2 || input.length() > 3) {
                System.out.println("Invalid input! Format should be like A5 or B10.");
                continue;
            }

            char letter = input.charAt(0);
            String numberPart = input.substring(1);

            if (letter < 'A' || letter >= 'A' + SIZE) {
                System.out.println("Invalid row! Must be from A to " + (char)('A' + SIZE - 1));
                continue;
            }

            if (!numberPart.matches("\\d+")) {
                System.out.println("Column must be a number!");
                continue;
            }

            int col = Integer.parseInt(numberPart) - 1;
            int row = letter - 'A';

            if (col < 0 || col >= SIZE) {
                System.out.println("Invalid column! Must be from 1 to " + SIZE);
                continue;
            }
            if (compBoard[row][col] == 'S') {
                System.out.println("Hit!");
                compBoard[row][col] = 'X';
                displayBoard[row][col] = 'X';
            } else if (displayBoard[row][col] == 'O' || displayBoard[row][col] == 'X') {
                System.out.println("You've already fired here!");
            } else if (displayBoard[row][col] == '#') {
                System.out.println("Missed!");
                displayBoard[row][col] = 'O';
            }

            break;
        }
    }


    static void initBoards() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                playerBoard[i][j] = '#';
                compBoard[i][j] = '#';
                displayBoard[i][j] = '#';
            }
        }
    }

    static void placeShip(char[][] board) {
        int x = rand.nextInt(SIZE);
        int y = rand.nextInt(SIZE - 2);
        boolean vertical = rand.nextBoolean();

        for (int i = 0; i < 3; i++) {
            if (vertical)
                board[x + i][y] = 'S';
            else
                board[x][y + i] = 'S';
        }
    }

    static boolean isAllShipsSunk(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 'S') return false;
            }
        }
        return true;
    }

    static void printBoard(char[][] board, String title) {
        System.out.println("\n" + title);
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) System.out.print(i + " ");
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
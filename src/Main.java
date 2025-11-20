import java.util.Random;
import java.util.Scanner;

public class Main {
    static final int SIZE = 10;
    static int[] SHIPS = {3, 3, 2, 2, 1, 1, 1};

    static char[][] player1Board = new char[SIZE][SIZE];
    static char[][] player2Board = new char[SIZE][SIZE];

    static char[][] display1 = new char[SIZE][SIZE]; // Player 1 shots on Player 2
    static char[][] display2 = new char[SIZE][SIZE]; // Player 2 shots on Player 1

    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();

    public static void main(String[] args) {
        initBoards();
        placeAllShips(player1Board);
        placeAllShips(player2Board);

        boolean player1Turn = true;

        while (true) {
            if (player1Turn) {
                System.out.println("===== PLAYER 1 TURN =====");
                printBoard(display1, "Player 1 shots");
                printBoard(player1Board, "Player 1 field");

                playerTurn(display1, player2Board);

                if (isAllShipsSunk(player2Board)) {
                    System.out.println("🎉 PLAYER 1 WINS!");
                    break;
                }
            } else {
                System.out.println("===== PLAYER 2 TURN =====");
                printBoard(display2, "Player 2 shots");
                printBoard(player2Board, "Player 2 field");

                playerTurn(display2, player1Board);

                if (isAllShipsSunk(player1Board)) {
                    System.out.println("🎉 PLAYER 2 WINS!");
                    break;
                }
            }

            System.out.println("Press Enter to end turn...");
            scanner.nextLine(); // ожидание
            clearConsole();     // очищаем экран

            player1Turn = !player1Turn;
        }
    }

    static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    static void playerTurn(char[][] display, char[][] enemyBoard) {
        while (true) {
            System.out.print("Enter coordinates (e.g., A5): ");
            String input = scanner.nextLine().toUpperCase();

            if (input.length() < 2 || input.length() > 3) {
                System.out.println("Invalid input!");
                continue;
            }

            char rowChar = input.charAt(0);
            String colStr = input.substring(1);

            if (rowChar < 'A' || rowChar >= 'A' + SIZE || !colStr.matches("\\d+")) {
                System.out.println("Invalid coordinates!");
                continue;
            }

            int row = rowChar - 'A';
            int col = Integer.parseInt(colStr) - 1;

            if (col < 0 || col >= SIZE) {
                System.out.println("Invalid column!");
                continue;
            }

            if (display[row][col] == 'X' || display[row][col] == 'O') {
                System.out.println("Already shot here!");
                continue;
            }

            if (enemyBoard[row][col] == 'S') {
                System.out.println("Hit!");
                enemyBoard[row][col] = 'X';
                display[row][col] = 'X';
                if (isShipDead(enemyBoard, row, col)) {
                    markAroundDeadShip(enemyBoard, display, row, col);
                    System.out.println("Ship destroyed!");
                }
            } else {
                System.out.println("Miss!");
                display[row][col] = 'O';
            }
            break;
        }
    }

    static void initBoards() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                player1Board[i][j] = '.';
                player2Board[i][j] = '.';
                display1[i][j] = '.';
                display2[i][j] = '.';
            }
    }

    static void placeShip(char[][] board, int length) {
        while (true) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            boolean vertical = rand.nextBoolean();

            if (canPlace(board, row, col, length, vertical)) {
                for (int i = 0; i < length; i++)
                    if (vertical) board[row + i][col] = 'S';
                    else board[row][col + i] = 'S';
                break;
            }
        }
    }

    static boolean canPlace(char[][] board, int row, int col, int length, boolean vertical) {
        if (vertical && row + length > SIZE) return false;
        if (!vertical && col + length > SIZE) return false;

        for (int i = 0; i < length; i++) {
            int r = vertical ? row + i : row;
            int c = vertical ? col : col + i;

            // проверяем саму клетку
            if (board[r][c] == 'S') return false;

            // проверка соседей 3x3 вокруг клетки
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nr = r + dr;
                    int nc = c + dc;

                    if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                        if (board[nr][nc] == 'S') return false;
                    }
                }
            }
        }
        return true;
    }

    static boolean isShipDead(char[][] board, int row, int col) {
        // ищем все клетки корабля (вертикальные и горизонтальные)
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : dirs) {
            int r = row, c = col;
            while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] != '.' && board[r][c] != '0') {
                if (board[r][c] == 'S') return false; // живой кусок остался
                r += d[0];
                c += d[1];
            }
        }

        return true; // нет живых частей
    }

    static void markAroundDeadShip(char[][] board, char[][] display, int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;

                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                    if (board[nr][nc] == '.') {
                        board[nr][nc] = 'O';      // запрет для постановки кораблей и стрельбы
                        display[nr][nc] = 'O';    // игрок видит 0 и не может туда стрелять
                    }
                }
            }
        }
    }


    static boolean isAllShipsSunk(char[][] board) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == 'S') return false;
        return true;
    }

    static void printBoard(char[][] board, String title) {
        System.out.println("\n" + title);
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) System.out.print(i + " ");
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < SIZE; j++) System.out.print(board[i][j] + " ");
            System.out.println();
        }
    }

    static void placeAllShips(char[][] board) {
        for (int ship : SHIPS) placeShip(board, ship);
    }
}
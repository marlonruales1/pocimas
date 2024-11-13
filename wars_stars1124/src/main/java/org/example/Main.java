import java.util.Scanner;
import java.util.Random;


public class Main {
    static void initializeBoard(char[][] board, char playerChar, char enemyChar, int[] playerPos, char wallChar, char potionChar) {
        Random random = new Random();
        int size = 10;
        int x, y;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 'L';
            }
        }
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (x == 9 && y == 9);
        board[x][y] = playerChar;
        playerPos[0] = x;
        playerPos[1] = y;
        int count = 0;
        while (count < 5) {
            x = random.nextInt(size);
            y = random.nextInt(size);
            if (board[x][y] == 'L' && !(x == 9 && y == 9)) {
                board[x][y] = enemyChar;
                count++;
            }
        }
        count = 0;
        while (count < 5) {
            x = random.nextInt(size);
            y = random.nextInt(size);
            if (board[x][y] == 'L' && !(x == 9 && y == 9)) {
                board[x][y] = wallChar;
                count++;
            }
        }
        count = 0;
        while (count < 5) {
            x = random.nextInt(size);
            y = random.nextInt(size);
            if (board[x][y] == 'L' && !(x == 9 && y == 9)) {
                board[x][y] = potionChar;
                count++;
            }
        }
    }


    static void displayBoard(char[][] board) {
        int size = board.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }


    static int[] getDirectionDelta(char direction) {
        int[] delta = new int[2];
        if (direction == 'W') {
            delta[0] = -1;
            delta[1] = 0;
        } else if (direction == 'S') {
            delta[0] = 1;
            delta[1] = 0;
        } else if (direction == 'A') {
            delta[0] = 0;
            delta[1] = -1;
        } else if (direction == 'D') {
            delta[0] = 0;
            delta[1] = 1;
        } else if (direction == 'Q') {
            delta[0] = -1;
            delta[1] = -1;
        } else if (direction == 'E') {
            delta[0] = -1;
            delta[1] = 1;
        } else if (direction == 'B') {
            delta[0] = 1;
            delta[1] = -1;
        } else if (direction == 'R') {
            delta[0] = 1;
            delta[1] = 1;
        }
        return delta;
    }


    static void movePlayer(char[][] board, int[] playerPos, int numSquares, char direction, int[] lives, boolean[] hasPotion, boolean[] canSwap, char playerChar, char enemyChar) {
        Scanner scanner = new Scanner(System.in);
        int size = board.length;
        if (canSwap[0]) {
            System.out.println("Intercambia la poción con cualquier celda 'L' ");
            System.out.println("Inserta fila (0-9):");
            int x = scanner.nextInt() % size;
            System.out.println("Inserta columna (0-9):");
            int y = scanner.nextInt() % size;
            if (board[x][y] == 'L') {
                board[playerPos[0]][playerPos[1]] = 'L';
                playerPos[0] = x;
                playerPos[1] = y;
                board[x][y] = playerChar;
                canSwap[0] = false;
            } else {
                System.out.println("Posición inválida. Pierdes turno.");
            }
        } else {
            int[] delta = getDirectionDelta(direction);
            processMovement(board, playerPos, delta[0], delta[1], numSquares, lives, hasPotion, canSwap, playerChar, enemyChar);
        }
    }


    static void processMovement(char[][] board, int[] playerPos, int deltaX, int deltaY, int numSquares, int[] lives, boolean[] hasPotion, boolean[] canSwap, char playerChar, char enemyChar) {
        int size = board.length;
        int x = playerPos[0];
        int y = playerPos[1];
        board[x][y] = 'L';
        for (int i = 0; i < numSquares; i++) {
            x = (x + deltaX + size) % size;
            y = (y + deltaY + size) % size;
            if (board[x][y] == 'M') {
                System.out.println("Has tocado pared. Pierdes turno.");
                break;
            } else if (board[x][y] == enemyChar) {
                lives[0]--;
                System.out.println("Has perdido una vida. Te quedan: " + lives[0]);
                if (lives[0] <= 0) {
                    System.out.println("No quedan vidas.");
                    playerPos[0] = x;
                    playerPos[1] = y;
                    board[x][y] = playerChar;
                    return;
                }
            } else if (board[x][y] == 'P') {
                hasPotion[0] = true;
                System.out.println("Has recogido una pócima.");
                board[x][y] = 'L';
            }
        }
        playerPos[0] = x;
        playerPos[1] = y;
        board[x][y] = playerChar;
        if (hasPotion[0]) {
            canSwap[0] = true;
            hasPotion[0] = false;
        }
    }


    public static void main(String[] args) {
        char[][] boardYoda = new char[10][10];
        char[][] boardVader = new char[10][10];
        int[] yodaPos = new int[2];
        int[] vaderPos = new int[2];
        int[] yodaLives = {3};
        int[] vaderLives = {3};
        boolean[] yodaHasPotion = {false};
        boolean[] vaderHasPotion = {false};
        boolean[] yodaCanSwap = {false};
        boolean[] vaderCanSwap = {false};
        initializeBoard(boardYoda, 'Y', 'D', yodaPos, 'M', 'P');
        initializeBoard(boardVader, 'V', 'R', vaderPos, 'M', 'P');
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Turno de Yoda:");
            displayBoard(boardYoda);
            System.out.println("Enter número de saltos (1-3):");
            int numSquares = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Elige dirección (W,A,S,D,Q,E,B,R):");
            char direction = scanner.nextLine().toUpperCase().charAt(0);
            movePlayer(boardYoda, yodaPos, numSquares, direction, yodaLives, yodaHasPotion, yodaCanSwap, 'Y', 'D');
            if (yodaPos[0] == 9 && yodaPos[1] == 9) {
                System.out.println("Yoda ha llegado al final. Yoda gana!");
                break;
            }
            if (yodaLives[0] <= 0) {
                System.out.println("Yoda ha perdido todas sus vidas. Vader gana!");
                break;
            }
            System.out.println("Turno de Vader:");
            displayBoard(boardVader);
            System.out.println("Elige el número de saltos (1-3):");
            numSquares = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Elige dirección (W,A,S,D,Q,E,B,R):");
            direction = scanner.nextLine().toUpperCase().charAt(0);
            movePlayer(boardVader, vaderPos, numSquares, direction, vaderLives, vaderHasPotion, vaderCanSwap, 'V', 'R');
            if (vaderPos[0] == 9 && vaderPos[1] == 9) {
                System.out.println("Vader ha llegado al final. Vader gana!");
                break;
            }
            if (vaderLives[0] <= 0) {
                System.out.println("Vader ha perdido sus vidas. Yoda gana!");
                break;
            }
        }
    }
}

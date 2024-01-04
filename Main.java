import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan =new Scanner(System.in);

        int[] food = {1,-1};
        int[] bomb = {1,-1};

        randBomb(bomb);
        randFood(food);

        Node head = firstSnake();
        Node thirdNode = head.next.next; // it is not working ı've tried to sent explodedBomb

        updateGrid(head,food,bomb);

        while (true) {
            System.out.println("Move your snake(w : for up, a : for left, d : for right, s : for down)");
            String answer = scan.nextLine().toLowerCase();
            switch (answer) {
                case "d":
                    move(head, 0, 1);
                    break;
                case "a":
                    move(head, 0, -1);
                    break;
                case "w":
                    move(head, -1, 0);
                    break;
                case "s":
                    move(head, 1, 0);
                    break;
                default:
                    continue;
            }
            if (snakeLength(head) <= 3) {
                System.out.println("Snake length dropped to 3 so Game Over.");
                break;
            }
            if (isMatch(head)) {
                System.out.println("Game Over!");
                break;
            }
            if (isSnakeEat(head, food)) {
                addNewNodeToTail(head);
                randFood(food);
            }
            if (isBombExploded(head,bomb)) {

            }

            updateGrid(head, food, bomb);

        }
        scan.close();
    }



    public static void updateGrid(Node head,int[] food,int[] bomb) {
        final int SIZE = 10;
        char[][] arr = new char[SIZE][SIZE];

        for (char[] row : arr) {
            Arrays.fill(row, '.');
        }

        int foodX = food[0];
        int foodY = food[1];
        arr[foodX][foodY] = '*';

        int bombX = bomb[0];
        int bombY = bomb[1];
        arr[bombX][bombY] = 'X';

        Node current = head;
        while (current != null) {
            int x = current.x;
            int y = current.y;
            if (current == head) {
                arr[x][y] = 'H'; // Represents the head of the snake
            } else {
                arr[x][y] = '0'; // Represents the body of the snake
            }
            current = current.next;
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(arr[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static Node firstSnake() {
        Random rand = new Random();
        int randX = rand.nextInt(4) + 4; // random position for x-coordinates
        int randY = rand.nextInt(4)+4; // random position for y-coordinate

        Node head = new Node();
        head.x = randX;
        head.y = randY;
        Node current = head;

        // Generate 4 more nodes to form a snake of length 5
        for (int i = 0; i < 4; i++) {
            int newX = randX; // Keeping x-coordinate the same
            int newY = randY - (i + 1); // Decrementing y-coordinate

            Node newNode = new Node();
            newNode.x = newX;
            newNode.y = newY;
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
        }

        return head; // this is my head of snake ı use it as a parameter
    }

    public static void move(Node snake, int dx, int dy) {
        Node current = snake;
        int prevX = snake.x;
        int prevY = snake.y;
        int nextX, nextY;

        // move the head
        snake.x += dx;
        snake.y += dy;

        // move the body
        while (current.next != null) {
            current = current.next;
            nextX = current.x;
            nextY = current.y;
            current.x = prevX;
            current.y = prevY;
            prevX = nextX;
            prevY = nextY;
        }
    }

    public static boolean isMatch(Node snake) {
        Node head = snake;

        int headX = head.x;
        int headY = head.y;

        // Check the bounds for snake head
        if (headX < 0 || headX >= 10 || headY < 0 || headY >= 10) {
            return true;
        }

        // traversing body after head
        Node current = head.next;
        while (current != null) {
            if (headX == current.x && headY == current.y) {
                return true; // for collision with itself
            }
            current = current.next;
        }
        return false;
    }

    public static int snakeLength(Node head) {
        int length = 0;//controlling snakeSize <= 3 when it's game over
        Node current = head;
        while (current != null) {
            length++;
            current = current.next;
        }
        return length;
    }

    public static boolean isSnakeEat(Node snake, int[] food) {
        Node head = snake; //controlling food
        int headX = head.x;
        int headY = head.y;

        int foodX = food[0];
        int foodY = food[1];


        return (headX == foodX && headY == foodY);
    }

    public static void addNewNodeToTail(Node snake) {
        Node current = snake;
        while (current.next != null) {
            current = current.next;
        }

        int newX = current.x;
        int newY = current.y;
        Node newTail = new Node();
        newTail.x = newX;
        newTail.y = newY;

        current.next = newTail;

    }

    public static boolean isBombExploded(Node head, int[] bomb) {
        int counter = 0;
        Node current = head.next;
        while (current != null) {
            if (head.x == bomb[0] && head.y == bomb[1]) {
                counter++;
                if (counter == 3) {
                    explodeBomb(head);
                    randBomb(bomb);
                    counter = 0;
                }
            }
            current = current.next;
        }
        return true;
    }
    public static void explodeBomb(Node head) {
        Node current = head.next;  // second node
        Node prev = head;
        int count = 2;

        while (current != null) {
            if (count == 3) {  // third node has reached by node 2
                prev.next = current.next;  // explode by passing it
                break;
            }
            prev = current;
            current = current.next;
            count++;
        }
    }
    public static void randFood(int[] arr) {
        Random rand = new Random();
        int randX = rand.nextInt(10);
        int randY = rand.nextInt(10);

        arr[0] = randX;
        arr[1] = randY;
    }

    public static void randBomb(int[] arr) {
        Random rand = new Random();
        int randX = rand.nextInt(10);
        int randY = rand.nextInt(10);

        arr[0] = randX;
        arr[1] = randY;
    }

}

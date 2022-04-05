package org.zhumanov;

import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final char DOT_X = 'X';
    private static final char DOT_O = 'O';
    private static final char DOT_EMPTY = '.';

    private static char dotPlayerOne;
    private static char dotPlayerTwo;
    private static char dotAi;


    static char field[][];

    private static int fieldSizeX;
    private static int fieldSizeY;
    private static int fieldSize;
    private static int winLine;

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame(){
        while (true){
            chooseDot();
            System.out.println("Установите размер поля");
            fieldSize = scanner.nextInt();
            if (fieldSize>3) {
                System.out.println("Поле больше 3x3, для победы соберите последовательность из 4 значков");
                winLine=4;
            } else winLine =3;
            playRound();
            System.out.println("Продолжить игру - Y");
            if(!scanner.next().toLowerCase(Locale.ROOT).equals("y")){
                System.out.println("Игра окончена");
                break;
            }
        }

    }
    private static boolean gameCheck(char dot) {
        if (checkWin(dot) && dot == dotPlayerOne) {
            System.out.println("Победа игрока 1");
            return true;
        } else if (checkWin(dot) && dot == dotAi) {
            System.out.println("Победа Компьютера");
            return true;
        }
        return checkDraw();
    }

    private static void playRound(){
        initField(fieldSize,fieldSize);
        printField();
        if(dotPlayerOne == DOT_X){
            playerOneFirst();
        }
        else {
            aiFirst();
        }
    }

    private static void playerOneFirst(){
        while (true){
            playerTurn();
            printField();
            if(gameCheck(dotPlayerOne)){
                break;
            }
            aiTurn();
            printField();
            if(gameCheck(dotAi)){
                break;
            }
        }
    }

    private static void aiFirst(){
        while (true){
            aiTurn();
            printField();
            if(gameCheck(dotAi)){
                break;
            }
            playerTurn();
            printField();
            if(gameCheck(dotPlayerOne)){
                break;
            }
        }
    }

    private static boolean checkWin(char dot) {
        int digScore = 0;
        int reverseDigScore =0;
        int horScore = 0;
        int verScore = 0;

        for (int i=0; i<fieldSize; i++){
            if(field[i][i]==dot) digScore++;
            else if(field[i][i]!=dot&& digScore<winLine) digScore=0;
            if (field[i][fieldSize-i-1]==dot) reverseDigScore++;
            else if(field[i][i]!=dot && reverseDigScore<winLine) reverseDigScore=0;
            //проверка на значок - боту давало победу за то, что ставил очки, продолжая мою линию
        }
        if (digScore>=winLine||reverseDigScore>=winLine) return true;

        //могут проверить только только две диагонали, которые идут через центр


        for (int x=0; x<fieldSizeX;x++){
            for (int y=0; y<fieldSizeY; y++){
                if (field[y][x]==dot) horScore++;
                else if (field[y][x]!=dot && horScore<winLine) horScore=0;
                if(horScore>=winLine) return true;

                if (field[x][y]==dot) verScore++;
                else if (field[x][y]!=dot && verScore<winLine) verScore=0;
                if (verScore>=winLine) return true;
            }
        }

        return false;

        /*if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot) return true;
        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot) return true;
        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot) return true;
        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot) return true;
        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot) return true;
        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot) return true;
         if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) return true;
        if (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot) return true;*/
    }

    private static void chooseDot() {
        System.out.print("Выберет знак X или O: ");

        if (scanner.next().toLowerCase(Locale.ROOT).equals("x")) {
                dotPlayerOne = DOT_X;
                dotAi = DOT_O;
        } else {
                dotPlayerOne = DOT_O;
                dotAi = DOT_X;
        }

    }

    private static void playerTurn() {
        int x;
        int y;
        do {
            System.out.print("Введите координату x и y: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(y, x));

        field[y][x] = dotPlayerOne;
    }

    private static void aiTurn() {
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellValid(y, x));

        field[y][x] = dotAi;
    } ///в будущем можно переделать на блокировку, а потом можно и менять сложность через интерфейс Behavior, по-моему

    private static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        System.out.println("Ничья");
        return true;
    }

    private static boolean isCellValid(int y, int x) {
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY && field[y][x] == DOT_EMPTY;
    }

    private static void initField(int sizeX, int sizeY) {
        fieldSizeY = sizeY;
        fieldSizeX = sizeX;
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2; i++) {
           System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < fieldSizeY; i++){
            System.out.print(i + 1 + "|");

            for (int j = 0; j < fieldSizeX; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

}

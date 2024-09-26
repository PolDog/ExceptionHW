import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner;
    static ArrayList<String> rawData;
    static String surname;
    static String name;
    static String middleName;
    static LocalDate birthday;
    static long phone;
    static char gender = '0';
    static String file;
    static boolean testRun;


    public static void main(String[] args) {
        testRun = true;
        String userInfo = userInput();
        try {
            checkAmountEntry(userInfo);
            parseGender();
            parseBirthday();
            parsePhone();
            parseSurname();
            parseName();
            parseMiddleName();
            saveToFile();
            showText("Records save to file " + surname + ".txt!");
            showText("Surname: " + surname + ", Name: " + name + ", Middle name:" + middleName + ", Birthday: " + birthday + ", Phone: " + phone + ", Gender: " + gender);
        } catch (IllegalArgumentException e) {
            showText(e.getMessage());
        } catch (IOException e) {
            showText(e.getMessage());
        }
    }

    public static String userInput() {
        scanner = new Scanner(System.in);
        showText("Enter you Surname, Name, Middle name, Birthday, Phone, Gender");
        showText("All data separated by spaces");
        showText("=============================================================");
        String data;
        data = "Ivanov Ivan Ivanovich 23.06.1981 1234567 m";
        if (!testRun) {
            data = scanner.nextLine();
        }
        return data;
    }

    public static void showText(String text) {
        System.out.println(text);
    }

    private static void checkAmountEntry(String input) {
        String[] amount = input.split(" ");
        int numberRecords = 6;
        if (amount.length < numberRecords) {
            throw new IllegalArgumentException("You have entered too little information.");
        }
        if (amount.length > numberRecords) {
            throw new IllegalArgumentException("You have entered unnecessary information.");
        }
        converToList(amount);
    }

    private static void converToList(String[] amount) {
        rawData = new ArrayList();
        for (int i = 0; i < amount.length; i++) {
            rawData.add(amount[i]);
        }
    }

    private static void parseGender() {
        for (int i = 0; i < rawData.size(); i++) {
            if (rawData.get(i).length() == 1) {
                char tmp = Character.toUpperCase(rawData.get(i).charAt(0));
                if (tmp == 'M' || tmp == 'F') {
                    gender = tmp;
                    rawData.remove(i);
                }
            }
        }
        if (gender == '0') {
            throw new IllegalArgumentException("Invalid gender.");
        }
    }

    private static void parseBirthday() throws DateTimeParseException {
        String tmp = "";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (int i = 0; i < rawData.size(); i++) {
            if (rawData.get(i).contains(".")) {
                tmp = rawData.get(i);
                rawData.remove(i);
            }
        }
        birthday = LocalDate.parse(tmp, dateFormat);
    }

    private static void parsePhone() {
        for (int i = 0; i < rawData.size(); i++) {
            try {
                phone = Long.parseLong(rawData.get(i));
                rawData.remove(i);
            } catch (Exception e) {
            }
        }
        if (phone == 0) {
            throw new IllegalArgumentException("Not correct phone number");
        }
    }

    private static void parseSurname() {
        surname = rawData.getFirst();
        rawData.removeFirst();
    }


    private static void parseName() {
        name = rawData.getFirst();
        rawData.removeFirst();
    }


    private static void parseMiddleName() {
        middleName = rawData.getFirst();
        rawData.removeFirst();
    }

    private static void saveToFile() throws IOException {
        file = surname + ".txt";
        String record = String.format("%s %s %s %s %d %c", surname, name, middleName, birthday, phone, gender);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(record);
            writer.newLine();
        }
    }
}
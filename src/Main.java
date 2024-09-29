import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        printOnScreen("Hello welcome to your contacts app!");
        List<Contacts>contacts = init();
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Add Contact");
            System.out.println("2. List Contacts");
            System.out.println("3. Update a Contact");
            System.out.println("4. Search a Contact");
            System.out.println("5. Delete a Contact");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    Contacts contact = addContact(scanner);
                    contacts.add(contact);
                    updateContacts(contacts);
                    break;
                case 2:
                    listContacts();
                    break;
                case 3:
                    updateContact(scanner);
                    break;
                case 4:
                    searchContact(scanner);
                    break;
                case 5:
                    deleteContact(scanner);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void deleteContact(Scanner scanner) throws IOException {
        List<Contacts> contacts = getList();
        System.out.println("Enter contact name or email address:");
        String name = scanner.nextLine();
        int counter = 0;
        for (Contacts contact : contacts) {
            if (contact.getName().equals(name) || contact.getEmail().equals(name)) {
                contacts.remove(counter);
                updateContacts(contacts);
                System.out.println("Contact deleted successfully.");
                return;
            }
            counter++;
        }
        System.out.println("Contact not found.");
    }

    private static void searchContact(Scanner scanner) throws IOException {
        List<Contacts> contacts = getList();
        System.out.println("Enter search term:");
        String searchTerm = scanner.nextLine();
        Stream<Contacts> stream = contacts.stream().filter(
                c -> c.getName().toLowerCase().contains(searchTerm.toLowerCase())
                || c.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                || c.getPhone().toLowerCase().contains(searchTerm.toLowerCase())
                || c.getAddress().toLowerCase().contains(searchTerm.toLowerCase())
        );

        displayContacts(stream.toList());
    }

    private static void updateContact(Scanner scanner) throws IOException {
        List<Contacts> contacts = getList();
        System.out.println("Enter contact name or email address:");
        String name = scanner.nextLine();
        int counter = 0;
        for (Contacts contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name) || contact.getEmail().equalsIgnoreCase(name)) {
                while (true){
                    System.out.println("Choose a field to update:");
                    System.out.println("1. To update name");
                    System.out.println("2. To update email");
                    System.out.println("3. To update phone");
                    System.out.println("4. To update address");
                    System.out.println("5. To save changes");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.println("Enter new name:");
                            String newName = scanner.nextLine();
                            contact.setName(newName);
                            break;
                        case 2:
                            System.out.println("Enter new email:");
                            String newEmail = scanner.nextLine();
                            contact.setEmail(newEmail);
                            break;
                        case 3:
                            System.out.println("Enter new phone:");
                            String newPhone = scanner.nextLine();
                            contact.setPhone(newPhone);
                            break;
                        case 4:
                            System.out.println("Enter new address:");
                            String newAddress = scanner.nextLine();
                            contact.setAddress(newAddress);
                            break;
                        case 5:
                            contacts.set(counter, contact);
                            updateContacts(contacts);
                            System.out.println("Contact updated successfully...");
                            return;
                        default:
                            System.out.println("Invalid option. Please try again.");
                            break;

                    }
                }

            }
            counter++;
        }
        System.out.println("Contact not found.");
    }

    private static List<Contacts> getList() throws IOException {
        List<Contacts> contacts = new ArrayList<>();
        readers(contacts);
        return contacts;
    }

    private static void updateContacts(List<Contacts> contacts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"))) {
            for (Contacts contact : contacts) {
                writer.write(
                        contact.getName() + ","
                                + contact.getPhone()
                                + ","
                                + contact.getEmail()
                                + ","
                                + contact.getAddress()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static void listContacts() throws IOException {
        List<Contacts> contacts = getList();
        displayContacts(contacts);
    }

    private static void displayContacts(List<Contacts> contacts) {
        for (Contacts contact : contacts) {
            System.out.println(" ---------------------- ");
            System.out.println("Name: " + contact.getName());
            System.out.println("Email: " + contact.getEmail());
            System.out.println("Phone: " + contact.getPhone());
            System.out.println("Address: " + contact.getAddress());
        }
    }

    private static void saveContactToFile(Contacts contact) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"));
        writer.write(contact.getName()+","+contact.getEmail()+","+contact.getPhone()+","+contact.getAddress());
        writer.close();
        System.out.println("Contacts saved successfully.");
    }

    private static Contacts addContact(Scanner scanner) {
        System.out.println("Enter contact name:");
        String name = scanner.nextLine();
        System.out.println("Enter contact email:");
        String email = scanner.nextLine();
        while (!validateEmail(email)) {
            System.out.println("Invalid email address. Please try again.");
            email =scanner.nextLine();
        }
        System.out.println("Enter contact phone:");
        String phone = scanner.nextLine();
        System.out.println("Enter contact address:");
        String address = scanner.nextLine();
        return new Contacts(name, email, phone, address);
    }

    private static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static void printOnScreen(String text) {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }        }
        System.out.println();
        for (int i = 0; i < text.length()/2; i++) {
            System.out.print("--");
        }

        System.out.println();System.out.println();
    }
    public static List<Contacts> init() throws IOException {
        List<Contacts> list = new ArrayList<>();
        File file = new File("contacts.txt");
        if (file.exists()) {
            readers(list);
        }else {
            file.createNewFile();
        }
        return list;
    }

    private static void readers(List<Contacts> list) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader("contacts.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            list.add(new Contacts(data[0], data[1], data[2], data[3]));
        }
    }

}
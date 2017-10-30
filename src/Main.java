import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String DATABASE = "./database.txt";   // This is constant

    private static Scanner in = new Scanner(System.in);    //  all methods can use this scanner

    public static void main(String[] args) throws ParseException, IOException {
        loadDatabase();

        while (true) {
            System.out.println("For reading all events press: 1");
            System.out.println("For creating a new event press: 2");
            System.out.println("For updating a event press: 3");
            System.out.println("For deleting a event press: 4");
            System.out.println("For exit: 5");
            System.out.println("####################################################");

            String digit = in.next();
            boolean isDigit = isParsable(digit);   // check input is digit

            if (isDigit && (Integer.parseInt(digit) == 1 || Integer.parseInt(digit) == 2 || Integer.parseInt(digit) == 3 || Integer.parseInt(digit) == 4 || Integer.parseInt(digit) == 5)) {
                int command = Integer.parseInt(digit);

                if (manageCommand(command)) {
                    System.out.println("Thank you for using our Event Manage System!");
                    return;
                }

            } else {
                System.out.println("Try again!");
            }
        }
    }

    private static boolean isParsable(String input) {
        boolean parsable = true;
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    private static void loadDatabase() throws ParseException, IOException {
        List<Event> events = readAllEvents();
        if (events.isEmpty()) {
            Event.ID_COUNTER = 0;     // if not have events counter is 0
        } else {
            Event.ID_COUNTER = events.get(events.size() - 1).getId() + 1;    // take last counter and add 1
        }
    }

    private static boolean manageCommand(int command) throws ParseException, IOException {
        switch (command) {
            case 1:
                List<Event> events = readAllEvents();
                printEvents(events);
                break;
            case 2:
                Event event = creteNewEvent();
                writeToDatabase(event);

                System.out.println("Event created successfully!");
                break;
            case 3:
                updateEvent();
                break;
            case 4:
                deleteEvent();
                break;
            case 5:
                return true;
        }

        return false;
    }

    private static void deleteEvent() throws ParseException, IOException {
        System.out.print("Input Event's id to delete: ");
        long id = Long.parseLong(in.next());

        List<Event> events = readAllEvents();                  // store all events before deleting
        List<Event> afterDeleting = new LinkedList<>();        // store all events after deleting

        for (Event event : events) {
            if (event.getId() != id) {
                afterDeleting.add(event);
            }
        }

        if (events.size() != afterDeleting.size()) {     // if have difference in size delete is made
            System.out.println("Event with id: " + id + " has been deleted!");
        } else {
            System.out.println("Event with id: " + id + " does not exist!");
        }
        updateAllEvents(afterDeleting);
    }

    private static void updateAllEvents(List<Event> events) {
        dropDatabase();

        for (Event event : events) {
            writeToDatabase(event);
        }
    }

    private static void dropDatabase() {
        try {
            File file = new File(DATABASE);

            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateEvent() throws ParseException, IOException {
        System.out.print("Input Event's id to update: ");
        long id = Long.parseLong(in.next());

        boolean hasFound = false;
        List<Event> events = readAllEvents();
        for (Event event : events) {
            if (event.getId() == id) {
                Event editedEvent = creteNewEvent();

                mapEvents(event, editedEvent);
                hasFound = true;
                break;
            }
        }

        if (!hasFound)
            System.out.println("Event with id: " + id + " does not exist!");
        else {
            System.out.println("Event with id: " + id + " has been updated successfully!");

            dropDatabase();

            for (Event event : events) {
                writeToDatabase(event);
            }
        }
    }

    private static void mapEvents(Event event, Event editedEvent) {
        event.setName(editedEvent.getName());
        event.setLocation(editedEvent.getLocation());
        event.setStartDate(editedEvent.getStartDate());
        event.setEndDate(editedEvent.getEndDate());
        event.setDuration(editedEvent.getDuration());
    }

    private static Event creteNewEvent() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        System.out.print("Name of the event: ");
        String name = in.next();

        System.out.print("Location of the event: ");
        String location = in.next();

        System.out.print("Start Date of the event(yyyy-MM-dd): ");
        Date startDate = df.parse(in.next());

        Date endDate;
        while (true) {
            System.out.print("End Date of the event(yyyy-MM-dd): ");
            endDate = df.parse(in.next());

            if (
                    endDate.after(startDate) && startDate.before(endDate)) {
                break;
            }
        }
        double duration = 0.0;
        System.out.print("Duration of the event: ");
        while (true) {
            try {
                duration =  Double.parseDouble(in.next());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Duration of the event: ");
            }
        }


        return new Event(name, location, startDate, endDate, duration);
    }

    private static void writeToDatabase(Event event) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE, true))) {
            bw.write(event.toString());
            bw.write(System.lineSeparator());  // add new line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Event> readAllEvents() throws ParseException, IOException {
        List<Event> events = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DATABASE))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                Event event = getEvent(line);
                events.add(event);
            }

        } catch (IOException e) {
            File file = new File(DATABASE); // create new file to store information if database file is empty
        }

        return events;
    }

    private static void printEvents(List<Event> events) {
        System.out.println("All events: ");

        for (Event event : events) {
            System.out.println("####################################################");
            System.out.println(event.format());
            System.out.println("####################################################");
        }
    }

    private static Event getEvent(String line) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String[] tokens = line.split("\\s");

        long id = Long.parseLong(tokens[0]);
        String name = tokens[1];
        String location = tokens[2];
        Date startDate = df.parse(tokens[3]);
        Date endDate = df.parse(tokens[4]);
        double duration = Double.parseDouble(tokens[5]);

        return new Event(id, name, location, startDate, endDate, duration);
    }
}

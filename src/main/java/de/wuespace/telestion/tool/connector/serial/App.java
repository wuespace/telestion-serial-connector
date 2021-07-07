package de.wuespace.telestion.tool.connector.serial;

import org.apache.commons.cli.*;

import java.util.Optional;

public class App {

    public static void main(String[] args) {
        var options = new Options();
        options.addOption("g", "greet", false, "prints the known greeting message");
        options.addOption("h", "help", false, "prints the this message");
        var parser = new DefaultParser();
        CommandLine cmdLine = null;
        try {
            cmdLine = parser.parse(options, args);
            if(cmdLine.hasOption("g")){
                System.out.println("Hello World!");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Failed to parse input arguments.");
        }
        var helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(200,
                "telestion-serial-client [OPTIONS]...",
                """
                        WARNING: The tool has no functionality other than to greet yet!
                        telestion-serial-client is a tool to forward data from a serial connection to a tcp server back and forth.

                        """,
                options, "\ntelestioin-serial-client was build for the Telestion Ground Station.",false);
    }

    //        var options = buildOptions();
//        var parser = new DefaultParser();
//
//        var line = parser.parse(options, args);
//        var serialInterface = parseInterface(line);
//        var ipAddress = parseIpAddress(line);
//        var port = parsePort(line);
//        var logfile = parseLogfile(line).orElse(null);
//        if(serialInterface.isEmpty() || ipAddress.isEmpty() || port.isEmpty()) {
//            System.out.println();
//            printHelp(options);
//            return;
//        }
//        var separated = line.hasOption("s");
//        var display = line.hasOption("d");
//
//        //var socket = new Socket(ipAddress.get(), port.get());
//        var conn = new SerialPort(serialInterface.get());
//        var fromSerial = System.out;//new TeeOutputStream(socket.getOutputStream(), System.out);
//        var toSerial = System.out;//new TeeOutputStream(conn.getOutputStream(), System.out);
//
//        var connThread = new Thread(() -> {
//            try {
//                conn.getInputStream().transferTo(fromSerial);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        /*var socketThread = new Thread(() -> {
//            try {
//                socket.getInputStream().transferTo(toSerial);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });*/
//        connThread.start();
//        //socketThread.start();
//
//        //BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//        //System.out.println("Running... Press q to exit.");
//        //console.readLine();
//        //System.out.println("Exiting...");
//
//        //connThread.interrupt();
//        //socketThread.interrupt();
//
//                //TODO add more control flags
//        //TODO add exception handling
//
//        fromSerial.close();
//        toSerial.close();
//        //socket.close();


    @SuppressWarnings("unchecked")
    private static <T> Optional<T> parseOption(CommandLine line, String option, String missing, String failed, boolean mandatory, Class<T> clazz){
        if(!line.hasOption(option)){
            if(!mandatory){
                return Optional.empty();
            }
            System.out.println(missing);
            return Optional.empty();
        }
        try {
            return Optional.of((T) line.getParsedOptionValue(option));
        } catch (ParseException e) {
            System.out.println(failed);
            return Optional.empty();
        }
    }

    private static Optional<String> parseLogfile(CommandLine line) {
        return parseOption(line, "l",
                null,
                "Argument logfile is not valid.",
                false,
                String.class);
    }

    private static Optional<Integer> parsePort(CommandLine line) {
        return parseOption(line, "a",
                "Server port is missing. Please specify a port.",
                "Argument port is not valid.",
                true,
                Integer.class);
    }

    private static Optional<String> parseInterface(CommandLine line){
        return parseOption(line, "i",
                "Interface definition is missing. Please specify an interface.",
                "Argument interface is not valid.",
                true,
                String.class);
    }

    private static Optional<String> parseIpAddress(CommandLine line){
        return parseOption(line, "a",
                "Server ip address is missing. Please specify an ip address.",
                "Argument ip address is not valid.",
                true,
                String.class);
    }

    private static void printHelp(Options options){
        var helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(200,
                "telestion-serial-client -i <interface> -a <ip-address> -p <ip-port> [OPTIONS]...",
                "telestion-serial-client is a tool to forward data from a serial connection to a tcp server back and forth.\n" +
                        "exit the application with the command \"q\"\n\n",
                options, "\ntelestioin-serial-client was build for the Telestion Ground Station.",false);

    }

    private static Options buildOptions(){
        var options = new Options();
        options.addOption("h", "help", false, "print this message");
        addRequiredOptions(options);
        addDebugOptions(options);
        return options;
    }

    private static void addRequiredOptions(Options options){
        options.addOption(Option.builder("i")
                .longOpt("interface")
                .desc("the serial interface (mandatory)")
                .required(true)
                .hasArg(true)
                .numberOfArgs(1)
                .argName("interface")
                .type(String.class)
                .optionalArg(false)
                .build());
        options.addOption(Option.builder("p")
                .longOpt("port")
                .desc("the port of the tcp server (mandatory)")
                .required(true)
                .hasArg(true)
                .numberOfArgs(1)
                .argName("server-port")
                .type(int.class)
                .optionalArg(false)
                .build());
        options.addOption(Option.builder("a")
                .longOpt("address")
                .desc("the ip address of the tcp server (mandatory)")
                .required(true)
                .hasArg(true)
                .numberOfArgs(1)
                .argName("server-ip-address")
                .type(String.class)
                .optionalArg(false)
                .build());
    }

    private static void addDebugOptions(Options options){
        options.addOption(new Option("d", "display", false, "displays the incoming and outgoing data to the terminal"));
        options.addOption(Option.builder("l")
                .longOpt("logfile")
                .desc("saves the incoming and outgoing data to this file")
                .hasArg(true)
                .numberOfArgs(1)
                .argName("logfile")
                .type(String.class)
                .optionalArg(false)
                .build());
        options.addOption(new Option("s", "separate", false, "saves the incoming and outgoing data to two separate files"));
    }
}

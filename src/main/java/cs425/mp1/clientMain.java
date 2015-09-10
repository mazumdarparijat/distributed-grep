package cs425.mp1;

import org.apache.commons.cli.*;

/**
 * Created by parijatmazumdar on 09/09/15.
 */
public class clientMain {

    private static class FormatCommandLineInputs {
        public final String configFileName;
        public final grepQuery query;

        public FormatCommandLineInputs(String [] args) {
            Options op=createOptions();
            CommandLineParser parser=new DefaultParser();
            CommandLine line=null;
            try {
                line=parser.parse(op,args);
            } catch (ParseException e) {
                printHelp(op);
                e.printStackTrace();
            }

            if (line.hasOption("help")) {
                printHelp(op);
                query=null;
                configFileName=null;
            }
            else if (!line.hasOption("configFile") || !line.hasOption("regex")) {
                query=null;
                configFileName=null;
                try {
                    throw new ParseException("configFile and regex are required arguments.");
                } catch (ParseException e) {
                    e.printStackTrace();
                    printHelp(op);
                }
            }
            else {
                configFileName=line.getOptionValue("configFile");
                String queryString=line.getOptionValue("regex");

                if (line.hasOption("w")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\\b");
                    sb.append(queryString);
                    sb.append("\\b");
                    queryString=sb.toString();
                }

                if (line.hasOption("x")) {
                    StringBuilder sb = new StringBuilder();
                    if (queryString.charAt(0)!='^')
                        sb.append('^');
                    sb.append(queryString);

                    if (queryString.charAt(queryString.length()-1)!='$')
                        sb.append('$');

                    queryString=sb.toString();
                }

                boolean ignoreCase=false;
                boolean invertMatch=false;
                boolean printLineNumbers=false;
                if (line.hasOption("i"))
                    ignoreCase=true;
                if (line.hasOption("v"))
                    invertMatch=true;
                if(line.hasOption("n"))
                    printLineNumbers=true;

                query=new grepQuery(queryString,ignoreCase,invertMatch,printLineNumbers);
            }
        }

        private Options createOptions() {
            Option help = new Option("help","print this help message");
            Option v = new Option("v","grep option to invert match");
            Option i = new Option("i", "grep option to ignore case");
            Option n = new Option("n","grep option to print line numbers");
            Option w = new Option("w","grep option to match only whole words");
            Option x = new Option("x","grep option to match whole sentence only");
            Option configFile = OptionBuilder.withArgName("file").hasArg().withDescription("*Required Option* configuration file")
                    .create("configFile");
            Option regex = OptionBuilder.withArgName("regexString").hasArg().withDescription("*Required Option* grep string")
                    .create("regex");

            Options op=new Options();
            op.addOption(help);
            op.addOption(v);
            op.addOption(i);
            op.addOption(n);
            op.addOption(w);
            op.addOption(x);
            op.addOption(configFile);
            op.addOption(regex);
            return op;
        }

        private void printHelp(Options op) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("distributedGrep",op);
        }
    }

    public static void main(String [] args) {
        FormatCommandLineInputs format = new FormatCommandLineInputs(args);
        if (format.configFileName!=null)
            grepClient.getNewInstance(format.configFileName).executeGrep(format.query);
    }
}

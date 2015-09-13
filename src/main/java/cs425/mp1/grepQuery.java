package cs425.mp1;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for storing the query pattern for grep along with options
 */
class grepQuery {
    public final String queryPattern;
    public final boolean ignoreCase;
    public final boolean invertMatch;
    public final boolean printLineNumbers;

    /** Constructor
     * @param queryPattern
     * @param ignoreCase
     * @param invertMatch
     * @param printLineNumbers
     */
    public grepQuery(String queryPattern,boolean ignoreCase,boolean invertMatch,boolean printLineNumbers) {
        this.queryPattern=queryPattern;
        this.ignoreCase=ignoreCase;
        this.invertMatch=invertMatch;
        this.printLineNumbers=printLineNumbers;
    }

    /** Serialize class to string - used in passing class object over socket
     * @return Class serialized to string
     */
    public String serialize() {
        int opts=(ignoreCase) ? 1<<2 : 0;
        opts += (invertMatch ? 1<<1 : 0);
        opts += printLineNumbers ? 1 : 0;

        StringBuilder sb=new StringBuilder();
        sb.append("<pattern>");
        sb.append(queryPattern);
        sb.append("</pattern>");
        sb.append("<options>");
        sb.append(String.valueOf(opts));
        sb.append("</options>");
        return sb.toString();
    }

    /** Deserialize serialized class object
     * @param serializedGrepQuery serialized class object
     * @return object of this class
     * @throws IOException
     */
    public static grepQuery deserialize(String serializedGrepQuery) throws IOException {
        Pattern p=Pattern.compile("<pattern>(.*)</pattern>");
        Matcher m=p.matcher(serializedGrepQuery);
        String queryString;
        if (m.find())
            queryString = m.group(1);
        else
            throw new IOException("Following string passed for deserialization is malformed : " + serializedGrepQuery);

        p=Pattern.compile("<options>(.*)</options>");
        m=p.matcher(serializedGrepQuery);
        int opts;
        if (m.find())
            opts = Integer.parseInt(m.group(1));
        else
            throw new IOException("Following string passed for deserialization is malformed : " + serializedGrepQuery);

        boolean printLineNums= ((opts & 1) == 1) ? true : false;
        boolean invertM= ((opts & 2) == 2) ? true : false;
        boolean ignoreC= ((opts & 4) == 4) ? true : false;
        return new grepQuery(queryString,ignoreC,invertM,printLineNums);
    }
}

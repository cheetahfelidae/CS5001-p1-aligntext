import java.util.ArrayList;

/**
 * Practical 1 - Aligning Text.
 * author #160026335
 */

public class AlignText {
    private static final int MAX_NUM_ARGUMENTS = 3;

    /**
     * This method delivers an array of words to be printed according to the requested align mode.
     * If the third command line argument is missing, the array of words will be printed in right-align text as a default
     *
     * @param align_mode the desired align mode (from the 1st arg)
     * @param words words contained on a particular line
     * @param line_length the desired length (from the 2rd arg)
     */
    private void printLine(String align_mode, ArrayList<String> words, int line_length) {
        switch (align_mode) {
            case "L":
                printLeftAlignText(words);
                break;
            case "C":
                printCentreAlignText(words, line_length);
                break;
            case "J":
                printFullyJustifiedText(words, line_length);
                break;
            default:
                printRightAlignText(words, line_length);
        }
    }

    /**
     * The methods prints a particular line containing words and spaces between words from the left hand-side.
     *
     * @param words an array of words contained on a particular line
     */
    private void printLeftAlignText(ArrayList<String> words) {
        printWords(words);
        System.out.println();
    }

    /**
     * The methods prints a particular line containing words and spaces between words from the right hand-side.
     *
     * @param words an array of words contained on a particular line
     * @param line_length the desired length (from the 2rd arg)
     */
    private void printRightAlignText(ArrayList<String> words, int line_length) {
        printSpaces(line_length - getUsedChars(words));
        printWords(words);
        System.out.println();
    }

    /**
     * This method find the number of left spaces from a particular line containing words and spaces between words
     * and divides these spaces to place at the start and the end of the line.
     * If the number of left spaces is an odd number, the extra space will be placed at the start of the line.
     *
     * @param words an array of words contained on a particular line
     * @param line_length the desired length (from the 2rd arg)
     */
    private void printCentreAlignText(ArrayList<String> words, int line_length) {
        int num_spaces_left = line_length - getUsedChars(words);

        printSpaces(num_spaces_left % 2 == 1 ? num_spaces_left / 2 + 1 : num_spaces_left / 2);
        printWords(words);
        printSpaces(num_spaces_left / 2);
        System.out.println();
    }

    /**
     * This method distributes spaces as evenly as possible between words on the line
     * and then prints the line containing words and spaces.
     *
     * @param words an array of words contained on a particular line
     * @param line_length the desired length (from the 2rd arg)
     */
    private void printFullyJustifiedText(ArrayList<String> words, int line_length) {
        int num_spaces_left = line_length - getUsedChars(words); // excluding spaces between words
        int num_spaces_btw_words = words.size() > 1 ? 1 + (num_spaces_left / (words.size() - 1)) : 0;

        if (num_spaces_btw_words > 0) {
            num_spaces_left -= (num_spaces_btw_words - 1) * (words.size() - 1);
        }

        for (int i = 0; i < words.size(); i++) {
            if (i > 0) {
                // print extra spaces at the last words of the line
                if (i == words.size() - num_spaces_left) {
                    num_spaces_btw_words++;
                }
                printSpaces(num_spaces_btw_words);
            }
            System.out.print(words.get(i));
        }
        System.out.println();
    }

    /**
     * This method prints words and spaces between them.
     *
     * @param words an array of words contained on a particular line
     */
    private void printWords(ArrayList<String> words) {
        for (int k = 0; k < words.size(); k++) {
            if (k > 0) {
                printSpaces(1);
            }
            System.out.print(words.get(k));
        }
    }

    /**
     * This method prints spaces according to the specific length.
     *
     * @param length the number of spaces you want to print
     */
    private void printSpaces(int length) {
        if (length > 0) {
            System.out.printf("%" + length + "s", "");
        }
    }

    /**
     * This method tells how many spaces are being used on a particular line containing words and spaces between words.
     *
     * @param line_words represents a particular line containing words
     * @return the number of used spaces for the line
     */
    private int getUsedChars(ArrayList<String> line_words) {
        int length = 0;
        for (int i = 0; i < line_words.size(); i++) {
            length += line_words.get(i).length();
        }
        return length + line_words.size() - 1;
    }

    /**
     * 1. Extract words from each paragraph.
     * 2. These extracted words will be on each line as many as possible within the specified length.
     * 3. Each line will be formatted with the specified align mode.
     *
     * @param paragraphs paragraphs read from the file containing text
     * @param line_length the desired length (from the 2rd arg)
     * @param align_mode the desired align mode (from the 1st arg)
     */
    public void alignText(String[] paragraphs, int line_length, String align_mode) {
        ArrayList<String> line_words = new ArrayList<>();

        for (int i = 0; i < paragraphs.length; i++) {
            String[] ext_words = paragraphs[i].split("\\s+");

            int j = 0;
            while (j < ext_words.length) {
                if (line_words.size() == 0 || line_length - getUsedChars(line_words) - (1 + ext_words[j].length()) >= 0) {
                    line_words.add(ext_words[j]);
                    j++;
                } else {
                    printLine(align_mode, line_words, line_length);

                    line_words.clear();
                }
            }

            // from the while loop of ext_words above, there might be the last of words from the paragraph not being printed out
            if (align_mode.equals("J")) {
                printLeftAlignText(line_words);
            } else {
                printLine(align_mode, line_words, line_length);
            }

            line_words.clear();
        }
    }

    /**
     * This main method detects and catches expected errors
     * such as negative numbers of the desired length, 1st and 2nd arguments missing and a text file not found.
     * @param args 1st arg: path of the file containing the text
     *             , 2nd arg: the desired length of the line for wrapping the text
     *             , 3rd arg (optional): align mode for text ("R" for right-align, "L" for left-align, "C" centre-align, "J", fully justify text)
     *
     *             If the 3rd argument is NOT provided, the programme will select "R" as a default align mode
     */
    public static void main(String[] args) {

        try {
            String[] paragraphs = FileUtil.readFile(args[0]);
            int length = Integer.parseInt(args[1]);
            String align_mode = args.length == MAX_NUM_ARGUMENTS ? args[2] : "";

            if (length > 0) {
                new AlignText().alignText(paragraphs, length, align_mode);
            } else {
                System.out.println("usage: java AlignText file_name line_length <align_mode>");
            }
        } catch (Exception e) {
            System.out.println("usage: java AlignText file_name line_length <align_mode>");
        }

    }
}
